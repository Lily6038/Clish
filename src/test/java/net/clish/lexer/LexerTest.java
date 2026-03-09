package net.clish.lexer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void testKeywords() {
        Lexer lexer = new Lexer("if while for");
        var tokens = lexer.tokenize();
        assertEquals(3, tokens.size());
        assertEquals(TokenType.IF, tokens.get(0).getType());
        assertEquals(TokenType.WHILE, tokens.get(1).getType());
        assertEquals(TokenType.FOR, tokens.get(2).getType());
    }

    @Test
    void testOperators() {
        Lexer lexer = new Lexer("+ - * / = == != < <= > >= && ||");
        var tokens = lexer.tokenize();
        assertEquals(13, tokens.size());
    }

    @Test
    void testStringLiteral() {
        Lexer lexer = new Lexer("\"hello world\"");
        var tokens = lexer.tokenize();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.STRING, tokens.get(0).getType());
        assertEquals("hello world", tokens.get(0).getLiteral());
    }

    @Test
    void testNumberLiteral() {
        Lexer lexer = new Lexer("42 3.14");
        var tokens = lexer.tokenize();
        assertEquals(2, tokens.size());
        assertEquals("42", tokens.get(0).getLiteral());
        assertEquals("3.14", tokens.get(1).getLiteral());
    }

    @Test
    void testIdentifier() {
        Lexer lexer = new Lexer("myVar _private count1");
        var tokens = lexer.tokenize();
        assertEquals(3, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
    }
}
