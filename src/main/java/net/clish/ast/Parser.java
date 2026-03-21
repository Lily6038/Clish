package net.clish.ast;

import net.clish.lexer.Lexer;
import net.clish.lexer.Token;
import net.clish.lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Recursive descent parser for Clish.
 * Converts token stream into an AST.
 */
public class Parser {
    private final List<Token> tokens;
    private int current = 0;
    private int loopDepth = 0;
    private int functionDepth = 0;

    public Parser(String source) {
        Lexer lexer = new Lexer(source);
        this.tokens = lexer.tokenize();
    }

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Parse the tokens and return the AST.
     */
    public ProgramNode parse() {
        Token firstToken = tokens.isEmpty() ? new Token(TokenType.EOF, "", 1, 1) : tokens.get(0);
        ProgramNode program = new ProgramNode(firstToken);

        while (!isAtEnd()) {
            try {
                if (check(TokenType.NEWLINE)) {
                    advance(); // skip newlines
                    continue;
                }
                program.addStatement(declaration());
            } catch (ParseError error) {
                synchronize();
            }
        }

        return program;
    }

    // ==================== Declaration Parsing ====================

    private ASTNode declaration() {
        if (match(TokenType.FUNCTION)) {
            return functionDeclaration();
        }
        if (match(TokenType.LOCAL)) {
            return variableDeclaration(true);
        }
        return statement();
    }

    private ASTNode variableDeclaration(boolean isLocal) {
        Token nameToken = consume(TokenType.IDENTIFIER, "Expected variable name");
        String name = nameToken.getLiteral();

        ASTNode initializer = null;
        if (match(TokenType.ASSIGN)) {
            initializer = expression();
        }

        // Handle semicolon
        if (match(TokenType.SEMICOLON)) {
            // Ok
        }

        return new VariableDeclarationNode(nameToken, name, initializer, isLocal);
    }

    private ASTNode functionDeclaration() {
        Token nameToken = consume(TokenType.IDENTIFIER, "Expected function name");
        String name = nameToken.getLiteral();

        consume(TokenType.LPAREN, "Expected '(' after function name");
        List<String> parameters = new ArrayList<>();

        if (!check(TokenType.RPAREN)) {
            do {
                Token paramToken = consume(TokenType.IDENTIFIER, "Expected parameter name");
                parameters.add(paramToken.getLiteral());
            } while (match(TokenType.COMMA));
        }

        consume(TokenType.RPAREN, "Expected ')' after parameters");
        consume(TokenType.LBRACE, "Expected '{' before function body");

        functionDepth++;
        List<ASTNode> body = block();
        functionDepth--;

        return new FunctionDeclarationNode(nameToken, name, parameters, body);
    }

    // ==================== Statement Parsing ====================

    private ASTNode statement() {
        if (match(TokenType.IF)) {
            return ifStatement();
        }
        if (match(TokenType.FOR)) {
            return forStatement();
        }
        if (match(TokenType.WHILE)) {
            return whileStatement();
        }
        if (match(TokenType.DO)) {
            return doWhileStatement();
        }
        if (match(TokenType.RETURN)) {
            return returnStatement();
        }
        if (match(TokenType.BREAK)) {
            return breakStatement();
        }
        if (match(TokenType.CONTINUE)) {
            return continueStatement();
        }
        if (match(TokenType.TRY)) {
            return tryStatement();
        }
        if (match(TokenType.SPAWN)) {
            return spawnStatement();
        }
        if (match(TokenType.LBRACE)) {
            // Block statement
            List<ASTNode> statements = new ArrayList<>();
            while (!check(TokenType.RBRACE) && !isAtEnd()) {
                if (check(TokenType.NEWLINE)) {
                    advance();
                    continue;
                }
                statements.add(declaration());
            }
            consume(TokenType.RBRACE, "Expected '}' after block");
            return new ExpressionStatementNode(null, new ObjectLiteralNode(null, new HashMap<>()));
        }

        return expressionStatement();
    }

    private ASTNode ifStatement() {
        consume(TokenType.LPAREN, "Expected '(' after 'if'");
        ASTNode condition = expression();
        consume(TokenType.RPAREN, "Expected ')' after condition");
        consume(TokenType.LBRACE, "Expected '{' before if body");

        loopDepth++;
        List<ASTNode> thenBranch = block();
        loopDepth--;

        List<ASTNode> elseBranch = new ArrayList<>();
        List<Pair<ASTNode, List<ASTNode>>> elifBranches = new ArrayList<>();

        if (match(TokenType.ELSE)) {
            if (match(TokenType.IF)) {
                // Else if
                consume(TokenType.LPAREN, "Expected '(' after 'if'");
                ASTNode elifCondition = expression();
                consume(TokenType.RPAREN, "Expected ')' after condition");
                consume(TokenType.LBRACE, "Expected '{' before elif body");
                loopDepth++;
                List<ASTNode> elifBody = block();
                loopDepth--;
                elifBranches.add(new Pair<>(elifCondition, elifBody));
            } else {
                consume(TokenType.LBRACE, "Expected '{' before else body");
                loopDepth++;
                elseBranch = block();
                loopDepth--;
            }
        } else if (match(TokenType.ELIF)) {
            // Handle elif similarly
            consume(TokenType.LPAREN, "Expected '(' after 'elif'");
            ASTNode elifCondition = expression();
            consume(TokenType.RPAREN, "Expected ')' after condition");
            consume(TokenType.LBRACE, "Expected '{' before elif body");
            loopDepth++;
            List<ASTNode> elifBody = block();
            loopDepth--;
            elifBranches.add(new Pair<>(elifCondition, elifBody));
        }

        return new IfStatementNode(null, condition, thenBranch, elifBranches, elseBranch);
    }

    private ASTNode forStatement() {
        consume(TokenType.LPAREN, "Expected '(' after 'for'");

        ASTNode initializer = null;
        if (!check(TokenType.SEMICOLON)) {
            if (match(TokenType.LOCAL)) {
                initializer = variableDeclaration(true);
            } else {
                initializer = expressionStatement();
            }
        } else {
            consume(TokenType.SEMICOLON, "Expected ';'");
        }

        ASTNode condition = null;
        if (!check(TokenType.SEMICOLON)) {
            condition = expression();
        }
        consume(TokenType.SEMICOLON, "Expected ';' after loop condition");

        ASTNode increment = null;
        if (!check(TokenType.RPAREN)) {
            increment = expression();
        }
        consume(TokenType.RPAREN, "Expected ')' after for clauses");
        consume(TokenType.LBRACE, "Expected '{' before for body");

        loopDepth++;
        List<ASTNode> body = block();
        loopDepth--;

        return new ForStatementNode(null, initializer, condition, increment, body);
    }

    private ASTNode whileStatement() {
        consume(TokenType.LPAREN, "Expected '(' after 'while'");
        ASTNode condition = expression();
        consume(TokenType.RPAREN, "Expected ')' after condition");
        consume(TokenType.LBRACE, "Expected '{' before while body");

        loopDepth++;
        List<ASTNode> body = block();
        loopDepth--;

        return new WhileStatementNode(null, condition, body);
    }

    private ASTNode doWhileStatement() {
        consume(TokenType.LBRACE, "Expected '{' before do body");

        loopDepth++;
        List<ASTNode> body = block();
        loopDepth--;

        consume(TokenType.WHILE, "Expected 'while' after do body");
        consume(TokenType.LPAREN, "Expected '(' after 'while'");
        ASTNode condition = expression();
        consume(TokenType.RPAREN, "Expected ')' after condition");

        return new DoWhileStatementNode(null, body, condition);
    }

    private ASTNode returnStatement() {
        Token returnToken = previous();

        ASTNode value = null;
        if (!check(TokenType.SEMICOLON) && !check(TokenType.NEWLINE)) {
            value = expression();
        }

        // Consume optional semicolon
        match(TokenType.SEMICOLON);

        return new ReturnStatementNode(returnToken, value);
    }

    private ASTNode breakStatement() {
        Token breakToken = previous();
        if (loopDepth == 0) {
            error(breakToken, "Cannot use 'break' outside of a loop");
        }
        match(TokenType.SEMICOLON);
        return new BreakStatementNode(breakToken);
    }

    private ASTNode continueStatement() {
        Token continueToken = previous();
        if (loopDepth == 0) {
            error(continueToken, "Cannot use 'continue' outside of a loop");
        }
        match(TokenType.SEMICOLON);
        return new ContinueStatementNode(continueToken);
    }

    private ASTNode tryStatement() {
        consume(TokenType.LBRACE, "Expected '{' before try block");
        loopDepth++;
        List<ASTNode> tryBlock = block();
        loopDepth--;

        String catchVariable = null;
        List<ASTNode> catchBlock = new ArrayList<>();
        List<ASTNode> finallyBlock = null;

        if (match(TokenType.CATCH)) {
            consume(TokenType.LPAREN, "Expected '(' after 'catch'");
            Token varToken = consume(TokenType.IDENTIFIER, "Expected variable name in catch");
            catchVariable = varToken.getLiteral();
            consume(TokenType.RPAREN, "Expected ')' after catch variable");
            consume(TokenType.LBRACE, "Expected '{' before catch block");
            loopDepth++;
            catchBlock = block();
            loopDepth--;
        }

        if (match(TokenType.FINALLY)) {
            consume(TokenType.LBRACE, "Expected '{' before finally block");
            loopDepth++;
            finallyBlock = block();
            loopDepth--;
        }

        return new TryStatementNode(null, tryBlock, catchVariable, catchBlock, finallyBlock);
    }

    private ASTNode spawnStatement() {
        Token spawnToken = previous();
        consume(TokenType.LBRACE, "Expected '{' before spawn block");
        functionDepth++;
        List<ASTNode> block = block();
        functionDepth--;
        return new SpawnNode(spawnToken, block);
    }

    private ASTNode expressionStatement() {
        ASTNode expr = expression();
        match(TokenType.SEMICOLON);
        return new ExpressionStatementNode(null, expr);
    }

    private List<ASTNode> block() {
        List<ASTNode> statements = new ArrayList<>();

        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            if (check(TokenType.NEWLINE)) {
                advance();
                continue;
            }
            statements.add(declaration());
        }

        if (!isAtEnd()) {
            consume(TokenType.RBRACE, "Expected '}' after block");
        }

        return statements;
    }

    // ==================== Expression Parsing ====================

    private ASTNode expression() {
        return pipe();
    }

    // Pipe: lowest precedence (below assignment)
    private ASTNode pipe() {
        ASTNode left = assignment();

        while (match(TokenType.PIPE)) {
            ASTNode right = assignment();
            left = new PipeNode(previous(), left, right);
        }

        return left;
    }

    // Error propagation: expression?
    private ASTNode errorPropagation() {
        ASTNode expr = or();

        if (match(TokenType.QUESTION)) {
            return new ErrorPropagationNode(previous(), expr);
        }

        return expr;
    }

    private ASTNode assignment() {
        ASTNode expr = or();

        if (match(TokenType.ASSIGN)) {
            ASTNode value = assignment();
            if (expr instanceof IdentifierNode identifier) {
                return new AssignmentNode(previous(), identifier.getName(), value);
            }
            error(previous(), "Invalid assignment target");
        }

        return expr;
    }

    private ASTNode ternary() {
        ASTNode condition = errorPropagation();

        if (match(TokenType.QUESTION)) {
            ASTNode thenExpr = ternary();
            consume(TokenType.COLON, "Expected ':' in ternary expression");
            ASTNode elseExpr = ternary();
            return new TernaryExpressionNode(previous(), condition, thenExpr, elseExpr);
        }

        return condition;
    }

    private ASTNode or() {
        ASTNode left = and();

        while (match(TokenType.OR)) {
            String operator = previous().getLiteral();
            ASTNode right = and();
            left = new BinaryExpressionNode(previous(), left, operator, right);
        }

        return left;
    }

    private ASTNode and() {
        ASTNode left = equality();

        while (match(TokenType.AND)) {
            String operator = previous().getLiteral();
            ASTNode right = equality();
            left = new BinaryExpressionNode(previous(), left, operator, right);
        }

        return left;
    }

    private ASTNode equality() {
        ASTNode left = comparison();

        while (match(TokenType.EQUAL, TokenType.NOT_EQUAL)) {
            String operator = previous().getLiteral();
            ASTNode right = comparison();
            left = new BinaryExpressionNode(previous(), left, operator, right);
        }

        return left;
    }

    private ASTNode comparison() {
        ASTNode left = addition();

        while (match(TokenType.LESS, TokenType.LESS_EQUAL, TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            String operator = previous().getLiteral();
            ASTNode right = addition();
            left = new BinaryExpressionNode(previous(), left, operator, right);
        }

        return left;
    }

    private ASTNode addition() {
        ASTNode left = multiplication();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            String operator = previous().getLiteral();
            ASTNode right = multiplication();
            left = new BinaryExpressionNode(previous(), left, operator, right);
        }

        return left;
    }

    private ASTNode multiplication() {
        ASTNode left = unary();

        while (match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULO)) {
            String operator = previous().getLiteral();
            ASTNode right = unary();
            left = new BinaryExpressionNode(previous(), left, operator, right);
        }

        return left;
    }

    private ASTNode unary() {
        if (match(TokenType.NOT, TokenType.MINUS)) {
            String operator = previous().getLiteral();
            ASTNode operand = unary();
            return new UnaryExpressionNode(previous(), operator, operand);
        }

        return call();
    }

    private ASTNode call() {
        ASTNode expr = primary();

        while (true) {
            if (match(TokenType.LPAREN)) {
                // Function call
                List<ASTNode> arguments = new ArrayList<>();
                if (!check(TokenType.RPAREN)) {
                    do {
                        arguments.add(expression());
                    } while (match(TokenType.COMMA));
                }
                consume(TokenType.RPAREN, "Expected ')' after arguments");
                expr = new CallExpressionNode(previous(), expr, arguments);
            } else if (match(TokenType.LBRACKET)) {
                // Index access
                ASTNode index = expression();
                consume(TokenType.RBRACKET, "Expected ']' after index");
                expr = new IndexExpressionNode(previous(), expr, index);
            } else if (match(TokenType.DOT)) {
                // Property access
                Token propertyToken = consume(TokenType.IDENTIFIER, "Expected property name");
                expr = new PropertyAccessNode(previous(), expr, propertyToken.getLiteral());
            } else {
                break;
            }
        }

        return expr;
    }

    private ASTNode primary() {
        if (match(TokenType.FALSE)) {
            return new BooleanLiteralNode(previous());
        }
        if (match(TokenType.TRUE)) {
            return new BooleanLiteralNode(previous());
        }
        if (match(TokenType.NULL)) {
            return new NullLiteralNode(previous());
        }
        if (match(TokenType.NUMBER)) {
            return new NumberLiteralNode(previous());
        }
        if (match(TokenType.STRING)) {
            return new StringLiteralNode(previous());
        }
        if (match(TokenType.IDENTIFIER)) {
            return primaryAfterIdentifier();
        }

        // ok(expression) - Success result
        if (match(TokenType.OK)) {
            return okExpression();
        }

        // err(expression) or err(expression, expression) - Error result
        if (match(TokenType.ERR)) {
            return errExpression();
        }

        // channel() or channel(expression) - Create channel
        if (match(TokenType.CHANNEL)) {
            return channelExpression();
        }

        // wait or wait(expression) - Wait for job
        if (match(TokenType.WAIT)) {
            return waitExpression();
        }

        // send(channel, value) - Send to channel
        if (match(TokenType.SEND)) {
            return sendExpression();
        }

        // receive(channel) - Blocking receive
        if (match(TokenType.RECEIVE)) {
            return receiveExpression();
        }

        // tryReceive(channel) - Non-blocking receive
        if (match(TokenType.TRY_RECEIVE)) {
            return tryReceiveExpression();
        }

        // coproc name { block } - Create co-process
        if (match(TokenType.COPROC)) {
            return coprocExpression();
        }

        // $! - Job ID
        if (match(TokenType.DOLLAR)) {
            consume(TokenType.NOT, "Expected '!' after '$' for job ID");
            return new JobIdNode(previous());
        }

        if (match(TokenType.LPAREN)) {
            ASTNode expr = expression();
            consume(TokenType.RPAREN, "Expected ')' after expression");
            return expr;
        }

        if (match(TokenType.LBRACKET)) {
            // Array literal
            List<ASTNode> elements = new ArrayList<>();
            if (!check(TokenType.RBRACKET)) {
                do {
                    elements.add(expression());
                } while (match(TokenType.COMMA));
            }
            consume(TokenType.RBRACKET, "Expected ']' after array elements");
            return new ArrayLiteralNode(previous(), elements);
        }

        if (match(TokenType.LBRACE)) {
            // Object literal
            Map<String, ASTNode> properties = new HashMap<>();
            if (!check(TokenType.RBRACE)) {
                do {
                    Token keyToken = consume(TokenType.IDENTIFIER, "Expected property name");
                    String key = keyToken.getLiteral();
                    consume(TokenType.COLON, "Expected ':' after property name");
                    ASTNode value = expression();
                    properties.put(key, value);
                } while (match(TokenType.COMMA));
            }
            consume(TokenType.RBRACE, "Expected '}' after object literal");
            return new ObjectLiteralNode(previous(), properties);
        }

        // Try to declare a variable if identifier expected
        Token token = peek();
        error(token, "Unexpected token: " + token.getLiteral());
        return new NullLiteralNode(token);
    }

    // Handle identifier followed by .in/.out/.pid or other property access
    private ASTNode primaryAfterIdentifier() {
        ASTNode expr = new IdentifierNode(previous());

        // Check for co-process access: identifier.in, identifier.out, identifier.pid
        if (match(TokenType.DOT)) {
            Token propertyToken = consume(TokenType.IDENTIFIER, "Expected property name");
            String property = propertyToken.getLiteral();
            if (property.equals("in") || property.equals("out") || property.equals("pid")) {
                expr = new CoprocAccessNode(previous(), expr, property);
            } else {
                expr = new PropertyAccessNode(previous(), expr, property);
            }
        }

        // Continue with call/index/property chain
        while (true) {
            if (match(TokenType.LPAREN)) {
                List<ASTNode> arguments = new ArrayList<>();
                if (!check(TokenType.RPAREN)) {
                    do {
                        arguments.add(expression());
                    } while (match(TokenType.COMMA));
                }
                consume(TokenType.RPAREN, "Expected ')' after arguments");
                expr = new CallExpressionNode(previous(), expr, arguments);
            } else if (match(TokenType.LBRACKET)) {
                ASTNode index = expression();
                consume(TokenType.RBRACKET, "Expected ']' after index");
                expr = new IndexExpressionNode(previous(), expr, index);
            } else if (match(TokenType.DOT)) {
                Token propertyToken = consume(TokenType.IDENTIFIER, "Expected property name");
                String property = propertyToken.getLiteral();
                if (property.equals("in") || property.equals("out") || property.equals("pid")) {
                    expr = new CoprocAccessNode(previous(), expr, property);
                } else {
                    expr = new PropertyAccessNode(previous(), expr, property);
                }
            } else {
                break;
            }
        }

        return expr;
    }

    // ok(expression)
    private ASTNode okExpression() {
        consume(TokenType.LPAREN, "Expected '(' after 'ok'");
        ASTNode value = expression();
        consume(TokenType.RPAREN, "Expected ')' after ok value");
        return new OkExpressionNode(previous(), value);
    }

    // err(expression) or err(expression, expression)
    private ASTNode errExpression() {
        consume(TokenType.LPAREN, "Expected '(' after 'err'");
        ASTNode message = expression();
        ASTNode code = null;
        if (match(TokenType.COMMA)) {
            code = expression();
        }
        consume(TokenType.RPAREN, "Expected ')' after err arguments");
        return new ErrExpressionNode(previous(), message, code);
    }

    // channel() or channel(expression)
    private ASTNode channelExpression() {
        consume(TokenType.LPAREN, "Expected '(' after 'channel'");
        ASTNode bufferSize = null;
        if (!check(TokenType.RPAREN)) {
            bufferSize = expression();
        }
        consume(TokenType.RPAREN, "Expected ')' after channel arguments");
        return new ChannelNode(previous(), bufferSize);
    }

    // wait or wait(expression)
    private ASTNode waitExpression() {
        Token waitToken = previous();
        ASTNode jobId = null;
        if (!check(TokenType.LBRACE) && !check(TokenType.NEWLINE) && !check(TokenType.RBRACE) && !isAtEnd()) {
            jobId = expression();
        }
        return new WaitNode(waitToken, jobId);
    }

    // receive(expression) or tryReceive(expression)
    private ASTNode receiveExpression() {
        Token token = previous();
        consume(TokenType.LPAREN, "Expected '(' after receive");
        ASTNode channel = expression();
        consume(TokenType.RPAREN, "Expected ')' after receive channel");
        return new ReceiveNode(token, channel);
    }

    private ASTNode tryReceiveExpression() {
        Token token = previous();
        consume(TokenType.LPAREN, "Expected '(' after tryReceive");
        ASTNode channel = expression();
        consume(TokenType.RPAREN, "Expected ')' after tryReceive channel");
        return new TryReceiveNode(token, channel);
    }

    // send(expression, expression)
    private ASTNode sendExpression() {
        Token token = previous();
        consume(TokenType.LPAREN, "Expected '(' after 'send'");
        ASTNode channel = expression();
        consume(TokenType.COMMA, "Expected ',' between send arguments");
        ASTNode value = expression();
        consume(TokenType.RPAREN, "Expected ')' after send arguments");
        return new SendNode(token, channel, value);
    }

    // coproc IDENTIFIER { block }
    private ASTNode coprocExpression() {
        Token token = previous();
        Token nameToken = consume(TokenType.IDENTIFIER, "Expected co-process name");
        String name = nameToken.getLiteral();
        consume(TokenType.LBRACE, "Expected '{' before co-process body");
        functionDepth++;
        List<ASTNode> block = block();
        functionDepth--;
        return new CoprocNode(token, name, block);
    }

    // ==================== Helper Methods ====================

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return current >= tokens.size() || peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        if (current >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        error(peek(), message);
        return peek();
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().getType() == TokenType.SEMICOLON) return;
            if (check(TokenType.IF) || check(TokenType.FOR) ||
                check(TokenType.WHILE) || check(TokenType.FUNCTION) ||
                check(TokenType.RETURN)) {
                return;
            }
            advance();
        }
    }

    private void error(Token token, String message) {
        throw new ParseError("Parse error at " + token.getLine() + ":" + token.getColumn() + ": " + message);
    }

    /**
     * Custom parse error exception.
     */
    public static class ParseError extends RuntimeException {
        public ParseError(String message) {
            super(message);
        }
    }
}
