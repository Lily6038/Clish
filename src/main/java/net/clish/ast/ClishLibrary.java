package net.clish.ast;

import java.util.List;

/**
 * Represents a library function that can be called from scripts.
 */
public interface ClishLibrary {
    String getName();
    Object call(List<Object> args);
}
