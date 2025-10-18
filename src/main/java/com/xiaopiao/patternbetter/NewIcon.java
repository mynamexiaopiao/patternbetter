package com.xiaopiao.patternbetter;

import appeng.client.gui.style.Blitter;
import net.minecraft.resources.ResourceLocation;

public class NewIcon {
    @SuppressWarnings("all")
    private static final ResourceLocation TEXTURE = new ResourceLocation(PatternBetter.MODID,"textures/gui/nicons.png");



    public static final Blitter MULTIPLY2;
    public static final Blitter DIVIDE2;
    public static final Blitter MULTIPLY5;
    public static final Blitter DIVIDE5;
    public static final Blitter MULTIPLY10;
    public static final Blitter DIVIDE10;
    public static final Blitter PATTERNSINTO;
    public static final Blitter EDITON;
    public static final Blitter EDITOFF;
    public static final Blitter REVERSEAll;
    public static final Blitter REVERSETHISPAGE;
    public static final Blitter BALANCE;

    static {
        MULTIPLY2 = Blitter.texture(TEXTURE, 64, 64).src(32, 0, 16, 16);
        DIVIDE2 = Blitter.texture(TEXTURE, 64, 64).src(48, 0, 16, 16);
        MULTIPLY5 = Blitter.texture(TEXTURE, 64, 64).src(0, 0, 16, 16);
        DIVIDE5 = Blitter.texture(TEXTURE, 64, 64).src(16, 0, 16, 16);
        MULTIPLY10 = Blitter.texture(TEXTURE, 64, 64).src(0, 16, 16, 16);
        DIVIDE10 = Blitter.texture(TEXTURE, 64, 64).src(16, 16, 16, 16);
        PATTERNSINTO = Blitter.texture(TEXTURE, 64, 64).src(32, 16, 16, 16);
        EDITON = Blitter.texture(TEXTURE, 64, 64).src(16, 32, 16, 16);
        EDITOFF = Blitter.texture(TEXTURE, 64, 64).src(0, 32, 16, 16);
        REVERSEAll = Blitter.texture(TEXTURE, 64, 64).src(32, 32, 16, 16);
        REVERSETHISPAGE = Blitter.texture(TEXTURE, 64, 64).src(48, 32, 16, 16);
        BALANCE = Blitter.texture(TEXTURE, 64, 64).src(48, 16, 16, 16);
    }
}
