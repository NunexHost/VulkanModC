package net.vulkanmod.mixin.render.vertex;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererM {

    /**
     * @author
     * @reason
     */
    @Overwrite
    private void vertex(VertexConsumer vertexConsumer, double d, double e, double f, float g, float h, float i, float j, float k, int l) {
        // Pre-calculate frequently used values for potential performance gains
        float xd = (float) d;
        float yd = (float) e;
        float zd = (float) f;

        // Use a single call to vertex() for potential efficiency
        vertexConsumer.vertex(xd, yd, zd, g, h, i, 1.0f, j, k, 0, l, 0.0F, 1.0F, 0.0F);
    }
}
