package com.xiaopiao.patternbetter;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PatternBetter.MODID)
public class PatternBetter {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "patternbetter";

    public PatternBetter(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, com.xiaopiao.patternbetter.ModConfig.CONFIG);


        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

}
