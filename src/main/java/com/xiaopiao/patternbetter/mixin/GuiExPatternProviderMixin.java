package com.xiaopiao.patternbetter.mixin;


import appeng.client.gui.Icon;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.PaletteColor;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.AETextField;
import appeng.menu.SlotSemantics;
import com.glodblock.github.extendedae.api.IPage;
import com.glodblock.github.extendedae.client.button.ActionEPPButton;
import com.glodblock.github.extendedae.client.gui.GuiExPatternProvider;
import com.glodblock.github.extendedae.container.ContainerExPatternProvider;
import com.glodblock.github.extendedae.network.EAENetworkHandler;
import com.glodblock.github.extendedae.network.packet.CUpdatePage;
import com.xiaopiao.patternbetter.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(GuiExPatternProvider.class)
public abstract class GuiExPatternProviderMixin extends PatternProviderScreen<ContainerExPatternProvider> {

    @Unique
    private static int iPage = 0;
    @Unique
    private static int maxPage = 0;
    @Unique
    private ActionEPPButton nextPage;
    @Unique
    private ActionEPPButton prevPage;
    @Unique
    private AETextField numberInputField;
    @Unique
    private String numberInput;

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
                    , leftPos+ textWidth +imageWidth/10, topPos + imageHeight/5-18,color,false);
        }
    }


    @Unique
    public void updateBeforeRender() {
        super.updateBeforeRender();
        try {
            //反射获取page和maxPage
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

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    @Inject(method = "<init>", at = @At("RETURN"),remap = false)
    private void injectInit(ContainerExPatternProvider menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        ContainerExPatternProvider menu1 = this.getMenu();

        if (menu1 instanceof IPage page1){
            //前进后退按钮
            this.prevPage = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> page1.getPage() - 1)), Icon.ARROW_LEFT);
            this.nextPage = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> page1.getPage() + 1)), Icon.ARROW_RIGHT);

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
                EAENetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> finalPage));

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
                EAENetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> finalPage));
            } catch (NumberFormatException e) {
                // 忽略无效数字格式
            }
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int btn) {
        if (btn == 1 && this.numberInputField.isMouseOver(x, y)) {
            this.numberInputField.setValue("");
        }
        return super.mouseClicked(x, y, btn);
    }

}
