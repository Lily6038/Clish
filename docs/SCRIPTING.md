# Clish Scripting Language Documentation

## Overview

Clish is a shell-like scripting language for Minecraft that allows players to create and execute scripts.

## Variables

```clish
local x = 10
local name = "Hello"
local isActive = true
```

## Control Flow

### If/Else

```clish
if (x > 10) {
    echo("x is greater than 10")
} elif (x > 5) {
    echo("x is greater than 5")
} else {
    echo("x is small")
}
```

### For Loop

```clish
for (local i = 0; i < 10; i = i + 1) {
    echo(i)
}
```

### While Loop

```clish
local count = 0
while (count < 5) {
    echo(count)
    count = count + 1
}
```

## Functions

```clish
function greet(name) {
    echo("Hello, " + name + "!")
    return "done"
}

local result = greet("Player")
```

## Operators

| Operator | Description |
|----------|-------------|
| + | Add / Concatenate |
| - | Subtract |
| * | Multiply |
| / | Divide |
| % | Modulo |
| == | Equal |
| != | Not Equal |
| < | Less Than |
| > | Greater Than |
| <= | Less or Equal |
| >= | Greater or Equal |
| && | And |
| \|\| | Or |
| ! | Not |

## Data Types

- **Numbers**: `10`, `3.14`
- **Strings**: `"Hello"`, `'World'`
- **Booleans**: `true`, `false`
- **Arrays**: `[1, 2, 3]`
- **Objects**: `{key: "value"}`

## Built-in Functions

### echo
Prints values to console.

```clish
echo("Hello, World!")
```

### len
Returns length of string/array.

```clish
len("hello")  # Returns 5
```

### type
Returns type of value.

```clish
type(123)     # Returns "int"
type("text")   # Returns "string"
```

### toInt, toFloat
Convert values to numbers.

```clish
toInt("42")    # Returns 42
toFloat("3.14") # Returns 3.14
```

### exit
Exit script with code.

```clish
exit(0)  # Normal exit
exit(1)  # Error exit
```

## Math Library

```clish
math.abs(-5)        # 5
math.floor(3.7)     # 3
math.ceil(3.2)      # 4
math.round(3.5)     # 4
math.sqrt(16)       # 4
math.pow(2, 3)      # 8
math.sin(0)         # 0
math.cos(0)         # 1
math.random()       # Random 0-1
math.pi             # 3.14159...
math.e              # 2.71828...
```

## String Library

```clish
string.length("hello")           # 5
string.substring("hello", 1, 3)   # "el"
string.indexOf("hello", "l")     # 2
string.replace("hello", "l", "r") # "herro"
string.trim("  hello  ")         # "hello"
string.toUpperCase("hello")     # "HELLO"
string.split("a,b,c", ",")       # ["a", "b", "c"]
string.contains("hello", "ell")  # true
```

## Regex Library

```clish
regex.test("hello", "^hel")      # true
regex.contains("hello", "l")    # true
regex.find("abc123def", "\\d+") # ["123"]
regex.replace("hello", "l", "r") # "herro"
regex.split("a,b,c", ",")        # ["a", "b", "c"]
```

## Time Library

```clish
time.now()           # Current timestamp (ms)
time.timestamp()     # Current timestamp (seconds)
time.sleep(1000)    # Sleep 1 second
time.format(time, "yyyy-MM-dd")  # Format date
time.parse("2024-01-01", "yyyy-MM-dd")  # Parse date
```

## NBT Library

```clish
nbt.create()              # Create empty compound
nbt.get(compound, "key") # Get value by path
nbt.set(compound, "key", value)  # Set value
nbt.has(compound, "key")  # Check if path exists
nbt.keys(compound)        # Get all keys
nbt.toString(compound)   # Convert to JSON
```
