package net.vulkanmod.mixin.chunk;

import net.minecraft.client.renderer.chunk.VisibilitySet;
import net.minecraft.core.Direction;
import net.vulkanmod.interfaces.VisibilitySetExtended;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(VisibilitySet.class)
public class VisibilitySetMixin implements VisibilitySetExtended {

    private long vis = 0;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void set(Direction dir1, Direction dir2, boolean p_112989_) {
        int dir1Ordinal = dir1.ordinal();
        int dir2Ordinal = dir2.ordinal();
        this.vis |= 1L << ((dir1Ordinal << 3) + dir2Ordinal);
        if (dir1Ordinal != dir2Ordinal) {
            this.vis |= 1L << ((dir2Ordinal << 3) + dir1Ordinal);
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void setAll(boolean bl) {
        this.vis = bl ? 0xFFFFFFFFFFFFFFFFL : 0;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean visibilityBetween(Direction dir1, Direction dir2) {
        int dir1Ordinal = dir1.ordinal();
        int dir2Ordinal = dir2.ordinal();
        return (this.vis & (1L << ((dir1Ordinal << 3) + dir2Ordinal))) != 0;
    }

    @Override
    public long getVisibility() {
        return this.vis;
    }
}
