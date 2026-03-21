package net.clish.runtime;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages background jobs spawned by the spawn statement.
 */
public class JobManager {
    private static final AtomicInteger nextId = new AtomicInteger(1);
    private static final ConcurrentMap<Integer, Job> jobs = new ConcurrentHashMap<>();
    private static int lastJobId = 0;

    /**
     * Spawn a new background task.
     * Returns the job ID.
     */
    public static int spawn(Runnable task) {
        int id = nextId.getAndIncrement();
        Thread thread = new Thread(task, "clish-job-" + id);
        Job job = new Job(id, thread);
        jobs.put(id, job);
        lastJobId = id;
        thread.start();
        return id;
    }

    /**
     * Wait for a specific job to complete.
     */
    public static void waitFor(int jobId) throws InterruptedException {
        Job job = jobs.get(jobId);
        if (job != null) {
            job.thread.join();
            job.completed = true;
        }
    }

    /**
     * Wait for all jobs to complete.
     */
    public static void waitForAll() throws InterruptedException {
        for (Job job : jobs.values()) {
            job.thread.join();
            job.completed = true;
        }
    }

    /**
     * Wait for a specific job with timeout.
     * Returns true if the job completed, false if timeout occurred.
     */
    public static boolean waitFor(int jobId, long timeoutMs) throws InterruptedException {
        Job job = jobs.get(jobId);
        if (job == null) {
            return true;
        }
        long deadline = System.currentTimeMillis() + timeoutMs;
        long remaining = timeoutMs;
        while (job.thread.isAlive() && remaining > 0) {
            job.thread.join(remaining);
            remaining = deadline - System.currentTimeMillis();
        }
        if (!job.thread.isAlive()) {
            job.completed = true;
            return true;
        }
        return false;
    }

    /**
     * Get the ID of the last spawned job.
     */
    public static int getLastJobId() {
        return lastJobId;
    }

    /**
     * Clean up completed jobs.
     */
    public static void cleanup() {
        jobs.entrySet().removeIf(entry -> entry.getValue().completed);
    }
}
