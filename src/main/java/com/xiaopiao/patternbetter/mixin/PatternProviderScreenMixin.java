package com.xiaopiao.patternbetter.mixin;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.VerticalButtonBar;
import appeng.menu.implementations.PatternProviderMenu;
import com.glodblock.github.extendedae.client.button.ActionEPPButton;
import com.glodblock.github.extendedae.network.EPPNetworkHandler;
import com.glodblock.github.glodium.network.packet.CGenericPacket;
import com.xiaopiao.patternbetter.ModConfig;
import com.xiaopiao.patternbetter.NewIcon;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(value = PatternProviderScreen.class )
@OnlyIn(Dist.CLIENT)
public class PatternProviderScreenMixin<C extends PatternProviderMenu> extends AEBaseScreen<C> {
    @Unique
    VerticalButtonBar rightToolbar = new VerticalButtonBar();

    public PatternProviderScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("RETURN"),remap = false)
    private void injectInit(PatternProviderMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (ModConfig.balanceMode){
            ActionEPPButton balance = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("balance")), NewIcon.BALANCE);
            balance.setMessage(Component.translatable("gui.patternbetter.balance"));
            balance.setTooltip(Tooltip.create(Component.translatable("gui.patternbetter.balance.tooltip")));
            rightToolbar.add(balance);
        }

        ActionEPPButton multiply2 = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("multiply2")), NewIcon.MULTIPLY2);
        ActionEPPButton divide2 = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("divide2")), NewIcon.DIVIDE2);
        ActionEPPButton multiply5 = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("multiply5")), NewIcon.MULTIPLY5);
        ActionEPPButton divide5 = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("divide5")), NewIcon.DIVIDE5);
        ActionEPPButton multiply10 = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("multiply10")), NewIcon.MULTIPLY10);
        ActionEPPButton divide10 = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("divide10")), NewIcon.DIVIDE10);



        Class<? extends VerticalButtonBar> aClass = rightToolbar.getClass();
        Method setRight = aClass.getMethod("setRight" , boolean.class);
        setRight.invoke(rightToolbar , true);

        if (ModConfig.patternsInto){
            ActionEPPButton patternsInto = new ActionEPPButton((b) -> EPPNetworkHandler.INSTANCE.sendToServer(new CGenericPacket("patternsInto")), NewIcon.PATTERNSINTO);
            patternsInto.setMessage(Component.translatable("gui.patternbetter.patternsInto"));
            patternsInto.setTooltip(Tooltip.create(Component.translatable("gui.patternbetter.patternsInto.tooltip")));
            this.addToLeftToolbar(patternsInto);
        }

        rightToolbar.add(divide2);
        rightToolbar.add(multiply2);
        rightToolbar.add(divide5);
        rightToolbar.add(multiply5);
        rightToolbar.add(divide10);
        rightToolbar.add(multiply10);

        this.widgets.add("rightBar", rightToolbar);
    }
}
