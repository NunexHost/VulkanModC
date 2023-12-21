package net.vulkanmod.mixin.chunk;

import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Direction.class)
public class DirectionMixin {

    @Shadow @Final private static Direction[] BY_3D_DATA;

    @Shadow @Final private int oppositeIndex;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public Direction getOpposite() {
        if (this.cachedOpposite == null) {
            this.cachedOpposite = BY_3D_DATA[this.oppositeIndex];
        }
        return this.cachedOpposite;
    }
}
