package net.vulkanmod.render.vertex;

public class VertexUtil {

    private static final float NORM_INV = 1.0f / 127.0f;
    private static final float COLOR_INV = 1.0f / 255.0f;

    public static int packColor(float r, float g, float b, float a) {
        int rgba = ((int)(r * 255.0f) & 0xFF) |
                ((int)(g * 255.0f) & 0xFF) << 8 |
                ((int)(b * 255.0f) & 0xFF) << 16 |
                ((int)(a * 255.0f) & 0xFF) << 24;
        return rgba;
    }

    public static int packNormal(float x, float y, float z) {
        int nx = (int)(x * NORM_INV * 127.0f);
        int ny = (int)(y * NORM_INV * 127.0f);
        int nz = (int)(z * NORM_INV * 127.0f);
        return (nx & 0xFF) | (ny & 0xFF) << 8 | (nz & 0xFF) << 16;
    }

    public static float unpackColorR(int i) {
        return (i >> 24) & 0xFF;
    }

    public static float unpackColorG(int i) {
        return (i >> 16) & 0xFF;
    }

    public static float unpackColorB(int i) {
        return (i >> 8) & 0xFF;
    }

    public static float unpackN1(int i) {
        return (i & 0xFF) * NORM_INV;
    }

    public static float unpackN2(int i) {
        return (i >> 8) & 0xFF;
    }

    public static float unpackN3(int i) {
        return (i >> 16) & 0xFF;
    }
}
