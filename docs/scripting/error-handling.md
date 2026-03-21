# Error Handling

## Overview

Clish uses a **Result type** pattern for error handling instead of exceptions. This approach:

- Makes errors explicit in function signatures
- Forces handling at compile/script level
- Prevents silent failures
- Enables clean error propagation with `?`

## Result Type

Functions that can fail return a `Result` type instead of throwing exceptions.

### Creating Results

```clish
# Create a successful result
let okResult = ok(42)
let okResult = ok({data: "value"})  # Can wrap any value

# Create an error result
let errResult = err("Something went wrong")
let errResult = err("Not found", 404)  # With error code
```

### Checking Results

```clish
# Using built-in predicates
if (isOk(result)) {
    echo "Success!"
}

if (isError(result)) {
    echo "Failed!"
}

# Getting values
let value = unwrap(okResult)    # Returns value, crashes if error
let value = unwrapOr(okResult, 0)  # Returns value or default
```

### Error Propagation with `?`

The `?` operator propagates errors automatically:

```clish
function divide(a, b) {
    if (b == 0) {
        return err("Division by zero")
    }
    return ok(a / b)
}

function calculate() {
    let result = divide(10, 0)?  # Returns early if error
    # Only reaches here if divide returned ok
    return ok(result * 2)
}
```

Equivalent to:
```clish
function calculate() {
    let result = divide(10, 0)
    if (isError(result)) {
        return result
    }
    return ok(unwrap(result) * 2)
}
```

### `?` Operator Rules

The `?` operator works on:
- Function calls that return `Result`
- The `ok()` and `err()` expressions

```clish
# Works on function calls
let value = mightFail()?

# Works on direct Result expressions
let value = (b == 0 ? err("zero") : ok(a/b))?
```

## try/catch/finally

Exception-style error handling for Result errors:

```clish
try {
    let result = riskyOperation()
    echo "Operation succeeded"
} catch (e) {
    echo "Caught error: " + e.message
} finally {
    echo "Always runs"
}
```

### Catch Variable

The catch block receives the error Result:

```clish
try {
    failingFunction()
} catch (e) {
    if (e.code == 404) {
        echo "Not found"
    } else {
        echo "Error: " + e.message
    }
}
```

### finally Block (Optional)

```clish
try {
    openResource()
} catch (e) {
    echo "Failed"
} finally {
    closeResource()  # Always executes
}
```

## Built-in Functions

### Result Predicates

```clish
isOk(result)      # Returns true if result is OK
isError(result)   # Returns true if result is an error
```

### Value Extraction

```clish
unwrap(result)           # Returns value, crashes if error
unwrapOr(result, default) # Returns value or default
unwrapError(result)       # Returns error message
```

### Transformation

```clish
map(result, fn)      # Transform ok value, pass errors through
flatMap(result, fn)   # Chain Result-returning functions
```

### Creating Results

```clish
ok(value)           # Create successful result
err(message)        # Create error without code
err(message, code)  # Create error with code
```

## Patterns

### Early Return

```clish
function process(data) {
    let validated = validate(data)?
    let parsed = parse(validated)?
    let transformed = transform(parsed)?
    return ok(transformed)
}
```

### Default Values

```clish
let config = readConfig("app.conf") | {
    echo "Using defaults"
    defaultConfig
}
```

### Chaining with map

```clish
let number = parseInt(userInput)
let doubled = map(number, fn(x) { x * 2 })
```

### Pattern Matching Style

```clish
let result = operation()
if (isError(result)) {
    handleError(result)
} else {
    useValue(result)
}
```

## Examples

### File Operations

```clish
function readFile(path) {
    let content = nbt.read(path)
    if (isError(content)) {
        return err("Failed to read: " + path)
    }
    return ok(content)
}

function processConfig() {
    let config = readFile("config.json")?
    return parseJson(config)
}
```

### Validation Chain

```clish
function validateUser(user) {
    if (user.age < 0) {
        return err("Invalid age")
    }
    if (user.age > 150) {
        return err("Age too large")
    }
    if (len(user.name) == 0) {
        return err("Name required")
    }
    return ok(user)
}
```

### Try/Catch for Recovery

```clish
function loadResource(name) {
    try {
        let data = riskyLoad(name)
        return ok(data)
    } catch (e) {
        echo "Loading failed, using cache"
        return ok(cache)
    }
}
```

### Result as Maybe Type

```clish
function findPlayer(name) {
    let players = @e[type=player]
    for (player in players) {
        if (player.name == name) {
            return ok(player)
        }
    }
    return err("Player not found", 404)
}
```
