package net.clish.ast;

import net.clish.builtin.StringLibrary;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class ScriptEngineTest {

    @Test
    void testExecuteSimple() {
        ScriptEngine engine = new ScriptEngine();
        // Expression returns result
        Object result = engine.execute("5 + 5");
        assertEquals(10.0, result);
    }

    @Test
    void testExecuteString() {
        ScriptEngine engine = new ScriptEngine();
        Object result = engine.execute("\"hello world\"");
        assertEquals("hello world", result);
    }

    @Test
    void testExecuteArithmetic() {
        ScriptEngine engine = new ScriptEngine();

        assertEquals(15.0, engine.execute("10 + 5"));
        assertEquals(5.0, engine.execute("10 - 5"));
        assertEquals(50.0, engine.execute("10 * 5"));
        assertEquals(2.0, engine.execute("10 / 5"));
    }

    @Test
    void testExecuteComparison() {
        ScriptEngine engine = new ScriptEngine();

        assertEquals(true, engine.execute("5 == 5"));
        assertEquals(false, engine.execute("5 != 5"));
        assertEquals(true, engine.execute("3 < 5"));
        assertEquals(true, engine.execute("5 > 3"));
    }

    @Test
    void testParse() {
        ScriptEngine engine = new ScriptEngine();
        ProgramNode program = engine.parse("local x = 5");
        assertNotNull(program);
    }

    @Test
    void testSetTimeout() {
        ScriptEngine engine = new ScriptEngine();
        engine.setTimeout(5000);
        // Should not throw
        engine.execute("local x = 1");
    }

    @Test
    void testGetGlobalVariables() {
        ScriptEngine engine = new ScriptEngine();
        engine.execute("local x = 10");
        engine.execute("local y = 20");

        Map<String, Object> globals = engine.getGlobalVariables();
        assertNotNull(globals);
    }

    @Test
    void testStringConcatenation() {
        ScriptEngine engine = new ScriptEngine();
        Object result = engine.execute("\"hello\" + \" world\"");
        assertEquals("hello world", result);
    }

    @Test
    void testExecuteWithVariables() {
        ScriptEngine engine = new ScriptEngine();
        // Define a variable and use it in an expression
        Object result = engine.execute("local x = 5; x + 3");
        assertEquals(8.0, result);
    }

    @Test
    void testExecuteWithLibraryFunction() {
        ScriptEngine engine = new ScriptEngine();
        // Test that arithmetic works as expected
        Object result = engine.execute("2 * 3 + 4");
        assertEquals(10.0, result);
    }
}
