package net.clish.ast;

import net.clish.lexer.Token;
import net.clish.runtime.Channel;
import net.clish.runtime.CoProcess;
import net.clish.runtime.JobManager;
import net.clish.runtime.ResultType;

import java.util.*;
import java.util.function.Consumer;

/**
 * AST Walker interpreter for Clish.
 * Executes the parsed AST with timeout protection.
 */
public class Interpreter {
    private final Map<String, ClishFunction> globalFunctions = new HashMap<>();
    private final Map<String, ClishLibrary> libraries = new HashMap<>();
    private final Map<String, Object> globalVariables = new HashMap<>();
    private final Map<String, CoProcess> coProcesses = new HashMap<>();

    private Scope globalScope;
    private long startTime;
    private long timeoutMs = 60000; // 60 seconds default
    private boolean interrupted = false;
    private Consumer<String> outputConsumer;

    public Interpreter() {
        this.globalScope = new Scope(null);
        initializeBuiltins();
    }

    private void initializeBuiltins() {
        // Built-in functions will be added through libraries
    }

    /**
     * Execute a parsed program.
     */
    public Object execute(ProgramNode program) {
        globalScope = new Scope(null);
        startTime = System.currentTimeMillis();
        interrupted = false;

        Object result = null;

        // First, register all function declarations
        for (ASTNode statement : program.getStatements()) {
            if (statement instanceof FunctionDeclarationNode funcDecl) {
                ClishFunction function = new ClishFunction(
                    funcDecl.getName(),
                    funcDecl.getParameters(),
                    funcDecl.getBody()
                );
                globalScope.define(funcDecl.getName(), function);
            }
        }

        // Then execute statements
        for (ASTNode statement : program.getStatements()) {
            if (interrupted) {
                throw new RuntimeException("Script execution interrupted");
            }
            checkTimeout();

            if (statement instanceof FunctionDeclarationNode) {
                continue; // Already registered
            }

            result = executeStatement(statement, globalScope);
        }

        return result;
    }

    /**
     * Execute a single statement and return its value.
     */
    private Object executeStatement(ASTNode node, Scope scope) {
        checkTimeout();

        if (node instanceof ExpressionStatementNode exprStmt) {
            return evaluate(exprStmt.getExpression(), scope);
        }

        if (node instanceof VariableDeclarationNode varDecl) {
            Object value = null;
            if (varDecl.getInitializer() != null) {
                value = evaluate(varDecl.getInitializer(), scope);
            }
            scope.define(varDecl.getName(), value);
            return null;
        }

        if (node instanceof IfStatementNode ifStmt) {
            return executeIfStatement(ifStmt, scope);
        }

        if (node instanceof ForStatementNode forStmt) {
            return executeForStatement(forStmt, scope);
        }

        if (node instanceof WhileStatementNode whileStmt) {
            return executeWhileStatement(whileStmt, scope);
        }

        if (node instanceof DoWhileStatementNode doWhileStmt) {
            return executeDoWhileStatement(doWhileStmt, scope);
        }

        if (node instanceof ReturnStatementNode returnStmt) {
            Object value = null;
            if (returnStmt.getValue() != null) {
                value = evaluate(returnStmt.getValue(), scope);
            }
            throw new ReturnException(value);
        }

        if (node instanceof BreakStatementNode) {
            throw new BreakException();
        }

        if (node instanceof ContinueStatementNode) {
            throw new ContinueException();
        }

        if (node instanceof TryStatementNode tryStmt) {
            return executeTryStatement(tryStmt, scope);
        }

        if (node instanceof SpawnNode spawnNode) {
            return executeSpawnStatement(spawnNode, scope);
        }

        return null;
    }

    private Object executeTryStatement(TryStatementNode tryStmt, Scope scope) {
        Object result = null;
        try {
            for (ASTNode stmt : tryStmt.getTryBlock()) {
                try {
                    executeStatement(stmt, scope);
                } catch (ReturnException e) {
                    throw e;
                }
            }
        } catch (ReturnException e) {
            result = e.getValue();
        }

        // If result is an error and we have a catch block, execute it
        if (result instanceof ResultType rt && rt.isError()) {
            if (tryStmt.getCatchBlock() != null && !tryStmt.getCatchBlock().isEmpty()) {
                Scope catchScope = new Scope(scope);
                catchScope.define(tryStmt.getCatchVariable(), rt);
                for (ASTNode stmt : tryStmt.getCatchBlock()) {
                    try {
                        executeStatement(stmt, catchScope);
                    } catch (ReturnException e) {
                        throw e;
                    }
                }
            }
        }

        // Execute finally block if present
        if (tryStmt.getFinallyBlock() != null && !tryStmt.getFinallyBlock().isEmpty()) {
            for (ASTNode stmt : tryStmt.getFinallyBlock()) {
                try {
                    executeStatement(stmt, scope);
                } catch (ReturnException e) {
                    throw e;
                }
            }
        }

        return null;
    }

    private Object executeSpawnStatement(SpawnNode spawnNode, Scope scope) {
        // Create a copy of the current scope for the spawned task
        final Scope spawnScope = new Scope(scope);
        final List<ASTNode> block = new ArrayList<>(spawnNode.getBlock());

        Runnable task = () -> {
            for (ASTNode stmt : block) {
                try {
                    executeStatement(stmt, spawnScope);
                } catch (ReturnException e) {
                    // Ignore return in spawned tasks
                }
            }
        };

        int jobId = JobManager.spawn(task);
        return jobId;
    }

    private Object executeIfStatement(IfStatementNode ifStmt, Scope scope) {
        Object condition = evaluate(ifStmt.getCondition(), scope);
        if (isTruthy(condition)) {
            for (ASTNode stmt : ifStmt.getThenBranch()) {
                try {
                    executeStatement(stmt, scope);
                } catch (BreakException | ContinueException | ReturnException e) {
                    throw e;
                }
            }
        } else {
            // Check elif branches
            for (var elif : ifStmt.getElifBranches()) {
                Object elifCondition = evaluate(elif.getFirst(), scope);
                if (isTruthy(elifCondition)) {
                    for (ASTNode stmt : elif.getSecond()) {
                        try {
                            executeStatement(stmt, scope);
                        } catch (BreakException | ContinueException | ReturnException e) {
                            throw e;
                        }
                    }
                    return null;
                }
            }

            // Else branch
            if (!ifStmt.getElseBranch().isEmpty()) {
                for (ASTNode stmt : ifStmt.getElseBranch()) {
                    try {
                        executeStatement(stmt, scope);
                    } catch (BreakException | ContinueException | ReturnException e) {
                        throw e;
                    }
                }
            }
        }
        return null;
    }

    private Object executeForStatement(ForStatementNode forStmt, Scope scope) {
        Scope loopScope = new Scope(scope);

        // Initializer
        if (forStmt.getInitializer() != null) {
            executeStatement(forStmt.getInitializer(), loopScope);
        }

        // Loop
        while (true) {
            checkTimeout();

            // Condition
            if (forStmt.getCondition() != null) {
                Object condValue = evaluate(forStmt.getCondition(), loopScope);
                if (!isTruthy(condValue)) {
                    break;
                }
            }

            try {
                // Body
                for (ASTNode stmt : forStmt.getBody()) {
                    executeStatement(stmt, loopScope);
                }

                // Increment
                if (forStmt.getIncrement() != null) {
                    evaluate(forStmt.getIncrement(), loopScope);
                }
            } catch (BreakException e) {
                break;
            } catch (ContinueException e) {
                // Increment still runs after continue
                if (forStmt.getIncrement() != null) {
                    evaluate(forStmt.getIncrement(), loopScope);
                }
            }
        }

        return null;
    }

    private Object executeWhileStatement(WhileStatementNode whileStmt, Scope scope) {
        while (true) {
            checkTimeout();

            Object condition = evaluate(whileStmt.getCondition(), scope);
            if (!isTruthy(condition)) {
                break;
            }

            try {
                for (ASTNode stmt : whileStmt.getBody()) {
                    executeStatement(stmt, scope);
                }
            } catch (BreakException e) {
                break;
            } catch (ContinueException e) {
                // Continue - re-evaluate condition
            }
        }

        return null;
    }

    private Object executeDoWhileStatement(DoWhileStatementNode doWhileStmt, Scope scope) {
        do {
            checkTimeout();

            try {
                for (ASTNode stmt : doWhileStmt.getBody()) {
                    executeStatement(stmt, scope);
                }
            } catch (BreakException e) {
                break;
            } catch (ContinueException e) {
                // Continue - check condition
            }
        } while (isTruthy(evaluate(doWhileStmt.getCondition(), scope)));

        return null;
    }

    /**
     * Evaluate an expression and return its value.
     */
    private Object evaluate(ASTNode node, Scope scope) {
        checkTimeout();

        if (node == null) {
            return null;
        }

        if (node instanceof NumberLiteralNode numLit) {
            return numLit.isInteger() ? (int) numLit.getValue() : numLit.getValue();
        }

        if (node instanceof StringLiteralNode strLit) {
            return strLit.getValue();
        }

        if (node instanceof BooleanLiteralNode boolLit) {
            return boolLit.getValue();
        }

        if (node instanceof NullLiteralNode) {
            return null;
        }

        if (node instanceof IdentifierNode identifier) {
            // First check scope for variables and user functions
            Object value = scope.get(identifier.getName());
            if (value != null) return value;
            // Then check libraries for built-in functions
            ClishLibrary library = libraries.get(identifier.getName());
            if (library != null) return library;
            return null;
        }

        if (node instanceof AssignmentNode assignment) {
            Object value = evaluate(assignment.getValue(), scope);
            scope.assign(assignment.getTarget(), value);
            return value;
        }

        if (node instanceof BinaryExpressionNode binExpr) {
            return evaluateBinaryExpression(binExpr, scope);
        }

        if (node instanceof UnaryExpressionNode unaryExpr) {
            return evaluateUnaryExpression(unaryExpr, scope);
        }

        if (node instanceof TernaryExpressionNode ternaryExpr) {
            Object condition = evaluate(ternaryExpr.getCondition(), scope);
            return isTruthy(condition)
                ? evaluate(ternaryExpr.getThenExpr(), scope)
                : evaluate(ternaryExpr.getElseExpr(), scope);
        }

        if (node instanceof CallExpressionNode callExpr) {
            return evaluateCallExpression(callExpr, scope);
        }

        if (node instanceof IndexExpressionNode indexExpr) {
            return evaluateIndexExpression(indexExpr, scope);
        }

        if (node instanceof PropertyAccessNode propAccess) {
            return evaluatePropertyAccess(propAccess, scope);
        }

        if (node instanceof ArrayLiteralNode arrayLit) {
            List<Object> elements = new ArrayList<>();
            for (ASTNode elem : arrayLit.getElements()) {
                elements.add(evaluate(elem, scope));
            }
            return elements;
        }

        if (node instanceof ObjectLiteralNode objLit) {
            Map<String, Object> properties = new HashMap<>();
            for (var entry : objLit.getProperties().entrySet()) {
                properties.put(entry.getKey(), evaluate(entry.getValue(), scope));
            }
            return properties;
        }

        // Error handling nodes
        if (node instanceof OkExpressionNode okExpr) {
            Object value = evaluate(okExpr.getValue(), scope);
            return ResultType.ok(value);
        }

        if (node instanceof ErrExpressionNode errExpr) {
            Object message = evaluate(errExpr.getMessage(), scope);
            if (errExpr.getCode() != null) {
                Object code = evaluate(errExpr.getCode(), scope);
                return ResultType.error(message.toString(), ((Number) code).intValue());
            }
            return ResultType.error(message.toString());
        }

        if (node instanceof ErrorPropagationNode errorProp) {
            Object result = evaluate(errorProp.getExpression(), scope);
            if (result instanceof ResultType rt) {
                if (rt.isError()) {
                    throw new ReturnException(rt);
                }
                return rt.getValue();
            }
            // If not a Result, return as-is
            return result;
        }

        // Concurrency nodes
        if (node instanceof ChannelNode channelNode) {
            int bufferSize = 0;
            if (channelNode.getBufferSize() != null) {
                Object size = evaluate(channelNode.getBufferSize(), scope);
                bufferSize = ((Number) size).intValue();
            }
            return new Channel(bufferSize);
        }

        if (node instanceof SendNode sendNode) {
            Object channelObj = evaluate(sendNode.getChannel(), scope);
            Object value = evaluate(sendNode.getValue(), scope);
            if (channelObj instanceof Channel ch) {
                try {
                    ch.send(value);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        }

        if (node instanceof ReceiveNode receiveNode) {
            Object channelObj = evaluate(receiveNode.getChannel(), scope);
            if (channelObj instanceof Channel ch) {
                try {
                    return ch.receive();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return null;
        }

        if (node instanceof TryReceiveNode tryReceiveNode) {
            Object channelObj = evaluate(tryReceiveNode.getChannel(), scope);
            if (channelObj instanceof Channel ch) {
                return ch.tryReceive();
            }
            return null;
        }

        if (node instanceof WaitNode waitNode) {
            if (waitNode.getJobId() != null) {
                Object jobIdObj = evaluate(waitNode.getJobId(), scope);
                int jobId = ((Number) jobIdObj).intValue();
                try {
                    JobManager.waitFor(jobId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                try {
                    JobManager.waitForAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        }

        if (node instanceof JobIdNode) {
            return JobManager.getLastJobId();
        }

        if (node instanceof CoprocNode coprocNode) {
            String name = coprocNode.getName();
            Channel in = new Channel();
            Channel out = new Channel();
            // Pre-create the CoProcess with a placeholder PID, will update after spawn
            CoProcess coproc = new CoProcess(name, in, out, 0);
            coProcesses.put(name, coproc);
            scope.define(name, coproc);

            int pid = JobManager.spawn(() -> {
                // Set up self reference for the coproc
                Scope coprocScope = new Scope(scope);
                coprocScope.define("self", coproc);
                for (ASTNode stmt : coprocNode.getBlock()) {
                    try {
                        executeStatement(stmt, coprocScope);
                    } catch (ReturnException e) {
                        break;
                    }
                }
            });
            // Update with actual PID
            coProcesses.put(name, new CoProcess(name, in, out, pid));
            return coproc;
        }

        if (node instanceof CoprocAccessNode coprocAccess) {
            Object coprocObj = evaluate(coprocAccess.getCoproc(), scope);
            if (coprocObj instanceof CoProcess coproc) {
                switch (coprocAccess.getMember()) {
                    case "in": return coproc.in;
                    case "out": return coproc.out;
                    case "pid": return coproc.pid;
                }
            }
            return null;
        }

        if (node instanceof PipeNode pipeNode) {
            Object left = evaluate(pipeNode.getLeft(), scope);
            Object right = evaluate(pipeNode.getRight(), scope);
            // Pipe: pass left as first argument to right if right is callable
            if (right instanceof ClishFunction func) {
                List<Object> args = new ArrayList<>();
                args.add(left);
                return callFunction(func, args, scope);
            }
            if (right instanceof ClishLibrary library) {
                List<Object> args = new ArrayList<>();
                args.add(left);
                return library.call(args);
            }
            return right;
        }

        return null;
    }

    private Object evaluateBinaryExpression(BinaryExpressionNode expr, Scope scope) {
        Object left = evaluate(expr.getLeft(), scope);
        Object right = evaluate(expr.getRight(), scope);

        String op = expr.getOperator();

        return switch (op) {
            case "+" -> {
                if (left instanceof Number && right instanceof Number) {
                    yield ((Number) left).doubleValue() + ((Number) right).doubleValue();
                }
                if (left instanceof String || right instanceof String) {
                    yield String.valueOf(left) + String.valueOf(right);
                }
                if (left instanceof List && right instanceof List) {
                    List<Object> result = new ArrayList<>((List<?>) left);
                    result.addAll((List<?>) right);
                    yield result;
                }
                yield null;
            }
            case "-" -> ((Number) left).doubleValue() - ((Number) right).doubleValue();
            case "*" -> {
                if (left instanceof Number && right instanceof Number) {
                    yield ((Number) left).doubleValue() * ((Number) right).doubleValue();
                }
                if (left instanceof String && right instanceof Integer) {
                    // String repetition
                    yield ((String) left).repeat((Integer) right);
                }
                yield null;
            }
            case "/" -> ((Number) left).doubleValue() / ((Number) right).doubleValue();
            case "%" -> ((Number) left).doubleValue() % ((Number) right).doubleValue();
            case "==" -> Objects.equals(left, right);
            case "!=" -> !Objects.equals(left, right);
            case "<" -> ((Number) left).doubleValue() < ((Number) right).doubleValue();
            case "<=" -> ((Number) left).doubleValue() <= ((Number) right).doubleValue();
            case ">" -> ((Number) left).doubleValue() > ((Number) right).doubleValue();
            case ">=" -> ((Number) left).doubleValue() >= ((Number) right).doubleValue();
            case "&&" -> isTruthy(left) && isTruthy(right);
            case "||" -> isTruthy(left) || isTruthy(right);
            default -> null;
        };
    }

    private Object evaluateUnaryExpression(UnaryExpressionNode expr, Scope scope) {
        Object operand = evaluate(expr.getOperand(), scope);

        return switch (expr.getOperator()) {
            case "-" -> -((Number) operand).doubleValue();
            case "!" -> !isTruthy(operand);
            default -> null;
        };
    }

    private Object evaluateCallExpression(CallExpressionNode callExpr, Scope scope) {
        Object callee = evaluate(callExpr.getCallee(), scope);

        // Evaluate arguments
        List<Object> args = new ArrayList<>();
        for (ASTNode arg : callExpr.getArguments()) {
            args.add(evaluate(arg, scope));
        }

        // Check if it's a library function
        if (callee instanceof ClishLibrary library) {
            return library.call(args);
        }

        // Check if it's a user-defined function
        if (callee instanceof ClishFunction function) {
            return callFunction(function, args, scope);
        }

        return null;
    }

    private Object callFunction(ClishFunction function, List<Object> args, Scope parentScope) {
        Scope functionScope = new Scope(parentScope);

        // Bind parameters to arguments
        List<String> params = function.getParameters();
        for (int i = 0; i < params.size(); i++) {
            if (i < args.size()) {
                functionScope.define(params.get(i), args.get(i));
            } else {
                functionScope.define(params.get(i), null);
            }
        }

        // Execute function body
        try {
            for (ASTNode stmt : function.getBody()) {
                executeStatement(stmt, functionScope);
            }
        } catch (ReturnException e) {
            return e.getValue();
        }

        return null;
    }

    private Object evaluateIndexExpression(IndexExpressionNode indexExpr, Scope scope) {
        Object object = evaluate(indexExpr.getObject(), scope);
        Object index = evaluate(indexExpr.getIndex(), scope);

        if (object instanceof List list) {
            if (index instanceof Number num) {
                int idx = num.intValue();
                if (idx < 0) idx = list.size() + idx;
                return idx >= 0 && idx < list.size() ? list.get(idx) : null;
            }
        }

        if (object instanceof Map map) {
            return map.get(index.toString());
        }

        return null;
    }

    private Object evaluatePropertyAccess(PropertyAccessNode propAccess, Scope scope) {
        Object object = evaluate(propAccess.getObject(), scope);

        if (object instanceof Map map) {
            return map.get(propAccess.getProperty());
        }

        return null;
    }

    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof Number) return ((Number) value).doubleValue() != 0;
        if (value instanceof String) return !((String) value).isEmpty();
        if (value instanceof List) return !((List<?>) value).isEmpty();
        if (value instanceof Map) return !((Map<?, ?>) value).isEmpty();
        if (value instanceof ResultType rt) return rt.isOk();
        return true;
    }

    private void checkTimeout() {
        if (System.currentTimeMillis() - startTime > timeoutMs) {
            throw new RuntimeException("Script execution timed out");
        }
    }

    /**
     * Register a library function.
     */
    public void registerLibrary(String name, ClishLibrary library) {
        libraries.put(name, library);
        if (outputConsumer != null) {
            library.setOutputConsumer(outputConsumer);
        }
    }

    /**
     * Set the output consumer for library functions.
     */
    public void setOutputConsumer(Consumer<String> outputConsumer) {
        this.outputConsumer = outputConsumer;
        for (ClishLibrary library : libraries.values()) {
            library.setOutputConsumer(outputConsumer);
        }
    }

    /**
     * Set the execution timeout.
     */
    public void setTimeout(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    /**
     * Interrupt the running script.
     */
    public void interrupt() {
        this.interrupted = true;
    }

    /**
     * Get global scope for built-in variables.
     */
    public Map<String, Object> getGlobalVariables() {
        return globalVariables;
    }

    // Exception classes for control flow
    private static class ReturnException extends RuntimeException {
        private final Object value;

        public ReturnException(Object value) {
            super(null, null, true, false);
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }

    private static class BreakException extends RuntimeException {
        public BreakException() {
            super(null, null, true, false);
        }
    }

    private static class ContinueException extends RuntimeException {
        public ContinueException() {
            super(null, null, true, false);
        }
    }
}

/**
 * Represents a user-defined function.
 */
class ClishFunction {
    private final String name;
    private final List<String> parameters;
    private final List<ASTNode> body;

    public ClishFunction(String name, List<String> parameters, List<ASTNode> body) {
        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public List<ASTNode> getBody() {
        return body;
    }
}

/**
 * Scope for variable management.
 */
class Scope {
    private final Scope parent;
    private final Map<String, Object> values = new HashMap<>();

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Object get(String name) {
        if (values.containsKey(name)) {
            return values.get(name);
        }
        if (parent != null) {
            return parent.get(name);
        }
        return null;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public void assign(String name, Object value) {
        if (values.containsKey(name)) {
            values.put(name, value);
        } else if (parent != null) {
            parent.assign(name, value);
        } else {
            values.put(name, value);
        }
    }

    public boolean has(String name) {
        return values.containsKey(name) || (parent != null && parent.has(name));
    }
}
