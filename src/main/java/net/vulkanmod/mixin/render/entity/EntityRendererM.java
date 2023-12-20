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

    @Redirect(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/culling/Frustum;isVisible(Lnet/minecraft/world/phys/AABB;)Z"))
    private boolean isVisible(Frustum frustum, AABB aabb) {
        if(Initializer.CONFIG.entityCulling) {
            WorldRenderer worldRenderer = WorldRenderer.getInstance();

            Vec3 pos = aabb.getCenter();

            RenderSection section = worldRenderer.getSectionGrid().getSectionAtBlockPos((int) pos.x(), (int) pos.y(), (int) pos.z());

            if(section == null)
                return frustum.isVisible(aabb);

            return worldRenderer.isEntityChunkRecentlyRendered(section, entity);
        } else {
            return frustum.isVisible(aabb);
        }
    }

    // Opcional: incorporar a lógica de visibilidade individual das entidades

    @Redirect(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;shouldRender(DD)Z"))
    private boolean shouldRender(Entity entity, double d, double e, double f) {
        return entity.shouldRender(d, e, f) && (Initializer.CONFIG.entityCulling ? worldRenderer.isEntityChunkRecentlyRendered(worldRenderer.getSectionGrid().getSectionAtBlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()), entity) : true);
    }
}
