package net.vulkanmod.render.vertex;

public class VertexUtil {

    private static final float NORM_INV = 1.0f / 127.0f;
    private static final float COLOR_INV = 1.0f / 255.0f;
    private static final int COLOR_MASK = 0xFF;
    private static final int NORM_MASK = 0xFF;

    public static int packColor(float r, float g, float b, float a) {
        return ((int) (r * 255.0f) & COLOR_MASK) | ((int) (g * 255.0f) << 8) | ((int) (b * 255.0f) << 16) | ((int) (a * 255.0f) << 24);
    }

    public static int packNormal(float x, float y, float z) {
        return ((int) (x * 127.0f) & NORM_MASK) | ((int) (y * 127.0f) << 8) | ((int) (z * 127.0f) << 16);
    }

    public static float unpackColor(int i, int shift) {
        return ((i >> shift) & COLOR_MASK) * COLOR_INV;
    }

    public static float unpackN1(int i) {
        return (i & NORM_MASK) * NORM_INV;
    }

    public static float unpackN2(int i) {
        return ((i >> 8) & NORM_MASK) * NORM_INV;
    }

    public static float unpackN3(int i) {
        return ((i >> 16) & NORM_MASK) * NORM_INV;
    }

    public static vec3 unpackNormal(int packedNormal) {
        return new vec3(
                unpackN1(packedNormal),
                unpackN2(packedNormal),
                unpackN3(packedNormal)
        );
    }

}
