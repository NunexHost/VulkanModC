package net.vulkanmod.render.chunk.build;

import net.minecraft.client.renderer.RenderType;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import net.vulkanmod.render.vertex.TerrainRenderType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ThreadBuilderPack {
    private static Function<TerrainRenderType, TerrainBufferBuilder> terrainBuilderConstructor;

    public static void defaultTerrainBuilderConstructor() {
        terrainBuilderConstructor = renderType -> new TerrainBufferBuilder(renderType.maxSize);
    }

    public static void setTerrainBuilderConstructor(Function<TerrainRenderType, TerrainBufferBuilder> constructor) {
        terrainBuilderConstructor = constructor;
    }

    private final Map<TerrainRenderType, LazyTerrainBufferBuilder> builders = new ConcurrentHashMap<>();

    public ThreadBuilderPack() {
        for (TerrainRenderType renderType : TerrainRenderType.getActiveLayers()) {
            builders.put(renderType, new LazyTerrainBufferBuilder(renderType));
        }
    }

    public TerrainBufferBuilder builder(TerrainRenderType renderType) {
        return builders.get(renderType).get();
    }

    public void clearAll() {
        builders.values().forEach(LazyTerrainBufferBuilder::clear);
    }

    public void discardAll() {
        builders.values().forEach(LazyTerrainBufferBuilder::discard);
    }

    private static class LazyTerrainBufferBuilder {
        private final TerrainRenderType renderType;
        private TerrainBufferBuilder builder;

        public LazyTerrainBufferBuilder(TerrainRenderType renderType) {
            this.renderType = renderType;
        }

        public TerrainBufferBuilder get() {
            if (builder == null) {
                builder = terrainBuilderConstructor.apply(renderType);
            }
            return builder;
        }

        public void clear() {
            if (builder != null) {
                builder.clear();
            }
        }

        public void discard() {
            if (builder != null) {
                builder.discard();
                builder = null;
            }
        }
    }
}
