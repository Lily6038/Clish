package net.clish.runtime;

/**
 * Represents a co-process with bidirectional communication channels.
 */
public class CoProcess {
    public final String name;
    public final Channel in;   // Input channel (main sends to coproc via this)
    public final Channel out;  // Output channel (coproc sends back via this)
    public final int pid;

    public CoProcess(String name, Channel in, Channel out, int pid) {
        this.name = name;
        this.in = in;
        this.out = out;
        this.pid = pid;
    }
}
