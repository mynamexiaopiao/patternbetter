package com.xiaopiao.patternbetter.mixin;

import appeng.client.Point;
import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.Icon;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.VerticalButtonBar;
import appeng.menu.implementations.PatternProviderMenu;
import com.glodblock.github.extendedae.api.IPage;
import com.glodblock.github.extendedae.client.button.ActionEPPButton;
import com.glodblock.github.extendedae.container.ContainerExPatternProvider;
import com.glodblock.github.extendedae.network.EAENetworkHandler;
import com.glodblock.github.extendedae.network.packet.CEAEGenericPacket;
import com.glodblock.github.extendedae.network.packet.CUpdatePage;
import com.xiaopiao.patternbetter.NewIcon;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(PatternProviderScreen.class)
public class PatternProviderScreenMixin<C extends PatternProviderMenu> extends AEBaseScreen<C> {

    VerticalButtonBar rightToolbar = new VerticalButtonBar();
    public PatternProviderScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("RETURN"),remap = false)
    private void injectInit(PatternProviderMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        ActionEPPButton multiply2 = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CEAEGenericPacket("multiply2")), NewIcon.MULTIPLY2);
        ActionEPPButton divide2 = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CEAEGenericPacket("divide2")), NewIcon.DIVIDE2);
        ActionEPPButton multiply5 = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CEAEGenericPacket("multiply5")), NewIcon.MULTIPLY5);
        ActionEPPButton divide5 = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CEAEGenericPacket("divide5")), NewIcon.DIVIDE5);
        ActionEPPButton multiply10 = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CEAEGenericPacket("multiply10")), NewIcon.MULTIPLY10);
        ActionEPPButton divide10 = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CEAEGenericPacket("divide10")), NewIcon.DIVIDE10);


        Class<? extends VerticalButtonBar> aClass = rightToolbar.getClass();
        Method setRight = aClass.getMethod("setRight" , boolean.class);
        setRight.invoke(rightToolbar , true);

        widgets.add("rightBar", rightToolbar);

        rightToolbar.add(divide2);
        rightToolbar.add(multiply2);
        rightToolbar.add(divide5);
        rightToolbar.add(multiply5);
        rightToolbar.add(divide10);
        rightToolbar.add(multiply10);



//        rightToolbar.setPosition(new Point(this.imageWidth, 50));
//
//        rightToolbar.populateScreen(this::addRenderableWidget,
//                new Rect2i(this.leftPos, this.topPos, this.imageWidth, this.imageHeight),
//                this);



    }
}
