package com.xiaopiao.patternbetter.mixin;

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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PatternProviderScreen.class)
public class PatternProviderScreenMixin<C extends PatternProviderMenu> extends AEBaseScreen<C> {
    public PatternProviderScreenMixin(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(method = "<init>", at = @At("RETURN"),remap = false)
    private void injectInit(PatternProviderMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {

        ActionEPPButton multiply = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CEAEGenericPacket("multiply")), NewIcon.MULTIPLY2);
        ActionEPPButton divide = new ActionEPPButton((b) -> EAENetworkHandler.INSTANCE.sendToServer(new CEAEGenericPacket("divide")), NewIcon.DIVIDE2);

        this.addToLeftToolbar(multiply);
        this.addToLeftToolbar(divide);



    }
}
