package com.xiaopiao.patternbetter.mixin;


import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.GenericStack;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.SlotSemantics;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.PatternProviderMenu;
import appeng.menu.slot.AppEngSlot;
import com.glodblock.github.extendedae.api.IPage;
import com.glodblock.github.extendedae.container.ContainerExPatternProvider;
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


@Mixin(value = ContainerExPatternProvider.class,priority = 1001)
@Implements({
        @Interface(iface = IPage.class, prefix = "IPage$"),
})
public abstract class ContainerExPatternProviderMixin extends PatternProviderMenu{

    @GuiSync(11451)
    @Unique
    public int page = 0;

    @Unique
    public int maxPage = 0;



    public ContainerExPatternProviderMixin(MenuType<? extends PatternProviderMenu> menuType, int id, Inventory playerInventory, PatternProviderLogicHost host) {
        super(menuType, id, playerInventory, host);
    }

    @Unique
    public void showPage() {
        List<Slot> slots = this.getSlots(SlotSemantics.ENCODED_PATTERN);
        int slot_id = 0;

        for(Slot s : slots) {
            int page_id = slot_id / 36;

            if (page_id > 0 && page_id == this.page) {
                s.x = slots.get(slot_id % 36).x;
                s.y = slots.get(slot_id % 36).y;
            }

            ((AppEngSlot)s).setActive(page_id == this.page);
            ++slot_id;
        }

    }

    @Inject(method = "<init>", at = @At("TAIL"),remap = false)
    public void init(int id, Inventory playerInventory, PatternProviderLogicHost host, CallbackInfo ci){


        int maxSlots = this.getSlots(SlotSemantics.ENCODED_PATTERN).size();
        this.maxPage = (maxSlots + 36 - 1) / 36;

    }






    @Unique
    public int IPage$getPage(){
        return this.page;
    }
    @Unique
    public void IPage$setPage(int page){
        this.page = page;
    }

}
