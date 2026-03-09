package net.clish.ast;

import net.clish.lexer.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * All AST node types for the Clish language.
 */

// Base class for nodes with a token
abstract class BaseNode implements ASTNode {
    private final Token token;

    protected BaseNode(Token token) {
        this.token = token;
    }

    @Override
    public Token getToken() {
        return token;
    }
}

// Program node - root of the AST
class ProgramNode extends BaseNode {
    private final List<ASTNode> statements;

    public ProgramNode(Token token) {
        super(token);
        this.statements = new ArrayList<>();
    }

    public List<ASTNode> getStatements() {
        return statements;
    }

    public void addStatement(ASTNode statement) {
        statements.add(statement);
    }

    @Override
    public String getType() {
        return "Program";
    }
}

// Expression statement (expression used as a statement)
class ExpressionStatementNode extends BaseNode {
    private final ASTNode expression;

    public ExpressionStatementNode(Token token, ASTNode expression) {
        super(token);
        this.expression = expression;
    }

    public ASTNode getExpression() {
        return expression;
    }

    @Override
    public String getType() {
        return "ExpressionStatement";
    }
}

// Literal nodes
class NumberLiteralNode extends BaseNode {
    private final double value;
    private final boolean isInteger;

    public NumberLiteralNode(Token token) {
        super(token);
        this.value = Double.parseDouble(token.getLiteral());
        this.isInteger = token.getLiteral().indexOf('.') == -1;
    }

    public double getValue() {
        return value;
    }

    public boolean isInteger() {
        return isInteger;
    }

    @Override
    public String getType() {
        return "NumberLiteral";
    }
}

class StringLiteralNode extends BaseNode {
    private final String value;

    public StringLiteralNode(Token token) {
        super(token);
        this.value = token.getLiteral();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "StringLiteral";
    }
}

class BooleanLiteralNode extends BaseNode {
    private final boolean value;

    public BooleanLiteralNode(Token token) {
        super(token);
        this.value = token.getLiteral().equals("true");
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "BooleanLiteral";
    }
}

class NullLiteralNode extends BaseNode {
    public NullLiteralNode(Token token) {
        super(token);
    }

    @Override
    public String getType() {
        return "NullLiteral";
    }
}

// Identifier node
class IdentifierNode extends BaseNode {
    private final String name;

    public IdentifierNode(Token token) {
        super(token);
        this.name = token.getLiteral();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "Identifier";
    }
}

// Variable declaration node
class VariableDeclarationNode extends BaseNode {
    private final String name;
    private final ASTNode initializer;
    private final boolean isLocal;

    public VariableDeclarationNode(Token token, String name, ASTNode initializer, boolean isLocal) {
        super(token);
        this.name = name;
        this.initializer = initializer;
        this.isLocal = isLocal;
    }

    public String getName() {
        return name;
    }

    public ASTNode getInitializer() {
        return initializer;
    }

    public boolean isLocal() {
        return isLocal;
    }

    @Override
    public String getType() {
        return "VariableDeclaration";
    }
}

// Binary expression node
class BinaryExpressionNode extends BaseNode {
    private final ASTNode left;
    private final String operator;
    private final ASTNode right;

    public BinaryExpressionNode(Token token, ASTNode left, String operator, ASTNode right) {
        super(token);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ASTNode getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getRight() {
        return right;
    }

    @Override
    public String getType() {
        return "BinaryExpression";
    }
}

// Unary expression node
class UnaryExpressionNode extends BaseNode {
    private final String operator;
    private final ASTNode operand;

    public UnaryExpressionNode(Token token, String operator, ASTNode operand) {
        super(token);
        this.operator = operator;
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getOperand() {
        return operand;
    }

    @Override
    public String getType() {
        return "UnaryExpression";
    }
}

// Ternary expression node (condition ? thenExpr : elseExpr)
class TernaryExpressionNode extends BaseNode {
    private final ASTNode condition;
    private final ASTNode thenExpr;
    private final ASTNode elseExpr;

    public TernaryExpressionNode(Token token, ASTNode condition, ASTNode thenExpr, ASTNode elseExpr) {
        super(token);
        this.condition = condition;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getThenExpr() {
        return thenExpr;
    }

    public ASTNode getElseExpr() {
        return elseExpr;
    }

    @Override
    public String getType() {
        return "TernaryExpression";
    }
}

// Function call node
class CallExpressionNode extends BaseNode {
    private final ASTNode callee;
    private final List<ASTNode> arguments;

    public CallExpressionNode(Token token, ASTNode callee, List<ASTNode> arguments) {
        super(token);
        this.callee = callee;
        this.arguments = arguments;
    }

    public ASTNode getCallee() {
        return callee;
    }

    public List<ASTNode> getArguments() {
        return arguments;
    }

    @Override
    public String getType() {
        return "CallExpression";
    }
}

// Index expression node (array[index] or object.property)
class IndexExpressionNode extends BaseNode {
    private final ASTNode object;
    private final ASTNode index;

    public IndexExpressionNode(Token token, ASTNode object, ASTNode index) {
        super(token);
        this.object = object;
        this.index = index;
    }

    public ASTNode getObject() {
        return object;
    }

    public ASTNode getIndex() {
        return index;
    }

    @Override
    public String getType() {
        return "IndexExpression";
    }
}

// Property access node (object.property)
class PropertyAccessNode extends BaseNode {
    private final ASTNode object;
    private final String property;

    public PropertyAccessNode(Token token, ASTNode object, String property) {
        super(token);
        this.object = object;
        this.property = property;
    }

    public ASTNode getObject() {
        return object;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public String getType() {
        return "PropertyAccess";
    }
}

// If statement node
class IfStatementNode extends BaseNode {
    private final ASTNode condition;
    private final List<ASTNode> thenBranch;
    private final List<ASTNode> elseBranch;
    private final List<Pair<ASTNode, List<ASTNode>>> elifBranches;

    public IfStatementNode(Token token, ASTNode condition, List<ASTNode> thenBranch,
                            List<Pair<ASTNode, List<ASTNode>>> elifBranches,
                            List<ASTNode> elseBranch) {
        super(token);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elifBranches = elifBranches;
        this.elseBranch = elseBranch;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public List<ASTNode> getThenBranch() {
        return thenBranch;
    }

    public List<ASTNode> getElseBranch() {
        return elseBranch;
    }

    public List<Pair<ASTNode, List<ASTNode>>> getElifBranches() {
        return elifBranches;
    }

    @Override
    public String getType() {
        return "IfStatement";
    }
}

// Simple pair class for elif branches
class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

// For statement node
class ForStatementNode extends BaseNode {
    private final ASTNode initializer;
    private final ASTNode condition;
    private final ASTNode increment;
    private final List<ASTNode> body;

    public ForStatementNode(Token token, ASTNode initializer, ASTNode condition,
                            ASTNode increment, List<ASTNode> body) {
        super(token);
        this.initializer = initializer;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    public ASTNode getInitializer() {
        return initializer;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getIncrement() {
        return increment;
    }

    public List<ASTNode> getBody() {
        return body;
    }

    @Override
    public String getType() {
        return "ForStatement";
    }
}

// While statement node
class WhileStatementNode extends BaseNode {
    private final ASTNode condition;
    private final List<ASTNode> body;

    public WhileStatementNode(Token token, ASTNode condition, List<ASTNode> body) {
        super(token);
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public List<ASTNode> getBody() {
        return body;
    }

    @Override
    public String getType() {
        return "WhileStatement";
    }
}

// Do-while statement node
class DoWhileStatementNode extends BaseNode {
    private final List<ASTNode> body;
    private final ASTNode condition;

    public DoWhileStatementNode(Token token, List<ASTNode> body, ASTNode condition) {
        super(token);
        this.body = body;
        this.condition = condition;
    }

    public List<ASTNode> getBody() {
        return body;
    }

    public ASTNode getCondition() {
        return condition;
    }

    @Override
    public String getType() {
        return "DoWhileStatement";
    }
}

// Function declaration node
class FunctionDeclarationNode extends BaseNode {
    private final String name;
    private final List<String> parameters;
    private final List<ASTNode> body;
    private final int arity;

    public FunctionDeclarationNode(Token token, String name, List<String> parameters,
                                    List<ASTNode> body) {
        super(token);
        this.name = name;
        this.parameters = parameters;
        this.body = body;
        this.arity = parameters.size();
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

    public int getArity() {
        return arity;
    }

    @Override
    public String getType() {
        return "FunctionDeclaration";
    }
}

// Return statement node
class ReturnStatementNode extends BaseNode {
    private final ASTNode value;

    public ReturnStatementNode(Token token, ASTNode value) {
        super(token);
        this.value = value;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "ReturnStatement";
    }
}

// Break statement node
class BreakStatementNode extends BaseNode {
    public BreakStatementNode(Token token) {
        super(token);
    }

    @Override
    public String getType() {
        return "BreakStatement";
    }
}

// Continue statement node
class ContinueStatementNode extends BaseNode {
    public ContinueStatementNode(Token token) {
        super(token);
    }

    @Override
    public String getType() {
        return "ContinueStatement";
    }
}

// Array literal node
class ArrayLiteralNode extends BaseNode {
    private final List<ASTNode> elements;

    public ArrayLiteralNode(Token token, List<ASTNode> elements) {
        super(token);
        this.elements = elements;
    }

    public List<ASTNode> getElements() {
        return elements;
    }

    @Override
    public String getType() {
        return "ArrayLiteral";
    }
}

// Object literal node
class ObjectLiteralNode extends BaseNode {
    private final Map<String, ASTNode> properties;

    public ObjectLiteralNode(Token token, Map<String, ASTNode> properties) {
        super(token);
        this.properties = properties;
    }

    public Map<String, ASTNode> getProperties() {
        return properties;
    }

    @Override
    public String getType() {
        return "ObjectLiteral";
    }
}

// Assignment expression node (variable = value)
class AssignmentNode extends BaseNode {
    private final String target;
    private final ASTNode value;

    public AssignmentNode(Token token, String target, ASTNode value) {
        super(token);
        this.target = target;
        this.value = value;
    }

    public String getTarget() {
        return target;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public String getType() {
        return "Assignment";
    }
}
