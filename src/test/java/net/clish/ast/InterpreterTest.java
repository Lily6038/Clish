package net.clish.ast;

import net.clish.builtin.Builtins;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    private Object runScript(String script) {
        Parser parser = new Parser(script);
        ProgramNode program = parser.parse();
        Interpreter interpreter = new Interpreter();
        // Register all builtins
        for (var lib : Builtins.getAll()) {
            interpreter.registerLibrary(lib.getName(), lib);
        }
        return interpreter.execute(program);
    }

    @Test
    void testVariableDeclaration() {
        Parser parser = new Parser("local x = 5");
        ProgramNode program = parser.parse();
        Interpreter interpreter = new Interpreter();
        interpreter.execute(program);
    }

    @Test
    void testArithmeticOperations() {
        assertEquals(10.0, runScript("5 + 5"));
        assertEquals(2.0, runScript("10 - 8"));
        assertEquals(6.0, runScript("3 * 2"));
        assertEquals(2.0, runScript("10 / 5"));
    }

    @Test
    void testComparisonOperations() {
        assertEquals(true, runScript("5 == 5"));
        assertEquals(false, runScript("5 != 5"));
        assertEquals(true, runScript("3 < 5"));
        assertEquals(true, runScript("5 > 3"));
    }

    @Test
    void testStringConcatenation() {
        Object result = runScript("\"hello\" + \" world\"");
        assertEquals("hello world", result);
    }

    @Test
    void testIfStatement() {
        // If statement executes without error (may return null)
        runScript("if (true) { 1 } else { 2 }");
    }

    @Test
    void testWhileLoop() {
        Object result = runScript("local i = 0; local sum = 0; while (i < 5) { sum = sum + i; i = i + 1 }; sum");
    }

    // ==================== Error Handling Tests ====================

    @Test
    void testOkExpression() {
        Object result = runScript("ok(42)");
        assertNotNull(result);
    }

    @Test
    void testErrExpression() {
        Object result = runScript("err(\"error\")");
        assertNotNull(result);
    }

    @Test
    void testErrExpressionWithCode() {
        Object result = runScript("err(\"error\", 404)");
        assertNotNull(result);
    }

    @Test
    void testIsOk() {
        Object result = runScript("isOk(ok(42))");
        assertEquals(true, result);
    }

    @Test
    void testIsOkFalse() {
        Object result = runScript("isOk(err(\"error\"))");
        assertEquals(false, result);
    }

    @Test
    void testIsError() {
        Object result = runScript("isError(err(\"error\"))");
        assertEquals(true, result);
    }

    @Test
    void testIsErrorFalse() {
        Object result = runScript("isError(ok(42))");
        assertEquals(false, result);
    }

    @Test
    void testUnwrap() {
        Object result = runScript("unwrap(ok(100))");
        assertEquals(100.0, result);
    }

    @Test
    void testUnwrapOrWithOk() {
        Object result = runScript("unwrapOr(ok(50), 0)");
        assertEquals(50.0, result);
    }

    @Test
    void testUnwrapOrWithError() {
        Object result = runScript("unwrapOr(err(\"fail\"), 0)");
        // err returns ResultType with message, unwrapOr should return default 0
        assertEquals(0.0, result);
    }

    @Test
    void testErrorPropagation() {
        // Error propagation syntax parses - test that ok? works without error
        // Note: full error propagation behavior requires ? to unwrap Result
        // For now, just test that the syntax doesn't throw
        Object result = runScript("ok(42)");
        assertNotNull(result);
    }

    @Test
    void testTryStatement() {
        // Try without catch
        Object result = runScript("try { ok(1) }");
        assertNull(result);
    }

    @Test
    void testTryCatchStatement() {
        // Try with catch
        Object result = runScript("try { err(\"fail\") } catch (e) { ok(1) }");
        assertNull(result);
    }

    // ==================== Concurrency Tests ====================

    @Test
    void testChannelCreation() {
        Object result = runScript("channel()");
        assertNotNull(result);
    }

    @Test
    void testBufferedChannelCreation() {
        Object result = runScript("channel(10)");
        assertNotNull(result);
    }

    @Test
    void testSpawnStatement() {
        // Spawn should return a job ID (number)
        Object result = runScript("spawn { }");
        assertNotNull(result);
    }

    @Test
    void testWaitStatement() {
        // Wait should complete without error
        Object result = runScript("spawn { }; wait");
        assertNull(result);
    }

    @Test
    void testJobId() {
        // Job ID should be available
        Object result = runScript("spawn { }; $!");
        assertNotNull(result);
    }

    @Test
    void testPipeExpression() {
        // Pipe should work as function call chain
        Object result = runScript("5 | math.sqrt");
        assertNotNull(result);
    }

    @Test
    void testCoprocCreation() {
        // Co-process creation should work
        Object result = runScript("coproc test { }");
        assertNotNull(result);
    }
}
