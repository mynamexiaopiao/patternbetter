package com.xiaopiao.patternbetter.mixin;


import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.PaletteColor;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.SlotSemantics;
import com.glodblock.github.extendedae.api.IPage;
import com.glodblock.github.extendedae.client.button.ActionEPPButton;
import com.glodblock.github.extendedae.client.button.EPPIcon;
import com.glodblock.github.extendedae.client.gui.GuiExPatternProvider;
import com.glodblock.github.extendedae.container.ContainerExPatternProvider;
import com.glodblock.github.extendedae.network.EPPNetworkHandler;
import com.glodblock.github.extendedae.network.packet.CUpdatePage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
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


            this.nextPage.setVisibility(page + 1 < maxPage);
            this.prevPage.setVisibility(page - 1 >= 0);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static int iPage = 0;


    public ActionEPPButton nextPage;
    public ActionEPPButton prevPage;
    @Inject(method = "<init>", at = @At("RETURN"),remap = false)
    private void injectInit(ContainerExPatternProvider menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {


        if (menu.isClientSide() && menu instanceof ContainerExPatternProvider){

                ContainerExPatternProvider menu1 = (ContainerExPatternProvider)this.getMenu();

                if (menu1 instanceof IPage page1){
                    this.prevPage = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> page1.getPage() - 1)), EPPIcon.LEFT);
                    this.nextPage = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CUpdatePage(() -> page1.getPage() + 1)), EPPIcon.RIGHT);


                }
                this.addToLeftToolbar(this.nextPage);
                this.addToLeftToolbar(this.prevPage);

        }


    }


}
