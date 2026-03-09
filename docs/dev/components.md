# Component Details

This document provides detailed information about key components.

## Lexer

**File:** `src/main/java/net/clish/lexer/`

The lexer tokenizes source code into a stream of tokens.

### Token Types

| Type | Description |
|------|-------------|
| `IDENTIFIER` | Variable/function names |
| `NUMBER` | Integer or float literals |
| `STRING` | String literals (single or double quotes) |
| `KEYWORD` | Reserved words (if, else, for, etc.) |
| `OPERATOR` | Operators (+, -, *, /, etc.) |
| `LPAREN` | Left parenthesis |
| `RPAREN` | Right parenthesis |
| `LBRACE` | Left brace |
| `RBRACE` | Right brace |
| `LBRACKET` | Left bracket |
| `RBRACKET` | Right bracket |
| `COMMA` | Comma |
| `SEMICOLON` | Semicolon |
| `DOT` | Dot |
| `COLON` | Colon |
| `NEWLINE` | Line break |
| `EOF` | End of file |

### Lexer Implementation

```java
// lexer/Lexer.java
public class Lexer {
    private final String source;
    private int position;
    private int line;

    public List<Token> tokenize() { ... }
    private Token nextToken() { ... }
    private void skipWhitespace() { ... }
    // ...
}
```

## Parser

**File:** `src/main/java/net/clish/ast/Parser.java`

The parser builds an Abstract Syntax Tree (AST) from tokens.

### AST Node Types

| Node | Description |
|------|-------------|
| `ProgramNode` | Root node containing all statements |
| `VariableNode` | Variable declaration/assignment |
| `FunctionCallNode` | Function call expression |
| `FunctionDefNode` | Function definition |
| `IfNode` | If/else statement |
| `ForNode` | For loop |
| `WhileNode` | While loop |
| `ReturnNode` | Return statement |
| `BinaryOpNode` | Binary operation |
| `UnaryOpNode` | Unary operation |
| `LiteralNode` | Literal value |
| `ArrayNode` | Array literal |
| `ObjectNode` | Object/struct literal |

### Parser Implementation

```java
// ast/Parser.java
public class Parser {
    private List<Token> tokens;
    private int position;

    public ASTNode parse() { ... }
    private ASTNode parseExpression() { ... }
    private ASTNode parseStatement() { ... }
    // ...
}
```

## Interpreter

**File:** `src/main/java/net/clish/ast/Interpreter.java`

The interpreter executes the AST.

### Key Methods

| Method | Description |
|--------|-------------|
| `execute(ASTNode)` | Execute an AST node |
| `visitProgram(ProgramNode)` | Execute program node |
| `visitVariable(VariableNode)` | Handle variable operations |
| `visitFunctionCall(FunctionCallNode)` | Call function |
| `visitIf(IfNode)` | Execute conditional |
| `visitFor(ForNode)` | Execute for loop |
| `visitWhile(WhileNode)` | Execute while loop |

### Variable Scope

Variables are stored in a stack of scope maps:

```java
private final List<Map<String, Object>> scopes;
```

### Function Calls

Functions are stored in the global scope and called with their arguments.

## ClishLibrary Interface

**File:** `src/main/java/net/clish/ast/ClishLibrary.java`

All built-in functions and APIs implement this interface.

```java
public interface ClishLibrary {
    String getName();  // e.g., "math.sqrt"
    Object call(List<Object> args);
}
```

### Adding a New Library Function

```java
public class MyFunction implements ClishLibrary {
    @Override
    public String getName() {
        return "my.function";
    }

    @Override
    public Object call(List<Object> args) {
        // Implementation
        return result;
    }
}
```

## Built-in Libraries

### Builtins

**File:** `src/main/java/net/clish/builtin/Builtins.java`

Core functions: `echo`, `len`, `type`, `toInt`, `toFloat`, `exit`, `sleep`

### Math Library

**File:** `src/main/java/net/clish/builtin/MathLibrary.java`

Math functions: `abs`, `floor`, `ceil`, `round`, `min`, `max`, `sqrt`, `pow`, `sin`, `cos`, `tan`, `log`, `random`, constants (`pi`, `e`)

### String Library

**File:** `src/main/java/net/clish/builtin/StringLibrary.java`

String functions: `length`, `substring`, `indexOf`, `replace`, `trim`, `toUpperCase`, `toLowerCase`, `split`, `join`, `contains`

### Regex Library

**File:** `src/main/java/net/clish/builtin/RegexLibrary.java`

Regex functions: `test`, `contains`, `find`, `replace`, `split`, `count`, `escape`

### Time Library

**File:** `src/main/java/net/clish/builtin/TimeLibrary.java`

Time functions: `now`, `timestamp`, `sleep`, `format`, `parse`, `date`, `add`, `subtract`, `diff`

### NBT Library

**File:** `src/main/java/net/clish/builtin/NbtLibrary.java`

NBT functions: `create`, `get`, `set`, `remove`, `has`, `keys`, `values`, `toString`, `type`

## Game APIs

### Player API

**File:** `src/main/java/net/clish/api/PlayerApi.java`

Functions: `player.x`, `player.y`, `player.z`, `player.health`, `player.food`, `player.dimension`, `player.inventory`

### Block API

**File:** `src/main/java/net/clish/api/BlockApi.java`

Functions: `block.get`, `block.nbt`, `block.exists`, `block.light`

### Input API

**File:** `src/main/java/net/clish/api/InputApi.java`

Functions: `input.leftClick`, `input.rightClick`, `input.mouseMove`, `input.keyPress`, `input.typeText`

### Command API

**File:** `src/main/java/net/clish/api/CommandApi.java`

Functions: `command.execute`, `command.run`

## Configuration

**File:** `src/main/java/net/clish/config/ClishConfig.java`

Stores mod settings like timeout values.
