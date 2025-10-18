package com.xiaopiao.patternbetter.mixin;

import appeng.client.Point;
import appeng.client.gui.ICompositeWidget;
import appeng.client.gui.widgets.VerticalButtonBar;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(value = VerticalButtonBar.class)
public abstract class VerticalButtonBarMixin implements ICompositeWidget {
    @Shadow(remap = false) public abstract Rect2i getBounds();
    @Shadow(remap = false) private Point position;

    @Shadow(remap = false) @Final private static int MARGIN;
    @Shadow(remap = false) @Final private List<Button> buttons;
    @Shadow(remap = false) private Point screenOrigin;
    @Shadow(remap = false) @Final private static int VERTICAL_SPACING;
    @Shadow(remap = false) private Rect2i bounds;

    @Unique
    boolean isRight = false;

    @Unique
    public void setRight(boolean right) {
        isRight = right;
    }


    /**
     * @author xiaopiao
     * @reason add right toolbar
     */
    @Overwrite(remap = false)
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
