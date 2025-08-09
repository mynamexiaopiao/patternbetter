package com.xiaopiao.patternbetter.mixin;


import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.SlotSemantics;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.PatternProviderMenu;
import appeng.menu.slot.AppEngSlot;
import com.glodblock.github.extendedae.api.IPage;
import com.glodblock.github.extendedae.container.ContainerExPatternProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(value = ContainerExPatternProvider.class)
@Implements(@Interface(iface = IPage.class, prefix = "aebetter$"))
public abstract class ContainerExPatternProviderMixin extends PatternProviderMenu{

    public ContainerExPatternProviderMixin(int id, Inventory playerInventory, PatternProviderLogicHost host) {
        super(id, playerInventory, host);
    }



    @GuiSync(11451)
    @Unique
    public int page = 0;

    @Unique
    public int maxPage = 0;

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


        int maxSlots = (this).getSlots(SlotSemantics.ENCODED_PATTERN).size();
        this.maxPage = (maxSlots + 36 - 1) / 36;

    }


    @Unique
    public int aebetter$getPage(){
        return this.page;
    }
    @Unique
    public void aebetter$setPage(int page){
        this.page = page;
    }
}
