package com.xiaopiao.patternbetter.mixin;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.PatternProviderMenu;
import com.glodblock.github.glodium.network.packet.sync.ActionMap;
import com.glodblock.github.glodium.network.packet.sync.IActionHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(value =  PatternProviderMenu.class)
@Implements(@Interface(iface = IActionHolder.class, prefix = "IActionHolder$"))
public class PatternProviderMenuMixin extends AEBaseMenu {

    @Unique
    public ActionMap actionMap = ActionMap.create();

    public PatternProviderMenuMixin(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Inject(method = "<init>", at = @At("TAIL"),remap = false)
    public void init(MenuType menuType, int id, Inventory playerInventory, PatternProviderLogicHost host, CallbackInfo ci){

        this.actionMap.put("multiply", (paras) -> multiply2(false));
        this.actionMap.put("divide", (paras) -> multiply2(true));
    }
    @Unique
    public void multiply2(boolean is){
        List<Slot> slots = (this).getSlots(SlotSemantics.ENCODED_PATTERN);
        for (Slot slot : slots) {
            ItemStack stack = slot.getItem();
            IPatternDetails detail = PatternDetailsHelper.decodePattern(stack, (this).getPlayer().level());
            if (detail instanceof AEProcessingPattern process) {
                GenericStack[] input = (GenericStack[])process.getSparseInputs().toArray(new GenericStack[0]);
                GenericStack[] output = (GenericStack[])process.getOutputs().toArray(new GenericStack[0]);
                GenericStack[] mulInput = new GenericStack[input.length];
                GenericStack[] mulOutput = new GenericStack[output.length];


                if ((hasStackWithCountOne( input) || hasStackWithCountOne(output)) && is)continue;

                modifyStacks(input,  mulInput, 2, is);
                modifyStacks(output, mulOutput, 2, is);

                ItemStack newPattern = PatternDetailsHelper.encodeProcessingPattern(Arrays.stream(mulInput).toList(), Arrays.stream(mulOutput).toList());
                slot.set(newPattern);

            }
        }
    }



    private void modifyStacks(GenericStack[] input, GenericStack[] mulInput,int scale, boolean div) {
        for(int i = 0; i < input.length; ++i) {
            if (input[i] != null ) {
                long amt = div ? input[i].amount() / (long)scale : input[i].amount() * (long)scale;
                mulInput[i] = new GenericStack(input[i].what(), amt);
            }
        }
    }

    private static boolean hasStackWithCountOne(GenericStack[] stacks) {
        for (GenericStack stack : stacks) {
            if (stack != null && stack.amount() == 1) {
                return true;
            }
        }
        return false;
    }

    @Unique
    public @NotNull ActionMap IActionHolder$getActionMap(){
        return this.actionMap;
    }
}
