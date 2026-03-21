# Operators

Clish provides various operators for performing operations on values.

## Arithmetic Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `+` | Addition / Concatenation | `5 + 3` = 8, `"a" + "b"` = "ab" |
| `-` | Subtraction | `5 - 3` = 2 |
| `*` | Multiplication | `5 * 3` = 15 |
| `/` | Division | `5 / 3` = 1 (integer) |
| `%` | Modulo (remainder) | `5 % 3` = 2 |
| `^` | Power | `2 ^ 3` = 8 |

### Examples

```clish
local sum = 10 + 5           # 15
local diff = 10 - 5          # 5
local product = 10 * 5      # 50
local quotient = 10 / 3     # 3
local remainder = 10 % 3     # 1
local power = 2 ^ 10        # 1024
local concat = "Hello" + " " + "World"  # "Hello World"
```

## Comparison Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `==` | Equal | `5 == 5` = true |
| `!=` | Not Equal | `5 != 3` = true |
| `<` | Less Than | `3 < 5` = true |
| `>` | Greater Than | `5 > 3` = true |
| `<=` | Less Than or Equal | `3 <= 5` = true |
| `>=` | Greater Than or Equal | `5 >= 5` = true |

### Examples

```clish
5 == 5      # true
5 != 3      # true
3 < 5       # true
5 > 3       # true
3 <= 5      # true
5 >= 5      # true

# String comparison
"apple" == "apple"  # true
"apple" < "banana"  # true (lexicographic)
```

## Logical Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `&&` | Logical AND | `true && false` = false |
| `\|\|` | Logical OR | `true \|\| false` = true |
| `!` | Logical NOT | `!true` = false |

### Examples

```clish
true && true     # true
true && false    # false
false && false   # false

true || true     # true
true || false    # true
false || false   # false

!true            # false
!false           # true
!5               # false (truthy value negated)
```

### Short-Circuit Evaluation

```clish
# AND: stops at first false
false && echo("not printed")
true && echo("printed")    # "printed"

# OR: stops at first true
true || echo("not printed")
false || echo("printed")   # "printed"
```

## Assignment Operators

| Operator | Description | Example |
|----------|-------------|---------|
| `=` | Simple assignment | `x = 5` |
| `+=` | Add and assign | `x += 3` (same as `x = x + 3`) |
| `-=` | Subtract and assign | `x -= 3` |
| `*=` | Multiply and assign | `x *= 3` |
| `/=` | Divide and assign | `x /= 3` |
| `%=` | Modulo and assign | `x %= 3` |

### Examples

```clish
local x = 10
x += 5     # x = 15
x -= 3     # x = 12
x *= 2     # x = 24
x /= 4     # x = 6
x %= 4     # x = 2
```

## Increment/Decrement

| Operator | Description |
|----------|-------------|
| `++` | Increment by 1 |
| `--` | Decrement by 1 |

### Examples

```clish
local i = 0
i++     # i = 1
i++     # i = 2
i--     # i = 1
```

## Operator Precedence

Operators are evaluated in this order (highest to lowest):

1. `()` - Parentheses
2. `!`, `++`, `--` - Unary
3. `^` - Power
4. `*`, `/`, `%` - Multiplicative
5. `+`, `-` - Additive
6. `<`, `>`, `<=`, `>=` - Relational
7. `==`, `!=` - Equality
8. `&&` - Logical AND
9. `||` - Logical OR
10. `=`, `+=`, `-=`, etc. - Assignment

### Examples

```clish
# Without parentheses
2 + 3 * 4     # 14 (not 20)
(2 + 3) * 4   # 20

# Complex expression
a && b || c   # (a && b) || c
a && (b || c) # different behavior
```

## Ternary Operator

```clish
local result = condition ? valueIfTrue : valueIfFalse
```

### Example

```clish
local age = 20
local status = age >= 18 ? "adult" : "minor"
```

## Error Propagation Operator

The `?` operator propagates errors from Result-returning functions:

```clish
let result = mightFail()?
```

If `mightFail()` returns an error, the `?` operator returns that error immediately. If it returns ok, execution continues with the unwrapped value.

### Example

```clish
function divide(a, b) {
    if (b == 0) {
        return err("Division by zero")
    }
    return ok(a / b)
}

function calculate() {
    let result = divide(10, 2)?  # Returns early if error
    return ok(result * 2)
}
```

## Pipe Operator

The `|` operator creates data flow pipelines:

```clish
let result = value | transformFunction | anotherTransform
```

### Example

```clish
let numbers = [1, 2, 3, 4, 5]

let result = numbers | map(fn(x) { x * 2 }) | filter(fn(x) { x > 4 })
# result = [6, 8, 10]
```

### Pipe with Error Propagation

```clish
let result = riskyValue | flatMap(fn(x) { process(x) })
```
