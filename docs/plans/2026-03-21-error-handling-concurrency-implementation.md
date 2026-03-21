# Error Handling & Concurrency Implementation Plan

**Date:** 2026-03-21
**Author:** Claude
**Status:** Draft

---

## Overview

This plan covers implementation of:
1. **Error Handling** — Result type with `?` propagation, try/catch/finally
2. **Concurrency** — Channels, pipes, spawn/wait, co-processes

---

## Phase Order: Documentation First

The implementation follows a **docs-first** approach:
1. Write documentation files (design, usage, examples)
2. Update grammar reference
3. Implement code based on docs
4. Write tests

---

## Phase 1: Documentation (First)

### 1.1 Error Handling Documentation

Create `docs/scripting/error-handling.md`:

| Section | Content |
|---------|---------|
| Overview | Why Result types vs exceptions |
| Result Type | `ok()` and `err()` creation |
| `?` Operator | Error propagation syntax |
| try/catch/finally | Statement form |
| Builtin Functions | `isOk()`, `isError()`, `unwrap()`, etc. |
| Patterns | Early return, chaining, default values |
| Examples | Real-world error handling patterns |

### 1.2 Concurrency Documentation

Create `docs/scripting/concurrency.md`:

| Section | Content |
|---------|---------|
| Philosophy | Shell-like concurrency model |
| Channels | Creation, send, receive, tryReceive |
| Pipes | Data flow with `|` operator |
| Spawn & Jobs | Background tasks, `wait`, `$!` |
| Co-Processes | Bidirectional communication |
| Patterns | Producer/consumer, worker pools, fan-out |
| Examples | Parallel entity scanning, concurrent NBT reads |

### 1.3 Update Existing Docs

| File | Changes |
|------|---------|
| `docs/scripting/syntax.md` | Add `?` operator, pipe operator |
| `docs/scripting/operators.md` | Add `?` operator section |
| `docs/scripting/functions.md` | Result-returning function conventions |
| `docs/scripting/control-flow.md` | Add try/catch/finally |

---

## Phase 2: Grammar Reference (Second)

Update `docs/grammar-reference.md`:

| Section | Changes |
|---------|---------|
| Section 6Z | Error Handling grammar (already drafted) |
| Section 6Y | Concurrency grammar (already drafted) |
| Section 7X.6 | Error handling keywords |
| Section 8 | New AST nodes |
| Section 11 | Implementation roadmap |

---

## Phase 3: Implementation (Third)

### 3.1 Token Types (TokenType.java)

Add new tokens:

```java
// Error handling
TRY(Token.class, "try"),
CATCH(Token.class, "catch"),
FINALLY(Token.class, "finally"),
OK(Token.class, "ok"),
ERR(Token.class, "err"),

// Concurrency
CHANNEL(Token.class, "channel"),
SEND(Token.class, "send"),
RECEIVE(Token.class, "receive"),
TRY_RECEIVE(Token.class, "tryReceive"),
SPAWN(Token.class, "spawn"),
WAIT(Token.class, "wait"),
COPROC(Token.class, "coproc"),
PIPE(Operator.class, "|"),
```

### 3.2 Result Type (ResultType.java)

Create new file:

```java
public class ResultType {
    public enum Kind { OK, ERROR }
    public final Kind kind;
    public final Object value;      // For OK
    public final String message;    // For ERROR
    public final int code;          // For ERROR

    private ResultType(Kind kind, Object value, String message, int code) { ... }

    public static ResultType ok(Object value) { ... }
    public static ResultType error(String message) { ... }
    public static ResultType error(String message, int code) { ... }

    public boolean isOk() { return kind == Kind.OK; }
    public boolean isError() { return kind == Kind.ERROR; }
    public Object getValue() { return value; }
    public String getMessage() { return message; }
    public int getCode() { return code; }
}
```

### 3.3 New AST Nodes (Nodes.java)

```java
// Error Handling Nodes
public class OkExpressionNode extends ASTNode {
    public final ASTNode value;
}

public class ErrExpressionNode extends ASTNode {
    public final ASTNode message;
    public final ASTNode code;  // optional
}

public class ErrorPropagationNode extends ASTNode {
    public final ASTNode expression;
}

public class TryStatementNode extends ASTNode {
    public final ASTNode tryBlock;
    public final String catchVariable;
    public final ASTNode catchBlock;
    public final ASTNode finallyBlock;  // optional
}

// Concurrency Nodes
public class ChannelNode extends ASTNode {
    public final ASTNode bufferSize;  // optional
}

public class SendNode extends ASTNode {
    public final ASTNode channel;
    public final ASTNode value;
}

public class ReceiveNode extends ASTNode {
    public final ASTNode channel;
}

public class TryReceiveNode extends ASTNode {
    public final ASTNode channel;
}

public class SpawnNode extends ASTNode {
    public final ASTNode block;
}

public class WaitNode extends ASTNode {
    public final ASTNode jobId;  // optional
}

public class JobIdNode extends ASTNode {
    // Represents $! - returns last background job ID
}

public class CoprocNode extends ASTNode {
    public final String name;
    public final ASTNode block;
}

public class CoprocAccessNode extends ASTNode {
    public final ASTNode coproc;
    public final String member;  // "in", "out", or "pid"
}

public class PipeNode extends ASTNode {
    public final ASTNode left;
    public final ASTNode right;
}
```

### 3.4 Parser Changes (Parser.java)

Add parsing methods for error handling:

```java
private ASTNode tryExpression() {
    // try { block } catch (e) { block } finally { block }?
}

private ASTNode okExpression() {
    // ok(expression)
}

private ASTNode errExpression() {
    // err(expression) or err(expression, expression)?
}

private ASTNode errorPropagation() {
    // expression '?'
}

private ASTNode pipeExpression() {
    // pipeExpression '|' errorPropagation
}
```

Add parsing methods for concurrency:

```java
private ASTNode channelExpression() {
    // channel() or channel(expression)?
}

private ASTNode sendExpression() {
    // send(expression, expression)
}

private ASTNode receiveExpression() {
    // receive(expression)
}

private ASTNode tryReceiveExpression() {
    // tryReceive(expression)
}

private ASTNode spawnExpression() {
    // spawn { block }
}

private ASTNode waitExpression() {
    // wait or wait(expression)
}

private ASTNode jobIdExpression() {
    // $!
}

private ASTNode coprocExpression() {
    // coproc IDENTIFIER { block }
}

private ASTNode coprocAccessExpression() {
    // IDENTIFIER . (in | out | pid)
}
```

### 3.5 Interpreter Changes (Interpreter.java)

Add Result builtin functions:

```java
private Object builtinIsOk(List<Object> args) { ... }
private Object builtinIsError(List<Object> args) { ... }
private Object builtinUnwrap(List<Object> args) { ... }
private Object builtinUnwrapOr(List<Object> args) { ... }
private Object builtinMap(List<Object> args) { ... }
private Object builtinFlatMap(List<Object> args) { ... }
private Object builtinOk(List<Object> args) { ... }
private Object builtinErr(List<Object> args) { ... }
```

Add Concurrency builtin functions:

```java
private Object builtinChannel(List<Object> args) { ... }
private Object builtinSend(List<Object> args) { ... }
private Object builtinReceive(List<Object> args) { ... }
private Object builtinTryReceive(List<Object> args) { ... }
private Object builtinSpawn(List<Object> args) { ... }
private Object builtinWait(List<Object> args) { ... }
```

Implement visit methods for new nodes:
- `visitOkExpressionNode`
- `visitErrExpressionNode`
- `visitErrorPropagationNode` — returns early if error
- `visitTryStatementNode`
- `visitChannelNode`
- `visitSendNode`
- `visitReceiveNode`
- `visitTryReceiveNode`
- `visitSpawnNode`
- `visitWaitNode`
- `visitJobIdNode`
- `visitCoprocNode`
- `visitCoprocAccessNode`
- `visitPipeNode`

### 3.6 New Runtime Classes

Create `Channel.java`:

```java
public class Channel {
    private final BlockingQueue<Object> queue;
    private final int bufferSize;

    public Channel() { this(0); }
    public Channel(int bufferSize) { ... }

    public void send(Object value) throws InterruptedException { ... }
    public Object receive() throws InterruptedException { ... }
    public Object tryReceive() { return queue.poll(); }
}
```

Create `Job.java`:

```java
public class Job {
    public final int id;
    public final Thread thread;
    public volatile boolean completed;
}
```

Create `JobManager.java`:

```java
public class JobManager {
    private static final AtomicInteger nextId = new AtomicInteger(1);
    private static final Map<Integer, Job> jobs = new ConcurrentHashMap<>();

    public static int spawn(Runnable task) { ... }
    public static void wait(int jobId) { ... }
    public static void waitAll() { ... }
    public static int getLastJobId() { ... }
}
```

Create `CoProcess.java`:

```java
public class CoProcess {
    public final String name;
    public final Channel in;
    public final Channel out;
    public final int pid;
}
```

---

## Phase 4: Testing

### 4.1 Error Handling Tests

Create `test-result-basic.clish`:

```clish
echo "Testing Result type..."

# Test ok/err
local okResult = ok(42)
local errResult = err("test error", 5)

if (isOk(okResult)) {
    echo "PASS: ok() creates ok result"
}

if (isError(errResult)) {
    echo "PASS: err() creates error result"
}

# Test unwrap
local val = unwrap(ok(100))

# Test propagation
function failing() {
    return err("failure")
}

local x = failing()?  # Propagates error

# Test try/catch
try {
    failing()
} catch (e) {
    echo "PASS: catch triggered"
}
```

### 4.2 Concurrency Tests

Create `test-channel-basic.clish`:

```clish
echo "Testing channels..."

local ch = channel()

spawn {
    send(ch, "message")
}

local msg = receive(ch)
if (msg == "message") {
    echo "PASS: channel send/receive"
}

# test-spawn-wait.clish
spawn { time.sleep(100) }
spawn { time.sleep(50) }
wait
echo "PASS: all jobs completed"
```

Create `test-coproc.clish`:

```clish
echo "Testing co-process..."

coproc adder {
    while (true) {
        local msg = receive(self.in)
        send(self.out, msg.a + msg.b)
    }
}

send(adder.in, {a: 10, b: 20})
local sum = receive(adder.out)
if (sum == 30) {
    echo "PASS: co-process works"
}
```

---

## Phase 5: Finalization

| Step | Component |
|------|-----------|
| 1 | Run all tests |
| 2 | Update README if needed |
| 3 | Commit |

---

## Estimated Effort

| Phase | Complexity | Notes |
|-------|------------|-------|
| Documentation | Low | Writing docs |
| Grammar Reference | Low | Already drafted in grammar-reference.md |
| Error Handling | Medium | Result type, `?` operator |
| Channels | Medium | BlockingQueue-based |
| Jobs/Spawn | Medium | Thread management |
| Pipes | Low | Syntactic sugar |
| Co-process | Medium | Bidirectional channels |

---

## Risks & Mitigations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Thread pool exhaustion | High | Limit max concurrent jobs |
| Deadlocks | High | Use timeouts on receive |
| Memory leaks in channels | Medium | Weak references or explicit close |
| Backpressure | Low | Buffered channels with configurable size |

---

## Open Questions

1. **Should `?` work on any expression or only function calls?**
   - Recommend: only on function calls and `ok()`/`err()` results

2. **Should try/catch catch all errors or only Result errors?**
   - Recommend: only Result errors (explicit). Non-Result exceptions should propagate normally.

3. **Channel buffer size**
   - Recommend: configurable with default 0 (unbuffered)

4. **Job cleanup**
   - Recommend: when wait() completes

5. **Co-process termination**
   - Recommend: explicit `exit(code)` or when main process ends
