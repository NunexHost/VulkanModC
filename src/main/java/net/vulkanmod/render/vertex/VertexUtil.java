package net.vulkanmod.render.vertex;

public class VertexUtil {

    private static final int NORM_SHIFT = 8;
    private static final int COLOR_SHIFT = 24;

    public static int packColor(float r, float g, float b, float a) {
        return (int)r << COLOR_SHIFT | (int)g << (COLOR_SHIFT - 8) | (int)b << (COLOR_SHIFT - 16) | (int)a;
    }

    public static int packNormal(float x, float y, float z) {
        return (int)x << NORM_SHIFT | (int)y << (NORM_SHIFT - 8) | (int)z;
    }

    public static float unpackColorR(int i) {
        return (i >> COLOR_SHIFT) & 0xFF;
    }

    public static float unpackColorG(int i) {
        return (i >> (COLOR_SHIFT - 8)) & 0xFF;
    }

    public static float unpackColorB(int i) {
        return (i >> (COLOR_SHIFT - 16)) & 0xFF;
    }

    public static float unpackN1(int i) {
        return (i >> NORM_SHIFT) & 0xFF;
    }

    public static float unpackN2(int i) {
        return (i >> (NORM_SHIFT - 8)) & 0xFF;
    }

    public static float unpackN3(int i) {
        return i & 0xFF;
    }

}
