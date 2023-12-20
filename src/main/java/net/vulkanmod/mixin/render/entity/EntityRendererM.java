package net.vulkanmod.mixin.render.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.vulkanmod.Initializer;
import net.vulkanmod.render.chunk.RenderSection;
import net.vulkanmod.render.chunk.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class EntityRendererM<T extends Entity> {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean shouldRender(T entity, Frustum frustum, double d, double e, double f) {
        if (!entity.shouldRender(d, e, f)) {
            return false;
        } else if (entity.noCulling) {
            return true;
        } else {
            AABB aabb = entity.getBoundingBoxForCulling().inflate(0.5);
            if (aabb.hasNaN() || aabb.getSize() == 0.0) {
                aabb = new AABB(entity.getX() - 2.0, entity.getY() - 2.0, entity.getZ() - 2.0, entity.getX() + 2.0, entity.getY() + 2.0, entity.getZ() + 2.0);
            }

            WorldRenderer worldRenderer = WorldRenderer.getInstance();
            Vec3i pos = aabb.getCenter().toVec3i();
            RenderSection section = worldRenderer.getSectionGrid().getSectionAt(pos);

            if (section == null) {
                // If the section is not found, assume the entity is visible.
                return true;
            } else if (section.isVisible() && section.getLastFrame() == worldRenderer.getLastFrame()) {
                // If the section is visible and the entity has been rendered in this frame, it is still visible.
                return true;
            } else {
                // If the section is not visible or the entity has not been rendered in this frame, it is not visible.
                return false;
            }
        }
    }

    /**
     * @author
     * @reason
     * This method is commented out because it performs a full frustum check even if the entity is within the bounding section. This is an unnecessary step.
     */
    @Redirect(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/culling/Frustum;isVisible(Lnet/minecraft/world/phys/AABB;)Z"))
    private boolean isVisible(Frustum frustum, AABB aabb) {
        return false;
    }
}
