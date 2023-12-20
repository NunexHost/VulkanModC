package net.vulkanmod.render.chunk.util;

import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Util {

    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Direction[] XZ_DIRECTIONS;

    static {
        // Pre-cache DIRECTIONS
        XZ_DIRECTIONS = new Direction[4];
        for (Direction direction : DIRECTIONS) {
            if (direction.getAxis() == Direction.Axis.X || direction.getAxis() == Direction.Axis.Z) {
                XZ_DIRECTIONS[XZ_DIRECTIONS.length - 1] = direction;
                XZ_DIRECTIONS.length--;
            }
        }
    }

    public static long posLongHash(int x, int y, int z) {
        // Use bit shifts instead of masks and logical ANDs
        return (long) x << 32 | (long) z << 16 | (long) y;
    }

    public static int flooredLog(int v) {
        // Use a lookup table for pre-computed logarithms
        final int[] LOG2_TABLE = {
            0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
        };
        return LOG2_TABLE[v];
    }

    public static int align(int i, int alignment) {
        // Use the bitwise AND operation with the complement of the alignment mask
        return i & ~(alignment - 1);
    }

    public static ByteBuffer createCopy(ByteBuffer src) {
        // Use ByteBuffer.slice() to create a view of the existing buffer
        return src.slice();
    }
}
