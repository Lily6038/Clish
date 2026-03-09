package net.clish.lexer;

/**
 * Token types for the Clish lexer.
 */
public enum TokenType {
    // Literals
    IDENTIFIER,
    NUMBER,
    STRING,

    // Keywords
    IF,
    ELSE,
    ELIF,
    FOR,
    WHILE,
    DO,
    FUNCTION,
    RETURN,
    BREAK,
    CONTINUE,
    LOCAL,
    TRUE,
    FALSE,
    NULL,

    // Operators
    PLUS,         // +
    MINUS,        // -
    MULTIPLY,     // *
    DIVIDE,       // /
    MODULO,       // %
    ASSIGN,       // =
    EQUAL,        // ==
    NOT_EQUAL,    // !=
    LESS,         // <
    LESS_EQUAL,   // <=
    GREATER,      // >
    GREATER_EQUAL,// >=
    AND,          // &&
    OR,           // ||
    NOT,          // !
    QUESTION,     // ?
    COLON,        // :

    // Delimiters
    LPAREN,       // (
    RPAREN,       // )
    LBRACE,       // {
    RBRACE,       // }
    LBRACKET,     // [
    RBRACKET,     // ]
    COMMA,        // ,
    SEMICOLON,    // ;
    DOT,          // .
    DOLLAR,       // $

    // Special
    NEWLINE,
    COMMENT,
    EOF
}
