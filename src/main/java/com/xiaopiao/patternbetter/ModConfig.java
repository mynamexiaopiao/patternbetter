package com.xiaopiao.patternbetter;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = PatternBetter.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfig {

    private static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static ForgeConfigSpec.IntValue SLOT_VALUE = BUILDER.defineInRange("slotvalue",36,0,Integer.MAX_VALUE);

    public static final ForgeConfigSpec CONFIG = BUILDER.build();

    public static int slotValue;

    public static void getConfig() {
        slotValue = SLOT_VALUE.get();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        getConfig();
    }
}
