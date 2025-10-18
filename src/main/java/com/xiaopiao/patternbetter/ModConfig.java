package com.xiaopiao.patternbetter;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = PatternBetter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {

    protected static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    protected static ForgeConfigSpec.IntValue SLOT_VALUE = BUILDER.defineInRange(
            "slotvalue",36,0,Integer.MAX_VALUE);

    protected static ForgeConfigSpec.BooleanValue PAGE_BUTTON = BUILDER
            .comment("Whether to turn on the new button style")
            .define("pagebutton",true);


    protected static ForgeConfigSpec.BooleanValue JUMP_BOX = BUILDER
            .comment("Whether to turn on the jump box")
            .define("jumpbox",true);

    protected static ForgeConfigSpec.BooleanValue PATTERNS_INTO = BUILDER
            .comment("Whether to turn on the Quick Put patterns button")
            .define("patternsinto",true);

    protected static ForgeConfigSpec.BooleanValue EDIT_MODE = BUILDER
            .comment("Whether to turn on the Quick Put patterns button")
            .define("editmode",true);
    protected static ForgeConfigSpec.BooleanValue BALANCE_MODE = BUILDER
            .comment("Whether to turn on the Quick Put patterns button")
            .define("balancemode",true);


    public static final ForgeConfigSpec CONFIG = BUILDER.build();

    public static int slotValue;
    public static boolean pageButton;
    public static boolean jumpBox;
    public static boolean patternsInto;
    public static boolean editMode;
    public static boolean balanceMode;

    public static void getConfig() {
        slotValue = SLOT_VALUE.get();
        pageButton = PAGE_BUTTON.get();
        jumpBox = JUMP_BOX.get();
        patternsInto = PATTERNS_INTO.get();
        editMode = EDIT_MODE.get();
        balanceMode = BALANCE_MODE.get();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        getConfig();
    }
}
