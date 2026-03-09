# Data Types

Clish supports several data types for different kinds of data.

## Numbers

### Integers

Whole numbers without decimal points:

```clish
local age = 25
local count = -10
local zero = 0
```

### Floats

Numbers with decimal points:

```clish
local price = 19.99
local pi = 3.14159
local negative = -0.5
```

### Number Operations

```clish
local sum = 10 + 5       # 15
local diff = 10 - 5      # 5
local product = 10 * 5   # 50
local quotient = 10 / 5  # 2
local remainder = 10 % 3 # 1
local power = 2 ^ 3      # 8
```

## Strings

### String Literals

Use double or single quotes:

```clish
local name = "Hello"
local alsoString = 'World'
```

### String Concatenation

```clish
local greeting = "Hello" + ", " + "World"  # "Hello, World"
local repeated = "Ha" * 3                   # "HaHaHa"
```

### String Indexing

```clish
local str = "Hello"
local first = str[0]    # "H"
local last = str[-1]    # "o"
```

### String Slicing

```clish
local str = "Hello World"
local part = str[0:5]   # "Hello"
local fromIndex = str[6:]  # "World"
```

## Booleans

### Boolean Values

```clish
local isActive = true
local isDone = false
```

### Boolean Operations

```clish
local andResult = true && false    # false
local orResult = true || false     # true
local notResult = !true            # false
```

### Truthiness

- `false` and `null` are falsy
- Everything else is truthy:

```clish
if ("non-empty") { echo("truthy") }
if ("") { echo("falsy") }
if (0) { echo("falsy") }
if (123) { echo("truthy") }
```

## Arrays

### Array Literals

```clish
local numbers = [1, 2, 3, 4, 5]
local mixed = [1, "two", true, 4.0]
local empty = []
```

### Array Indexing

```clish
local fruits = ["apple", "banana", "cherry"]
local first = fruits[0]    # "apple"
local last = fruits[-1]   # "cherry"
```

### Array Slicing

```clish
local numbers = [0, 1, 2, 3, 4]
local slice = numbers[1:3]  # [1, 2]
```

### Array Modification

```clish
local arr = [1, 2, 3]
arr[0] = 10                # [10, 2, 3]
arr.append(4)              # [10, 2, 3, 4]
arr.remove(0)              # [2, 3, 4]
```

### Array Functions

```clish
local arr = [1, 2, 3]
len(arr)          # 3
arr.contains(2)   # true
arr.indexOf(2)   # 1
```

## Objects

### Object Literals

```clish
local person = {
    name: "Alice",
    age: 30,
    isActive: true
}
```

### Accessing Properties

```clish
local name = person.name
local alsoName = person["name"]
```

### Modifying Properties

```clish
person.age = 31
person["city"] = "New York"
```

### Nested Objects

```clish
local user = {
    profile: {
        name: "Bob",
        email: "bob@example.com"
    },
    settings: {
        notifications: true
    }
}

local email = user.profile.email
```

### Object Functions

```clish
local obj = {a: 1, b: 2}
keys(obj)     # ["a", "b"]
has(obj, "a") # true
```

## Null

### Null Value

```clish
local noValue = null
```

### Null Checks

```clish
if (value == null) {
    echo("No value")
}

if (value != null) {
    echo("Has value: " + value)
}
```

## Type Checking

Use `type()` to check types:

```clish
type(123)       # "int"
type(3.14)      # "float"
type("text")    # "string"
type(true)      # "bool"
type([1,2])     # "array"
type({a:1})     # "object"
type(null)      # "null"
```

## Type Conversion

```clish
toInt("42")      # 42
toFloat("3.14")  # 3.14
toString(123)    # "123"
toBool(1)        # true
toBool(0)        # false
```
