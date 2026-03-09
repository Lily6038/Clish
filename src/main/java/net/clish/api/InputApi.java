package net.clish.api;

import net.clish.ast.ClishLibrary;

import java.util.List;

/**
 * Input simulation API for simulating mouse/keyboard input.
 */
public class InputApi {

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
            // TODO: Implement via Mixin
            System.out.println("[Input] Left click");
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
            // TODO: Implement via Mixin
            System.out.println("[Input] Right click");
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
            // TODO: Implement via Mixin
            System.out.println("[Input] Mouse move");
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
            // TODO: Implement via Mixin
            if (args.isEmpty()) return "Key code required";
            System.out.println("[Input] Key press: " + args.get(0));
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
            // TODO: Implement via Mixin
            if (args.isEmpty()) return "Text required";
            System.out.println("[Input] Type text: " + args.get(0));
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
