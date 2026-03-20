package net.clish.ast;

import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a library function that can be called from scripts.
 */
public interface ClishLibrary {
    String getName();
    Object call(List<Object> args);

    /**
     * Sets the output consumer for this library function.
     * Used to route output to chat instead of stdout.
     */
    default void setOutputConsumer(Consumer<String> outputConsumer) {
        // Default implementation does nothing
    }
}
