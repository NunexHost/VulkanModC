package net.vulkanmod.mixin.render.vertex;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Vec3i;
import net.vulkanmod.render.vertex.VertexUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VertexConsumer.class)
public interface VertexConsumerM {

    @Shadow void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ);

    /**
     * @author
     */
    @Overwrite
    default public void putBulkData(PoseStack.Pose matrixEntry, BakedQuad quad, float[] brightness, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        int[] js = quad.getVertices();
        Vec3i vec3i = quad.getDirection().getNormal();
        Vector3f vec3f = new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        Matrix4f matrix4f = matrixEntry.pose();
        vec3f.mul(matrixEntry.normal()); // Pre-calculate normal transformation for efficiency

        int j = js.length / 8;

        for (int k = 0; k < j; ++k) {
            int i = k * 8;
            float f = Float.intBitsToFloat(js[i]);
            float g = Float.intBitsToFloat(js[i + 1]);
            float h = Float.intBitsToFloat(js[i + 2]);

            // Cache frequently used values
            float brightnessK = brightness[k];
            int lightK = lights[k];
            float u = Float.intBitsToFloat(js[i + 4]);
            float v = Float.intBitsToFloat(js[i + 5]);

            float o, p, q;
            if (useQuadColorData) {
                float l = VertexUtil.unpackColorR(js[i + 3]);
                float m = VertexUtil.unpackColorG(js[i + 3]);
                float n = VertexUtil.unpackColorB(js[i + 3]);
                o = l * brightnessK * red;
                p = m * brightnessK * green;
                q = n * brightnessK * blue;
            } else {
                o = brightnessK * red;
                p = brightnessK * green;
                q = brightnessK * blue;
            }

            Vector4f vector4f = new Vector4f(f, g, h, 1.0f);
            vector4f.mul(matrix4f); // Transform vertex directly

            this.vertex(vector4f.x(), vector4f.y(), vector4f.z(), o, p, q, 1.0f, u, v, overlay, lightK, vec3f.x(), vec3f.y(), vec3f.z());
        }
    }
}
