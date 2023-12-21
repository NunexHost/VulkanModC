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

    // Create VFrustum instance once
    private final VFrustum vFrustum = new VFrustum();

    // Optional caching for frustum values
    private Matrix4f cachedFrustum;

    @Inject(method = "calculateFrustum", at = @At("HEAD"), cancellable = true)
    private void calculateFrustum(Matrix4f modelView, Matrix4f projection, CallbackInfo ci) {
        if (cachedFrustum == null || !cachedFrustum.equals(projection)) {
            // Calculate frustum only if necessary
            vFrustum.calculateFrustum(modelView, projection);
            cachedFrustum = projection; // Cache for potential reuse
        }
        ci.cancel();
    }

    @Inject(method = "prepare", at = @At("RETURN"))
    public void prepare(double d, double e, double f, CallbackInfo ci) {
        this.vFrustum.setCamOffset(this.camX, this.camY, this.camZ);
    }

    @Override
    public VFrustum customFrustum() {
        return vFrustum;
    }
}
