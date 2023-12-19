package net.vulkanmod.render.chunk.util;

import net.vulkanmod.render.chunk.DrawBuffers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public record DrawBufferSetQueue(long occupied, ConcurrentLinkedQueue<DrawBuffers> queue) {

    public DrawBufferSetQueue(int size) {
        this(0L, new ConcurrentLinkedQueue<>(size));
    }

    private static final long[] offsets = new long[32];

    static {
        for (int i = 0; i < 32; i++) {
            offsets[i] = 1L << i;
        }
    }

    public void add(DrawBuffers chunkArea) {
        if (chunkArea.areaIndex >= Long.SIZE) {
            throw new IndexOutOfBoundsException();
        }

        int index = chunkArea.areaIndex;
        int i = index >> 5; // bitset index
        long mask = offsets[index & 31]; // bitmask for this index

        if ((occupied & mask) == 0) {
            occupied |= mask;
            queue.add(chunkArea);
        }
    }

    public void clear() {
        occupied = 0;
        queue.clear();
    }

    public Iterator<DrawBuffers> iterator(boolean reverseOrder) {
        return queue.iterator(reverseOrder);
    }

    public Iterator<DrawBuffers> iterator() {
        return this.iterator(false);
    }
}
