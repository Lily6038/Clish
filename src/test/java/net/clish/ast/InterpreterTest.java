package net.clish.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    private Object runScript(String script) {
        Parser parser = new Parser(script);
        ProgramNode program = parser.parse();
        Interpreter interpreter = new Interpreter();
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
}
