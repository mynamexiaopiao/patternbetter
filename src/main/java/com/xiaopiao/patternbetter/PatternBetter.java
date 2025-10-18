package com.xiaopiao.patternbetter;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PatternBetter.MODID)
@Mod.EventBusSubscriber(modid = "pattern_better", bus = Mod.EventBusSubscriber.Bus.MOD)
public class PatternBetter {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "pattern_better";
    // Directly reference a slf4j logger
    @SuppressWarnings("all")
    public PatternBetter() throws IOException {

        if (FMLEnvironment.dist == Dist.CLIENT){
            new MyPack().load();
        }
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, com.xiaopiao.patternbetter.ModConfig.CONFIG);


    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("all")
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> new MyConfigScreen(parent)));
    }
}
