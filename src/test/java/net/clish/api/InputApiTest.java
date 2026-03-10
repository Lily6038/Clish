package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InputApiTest {

    @Test
    void testLeftClickApiName() {
        assertEquals("input.leftClick", new InputApi.LeftClick().getName());
    }

    @Test
    void testRightClickApiName() {
        assertEquals("input.rightClick", new InputApi.RightClick().getName());
    }

    @Test
    void testMouseMoveApiName() {
        assertEquals("input.mouseMove", new InputApi.MouseMove().getName());
    }

    @Test
    void testKeyPressApiName() {
        assertEquals("input.keyPress", new InputApi.KeyPress().getName());
    }

    @Test
    void testTypeTextApiName() {
        assertEquals("input.typeText", new InputApi.TypeText().getName());
    }

    @Test
    void testGetAllReturnsAllFunctions() {
        List<?> functions = InputApi.getAll();
        assertEquals(5, functions.size());
    }
}
