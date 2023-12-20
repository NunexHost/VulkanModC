package net.vulkanmod.render.chunk.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

public class ResettableQueue<T> implements Iterable<T> {
    T[] queue;
    int head = 0;
    int tail = 0;
    int capacity;

    public ResettableQueue() {
        this(1024);
    }

    @SuppressWarnings("unchecked")
    public ResettableQueue(int initialCapacity) {
        this.capacity = initialCapacity;

        this.queue = (T[])(new Object[capacity]);
    }

    public boolean hasNext() {
        return tail != head;
    }

    public T poll() {
        T t = queue[head];
        head = (head + 1) % capacity;

        return t;
    }

    public void add(T t) {
        if(t == null)
            return;

        if((tail + 1) % capacity == head) {
            // Resize the queue
            int newCapacity = capacity * 2;
            T[] newQueue = (T[])(new Object[newCapacity]);

            // Copy the old queue to the new one
            System.arraycopy(queue, head, newQueue, 0, tail - head);

            // Update the queue variables
            queue = newQueue;
            head = 0;
            tail = tail - head;
        }

        queue[tail] = t;
        tail = (tail + 1) % capacity;
    }

    public int size() {
        return (tail - head) % capacity;
    }

    public void clear() {
        head = 0;
        tail = 0;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            int pos = head;

            @Override
            public boolean hasNext() {
                return pos != tail;
            }

            @Override
            public T next() {
                T t = queue[pos];
                pos = (pos + 1) % capacity;

                return t;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Arrays.stream(queue, head, tail).forEach(action);
    }
}
