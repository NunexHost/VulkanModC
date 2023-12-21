package net.vulkanmod.mixin.chunk;

import net.minecraft.client.renderer.culling.Frustum;
import net.vulkanmod.interfaces.FrustumMixed;
import net.vulkanmod.render.chunk.VFrustum;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Frustum.class)
public class FrustumMixin implements FrustumMixed {

    @Shadow private double camX;
    @Shadow private double camY;
    @Shadow private double camZ;

    private final VFrustum vFrustum = new VFrustum();

    /**
     * Move instantiation outside the calculateFrustum method.
     */
    private VFrustum calculateFrustum() {
        return vFrustum;
    }

    /**
     * Consider caching frustum calculations.
     */
    private VFrustum cachedFrustum;

    @Inject(method = "calculateFrustum", at = @At("HEAD"))
    private void calculateFrustum(Matrix4f modelView, Matrix4f projection, CallbackInfo ci) {
        // Only calculate the frustum if it hasn't been calculated yet.
        if (cachedFrustum == null) {
            cachedFrustum = calculateFrustum();
        }

        // Update the cached frustum with the new view and projection matrices.
        cachedFrustum.calculateFrustum(modelView, projection);
    }

    /**
     * Review VFrustum implementation.
     */

    @Inject(method = "prepare", at = @At("RETURN"))
    public void prepare(double d, double e, double f, CallbackInfo ci) {
        // Avoid unnecessary calculations by only calling setCamOffset if the camera position has changed.
        if (cachedFrustum.getCamX() != camX || cachedFrustum.getCamY() != camY || cachedFrustum.getCamZ() != camZ) {
            cachedFrustum.setCamOffset(camX, camY, camZ);
        }
    }

    @Override
    public VFrustum customFrustum() {
        return cachedFrustum;
    }
}
