package net.clish.api;

import net.clish.ast.ClishLibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Input simulation API for simulating mouse/keyboard input.
 */
public class InputApi {
    private static final Logger LOGGER = LoggerFactory.getLogger("Clish");

    /**
     * Simulate left click.
     */
    public static class LeftClick implements ClishLibrary {
        @Override
        public String getName() {
            return "input.leftClick";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("InputApi.{} called with args: {}", getName(), args);
            // TODO: Implement via Mixin
            return "Input simulation requires Minecraft runtime";
        }
    }

    /**
     * Simulate right click.
     */
    public static class RightClick implements ClishLibrary {
        @Override
        public String getName() {
            return "input.rightClick";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("InputApi.{} called with args: {}", getName(), args);
            // TODO: Implement via Mixin
            return "Input simulation requires Minecraft runtime";
        }
    }

    /**
     * Move mouse.
     */
    public static class MouseMove implements ClishLibrary {
        @Override
        public String getName() {
            return "input.mouseMove";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("InputApi.{} called with args: {}", getName(), args);
            // TODO: Implement via Mixin
            return "Input simulation requires Minecraft runtime";
        }
    }

    /**
     * Press a key.
     */
    public static class KeyPress implements ClishLibrary {
        @Override
        public String getName() {
            return "input.keyPress";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("InputApi.{} called with args: {}", getName(), args);
            // TODO: Implement via Mixin
            if (args.isEmpty()) return "Key code required";
            return "Input simulation requires Minecraft runtime";
        }
    }

    /**
     * Type text.
     */
    public static class TypeText implements ClishLibrary {
        @Override
        public String getName() {
            return "input.typeText";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("InputApi.{} called with args: {}", getName(), args);
            // TODO: Implement via Mixin
            if (args.isEmpty()) return "Text required";
            return "Input simulation requires Minecraft runtime";
        }
    }

    /**
     * Get all input API functions.
     */
    public static List<ClishLibrary> getAll() {
        return List.of(
            new LeftClick(),
            new RightClick(),
            new MouseMove(),
            new KeyPress(),
            new TypeText()
        );
    }
}
