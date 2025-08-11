package com.xiaopiao.patternbetter;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.forgespi.locating.IModFile;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;



public class MyPack  {




    public void load() throws IOException {



//        ModContainer mod = ModList.get().getModContainerById(PatternBetter.MODID).get();
//        IModFile modFile = mod.getModInfo().getOwningFile().getFile();
//        Path resourcePath = modFile.getFilePath();

//        File RESOURCE_PACK = new File(resourcePath.toFile(), "META-INF/patternbetter.zip");

//
//        ModContainer mod = ModList.get().getModContainerById(PatternBetter.MODID).orElseThrow();
//        IModFile modFile = mod.getModInfo().getOwningFile().getFile();
//        Path resourcePath = modFile.getFilePath();
//
//        System.out.println(resourcePath);

        // 从 JAR 的 ClassLoader 读取资源流
        InputStream zipStream = getClass().getResourceAsStream("/assets/patternbetter.zip");
        if (zipStream == null) {
            throw new IllegalStateException("ZIP 资源未找到！");
        }

// 将 ZIP 流写入临时文件（因为 FilePackResources 需要物理文件）
        Path tempZip = Files.createTempFile("patternbetter", ".zip");
        Files.copy(zipStream, tempZip, StandardCopyOption.REPLACE_EXISTING);


        Pack.ResourcesSupplier resources = id -> new FilePackResources(id, tempZip.toFile(), true);
//        new Pack.Info(Component.literal("Built-in resources"), 15, FeatureFlagSet.of())
        // 注册内置资源包
        Minecraft.getInstance().getResourcePackRepository().addPackFinder(map -> {
            final Pack packInfo = Pack.create(PatternBetter.MODID,
                    Component.literal("pattern_better Builtin"),
                    true, // 内置资源包
                    resources,
                    Pack.readPackInfo(PatternBetter.MODID, resources),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    true, // 这个包标记为内置
                    PackSource.BUILT_IN
            );

            map.accept(packInfo);
        });
    }
}
