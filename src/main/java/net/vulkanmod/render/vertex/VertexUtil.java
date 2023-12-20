package net.vulkanmod.render.vertex;

public class VertexUtil {

    private static final float NORM_INV = 1.0f / 127.0f;
    private static final float COLOR_INV = 1.0f / 255.0f;

    /**
     * Precompute conversion constants.
     */
    private static final float NORM_SCALE = 255.0f * NORM_INV;
    private static final float COLOR_SCALE = 255.0f * COLOR_INV;

    /**
     * Pack a color value into a 32-bit integer.
     *
     * @param r Red component.
     * @param g Green component.
     * @param b Blue component.
     * @param a Alpha component.
     * @return Packed color value.
     */
    public static int packColor(float r, float g, float b, float a) {
        return (int) (r * COLOR_SCALE) |
                ((int) (g * COLOR_SCALE) << 8) |
                ((int) (b * COLOR_SCALE) << 16) |
                ((int) (a * COLOR_SCALE) << 24);
    }

    /**
     * Pack a normal value into a 32-bit integer.
     *
     * @param x X component.
     * @param y Y component.
     * @param z Z component.
     * @return Packed normal value.
     */
    public static int packNormal(float x, float y, float z) {
        return (int) (x * NORM_SCALE) |
                ((int) (y * NORM_SCALE) << 8) |
                ((int) (z * NORM_SCALE) << 16);
    }

    /**
     * Unpack a color value from a 32-bit integer.
     *
     * @param i Packed color value.
     * @param channel Channel to unpack.
     * @return Unpacked color value.
     */
    public static float unpackColor(int i, int channel) {
        return (i >> (24 - (channel * 8))) & 0xFF;
    }

    /**
     * Unpack a normal value from a 32-bit integer.
     *
     * @param i Packed normal value.
     * @param component Index of the component to unpack.
     * @return Unpacked normal value.
     */
    public static float unpackNormal(int i, int component) {
        return (i >> (24 - (component * 8))) & 0xFF;
    }

    /**
     * Combine color unpacking functions into a single function with a parameter indicating the desired channel.
     */
    public static float unpackColor(int i, ColorChannel channel) {
        switch (channel) {
            case RED:
                return unpackColor(i, 0);
            case GREEN:
                return unpackColor(i, 1);
            case BLUE:
                return unpackColor(i, 2);
            case ALPHA:
                return unpackColor(i, 3);
            default:
                throw new IllegalArgumentException("Invalid color channel: " + channel);
        }
    }

    /**
     * Combine normal unpacking functions into a single function with a parameter indicating the component index.
     */
    public static float unpackNormal(int i, int component) {
        return unpackNormal(i, ColorComponent.values()[component]);
    }
}
