package net.vulkanmod.render.texture;

import net.vulkanmod.vulkan.texture.VulkanImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkCommandBuffer;

import java.util.HashMap;
import java.util.Map;

public abstract class SpriteUtil {

    private static ThreadLocal<Boolean> doUpload = ThreadLocal.withInitial(() -> false);
    private static Map<VulkanImage, Boolean> transitionedLayouts = new HashMap<>();

    public static void setDoUpload(boolean b) {
        doUpload.set(b);
    }

    public static boolean shouldUpload() {
        return doUpload.get();
    }

    public static void addTransitionedLayout(VulkanImage image) {
        transitionedLayouts.put(image, true);
    }

    public static void transitionLayouts(VkCommandBuffer commandBuffer) {
        transitionedLayouts.forEach((image, enable) -> {
            if (enable) {
                image.readOnlyLayout(commandBuffer);
            }
        });

        transitionedLayouts.clear();
    }
}
