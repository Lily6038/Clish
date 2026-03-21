# Functions

Functions allow you to define reusable blocks of code.

## Function Declaration

### Basic Function

```clish
function greet() {
    echo("Hello!")
}

greet()  # Call the function
```

### Function with Parameters

```clish
function greet(name) {
    echo("Hello, " + name + "!")
}

greet("Player")
```

### Function with Multiple Parameters

```clish
function add(a, b) {
    return a + b
}

local result = add(5, 3)  # result = 8
```

## Return Values

### Return Single Value

```clish
function double(x) {
    return x * 2
}

local y = double(4)  # y = 8
```

### Return Multiple Values

```clish
function divide(a, b) {
    if (b == 0) {
        return 0, 0  # Error: division by zero
    }
    return a / b, a % b
}

local quotient, remainder = divide(10, 3)
# quotient = 3, remainder = 1
```

### Return Nothing

```clish
function log(message) {
    echo(message)
    # No return statement = return null
}
```

## Variable Scope

### Local Variables

Variables declared with `local` are scoped to the function:

```clish
function testScope() {
    local x = 10
    echo(x)  # 10
}

testScope()
# x is not accessible here
```

### Accessing Outer Variables

Functions can read variables from outer scopes:

```clish
local name = "World"

function greet() {
    echo("Hello, " + name)
}

greet()  # Hello, World
```

## Function Expressions

### Assigning Functions to Variables

```clish
local add = function(a, b) {
    return a + b
}

local result = add(3, 4)  # result = 7
```

### Higher-Order Functions

Pass functions as arguments:

```clish
function applyTwice(fn, value) {
    return fn(fn(value))
}

local result = applyTwice(function(x) { return x * 2 }, 3)
# result = 12
```

## Recursion

Functions can call themselves:

```clish
function factorial(n) {
    if (n <= 1) {
        return 1
    }
    return n * factorial(n - 1)
}

local result = factorial(5)  # result = 120
```

## Built-in Functions

### echo

Prints values to the console:

```clish
echo("Hello, World!")
echo(123)
echo(true)
```

### len

Returns the length of a string or array:

```clish
len("hello")      # 5
len([1, 2, 3])    # 3
```

### type

Returns the type of a value:

```clish
type(123)         # "int"
type("text")      # "string"
type(true)       # "bool"
type([1, 2])     # "array"
```

### toInt / toFloat

Convert values to numbers:

```clish
toInt("42")       # 42
toFloat("3.14")   # 3.14
```

### exit

Exit the script with an exit code:

```clish
exit(0)   # Normal exit
exit(1)   # Error exit
```

## Result-Returning Functions

Functions that can fail should return a `Result` type using `ok()` or `err()`:

```clish
function divide(a, b) {
    if (b == 0) {
        return err("Division by zero")
    }
    return ok(a / b)
}
```

### Using the `?` Operator

Use `?` to propagate errors:

```clish
function calculate() {
    let result = divide(10, 0)?
    return ok(result * 2)
}
```

### Without `?` (Explicit Handling)

```clish
function calculate() {
    let result = divide(10, 0)
    if (isError(result)) {
        return result
    }
    return ok(unwrap(result) * 2)
}
```

### sleep

Pause execution for a specified duration:

```clish
sleep(1000)  # Sleep for 1000ms (1 second)
```
