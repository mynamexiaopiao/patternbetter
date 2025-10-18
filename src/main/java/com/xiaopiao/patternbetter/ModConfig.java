package com.xiaopiao.patternbetter;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = PatternBetter.MODID)
public class ModConfig {

    private static ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static ModConfigSpec.IntValue SLOT_VALUE = BUILDER
            .comment("the number of slot")
            .defineInRange("slotvalue",36,0,Integer.MAX_VALUE);

    private static ModConfigSpec.BooleanValue PAGE_BUTTON = BUILDER
            .define("pagebutton",true);

    private static ModConfigSpec.BooleanValue JUMP_BOX = BUILDER
            .define("jumpbox",true);

    private static ModConfigSpec.BooleanValue PATTERNS_INTO = BUILDER
            .define("patternsinto",true);

    private static ModConfigSpec.BooleanValue EDIT_MODE = BUILDER
            .define("editmode",true);

    private static ModConfigSpec.BooleanValue BALANCE_MODE = BUILDER
            .define("balancemode",true);

    public static final ModConfigSpec CONFIG = BUILDER.build();

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
