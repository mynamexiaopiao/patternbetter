package com.xiaopiao.patternbetter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.client.telemetry.events.WorldLoadTimesEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.resource.ResourcePackLoader;

import java.io.IOException;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PatternBetter.MODID)
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
}
