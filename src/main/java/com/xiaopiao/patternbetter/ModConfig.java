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

    public static final ModConfigSpec CONFIG = BUILDER.build();

    public static int slotValue;

    public static void getConfig() {
        slotValue = SLOT_VALUE.get();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        getConfig();
    }
}
