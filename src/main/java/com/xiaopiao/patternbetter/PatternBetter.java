package com.xiaopiao.patternbetter;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PatternBetter.MODID)
@EventBusSubscriber(modid = PatternBetter.MODID)
public class PatternBetter {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "patternbetter";

    public PatternBetter(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, com.xiaopiao.patternbetter.ModConfig.CONFIG);
    }



    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class,ConfigurationScreen::new);
    }
}
