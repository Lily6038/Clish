package net.clish.lexer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Lexer for the Clish language.
 * Converts source code into a stream of tokens.
 */
public class Lexer {
    private final String source;
    private int position = 0;
    private int line = 1;
    private int column = 1;
    private int lineStart = 0;

    private static final Set<String> KEYWORDS = new HashSet<>();
    private static final Map<String, TokenType> KEYWORD_TO_TOKEN = new HashMap<>();

    private static void addKeyword(String keyword, TokenType tokenType) {
        KEYWORDS.add(keyword);
        KEYWORD_TO_TOKEN.put(keyword, tokenType);
    }

    static {
        // Simple keywords that map via toUpperCase()
        KEYWORDS.add("if");
        KEYWORDS.add("else");
        KEYWORDS.add("elif");
        KEYWORDS.add("for");
        KEYWORDS.add("while");
        KEYWORDS.add("do");
        KEYWORDS.add("function");
        KEYWORDS.add("return");
        KEYWORDS.add("break");
        KEYWORDS.add("continue");
        KEYWORDS.add("local");
        KEYWORDS.add("true");
        KEYWORDS.add("false");
        KEYWORDS.add("null");
        KEYWORDS.add("try");
        KEYWORDS.add("catch");
        KEYWORDS.add("finally");
        KEYWORDS.add("ok");
        KEYWORDS.add("err");
        KEYWORDS.add("channel");
        KEYWORDS.add("send");
        KEYWORDS.add("receive");
        KEYWORDS.add("spawn");
        KEYWORDS.add("wait");
        KEYWORDS.add("coproc");
        // Keywords with underscores in token name
        addKeyword("tryReceive", TokenType.TRY_RECEIVE);
    }

    public Lexer(String source) {
        this.source = source;
    }

    /**
     * Tokenize the entire source and return all tokens.
     */
    public java.util.List<Token> tokenize() {
        java.util.List<Token> tokens = new java.util.ArrayList<>();
        Token token;
        do {
            token = nextToken();
            if (token.getType() != TokenType.NEWLINE && token.getType() != TokenType.COMMENT && token.getType() != TokenType.EOF) {
                tokens.add(token);
            }
        } while (token.getType() != TokenType.EOF);
        return tokens;
    }

    /**
     * Get the next token from the input.
     */
    private Token nextToken() {
        // Skip whitespace (but preserve newlines for statement separation)
        while (peek() != '\0' && Character.isWhitespace(peek())) {
            if (peek() == '\n') {
                advance();
                line++;
                column = 1;
                lineStart = position;
                return new Token(TokenType.NEWLINE, "\n", line, 1);
            }
            advance();
        }

        // Check for end of input
        if (peek() == '\0') {
            return new Token(TokenType.EOF, "", line, column);
        }

        // Check for comments
        if (peek() == '#') {
            return scanComment();
        }

        // Check for string literals
        if (peek() == '"' || peek() == '\'') {
            return scanString();
        }

        // Check for numbers
        if (Character.isDigit(peek())) {
            return scanNumber();
        }

        // Check for identifiers and keywords
        if (Character.isLetter(peek()) || peek() == '_') {
            return scanIdentifier();
        }

        // Check for operators and delimiters
        return scanOperator();
    }

    private Token scanComment() {
        int startColumn = column;
        StringBuilder sb = new StringBuilder();
        while (peek() != '\0' && peek() != '\n') {
            sb.append(advance());
        }
        return new Token(TokenType.COMMENT, sb.toString(), line, startColumn);
    }

    private Token scanString() {
        int startColumn = column;
        char quote = peek();
        advance(); // consume opening quote
        StringBuilder sb = new StringBuilder();

        while (peek() != '\0' && peek() != quote) {
            if (peek() == '\\') {
                advance();
                char escaped = advance();
                switch (escaped) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r');
                    case '\\' -> sb.append('\\');
                    case '\'' -> sb.append('\'');
                    case '"' -> sb.append('"');
                    case '$' -> sb.append('$');
                    default -> sb.append(escaped);
                }
            } else if (peek() == '$') {
                // String interpolation will be handled in parsing
                sb.append(advance());
            } else {
                sb.append(advance());
            }
        }

        if (peek() == quote) {
            advance(); // consume closing quote
        }

        return new Token(TokenType.STRING, sb.toString(), line, startColumn);
    }

    private Token scanNumber() {
        int startColumn = column;
        StringBuilder sb = new StringBuilder();
        boolean isFloat = false;

        while (Character.isDigit(peek()) || peek() == '.') {
            if (peek() == '.') {
                if (isFloat) break;
                isFloat = true;
            }
            sb.append(advance());
        }

        return new Token(TokenType.NUMBER, sb.toString(), line, startColumn);
    }

    private Token scanIdentifier() {
        int startColumn = column;
        StringBuilder sb = new StringBuilder();

        while (Character.isLetterOrDigit(peek()) || peek() == '_') {
            sb.append(advance());
        }

        String value = sb.toString();
        TokenType type;
        if (KEYWORD_TO_TOKEN.containsKey(value)) {
            type = KEYWORD_TO_TOKEN.get(value);
        } else if (KEYWORDS.contains(value)) {
            type = TokenType.valueOf(value.toUpperCase());
        } else {
            type = TokenType.IDENTIFIER;
        }

        return new Token(type, value, line, startColumn);
    }

    private Token scanOperator() {
        int startColumn = column;
        char c = advance();

        return switch (c) {
            case '+' -> new Token(TokenType.PLUS, "+", line, startColumn);
            case '-' -> new Token(TokenType.MINUS, "-", line, startColumn);
            case '*' -> new Token(TokenType.MULTIPLY, "*", line, startColumn);
            case '/' -> new Token(TokenType.DIVIDE, "/", line, startColumn);
            case '%' -> new Token(TokenType.MODULO, "%", line, startColumn);
            case '(' -> new Token(TokenType.LPAREN, "(", line, startColumn);
            case ')' -> new Token(TokenType.RPAREN, ")", line, startColumn);
            case '{' -> new Token(TokenType.LBRACE, "{", line, startColumn);
            case '}' -> new Token(TokenType.RBRACE, "}", line, startColumn);
            case '[' -> new Token(TokenType.LBRACKET, "[", line, startColumn);
            case ']' -> new Token(TokenType.RBRACKET, "]", line, startColumn);
            case ',' -> new Token(TokenType.COMMA, ",", line, startColumn);
            case ';' -> new Token(TokenType.SEMICOLON, ";", line, startColumn);
            case '.' -> new Token(TokenType.DOT, ".", line, startColumn);
            case '$' -> new Token(TokenType.DOLLAR, "$", line, startColumn);
            case '?' -> new Token(TokenType.QUESTION, "?", line, startColumn);
            case ':' -> new Token(TokenType.COLON, ":", line, startColumn);
            case '=' -> {
                if (peek() == '=') {
                    advance();
                    yield new Token(TokenType.EQUAL, "==", line, startColumn);
                }
                yield new Token(TokenType.ASSIGN, "=", line, startColumn);
            }
            case '!' -> {
                if (peek() == '=') {
                    advance();
                    yield new Token(TokenType.NOT_EQUAL, "!=", line, startColumn);
                }
                yield new Token(TokenType.NOT, "!", line, startColumn);
            }
            case '<' -> {
                if (peek() == '=') {
                    advance();
                    yield new Token(TokenType.LESS_EQUAL, "<=", line, startColumn);
                }
                if (peek() == '<') {
                    advance();
                    // TODO: << operator for string concatenation
                }
                yield new Token(TokenType.LESS, "<", line, startColumn);
            }
            case '>' -> {
                if (peek() == '=') {
                    advance();
                    yield new Token(TokenType.GREATER_EQUAL, ">=", line, startColumn);
                }
                yield new Token(TokenType.GREATER, ">", line, startColumn);
            }
            case '&' -> {
                if (peek() == '&') {
                    advance();
                    yield new Token(TokenType.AND, "&&", line, startColumn);
                }
                yield new Token(TokenType.DOLLAR, "&", line, startColumn); // Treat single & as error
            }
            case '|' -> {
                if (peek() == '|') {
                    advance();
                    yield new Token(TokenType.OR, "||", line, startColumn);
                }
                yield new Token(TokenType.DOLLAR, "|", line, startColumn); // Treat single | as error
            }
            default -> new Token(TokenType.EOF, "", line, startColumn);
        };
    }

    private char peek() {
        if (position >= source.length()) {
            return '\0';
        }
        return source.charAt(position);
    }

    private char advance() {
        if (position >= source.length()) {
            return '\0';
        }
        char c = source.charAt(position);
        position++;
        column++;
        return c;
    }
}
