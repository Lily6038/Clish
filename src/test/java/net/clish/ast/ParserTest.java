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

    // ==================== Error Handling Tests ====================

    @Test
    void testParseOkExpression() {
        Parser parser = new Parser("ok(42)");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
        assertTrue(program.getStatements().get(0) instanceof ExpressionStatementNode);
    }

    @Test
    void testParseErrExpression() {
        Parser parser = new Parser("err(\"error\")");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseErrExpressionWithCode() {
        Parser parser = new Parser("err(\"error\", 404)");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseErrorPropagation() {
        Parser parser = new Parser("mightFail()?");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseTryStatement() {
        Parser parser = new Parser("try { risky() } catch (e) { handle(e) }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseTryStatementWithFinally() {
        Parser parser = new Parser("try { risky() } catch (e) { handle(e) } finally { cleanup() }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    // ==================== Concurrency Tests ====================

    @Test
    void testParseChannelExpression() {
        Parser parser = new Parser("channel()");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseBufferedChannelExpression() {
        Parser parser = new Parser("channel(10)");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseSendExpression() {
        Parser parser = new Parser("send(ch, value)");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseReceiveExpression() {
        Parser parser = new Parser("receive(ch)");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseTryReceiveExpression() {
        Parser parser = new Parser("tryReceive(ch)");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseSpawnStatement() {
        Parser parser = new Parser("spawn { echo \"hello\" }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseWaitStatement() {
        Parser parser = new Parser("wait");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseWaitWithJobId() {
        Parser parser = new Parser("wait(jobId)");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseJobId() {
        Parser parser = new Parser("$!");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseCoprocExpression() {
        Parser parser = new Parser("coproc worker { while(true) { receive(self.in) } }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParsePipeExpression() {
        Parser parser = new Parser("data | process");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }
}
