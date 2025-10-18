package com.xiaopiao.patternbetter.mixin;

import appeng.client.Point;
import appeng.client.gui.ICompositeWidget;
import appeng.client.gui.widgets.VerticalButtonBar;
import appeng.core.AppEng;
import com.xiaopiao.patternbetter.PatternBetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(VerticalButtonBar.class)
public abstract class VerticalButtonBarMixin implements ICompositeWidget {
    @Shadow public abstract Rect2i getBounds();
    @Shadow private Point position;

    @Shadow @Final private static int MARGIN;
    @Shadow @Final private List<Button> buttons;
    @Shadow private Point screenOrigin;
    @Shadow @Final private static int VERTICAL_SPACING;
    @Shadow private Rect2i bounds;
    ResourceLocation rs = ResourceLocation.fromNamespaceAndPath(PatternBetter.MODID,"rightbar");

    @Unique
    boolean isRight = false;

    @Unique
    public void setRight(boolean right) {
        isRight = right;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void drawBackgroundLayer(GuiGraphics guiGraphics, Rect2i bounds, Point mouse) {
        if (isRight){
            guiGraphics.blitSprite(
                    rs,
                    bounds.getX() + this.getBounds().getX() +this.getBounds().getWidth() , // 修改X坐标计算
                    bounds.getY() + this.getBounds().getY() + (ModList.get().isLoaded("appflux") || ModList.get().isLoaded("expandedae") ? 29 : -1),
                    1,
                    this.getBounds().getWidth() + 1,
                    this.getBounds().getHeight() + 4);
        }else {
            guiGraphics.blitSprite(
                    AppEng.makeId("vertical_buttons_bg"),
                    bounds.getX() + this.getBounds().getX() - 2,
                    bounds.getY() + this.getBounds().getY() - 1,
                    1,
                    this.getBounds().getWidth() + 1,
                    this.getBounds().getHeight() + 4);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void updateBeforeRender() {
        int currentY = position.getY() + MARGIN;
        int maxWidth = 0;

        for (Button button : buttons) {
            if (!button.visible) {
                continue;
            }


            if (isRight){
                button.setX(screenOrigin.getX() + position.getX() - MARGIN - button.getWidth() + getBounds().getWidth());
                button.setY(screenOrigin.getY() + currentY + (ModList.get().isLoaded("appflux") || ModList.get().isLoaded("expandedae") ? 30 : 0));
            }else {
                button.setX(screenOrigin.getX() + position.getX() - MARGIN - button.getWidth());
                button.setY(screenOrigin.getY() + currentY);
            }

            currentY += button.getHeight() + VERTICAL_SPACING;
            maxWidth = Math.max(button.getWidth(), maxWidth);
        }

        if (maxWidth == 0) {
            bounds = new Rect2i(0, 0, 0, 0);
        } else {
            int boundX = position.getX() - maxWidth - 2 * MARGIN;
            int boundY = position.getY();
            bounds = new Rect2i(
                    boundX,
                    boundY,
                    maxWidth + 2 * MARGIN,
                    currentY - boundY);
        }
    }
}