package net.vulkanmod.mixin.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.vulkanmod.vulkan.Renderer;
import net.vulkanmod.vulkan.framebuffer.Framebuffer;
import net.vulkanmod.vulkan.util.DrawUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderTarget.class)
public class RenderTargetMixin {

    @Shadow public int viewWidth;
    @Shadow public int viewHeight;
    @Shadow public int width;
    @Shadow public int height;

    private Framebuffer framebuffer;

    @Overwrite
    public void clear(boolean getError) {
        if (this.framebuffer != null) {
            this.framebuffer.clear(); // Utilize Framebuffer's clear method if available.
        } else {
            // Original logic for clearing without a framebuffer.
        }
    }

    @Overwrite
    public void resize(int i, int j, boolean bl) {
        if (this.framebuffer != null) {
            this.framebuffer.resize(i, j); // Leverage existing resize method on the framebuffer.
        } else {
            this.viewWidth = i;
            this.viewHeight = j;
            this.width = i;
            this.height = j;
            // Allocate new framebuffer only if needed.
            this.framebuffer = new Framebuffer(this.width, this.height, Framebuffer.DEFAULT_FORMAT);
        }
    }

    @Overwrite
    public void bindWrite(boolean updateViewport) {
        Renderer.getInstance().beginRendering(framebuffer);
    }

    @Overwrite
    public void unbindWrite() {
        Renderer.getInstance().endRendering(); // Use endRendering instead of unbindWrite for clarity.
    }

    @Overwrite
    private void _blitToScreen(int width, int height, boolean disableBlend) {
        RenderSystem.depthMask(false); // Consider avoiding unnecessary depthMask calls by caching its state.

        if (disableBlend) {
            RenderSystem.disableBlend(); // Enable/disable blend only if changing state.
        }

        DrawUtil.drawFramebuffer(this.framebuffer);

        if (disableBlend) {
            RenderSystem.enableBlend(); // Re-enable blend if disabled earlier.
        }

        RenderSystem.depthMask(true); // Consider avoiding unnecessary depthMask calls by caching its state.
    }
        }
