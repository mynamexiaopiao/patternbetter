package com.xiaopiao.patternbetter.mixin;



import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.SlotSemantics;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.PatternProviderMenu;
import appeng.menu.slot.AppEngSlot;
import com.glodblock.github.extendedae.api.IPage;
import com.glodblock.github.extendedae.container.ContainerExPatternProvider;
import com.glodblock.github.glodium.network.packet.sync.ActionMap;
import com.xiaopiao.patternbetter.EditMode;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.List;


@Mixin(value = ContainerExPatternProvider.class,priority = 1001)
@Implements({
        @Interface(iface = IPage.class, prefix = "IPage$")
})
public abstract class ContainerExPatternProviderMixin extends PatternProviderMenu{

    @GuiSync(11451)
    @Unique
    public int page = 0;

    @Unique
    public int maxPage = 0;


    //模式自动，用于数据同步
    @Unique
    @GuiSync(1)
    public EditMode mode;

    @Unique
    private  int maxSlots;

    //页面展示数据，用于数据同步
    @Unique
    private ContainerData pageData = new SimpleContainerData(36);

    //服务端数据存储
    @Unique
    public BitSet allSlotStates = new BitSet();

    @Unique
    public int playerSlotCount;

    public ContainerExPatternProviderMixin(MenuType<? extends PatternProviderMenu> menuType, int id, Inventory playerInventory, PatternProviderLogicHost host) {
        super(menuType, id, playerInventory, host);
    }

    @Inject(method = "<init>", at = @At("TAIL"),remap = false)
    public void init(int id, Inventory playerInventory, PatternProviderLogicHost host, CallbackInfo ci) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //初始化必要数据
        int maxSlots = this.getSlots(SlotSemantics.ENCODED_PATTERN).size();
        this.maxPage = (maxSlots + 36 - 1) / 36;
        this.maxSlots = maxSlots;
        //获取玩家的槽位
        List<Slot> playerSlots = (this).getSlots(SlotSemantics.PLAYER_INVENTORY);
        this.playerSlotCount = playerSlots.size();

        addDataSlots(pageData);

        // 初始化所有槽位状态
        allSlotStates.set(0, maxSlots + 36 ,true); // 全部设为true
        updatePageData(0); // 加载第一页


        this.mode = EditMode.EDITOFF;

        //反射获取父类actionMap
        Class<? extends ContainerExPatternProviderMixin> aClass = this.getClass();

        Method getActionMap = aClass.getMethod("getActionMap");

        ActionMap actionMap = (ActionMap)getActionMap.invoke( this);

        //接收客户端按钮变化数据
        actionMap.put("set", (o) -> this.setMode(o.get(0)));

        //获取客户端数据更新
        actionMap.put("upDoubleMode" ,paras -> setItemState((int) paras.get(0),(boolean)paras.get(1)));


        actionMap.put("reverseAll" ,b-> {
            // 翻转所有槽位状态
            allSlotStates.flip(0, maxSlots);
            updatePageData(this.page);
        });

        actionMap.put("reverseThisPage", b ->{
            int startIdx = page * 36;
            int endIdx = Math.min(startIdx + 36, maxSlots); // 防止越界
            for (int slotIdx = startIdx; slotIdx < endIdx; slotIdx++) {
                allSlotStates.set(slotIdx, !allSlotStates.get(slotIdx));
            }
            updatePageData(page); // 确保使用当前页
        });
    }

    /*
    * 分页展示基本
    */
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

    /*
    * 用于更新数据
    */
    public void updatePageData(int page) {
        int startIdx = page * 36;
        int endIdx = Math.min(startIdx + 36, maxSlots);

        for (int i = 0; i < 36; i++) {
            int slotIdx = startIdx + i;
            if (slotIdx < endIdx) {
                pageData.set(i, allSlotStates.get(slotIdx) ? 1 : 0);
            } else {
                pageData.set(i, 0); // 超出范围的槽位设为false
            }
        }
    }

    /*
     * 设置槽位状态
     */
    public void setItemState(int slotIndex, boolean state) {
        slotIndex -= 36;
        if (slotIndex < 0 || slotIndex >= maxSlots) return;

        allSlotStates.set(slotIndex , state);
        updatePageData(this.page); // <-- 无论如何都更新当前页数据
    }

    /*
     *获取槽位状态（反射给别的类调用）
     */
    public boolean getItemState(int slotIndex) {
        slotIndex -= 36;
        if (slotIndex < 0 || slotIndex >= maxSlots) {
            return false;
        }
        int pagePos = slotIndex % 36;
        return pageData.get(pagePos) == 1;
    }

    @Unique
    public int IPage$getPage(){
        return this.page;
    }

    /**
     * 设置当前页， 并更新数据 ， 实现首页—>最后一页切换
     */
    @Unique
    public void IPage$setPage(int page){
        if (page < 0 ){
            this.page = this.maxPage - 1;
        }else if (page >= this.maxPage){
            this.page = 0;
        } else {
            this.page = page;
        }

        if (!this.getPlayer().level().isClientSide) { // 确保在服务端执行
            updatePageData(this.page);
        }
    }

    @Unique
    public void setMode(int mode) {
        this.mode = EditMode.values()[mode];
    }

    @Unique
    public EditMode getMode() {
        return this.mode;
    }

}
