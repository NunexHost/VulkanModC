package net.vulkanmod.render.chunk.build;

import net.minecraft.client.renderer.RenderType;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import net.vulkanmod.render.vertex.TerrainRenderType;

import java.util.Arrays;
import java.util.EnumSet;
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

    private static final TerrainRenderType[] ACTIVE_LAYERS = TerrainRenderType.getActiveLayers().toArray(new TerrainRenderType[0]);

    private static final Map<TerrainRenderType, TerrainBufferBuilder> BUILDERS = Arrays.stream(ACTIVE_LAYERS)
            .collect(Collectors.toMap(Function.identity(), terrainBuilderConstructor));

    public static TerrainBufferBuilder builder(TerrainRenderType renderType) {
        return BUILDERS.get(renderType);
    }

    public static void clearAll() {
        BUILDERS.values().forEach(TerrainBufferBuilder::clear);
    }

    public static void discardAll() {
        BUILDERS.values().forEach(TerrainBufferBuilder::discard);
    }
}
