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

    // Error handling keywords
    TRY,
    CATCH,
    FINALLY,
    OK,
    ERR,

    // Concurrency keywords
    CHANNEL,
    SEND,
    RECEIVE,
    TRY_RECEIVE,
    SPAWN,
    WAIT,
    COPROC,

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
    PIPE,         // |

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
