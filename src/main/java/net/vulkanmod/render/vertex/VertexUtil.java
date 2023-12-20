package net.vulkanmod.render.vertex;

public class VertexUtil {

    private static final float NORM_INV = 1.0f / 127.0f;
    private static final float COLOR_INV = 1.0f / 255.0f;

    public static int packColor(float r, float g, float b, float a) {
        return (int)(r * 255.0f) | ((int)(g * 255.0f) << 8) | ((int)(b * 255.0f) << 16) | ((int)(a * 255.0f) << 24);
    }

    public static int packNormal(float x, float y, float z) {
        return (int)(x * 127.0f) | ((int)(y * 127.0f) << 8) | ((int)(z * 127.0f) << 16);
    }

    public static float unpackColorR(int i) {
        return (i >> 24) * COLOR_INV;
    }

    public static float unpackColorG(int i) {
        return (i >> 16) * COLOR_INV;
    }

    public static float unpackColorB(int i) {
        return (i >> 8) * COLOR_INV;
    }

    public static float unpackN1(int i) {
        return (i & 0xFF) * NORM_INV;
    }

    public static float unpackN2(int i) {
        return ((i >> 8) & 0xFF) * NORM_INV;
    }

    public static float unpackN3(int i) {
        return ((i >> 16) & 0xFF) * NORM_INV;
    }

}
