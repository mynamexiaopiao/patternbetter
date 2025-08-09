package com.xiaopiao.patternbetter.mixin;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.PatternProviderMenu;
import com.glodblock.github.glodium.network.packet.sync.IActionHolder;
import com.glodblock.github.glodium.network.packet.sync.Paras;
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
import java.util.Map;
import java.util.function.Consumer;

@Mixin(value = PatternProviderMenu.class)
@Implements(@Interface(iface = IActionHolder.class, prefix = "IActionHolder$"))
public abstract class PatternProviderMenuMixin extends AEBaseMenu {

    @Unique
    private final Map<String, Consumer<Paras>> actionMap = ((IActionHolder)this).createHolder();

    public PatternProviderMenuMixin(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }


    @Inject(method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/patternprovider/PatternProviderLogicHost;)V", at = @At("RETURN"),remap = false)
    public void init(MenuType menuType, int id, Inventory playerInventory, PatternProviderLogicHost host, CallbackInfo ci){

        this.actionMap.put("multiply2", (paras) -> multiply2(false,2));
        this.actionMap.put("divide2", (paras) -> multiply2(true ,2));
        this.actionMap.put("multiply5", (paras) -> multiply2(false,5));
        this.actionMap.put("divide5", (paras) -> multiply2(true , 5));
        this.actionMap.put("multiply10", (paras) -> multiply2(false , 10));
        this.actionMap.put("divide10", (paras) -> multiply2(true , 10));
    }
    @Unique
    public void multiply2(boolean is , int i){
        List<Slot> slots = (this).getSlots(SlotSemantics.ENCODED_PATTERN);
        for (Slot slot : slots) {
            ItemStack stack = slot.getItem();
            IPatternDetails detail = PatternDetailsHelper.decodePattern(stack, (this).getPlayer().level());
            if (detail instanceof AEProcessingPattern process) {
                GenericStack[] input = (GenericStack[])process.getSparseInputs();
                GenericStack[] output = (GenericStack[])process.getOutputs();
                GenericStack[] mulInput = new GenericStack[input.length];
                GenericStack[] mulOutput = new GenericStack[output.length];


                if ((hasStackWithCountOne( input , i) || hasStackWithCountOne(output , i)) && is)continue;

                modifyStacks(input,  mulInput, i, is);
                modifyStacks(output, mulOutput, i, is);

                ItemStack newPattern = PatternDetailsHelper.encodeProcessingPattern(mulInput,mulOutput);
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

    private static boolean hasStackWithCountOne(GenericStack[] stacks, int i) {
        for (GenericStack stack : stacks) {
            if (stack != null && (stack.amount() % i != 0  || stack.amount() /i <0)) {
                return true;
            }
        }
        return false;
    }

    @Unique
    public @NotNull Map<String, Consumer<Paras>> IActionHolder$getActionMap(){
        return this.actionMap;
    }
}
