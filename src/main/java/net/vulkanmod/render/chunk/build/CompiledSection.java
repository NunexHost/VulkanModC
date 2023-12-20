package net.vulkanmod.render.chunk.build;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.renderer.chunk.VisibilitySet;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vulkanmod.render.vertex.TerrainBufferBuilder;
import net.vulkanmod.render.vertex.TerrainRenderType;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.EnumSet;

public class CompiledSection {
    public static final CompiledSection UNCOMPILED = new CompiledSection() {
        public boolean canSeeThrough(Direction dir1, Direction dir2) {
            return false;
        }
    };

    public final BitSet renderTypes = new BitSet();
    public boolean isCompletelyEmpty;
    public final BlockEntity[] renderableBlockEntities = new BlockEntity[64];
    public VisibilitySet visibilitySet = new VisibilitySet();
    @Nullable
    public TerrainBufferBuilder.SortState transparencyState;

    public CompiledSection() {
        this.isCompletelyEmpty = true;
    }

    public void init(EnumSet<TerrainRenderType> renderTypes, List<BlockEntity> renderableBlockEntities) {
        this.renderTypes.or(renderTypes);
        this.isCompletelyEmpty = renderTypes.isEmpty();

        for (int i = 0; i < renderableBlockEntities.size(); i++) {
            this.renderableBlockEntities[i] = renderableBlockEntities.get(i);
        }

        this.visibilitySet.updateFromBlockEntities(this.renderableBlockEntities);
    }

    public boolean hasNoRenderableLayers() {
        return this.isCompletelyEmpty;
    }

    public boolean isEmpty(TerrainRenderType p_112759_) {
        return !this.renderTypes.get(p_112759_.ordinal());
    }

    public List<BlockEntity> getRenderableBlockEntities() {
        return Lists.newArrayList(this.renderableBlockEntities);
    }

    public boolean canSeeThrough(Direction dir1, Direction dir2) {
        return this.visibilitySet.visibilityBetween(dir1, dir2);
    }
}
