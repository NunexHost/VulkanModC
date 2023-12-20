package net.vulkanmod.render.chunk.build;

import net.minecraft.client.renderer.RenderType;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import net.vulkanmod.render.vertex.TerrainRenderType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ThreadBuilderPack {

    private static Function<TerrainRenderType, TerrainBufferBuilder> terrainBuilderConstructor;

    public static void defaultTerrainBuilderConstructor() {
        terrainBuilderConstructor = renderType -> new TerrainBufferBuilder(renderType.maxSize);
    }

    public static void setTerrainBuilderConstructor(Function<TerrainRenderType, TerrainBufferBuilder> constructor) {
        terrainBuilderConstructor = constructor;
    }

    private final Map<TerrainRenderType, TerrainBufferBuilder> builders = new HashMap<>();

    public ThreadBuilderPack() {
    }

    public synchronized TerrainBufferBuilder builder(TerrainRenderType renderType) {
        TerrainBufferBuilder builder = builders.get(renderType);
        if (builder == null) {
            builder = terrainBuilderConstructor.apply(renderType);
            builders.put(renderType, builder);
        }
        return builder;
    }

    public void clearAll() {
        builders.values().forEach(TerrainBufferBuilder::clear);
    }

    public void discardAll() {
        builders.values().forEach(TerrainBufferBuilder::discard);
    }
}
