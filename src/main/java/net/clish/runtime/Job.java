package net.clish.runtime;

/**
 * Represents a background job spawned by the spawn statement.
 */
public class Job {
    public final int id;
    public final Thread thread;
    public volatile boolean completed;

    public Job(int id, Thread thread) {
        this.id = id;
        this.thread = thread;
        this.completed = false;
    }
}
