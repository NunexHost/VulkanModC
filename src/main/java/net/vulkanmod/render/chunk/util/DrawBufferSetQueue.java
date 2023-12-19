package net.vulkanmod.render.chunk.util;

import net.vulkanmod.render.chunk.DrawBuffers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

public record DrawBufferSetQueue(int size, int[] set, StaticQueue<DrawBuffers> queue, ConcurrentHashMap<Integer, ConcurrentLinkedQueue<DrawBuffers>> cache)
{

    public DrawBufferSetQueue(int size) {
        this(size, new int[(int) Math.ceil((float)size / Integer.SIZE)], new StaticQueue<>(size), new ConcurrentHashMap<>());
    }

    public void add(DrawBuffers chunkArea) {
        if(chunkArea.areaIndex >= this.size)
            throw new IndexOutOfBoundsException();

        int i = chunkArea.areaIndex >> 5;
        if((this.set[i] & (1 << (chunkArea.areaIndex & 31))) == 0) {
            this.queue.add(chunkArea);
            this.set[i] |= (1 << (chunkArea.areaIndex & 31));
            this.cache.computeIfAbsent(chunkArea.areaIndex, k -> new ConcurrentLinkedQueue<>()).add(chunkArea);
        }
    }

    public void clear() {
        Arrays.fill(this.set, 0);

        this.queue.clear();
        this.cache.clear();
    }

    public Iterator<DrawBuffers> iterator(boolean reverseOrder) {
        return queue.iterator(reverseOrder);
    }

    public Iterator<DrawBuffers> iterator(int areaIndex, boolean reverseOrder) {
        ConcurrentLinkedQueue<DrawBuffers> queue = this.cache.get(areaIndex);
        if (queue == null) {
            return iterator(reverseOrder);
        }

        return queue.iterator(reverseOrder);
    }
}
