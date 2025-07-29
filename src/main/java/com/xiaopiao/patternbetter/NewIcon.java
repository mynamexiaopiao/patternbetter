package com.xiaopiao.patternbetter;

import appeng.client.gui.style.Blitter;
import net.minecraft.resources.ResourceLocation;

public class NewIcon {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(PatternBetter.MODID,"textures/guis/nicons.png");



    public static final Blitter MULTIPLY2;
    public static final Blitter DIVIDE2;

    static {
        MULTIPLY2 = Blitter.texture(TEXTURE, 64, 64).src(32, 0, 16, 16);
        DIVIDE2 = Blitter.texture(TEXTURE, 64, 64).src(48, 0, 16, 16);
    }
}
