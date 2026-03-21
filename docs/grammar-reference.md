# Clish Grammar Reference

This document provides a formal grammar specification for the Clish scripting language, mapping each grammar rule to its implementation location and documentation reference.

## Document Status

**Last Updated:** 2026-03-20
**Implementation Source:** `src/main/java/net/clish/`
**Documentation Source:** `docs/scripting/`

---

## 1. Lexical Grammar

### 1.1 Tokens

```
TOKEN ::=
    | IDENTIFIER
    | NUMBER
    | STRING
    | KEYWORD
    | OPERATOR
    | DELIMITER
    | NEWLINE
    | COMMENT
    | EOF
```

#### 1.1.1 Literals

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| `IDENTIFIER` | `TokenType.IDENTIFIER` (Lexer.java:88-98) | `syntax.md` (Identifiers section) | ✅ Implemented |
| `NUMBER` | `TokenType.NUMBER` (Lexer.java:144-158) | `data-types.md` (Numbers section) | ✅ Implemented |
| `STRING` | `TokenType.STRING` (Lexer.java:109-142) | `data-types.md` (Strings section) | ✅ Implemented |

#### 1.1.2 Keywords

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| `if` | `TokenType.IF` | `control-flow.md` | ✅ Implemented |
| `else` | `TokenType.ELSE` | `control-flow.md` | ✅ Implemented |
| `elif` | `TokenType.ELIF` | `control-flow.md` | ✅ Implemented |
| `for` | `TokenType.FOR` | `control-flow.md` | ✅ Implemented |
| `while` | `TokenType.WHILE` | `control-flow.md` | ✅ Implemented |
| `do` | `TokenType.DO` | `control-flow.md` | ✅ Implemented |
| `function` | `TokenType.FUNCTION` | `functions.md` | ✅ Implemented |
| `return` | `TokenType.RETURN` | `functions.md` | ✅ Implemented |
| `break` | `TokenType.BREAK` | `control-flow.md` | ✅ Implemented |
| `continue` | `TokenType.CONTINUE` | `control-flow.md` | ✅ Implemented |
| `local` | `TokenType.LOCAL` | `syntax.md` | ✅ Implemented |
| `true` | `TokenType.TRUE` | `data-types.md` | ✅ Implemented |
| `false` | `TokenType.FALSE` | `data-types.md` | ✅ Implemented |
| `null` | `TokenType.NULL` | `data-types.md` | ✅ Implemented |
| `try` | `TokenType.TRY` | `error-handling.md` | 🔨 In Progress |
| `catch` | `TokenType.CATCH` | `error-handling.md` | 🔨 In Progress |
| `finally` | `TokenType.FINALLY` | `error-handling.md` | 🔨 In Progress |
| `ok` | `TokenType.OK` | `error-handling.md` | 🔨 In Progress |
| `err` | `TokenType.ERR` | `error-handling.md` | 🔨 In Progress |
| `spawn` | `TokenType.SPAWN` | `concurrency.md` | 🔨 In Progress |
| `wait` | `TokenType.WAIT` | `concurrency.md` | 🔨 In Progress |
| `channel` | `TokenType.CHANNEL` | `concurrency.md` | 🔨 In Progress |
| `send` | `TokenType.SEND` | `concurrency.md` | 🔨 In Progress |
| `receive` | `TokenType.RECEIVE` | `concurrency.md` | 🔨 In Progress |
| `coproc` | `TokenType.COPROC` | `concurrency.md` | 🔨 In Progress |

#### 1.1.3 Operators

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| `+` | `TokenType.PLUS` | `operators.md`, `data-types.md` | ✅ Implemented |
| `-` | `TokenType.MINUS` | `operators.md` | ✅ Implemented |
| `*` | `TokenType.MULTIPLY` | `operators.md` | ✅ Implemented |
| `/` | `TokenType.DIVIDE` | `operators.md` | ✅ Implemented |
| `%` | `TokenType.MODULO` | `operators.md` | ✅ Implemented |
| `=` | `TokenType.ASSIGN` | `operators.md`, `syntax.md` | ✅ Implemented |
| `==` | `TokenType.EQUAL` | `operators.md` | ✅ Implemented |
| `!=` | `TokenType.NOT_EQUAL` | `operators.md` | ✅ Implemented |
| `<` | `TokenType.LESS` | `operators.md` | ✅ Implemented |
| `<=` | `TokenType.LESS_EQUAL` | `operators.md` | ✅ Implemented |
| `>` | `TokenType.GREATER` | `operators.md` | ✅ Implemented |
| `>=` | `TokenType.GREATER_EQUAL` | `operators.md` | ✅ Implemented |
| `&&` | `TokenType.AND` | `operators.md` | ✅ Implemented |
| `\|\|` | `TokenType.OR` | `operators.md` | ✅ Implemented |
| `!` | `TokenType.NOT` | `operators.md` | ✅ Implemented |
| `?` | `TokenType.QUESTION` | `operators.md` (Ternary), `error-handling.md` | ✅ Implemented (dual use) |
| `:` | `TokenType.COLON` | `operators.md` (Ternary) | ✅ Implemented |
| `\|` | `TokenType.PIPE` | `operators.md` (Pipe), `concurrency.md` | 🔨 In Progress |

#### 1.1.4 Delimiters

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| `(` `)` | `LPAREN`, `RPAREN` | Throughout | ✅ Implemented |
| `{` `}` | `LBRACE`, `RBRACE` | Throughout | ✅ Implemented |
| `[` `]` | `LBRACKET`, `RBRACKET` | `data-types.md` (Arrays) | ✅ Implemented |
| `,` | `COMMA` | Throughout | ✅ Implemented |
| `;` | `SEMICOLON` | `syntax.md` (Line Structure) | ✅ Implemented |
| `.` | `DOT` | `data-types.md` (Property Access) | ✅ Implemented |

---

## 2. Syntactic Grammar

### 2.1 Program Structure

```
program        → declaration* EOF
declaration    → functionDecl | localDecl | statement
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| `program` | `Parser.parse()` (Parser.java:34-51) | `syntax.md` | ✅ Implemented |
| `declaration` | `Parser.declaration()` (Parser.java:55-63) | N/A | ✅ Implemented |

### 2.2 Declarations

```
functionDecl   → FUNCTION IDENTIFIER '(' params? ')' '{' block '}'
localDecl      → LOCAL IDENTIFIER ('=' expression)? (';')?
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| `functionDecl` | `Parser.functionDeclaration()` (Parser.java:82-104) | `functions.md` | ✅ Implemented |
| `localDecl` | `Parser.variableDeclaration()` (Parser.java:65-80) | `syntax.md` | ✅ Implemented |

### 2.3 Statements

```
statement      → ifStmt | forStmt | whileStmt | doWhileStmt
               | returnStmt | breakStmt | continueStmt
               | blockStmt | expressionStmt

ifStmt         → IF '(' expression ')' '{' block '}'
               (ELIF '(' expression ')' '{' block '}')*
               (ELSE '{' block '}')?

forStmt        → FOR '(' [localDecl|exprStmt] ';' [expression] ';' [expression] ')' '{' block '}'

whileStmt      → WHILE '(' expression ')' '{' block '}'

doWhileStmt    → DO '{' block '}' WHILE '(' expression ')'

returnStmt     → RETURN [expression] [';']

breakStmt      → BREAK [';']

continueStmt   → CONTINUE [';']

blockStmt      → '{' declaration* '}'

expressionStmt → expression [';']
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| `ifStmt` | `Parser.ifStatement()` (Parser.java:147-190) | `control-flow.md` | ✅ Implemented |
| `forStmt` | `Parser.forStatement()` (Parser.java:192-224) | `control-flow.md` | ⚠️ C-style only, no `for...in` |
| `whileStmt` | `Parser.whileStatement()` (Parser.java:226-237) | `control-flow.md` | ✅ Implemented |
| `doWhileStmt` | `Parser.doWhileStatement()` (Parser.java:239-252) | `control-flow.md` | ✅ Implemented |
| `returnStmt` | `Parser.returnStatement()` (Parser.java:254-266) | `functions.md` | ⚠️ Single value only |
| `breakStmt` | `Parser.breakStatement()` (Parser.java:268-275) | `control-flow.md` | ✅ Implemented |
| `continueStmt` | `Parser.continueStatement()` (Parser.java:277-284) | `control-flow.md` | ✅ Implemented |
| `blockStmt` | Inline in `statement()` (Parser.java:130-142) | `syntax.md` | ✅ Implemented |
| `expressionStmt` | `Parser.expressionStatement()` (Parser.java:286-290) | `syntax.md` | ✅ Implemented |

### 2.4 Expressions

```
expression     → assignment
assignment     → IDENTIFIER '=' assignment | ternary
ternary        → or ('?' ternary ':' ternary)?
or             → and ('||' and)*
and            → equality ('&&' equality)*
equality       → comparison (('==' | '!=') comparison)*
comparison     → addition (('<' | '<=' | '>' | '>=') addition)*
addition       → multiplication (('+' | '-') multiplication)*
multiplication → unary (('*' | '/' | '%') unary)*
unary          → ('!' | '-') unary | call
call           → primary ( '(' arguments? ')' | '[' expression ']' | '.' IDENTIFIER )*
primary        → NUMBER | STRING | TRUE | FALSE | NULL | IDENTIFIER
               | '(' expression ')' | '[' expression (',' expression)* ']'
               | '{' (IDENTIFIER ':' expression (',' IDENTIFIER ':' expression)*)? '}'
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| `assignment` | `Parser.assignment()` (Parser.java:316-328) | `syntax.md`, `operators.md` | ⚠️ Simple `=` only |
| `ternary` | `Parser.ternary()` (Parser.java:330-341) | `operators.md` | ✅ Implemented |
| `or` | `Parser.or()` (Parser.java:343-353) | `operators.md` | ✅ Implemented |
| `and` | `Parser.and()` (Parser.java:355-365) | `operators.md` | ✅ Implemented |
| `equality` | `Parser.equality()` (Parser.java:367-377) | `operators.md` | ✅ Implemented |
| `comparison` | `Parser.comparison()` (Parser.java:379-389) | `operators.md` | ✅ Implemented |
| `addition` | `Parser.addition()` (Parser.java:391-401) | `operators.md` | ✅ Implemented |
| `multiplication` | `Parser.multiplication()` (Parser.java:403-413) | `operators.md` | ✅ Implemented |
| `unary` | `Parser.unary()` (Parser.java:415-423) | `operators.md` | ⚠️ `!` and `-` only |
| `call` | `Parser.call()` (Parser.java:425-454) | `functions.md` | ⚠️ No method calls on primaries |
| `primary` | `Parser.primary()` (Parser.java:456-514) | `data-types.md` | ✅ Core literals |

---

## 3. Literal Grammar

### 3.1 Number Literals

```
number         → INTEGER | FLOAT
INTEGER        → [0-9]+
FLOAT          → [0-9]+ '.' [0-9]+
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| Integer | `Lexer.lexNumber()` (Lexer.java:144-158) | `data-types.md` | ✅ Implemented |
| Float | `Lexer.lexNumber()` (Lexer.java:144-158) | `data-types.md` | ✅ Implemented |

### 3.2 String Literals

```
string         → '"' (escaped | ~'"')* '"'
               | '\'' (escaped | ~'\'')* '\''
escaped        → '\' (n | t | r | \\ | \' | \" | $)
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| Double-quoted | `Lexer.lexString()` (Lexer.java:109-142) | `data-types.md` | ✅ Implemented |
| Single-quoted | `Lexer.lexString()` (Lexer.java:109-142) | `data-types.md` | ✅ Implemented |
| Escape sequences | `Lexer.java:126-134` | `data-types.md` | ✅ Implemented |

### 3.3 Array Literals

```
array          → '[' expression (',' expression)* ']'
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| Array literal | `Parser.primary()` (Parser.java:482-491) | `data-types.md` | ✅ Implemented |
| Index access | `Parser.call()` (Parser.java:439-443) | `data-types.md` | ⚠️ Single index only |

### 3.4 Object Literals

```
object         → '{' (IDENTIFIER ':' expression (',' IDENTIFIER ':' expression)*)? '}'
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| Object literal | `Parser.primary()` (Parser.java:494-508) | `data-types.md` | ✅ Implemented |

---

## 4. Identifier Rules

```
identifier     → (LETTER | '_') (LETTER | DIGIT | '_')*
LETTER         → [a-zA-Z]
DIGIT          → [0-9]
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| Identifier | `Lexer.lexIdentifier()` (Lexer.java:160-172) | `syntax.md` | ✅ Implemented |

---

## 5. Comments

```
comment        → '#' (.*) NEWLINE
```

| Grammar Rule | Implementation | Documentation | Status |
|--------------|----------------|---------------|--------|
| Comment | `Lexer.lexComment()` (Lexer.java:100-107) | `syntax.md` | ✅ Implemented |

---

## 6. Operator Precedence

Parsed by recursive descent in `Parser.java`:

| Precedence | Level | Operators | Implementation |
|------------|-------|-----------|---------------|
| 1 (lowest) | assignment | `=` | `Parser.assignment()` |
| 2 | ternary | `? :` | `Parser.ternary()` |
| 3 | logical OR | `\|\|` | `Parser.or()` |
| 4 | logical AND | `&&` | `Parser.and()` |
| 5 | equality | `==`, `!=` | `Parser.equality()` |
| 6 | comparison | `<`, `<=`, `>`, `>=` | `Parser.comparison()` |
| 7 | additive | `+`, `-` | `Parser.addition()` |
| 8 | multiplicative | `*`, `/`, `%` | `Parser.multiplication()` |
| 9 (highest) | unary | `!`, `-` | `Parser.unary()` |
| 10 | pipe | `\|` | `Parser.pipe()` (NEW) |
| 11 | error prop | `?` | `Parser.errorPropagation()` (NEW) |

---

## 6X. Pipe Operator

Pipes create unidirectional data flow between expressions. The left expression's result is passed as input to the right expression.

```
pipe            → expression '|' expression
```

| Grammar Rule | Purpose | Example |
|--------------|---------|---------|
| `pipe` | Pass output to input | `echo("hello") \| string.toUpperCase()` |

**Implementation**: `Parser.pipe()` — lowest precedence (below assignment)

**Semantics**:
1. Evaluate left expression
2. Pass result as implicit first argument to right expression
3. Return right expression's result

**Examples**:
```clish
echo("hello world") | string.split(" ") | string.join(":")
string.length("hello") | math.sqrt()  # sqrt(5)
```

---

## 6Z. Error Handling (Result Type)

Clish uses **Result types** for explicit error handling — errors are values, not exceptions.

### 6Z.1 Result Type

```
resultType      → 'Result' '<' expression ',' expression '>'
               # Result<SuccessType, ErrorType>
               # For simplicity, all errors are 'Error' object: { message: string, code: number }
```

**Builtin Error Object**:
```clish
{ type: "Error", message: "description", code: 1 }
```

### 6Z.2 Result Creation

| Expression | Purpose | Example |
|------------|---------|---------|
| `ok(value)` | Create success result | `ok(42)` → `{type: "Ok", value: 42}` |
| `err(message)` | Create error result | `err("not found")` → `{type: "Error", message: "not found", code: 1}` |
| `err(message, code)` | Create error with code | `err("fail", 42)` |

### 6Z.3 Try/Catch (Classic)

```clish
try {
    risky()
} catch (e) {
    handle(e)
} finally {
    cleanup()
}
```

**Note**: Try/catch uses Result internally — `risky()` returning `err()` triggers catch.

### 6Z.4 Error Propagation with `?`

```
errorProp       → expression '?'
               # If expression is err, return early with the error
               # If expression is ok, unwrap and continue
```

| Expression | Purpose | Example |
|------------|---------|---------|
| `expr?` | Propagate error | `local x = parseInt(input)?` |

**Semantics**:
```clish
# These are equivalent:
local x = parseInt(input)?

# To this:
local __result = parseInt(input)
if (__result.type == "Error") {
    return __result  # Early return
}
local x = __result.value

# Or using try/catch:
try {
    local x = parseInt(input)
} catch (e) {
    return e
}
```

**Example with multiple `?`**:
```clish
function processFile(path) {
    local content = readFile(path)?      # Early return if error
    local parsed = parseJSON(content)?   # Early return if error
    return ok(validate(parsed))
}
```

### 6Z.5 Result Utilities

| Function | Purpose | Example |
|----------|---------|---------|
| `isOk(result)` | Check if success | `if (isOk(r)) { ... }` |
| `isError(result)` | Check if error | `if (isError(r)) { ... }` |
| `unwrap(result)` | Get value, crash if error | `local x = unwrap(ok(42))` |
| `unwrapOr(result, default)` | Get value or default | `local x = unwrapOr(r, 0)` |
| `map(result, fn)` | Transform success value | `local x = map(ok(5), \|v| v * 2)` |
| `flatMap(result, fn)` | Chain result-returning functions | `flatMap(r, \|v| process(v))` |

### 6Z.6 Grammar Integration

```
tryStatement    → TRY '{' block '}' CATCH '(' IDENTIFIER ')' '{' block '}' FINALLY '{' block '}'

tryExpr         → TRY '{' block '}' CATCH '(' IDENTIFIER ')' '{' block '}'

errorProp       → expression '?'

resultExpr      → okExpr | errExpr
okExpr          → OK '(' expression ')'
errExpr         → ERR '(' expression (',' expression)? ')'
```

**Operator Precedence** (updated):
| Precedence | Level | Operators |
|------------|-------|-----------|
| 1 (lowest) | assignment | `=` |
| 2 | pipe | `\|` |
| 3 | error prop | `?` |
| 4 | ternary | `? :` |
| ... | ... | ... |

---

## 6Y. Concurrency & Process Model

### 6Y.1 Channels

Channels provide typed message passing between concurrent tasks.

```
channel         → CHANNEL '(' expression? ')'
               # Creates channel with optional buffer size
               # channel() = unbuffered
               # channel(n) = buffer n messages
```

| Grammar Rule | Purpose | Example |
|--------------|---------|---------|
| `channel()` | Unbuffered channel | `local ch = channel()` |
| `channel(n)` | Buffered channel | `local ch = channel(10)` |

**Channel Expressions**:

```
sendExpr       → SEND '(' expression ',' expression ')'
               # send(channel, value)

receiveExpr    → RECEIVE '(' expression ')'
               # value = receive(channel) — blocks until message available

tryReceiveExpr → TRY_RECEIVE '(' expression ')'
               # value = tryReceive(channel) — returns null if empty
```

| Function | Type | Description |
|----------|------|-------------|
| `send(channel, value)` | Builtin | Send value to channel (blocks if full) |
| `receive(channel)` | Builtin | Blocking receive, waits for message |
| `tryReceive(channel)` | Builtin | Non-blocking receive, returns `null` if empty |

**Example**:
```clish
local ch = channel()

spawn {
    send(ch, "task result")
}

local result = receive(ch)  # Blocks until sender sends
```

---

### 6Y.2 Background Jobs

```
spawnExpr      → SPAWN '{' block '}'
               # Run block asynchronously, returns job ID

waitExpr       → WAIT expression?
               # wait — wait for all background jobs
               # wait(jobId) — wait for specific job
```

| Grammar Rule | Purpose | Example |
|--------------|---------|---------|
| `spawn { }` | Run block in background | `spawn { doWork() }` |
| `wait` | Wait for all jobs | `wait` |
| `wait(jobId)` | Wait for specific job | `wait(jobId)` |
| `$!` | Last job ID | `local id = $!` |

**Job Control Examples**:
```clish
# Run in background
doWork &          # Equivalent to: spawn { doWork() }
local jobId = $!  # Capture job ID

# Wait for completion
wait(jobId)

# Run multiple in parallel
spawn { task1() }
spawn { task2() }
wait  # Wait for all
```

---

### 6Y.3 Co-Processes

```
coprocExpr     → COPROC IDENTIFIER '{' block '}'
               # Creates bidirectional communication channel
               # Access via: IDENTIFIER.in and IDENTIFIER.out
```

| Grammar Rule | Purpose | Example |
|--------------|---------|---------|
| `coproc name { }` | Create co-process | `coproc worker { processRequests() }` |

**Co-Process Access**:

| Expression | Type | Description |
|------------|------|-------------|
| `name.in` | Channel | Send messages to co-process |
| `name.out` | Channel | Receive messages from co-process |
| `name.pid` | Number | Process ID of co-process |

**Example**:
```clish
coproc adder {
    while (true) {
        local msg = receive(self.in)
        local result = msg.a + msg.b
        send(self.out, result)
    }
}

send(adder.in, {a: 10, b: 20})
local sum = receive(adder.out)  # 30
```

**Note**: `self` refers to the current co-process's channels (in/out/pid).

---

## 7. Features Documented but NOT Implemented

The following features are documented in `docs/scripting/` but are **NOT** implemented in the current codebase:

### 7.1 Operators

| Feature | Documented In | Issue |
|---------|---------------|-------|
| Power operator `^` | `operators.md`, `data-types.md` | No `POWER` token in `TokenType.java` |
| Compound assignment `+=`, `-=`, `*=`, `/=`, `%=` | `operators.md` | No compound assignment parsing in `Parser.assignment()` |
| Increment/decrement `++`, `--` | `operators.md` | No `INC`/`DEC` tokens in `TokenType.java` |

### 7.2 Control Flow

| Feature | Documented In | Issue |
|---------|---------------|-------|
| `for...in` iteration | `control-flow.md` | `Parser.forStatement()` only handles C-style for loop |
| ForEach syntax | `control-flow.md` | Not parsed |

### 7.3 Data Types

| Feature | Documented In | Issue |
|---------|---------------|-------|
| String slicing `str[0:5]` | `data-types.md` | `Parser.call()` only handles single index |
| String multiplication `str * 3` | `data-types.md` | No `MULTIPLY` overload for string |
| Array slicing `arr[1:3]` | `data-types.md` | `IndexExpressionNode` only supports single index |

### 7.4 Functions

| Feature | Documented In | Issue |
|---------|---------------|-------|
| Function expressions (anonymous) | `functions.md` | `Parser.declaration()` only handles `function IDENTIFIER` |
| Multiple return values | `functions.md` | `ReturnStatementNode` only stores single value |

---

## 7Z. NEW: AST Nodes for Concurrency (Planned)

The following grammar defines the **multi-processing and concurrency** features:

### 7X.1 Channels

```
channelExpr    → CHANNEL '(' expression? ')'     # channel() or channel(bufferSize)
sendExpr       → SEND '(' expression ',' expression ')'   # send(channel, value)
receiveExpr    → RECEIVE '(' expression ')'       # receive(channel)
tryReceiveExpr → TRY_RECEIVE '(' expression ')'   # tryReceive(channel) - non-blocking
```

### 7X.2 Pipe Operator

```
pipeExpr       → pipeExpr '|' expression
               | assignment
```

### 7X.3 Spawn & Jobs

```
spawnExpr      → SPAWN '{' block '}'              # spawn { ... }
waitExpr       → WAIT expression?                 # wait or wait(jobId)
jobId          → '$!'                             # Last background job ID
```

### 7X.4 Co-Process

```
coprocExpr     → COPROC IDENTIFIER '{' block '}'  # coproc name { ... }
```

### 7X.5 Concurrency Keywords

| Keyword | Token | Purpose |
|---------|-------|---------|
| `channel` | `TokenType.CHANNEL` | Create a communication channel |
| `send` | `TokenType.SEND` | Send value to channel |
| `receive` | `TokenType.RECEIVE` | Blocking receive from channel |
| `tryReceive` | `TokenType.TRY_RECEIVE` | Non-blocking receive |
| `spawn` | `TokenType.SPAWN` | Run block in background |
| `wait` | `TokenType.WAIT` | Wait for job completion |
| `coproc` | `TokenType.COPROC` | Create co-process |
| `pipe` | `TokenType.PIPE` (implicit via `\|`) | Pipe operator |

### 7X.6 Error Handling Keywords

| Keyword | Token | Purpose |
|---------|-------|---------|
| `try` | `TokenType.TRY` | Start try block |
| `catch` | `TokenType.CATCH` | Catch exception |
| `finally` | `TokenType.FINALLY` | Finally block |
| `ok` | `TokenType.OK` | Create success result |
| `err` | `TokenType.ERR` | Create error result |
| `?` | `TokenType.QUESTION` (postfix) | Propagate error |
| `isOk` | Builtin function | Check if result is ok |
| `isError` | Builtin function | Check if result is error |
| `unwrap` | Builtin function | Unwrap or crash |
| `unwrapOr` | Builtin function | Unwrap or default |
| `map` | Builtin function | Transform success value |
| `flatMap` | Builtin function | Chain result functions |

---

## 7Y. Grammar Cleanup (Conflicts to Resolve)

The following documented features **should be removed** from docs as they conflict with shell-like design or are impractical:

| Feature | Reason for Removal |
|---------|-------------------|
| `for...in` iteration | Shell languages use `for` with glob/seq; C-style for loop is sufficient |
| Multiple return values | Shell scripts typically return single exit codes; use output parameters or channels |
| Anonymous functions | Contradicts shell simplicity; named functions align better with shell paradigm |
| String multiplication `str * n` | Not shell-like; use `string.repeat()` instead |
| String/Array slicing `[start:end]` | Not shell-like; use `string.substring()` or `array.slice()` library functions |
| Power operator `^` | Bash uses `**` or `expr`; `math.pow()` exists in library |
| Compound assignment `+=` | Shell uses `VAR=$((VAR + 1))` style; explicit assignment is clearer |

---

## 8. AST Node Types

| Node | Implementation | Used For |
|------|----------------|----------|
| `ProgramNode` | Nodes.java:28-48 | Root program node |
| `ExpressionStatementNode` | Nodes.java:51-67 | Expression as statement |
| `NumberLiteralNode` | Nodes.java:70-92 | Numeric literals |
| `StringLiteralNode` | Nodes.java:94-110 | String literals |
| `BooleanLiteralNode` | Nodes.java:112-128 | `true`/`false` |
| `NullLiteralNode` | Nodes.java:130-139 | `null` |
| `IdentifierNode` | Nodes.java:142-158 | Variable references |
| `VariableDeclarationNode` | Nodes.java:161-189 | `local x = ...` |
| `AssignmentNode` | Nodes.java:619-641 | `x = ...` |
| `BinaryExpressionNode` | Nodes.java:192-220 | `a + b`, `a && b`, etc. |
| `UnaryExpressionNode` | Nodes.java:223-245 | `!a`, `-a` |
| `TernaryExpressionNode` | Nodes.java:248-276 | `a ? b : c` |
| `CallExpressionNode` | Nodes.java:279-301 | `func(args)` |
| `IndexExpressionNode` | Nodes.java:304-326 | `arr[index]` |
| `PropertyAccessNode` | Nodes.java:329-351 | `obj.prop` |
| `ArrayLiteralNode` | Nodes.java:581-597 | `[a, b, c]` |
| `ObjectLiteralNode` | Nodes.java:600-616 | `{a: 1, b: 2}` |
| `IfStatementNode` | Nodes.java:354-390 | `if/elif/else` |
| `ForStatementNode` | Nodes.java:412-447 | `for(init; cond; inc)` |
| `WhileStatementNode` | Nodes.java:450-472 | `while(cond)` |
| `DoWhileStatementNode` | Nodes.java:475-497 | `do { } while(cond)` |
| `FunctionDeclarationNode` | Nodes.java:500-535 | `function name() { }` |
| `ReturnStatementNode` | Nodes.java:538-554 | `return value` |
| `BreakStatementNode` | Nodes.java:557-566 | `break` |
| `ContinueStatementNode` | Nodes.java:569-578 | `continue` |
| `ChannelNode` | **Planned** | `channel()` |
| `SendNode` | **Planned** | `send(ch, msg)` |
| `ReceiveNode` | **Planned** | `receive(ch)` |
| `TryReceiveNode` | **Planned** | `tryReceive(ch)` |
| `PipeNode` | **Planned** | `expr \| expr` |
| `SpawnNode` | **Planned** | `spawn { }` |
| `WaitNode` | **Planned** | `wait` |
| `JobIdNode` | **Planned** | `$!` |
| `CoprocNode` | **Planned** | `coproc name { }` |
| `CoprocAccessNode` | **Planned** | `name.in`, `name.out`, `name.pid` |
| `TryStatementNode` | **Planned** | `try { } catch(e) { }` |
| `TryExpressionNode` | **Planned** | `try { } catch(e) { }` as expression |
| `ErrorPropagationNode` | **Planned** | `expr?` |
| `OkExpressionNode` | **Planned** | `ok(value)` |
| `ErrExpressionNode` | **Planned** | `err(message)` |
| `ResultTypeNode` | **Planned** | Internal Result representation |

---

## 9. File Locations

### Implementation Files

| Component | File |
|-----------|------|
| Lexer | `src/main/java/net/clish/lexer/Lexer.java` |
| Token Types | `src/main/java/net/clish/lexer/TokenType.java` |
| Token | `src/main/java/net/clish/lexer/Token.java` |
| Parser | `src/main/java/net/clish/ast/Parser.java` |
| AST Nodes | `src/main/java/net/clish/ast/Nodes.java` |
| AST Node Interface | `src/main/java/net/clish/ast/ASTNode.java` |

### Documentation Files

| Document | File |
|----------|------|
| Syntax Reference | `docs/scripting/syntax.md` |
| Operators | `docs/scripting/operators.md` |
| Data Types | `docs/scripting/data-types.md` |
| Control Flow | `docs/scripting/control-flow.md` |
| Functions | `docs/scripting/functions.md` |

---

## 10. Summary Statistics

| Category | Implemented | Documented | Match |
|----------|-------------|------------|-------|
| Keywords | 14 | 14 | ✅ 100% |
| Operators | 17 | 24 | ⚠️ 71% |
| Literals | 4 | 4 | ✅ 100% |
| Control Flow | 6 | 7 | ⚠️ 86% |
| Data Types | 6 | 6 | ✅ 100% |
| Expression Types | 12 | 12 | ✅ 100% |
| Concurrency (NEW) | 0 | 11 | 📋 Planned |
| Error Handling (NEW) | 0 | 8 | 📋 Planned |

**Overall:** 59/67 core features implemented (88%)

---

## 11. Implementation Roadmap

### Phase 0: Error Handling Foundation (Recommended First)
Error handling should be implemented first as it's foundational for robust concurrency.

1. `TokenType` additions: `TRY`, `CATCH`, `FINALLY`, `OK`, `ERR`
2. `OkExpressionNode`, `ErrExpressionNode` — Result creation
3. `ErrorPropagationNode` — `?` operator
4. `TryStatementNode` — try/catch/finally statement
5. Builtin functions: `isOk()`, `isError()`, `unwrap()`, `unwrapOr()`, `map()`, `flatMap()`
6. **Tests**: Unit tests for all Result operations

### Phase 1: Core Concurrency
7. `ChannelNode`, `SendNode`, `ReceiveNode` — channel primitives
8. `SpawnNode`, `WaitNode`, `JobIdNode` — background jobs
9. `PipeNode` — pipe operator
10. **Tests**: Channel send/receive, spawn/wait

### Phase 2: Advanced Concurrency
11. `TryReceiveNode` — non-blocking receive
12. `CoprocNode`, `CoprocAccessNode` — co-processes
13. **Tests**: Co-process ping-pong, multi-worker

### Phase 3: Documentation & Cleanup
14. Remove deprecated features from docs (Section 7Y)
15. Add `docs/scripting/error-handling.md` with Result patterns
16. Add `docs/scripting/concurrency.md` with usage examples
17. Update `docs/scripting/operators.md` with `?` operator
