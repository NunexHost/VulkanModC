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

    private Direction cachedOpposite;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void initCachedOpposite(int oppositeIndex) {
        this.cachedOpposite = BY_3D_DATA[oppositeIndex];
    }

    @Overwrite
    public Direction getOpposite() {
        return cachedOpposite;
    }
}
