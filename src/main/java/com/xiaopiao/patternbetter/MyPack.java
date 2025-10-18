package com.xiaopiao.patternbetter;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class MyPack  {

    public void load() throws IOException {

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
