package com.xiaopiao.patternbetter;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PatternBetter.MODID)
public class PatternBetter {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "pattern_better";
    // Directly reference a slf4j logger

    public PatternBetter(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();


        context.registerConfig(ModConfig.Type.COMMON, com.xiaopiao.patternbetter.ModConfig.CONFIG);


    }


}
