package net.clish.ast;

import net.clish.lexer.Lexer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void testParseNumberLiteral() {
        Parser parser = new Parser("42");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
        assertTrue(program.getStatements().get(0) instanceof ExpressionStatementNode);
    }

    @Test
    void testParseStringLiteral() {
        Parser parser = new Parser("\"hello\"");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseVariableDeclaration() {
        Parser parser = new Parser("local x = 5");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseIfStatement() {
        Parser parser = new Parser("if (x == 1) { echo hello }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseWhileStatement() {
        Parser parser = new Parser("while (x < 10) { x = x + 1 }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseForStatement() {
        Parser parser = new Parser("for (local i = 0; i < 10; i = i + 1) { echo i }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseFunctionDeclaration() {
        Parser parser = new Parser("function add(a, b) { return a + b }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseBinaryExpression() {
        Parser parser = new Parser("a + b * c");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }
}
