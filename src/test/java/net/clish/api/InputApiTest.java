package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InputApiTest {

    @Test
    void testLeftClickReturnsPlaceholder() {
        InputApi.LeftClick fn = new InputApi.LeftClick();
        Object result = fn.call(List.of());
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testRightClickReturnsPlaceholder() {
        InputApi.RightClick fn = new InputApi.RightClick();
        Object result = fn.call(List.of());
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testMouseMoveReturnsPlaceholder() {
        InputApi.MouseMove fn = new InputApi.MouseMove();
        Object result = fn.call(List.of());
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testKeyPressReturnsPlaceholder() {
        InputApi.KeyPress fn = new InputApi.KeyPress();
        Object result = fn.call(List.of(65)); // 'A' key
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testKeyPressRequiresArgs() {
        InputApi.KeyPress fn = new InputApi.KeyPress();
        Object result = fn.call(List.of());
        assertEquals("Key code required", result);
    }

    @Test
    void testTypeTextReturnsPlaceholder() {
        InputApi.TypeText fn = new InputApi.TypeText();
        Object result = fn.call(List.of("hello"));
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testTypeTextRequiresArgs() {
        InputApi.TypeText fn = new InputApi.TypeText();
        Object result = fn.call(List.of());
        assertEquals("Text required", result);
    }

    @Test
    void testApiNames() {
        assertEquals("input.leftClick", new InputApi.LeftClick().getName());
        assertEquals("input.rightClick", new InputApi.RightClick().getName());
        assertEquals("input.mouseMove", new InputApi.MouseMove().getName());
        assertEquals("input.keyPress", new InputApi.KeyPress().getName());
        assertEquals("input.typeText", new InputApi.TypeText().getName());
    }

    @Test
    void testGetAllReturnsFiveFunctions() {
        List<?> all = InputApi.getAll();
        assertEquals(5, all.size());
    }
}
