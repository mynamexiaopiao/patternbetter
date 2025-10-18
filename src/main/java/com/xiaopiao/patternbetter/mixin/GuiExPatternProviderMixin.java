package com.xiaopiao.patternbetter.mixin;


import appeng.client.gui.Icon;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.PaletteColor;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AETextField;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.PatternProviderMenu;
import com.glodblock.github.extendedae.api.IPage;
import com.glodblock.github.extendedae.client.button.ActionEPPButton;
import com.glodblock.github.extendedae.client.button.CycleEPPButton;
import com.glodblock.github.extendedae.client.gui.GuiExPatternProvider;
import com.glodblock.github.extendedae.container.ContainerExPatternProvider;
import com.glodblock.github.extendedae.network.EPPNetworkHandler;
import com.glodblock.github.extendedae.network.packet.CUpdatePage;
import com.glodblock.github.glodium.network.packet.CGenericPacket;
import com.glodblock.github.glodium.network.packet.sync.IActionHolder;
import com.glodblock.github.glodium.network.packet.sync.Paras;
import com.xiaopiao.patternbetter.EditMode;
import com.xiaopiao.patternbetter.ModConfig;
import com.xiaopiao.patternbetter.NewIcon;
import com.xiaopiao.patternbetter.PatternBetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(value = GuiExPatternProvider.class )
@Implements(@Interface(iface = IActionHolder.class, prefix = "IActionHolder$"))
public abstract class GuiExPatternProviderMixin extends PatternProviderScreen<ContainerExPatternProvider> {
    @Unique
    private  int iPage = 0; //当前页数
    @Unique
    private  int maxPage = 0; //最大页数
    @Unique
    private ActionEPPButton nextPage;
    @Unique
    private ActionEPPButton prevPage;
    @Unique
    ActionEPPButton reverseAll;
    @Unique
    ActionEPPButton reverseThisPage;
    @Unique
    private AETextField numberInputField;
    @Unique
    private String numberInput; //页码输入框内容
    @Unique
    CycleEPPButton doubleModeButton; //双模式切换按钮
    @Unique
    private final Map<String, Consumer<Paras>> actionMap = ((IActionHolder)this).createHolder();
    @SuppressWarnings("all")
    @Unique
    private static final ResourceLocation CHECK = new ResourceLocation(PatternBetter.MODID, "textures/gui/1.png");
    @SuppressWarnings("all")
    @Unique
    private static final ResourceLocation CHECK1 = new ResourceLocation(PatternBetter.MODID, "textures/gui/2.png");

    public GuiExPatternProviderMixin(ContainerExPatternProvider menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }


    @Unique
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks){
        super.render(guiGraphics, mouseX, mouseY, partialTicks);


        int maxSlots = this.getMenu().getSlots(SlotSemantics.ENCODED_PATTERN).size();
        if (maxSlots>36){
            Font fontRenderer = Minecraft.getInstance().font;

            // 测量文本宽度（支持多语言）
            Component translatedText = Component.translatable("itemGroup.extendedae");
            int textWidth = fontRenderer.width(translatedText);


            //获取ae通用界面样式
            int color = style.getColor(PaletteColor.DEFAULT_TEXT_COLOR).toARGB();
            guiGraphics.drawString(font, Component.translatable("gui.pattern_provider.page").append(": "+(iPage + 1))
                    , leftPos+ textWidth +imageWidth/10 - 40, topPos + imageHeight/5-17,color,false);
        }


        //编辑模式符号渲染
        if (getDoubleMode() == EditMode.EDITON){
            List<Slot> patternSlots = this.getMenu().getSlots(SlotSemantics.ENCODED_PATTERN);
            for (Slot slot : patternSlots) {
                if (!slot.getItem().isEmpty()) {
                    if(this.iPage + 1 == (slot.index ) /36){
                        int sx = this.leftPos + slot.x;
                        int sy = this.topPos + slot.y;
                        int iconW = 8; // 你贴图的宽度
                        int iconH = 8; // 高度
                        int iconX = sx + 18 - iconW ;

                        //查看槽位数据以显示编辑模式不同状态
                        if (getSlotData(slot.index)){
                            guiGraphics.blit(CHECK, iconX, sy, 350,0, 0, iconW, iconH, iconW, iconH);
                        }else {
                            guiGraphics.blit(CHECK1, iconX, sy, 350,0, 0, iconW, iconH, iconW, iconH);
                        }
                    }
                }
            }
        }

    }

    @Unique
    public void updateBeforeRender() {
        super.updateBeforeRender();
        try {
            ContainerExPatternProvider menu1 = this.getMenu();

            Class<? extends ContainerExPatternProvider> aClass = menu1.getClass();
            Method showPage = aClass.getMethod("showPage");
            showPage.invoke(menu1);

            Field fieldPage = aClass.getDeclaredField("page");
            Integer page = (Integer)fieldPage.get(menu1);

            Field fieldMaxPage = aClass.getDeclaredField("maxPage");
            Integer maxPage = (Integer)fieldMaxPage.get(menu1);


            iPage = page;
            this.maxPage = maxPage;

            this.nextPage.setVisibility(true);
            this.prevPage.setVisibility(true);

            if (maxPage <= 1){
                this.nextPage.setVisibility(false);
                this.prevPage.setVisibility(false);
            }

            //按钮
            this.doubleModeButton.setState(getDoubleMode().ordinal());

            if (getDoubleMode() == EditMode.EDITON){
                reverseAll.setVisibility( true);
                reverseThisPage.setVisibility( true);
            }else {
                reverseAll.setVisibility( false);
                reverseThisPage.setVisibility( false);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    @Inject(method = "<init>", at = @At("RETURN"),remap = false)
    private void injectInit(ContainerExPatternProvider menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {

        ContainerExPatternProvider menu1 = this.getMenu();

        if (menu1 instanceof IPage page1){
            //前进后退按钮
            this.prevPage = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> page1.getPage() - 1)), Icon.ARROW_LEFT);
            this.nextPage = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> page1.getPage() + 1)), Icon.ARROW_RIGHT);

        }

        prevPage.setTooltip(Tooltip.create(Component.translatable("gui.pattern_provider.prev_page")));
        nextPage.setTooltip(Tooltip.create(Component.translatable("gui.pattern_provider.next_page")));

        if (ModConfig.pageButton){
            nextPage.setHalfSize( true);
            prevPage.setHalfSize( true);


            this.widgets.add("nextPage", nextPage);
            this.widgets.add("prevPage", prevPage);
        }else {
            this.addToLeftToolbar(prevPage);
            this.addToLeftToolbar(nextPage);
        }

        if (ModConfig.jumpBox){
            this.numberInputField = this.widgets.addTextField("numberInputField");
            this.numberInputField.setResponder(this::onlyNumber);
        }

        //模式切换按钮
        if (ModConfig.editMode){
            this.doubleModeButton = new CycleEPPButton();
            doubleModeButton.addActionPair(NewIcon.EDITOFF,Component.translatable("gui.patternbetter.button.editoff"),
                    b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("set" , EditMode.EDITOFF.ordinal())));
            doubleModeButton.addActionPair(NewIcon.EDITON,Component.translatable("gui.patternbetter.button.editon"),
                    b ->EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("set" , EditMode.EDITON.ordinal())));

            this.addToLeftToolbar(doubleModeButton);
        }

        //翻转按钮
        reverseAll = new ActionEPPButton(b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("reverseAll")), NewIcon.REVERSEAll);
        reverseThisPage = new ActionEPPButton(b -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("reverseThisPage")), NewIcon.REVERSETHISPAGE);

        reverseAll.setHalfSize(true);
        reverseThisPage.setHalfSize(true);

        reverseAll.setMessage(Component.translatable("gui.patternbetter.button.reverseAll"));
        reverseThisPage.setMessage(Component.translatable("gui.patternbetter.button.reverseThisPage"));

        this.widgets.add("reverseAll", reverseAll);
        this.widgets.add("reverseThisPage", reverseThisPage);
    }

    @Unique
    public void onlyNumber(String str){

        // 过滤只保留数字
        String filtered = str.replaceAll("[^0-9]", "");

        // 如果输入被修改了
        if (!str.equals(filtered)) {
            // 更新输入框显示
            this.numberInputField.setValue(filtered);
        }

        // 保存当前输入值
        this.numberInput = filtered;

        // 如果有过滤后的数字，尝试跳转页面
        if (!filtered.isEmpty()) {
            try {
                int inputValue = Integer.parseInt(filtered);

                // 处理前导零
                if (filtered.length() > 1 && filtered.startsWith("0")) {
                    filtered = String.valueOf(inputValue);
                    this.numberInputField.setValue(filtered);
                    this.numberInput = filtered;
                }

                // 限制最大值
                if (inputValue > maxPage && maxPage > 0) {
                    inputValue = maxPage;
                    filtered = String.valueOf(maxPage);
                    this.numberInputField.setValue(filtered);
                    this.numberInput = filtered;
                }

                // 发送页面跳转请求
                int pageToSend = inputValue - 1;
                pageToSend = pageToSend > maxPage ? 0 : pageToSend;
                int finalPage = pageToSend;
                EPPNetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> finalPage));

            } catch (NumberFormatException e) {
                // 忽略无效数字格式
            }
        }
    }

    protected void init() {
        super.init();
        if (numberInputField == null)return;
        this.setInitialFocus(numberInputField);
    }

    @Override
    protected void changeFocus(@NotNull ComponentPath path) {
        super.changeFocus(path);
        if (!numberInputField.getValue().isEmpty() && !numberInput.isEmpty()){
            try {
                int inputValue = Integer.parseInt(numberInput);
                int pageToSend = inputValue - 1;
                pageToSend = pageToSend > maxPage ? 0 : pageToSend;
                int finalPage = pageToSend;
                EPPNetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> finalPage));
            } catch (NumberFormatException e) {
                // 忽略无效数字格式
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int btn) {
        if (btn == 1 && this.numberInputField.isMouseOver(mouseX, mouseY)) {
            this.numberInputField.setValue("");
        }

        //黑名单模式左键点击切换
        if (btn == 0 && getDoubleMode() == EditMode.EDITON) { // 左键
            List<Slot> patternSlots = this.getMenu().getSlots(SlotSemantics.ENCODED_PATTERN);
            for (Slot slot : patternSlots) {
                if (!slot.getItem().isEmpty()) {
                    if (this.iPage + 1 == slot.index / 36) {
                        int sx = this.leftPos + slot.x;
                        int sy = this.topPos + slot.y;

                        // 槽位范围 18x18
                        if (mouseX >= sx && mouseX < sx + 18 &&
                                mouseY >= sy && mouseY < sy + 18) {

                            boolean current = getSlotData(slot.index);
                            boolean newState = !current;

                            // 同步服务端
                            SlotSendToServer(slot.index, newState);
                            return true; // 阻止原本取物品的行为
                        }
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, btn);
    }

    //发送数据到menu
    @Unique
    public void SlotSendToServer(int slotIndex , boolean is){
        EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("upDoubleMode" , slotIndex, is));
    }


    //反射获取按钮模式
    @Unique
    public boolean getSlotData(int slot){

        PatternProviderMenu menu1 = this.getMenu();

        Class<? extends PatternProviderMenu> aClass = menu1.getClass();

        try {
            Method getMode = aClass.getMethod("getItemState" , int.class);

            return (boolean) getMode.invoke(menu1 , slot);


        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //反射获取按钮模式
    @Unique
    public EditMode getDoubleMode(){
        PatternProviderMenu menu1 = this.getMenu();

        Class<? extends PatternProviderMenu> aClass = menu1.getClass();

        try {
            Method getMode = aClass.getMethod("getMode");

            return (EditMode)getMode.invoke(menu1);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Unique
    public @NotNull Map<String, Consumer<Paras>> IActionHolder$getActionMap(){
        return this.actionMap;
    }
}
