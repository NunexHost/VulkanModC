package net.vulkanmod.render.chunk.build;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.renderer.chunk.OcclusionConsumer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import net.vulkanmod.render.vertex.TerrainRenderType;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TerrainSection {
    public static final TerrainSection UNCOMPILED = new TerrainSection() {
        public boolean canSeeThrough(Direction dir1, Direction dir2) {
            return false;
        }
    };
    public final Set<TerrainRenderType> renderTypes = EnumSet.noneOf(TerrainRenderType.class);
    boolean isCompletelyEmpty = true;
    final List<BlockEntity> renderableBlockEntities = Lists.newArrayList();

    public boolean isEmpty() {
        return this.isCompletelyEmpty;
    }

    public boolean isEmpty(TerrainRenderType p_112759_) {
        return !this.renderTypes.contains(p_112759_);
    }

    public List<BlockEntity> getRenderableBlockEntities() {
        return this.renderableBlockEntities;
    }

    public boolean canSeeThrough(Direction dir1, Direction dir2) {
        return new OcclusionConsumer.VisibleFaces(dir1, dir2).apply();
    }
}
