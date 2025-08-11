package com.xiaopiao.patternbetter.mixin;


import appeng.client.gui.Icon;
import appeng.client.gui.style.Blitter;
import appeng.client.gui.widgets.IconButton;
import com.glodblock.github.extendedae.client.button.EPPButton;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EPPButton.class)
public abstract class EPPButtonMixin extends IconButton {

    @Shadow abstract Blitter getBlitterIcon();

    public EPPButtonMixin(OnPress onPress) {
        super(onPress);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (this.visible) {
            Item item = this.getItemOverlay();
            Blitter blitter = this.getBlitterIcon();
            if (this.isHalfSize()) {
                this.width = 8;
                this.height = 8;
            }

            int yOffset = this.isHovered() ? 1 : 0;
            if (this.isHalfSize()) {
                PoseStack pose = guiGraphics.pose();
                pose.pushPose();
                pose.translate((float)this.getX(), (float)this.getY(), 0.0F);
                pose.scale(0.5F, 0.5F, 1.0F);
                if (!this.isDisableBackground()) {
                    Icon bgIcon = this.isHovered() ? Icon.TOOLBAR_BUTTON_BACKGROUND_HOVER : (this.isFocused() ? Icon.TOOLBAR_BUTTON_BACKGROUND_FOCUS : Icon.TOOLBAR_BUTTON_BACKGROUND);
                    bgIcon.getBlitter().dest(0, 0).blit(guiGraphics);
                }

                blitter.dest(0, 0).blit(guiGraphics);
                pose.popPose();
            }
            else {
                if (!this.isDisableBackground()) {
                    Icon bgIcon = this.isHovered() ? Icon.TOOLBAR_BUTTON_BACKGROUND_HOVER : (this.isFocused() ? Icon.TOOLBAR_BUTTON_BACKGROUND_FOCUS : Icon.TOOLBAR_BUTTON_BACKGROUND);
                    bgIcon.getBlitter().dest(this.getX() - 1, this.getY() + yOffset, 18, 20).zOffset(2).blit(guiGraphics);
                }

                if (item != null) {
                    guiGraphics.renderItem(new ItemStack(item), this.getX(), this.getY() + 1 + yOffset, 0, 3);
                } else if (blitter != null) {
                    blitter.dest(this.getX(), this.getY() + 1 + yOffset).zOffset(3).blit(guiGraphics);
                }
            }
        }

    }
}
