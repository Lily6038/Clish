package net.clish.runtime;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A channel for message passing between concurrent tasks.
 * Based on BlockingQueue for thread-safe operations.
 */
public class Channel {
    private final BlockingQueue<Object> queue;
    private final int bufferSize;

    public Channel() {
        this(0);  // Unbuffered by default
    }

    public Channel(int bufferSize) {
        this.bufferSize = bufferSize;
        if (bufferSize == 0) {
            this.queue = new LinkedBlockingQueue<>();
        } else {
            this.queue = new LinkedBlockingQueue<>(bufferSize);
        }
    }

    /**
     * Send a value to the channel. Blocks if the channel is full (for buffered channels).
     */
    public void send(Object value) throws InterruptedException {
        queue.put(value);
    }

    /**
     * Try to send a value without blocking. Returns false if the channel is full.
     */
    public boolean trySend(Object value) {
        return queue.offer(value);
    }

    /**
     * Receive a value from the channel. Blocks until a value is available.
     */
    public Object receive() throws InterruptedException {
        return queue.take();
    }

    /**
     * Try to receive a value without blocking. Returns null if the channel is empty.
     */
    public Object tryReceive() {
        return queue.poll();
    }

    public int getBufferSize() {
        return bufferSize;
    }
}
