package net.vulkanmod.render.vertex;

public class VertexUtil {

    private static final float NORM_INV = 1.0f / 127.0f;
    private static final float COLOR_SCALE = 1.0f / 255.0f;

    public static int packColor(float r, float g, float b, float a) {
        return ((int)(r * COLOR_SCALE) & 0xFF) |
                ((int)(g * COLOR_SCALE) & 0xFF) << 8 |
                ((int)(b * COLOR_SCALE) & 0xFF) << 16 |
                ((int)(a * COLOR_SCALE) & 0xFF) << 24;
    }

    public static int packNormal(float x, float y, float z) {
        return ((int)(x * NORM_INV) & 0xFF) |
                ((int)(y * NORM_INV) & 0xFF) << 8 |
                ((int)(z * NORM_INV) & 0xFF) << 16;
    }

    public static float unpackColor(int i, int component) {
        return ((i >> (component * 8)) & 0xFF) * COLOR_SCALE;
    }

    public static float unpackNormal(int i, int component) {
        return ((i >> (component * 8)) & 0xFF) * NORM_INV;
    }
}
