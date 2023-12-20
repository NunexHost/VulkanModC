package net.vulkanmod.render.chunk.build;

import net.minecraft.client.renderer.RenderType;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import net.vulkanmod.render.vertex.TerrainRenderType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ThreadBuilderPack {
    private static final Function<TerrainRenderType, TerrainBufferBuilder> terrainBuilderConstructor;

    static {
        terrainBuilderConstructor = renderType -> new TerrainBufferBuilder(renderType.maxSize);
    }

    private final Map<TerrainRenderType, TerrainBufferBuilder> builders;

    private ThreadBuilderPack(Map<TerrainRenderType, TerrainBufferBuilder> builders) {
        this.builders = builders;
    }

    public static ThreadBuilderPack create() {
        Map<TerrainRenderType, TerrainBufferBuilder> builders = new HashMap<>();
        for (TerrainRenderType renderType : TerrainRenderType.getActiveLayers()) {
            builders.put(renderType, terrainBuilderConstructor.apply(renderType));
        }
        return new ThreadBuilderPack(builders);
    }

    public TerrainBufferBuilder builder(TerrainRenderType renderType) {
        return builders.get(renderType);
    }

    public void clearAll() {
        builders.values().forEach(TerrainBufferBuilder::clear);
    }

    public void discardAll() {
        builders.values().forEach(TerrainBufferBuilder::discard);
    }
}

