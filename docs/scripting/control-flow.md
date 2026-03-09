# Control Flow

Clish supports conditional statements and loops for controlling program execution.

## If / Else

### Basic If

```clish
if (condition) {
    # code runs when condition is true
}
```

### If-Else

```clish
if (x > 10) {
    echo("x is greater than 10")
} else {
    echo("x is 10 or less")
}
```

### If-Elif-Else

```clish
if (x > 10) {
    echo("x is greater than 10")
} elif (x > 5) {
    echo("x is greater than 5")
} else {
    echo("x is 5 or less")
}
```

### Nested Conditions

```clish
if (x > 0) {
    if (x > 10) {
        echo("x is large")
    } else {
        echo("x is positive but small")
    }
}
```

## For Loop

### C-Style For

```clish
for (local i = 0; i < 10; i = i + 1) {
    echo(i)
}
```

### For with Step

```clish
for (local i = 0; i < 100; i = i + 10) {
    echo(i)  # 0, 10, 20, 30, 40, 50, 60, 70, 80, 90
}
```

### For with Decrement

```clish
for (local i = 10; i > 0; i = i - 1) {
    echo(i)  # 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
}
```

### For Each (Array)

```clish
local items = ["apple", "banana", "cherry"]
for (local item in items) {
    echo(item)
}
```

## While Loop

### Basic While

```clish
local count = 0
while (count < 5) {
    echo(count)
    count = count + 1
}
```

### While True with Break

```clish
local x = 0
while (true) {
    x = x + 1
    if (x >= 10) {
        break
    }
}
echo(x)  # Prints 10
```

## Loop Control

### Break

Exit the loop immediately:

```clish
for (local i = 0; i < 100; i = i + 1) {
    if (i == 5) {
        break
    }
    echo(i)  # Prints 0, 1, 2, 3, 4
}
```

### Continue

Skip to the next iteration:

```clish
for (local i = 0; i < 5; i = i + 1) {
    if (i == 2) {
        continue
    }
    echo(i)  # Prints 0, 1, 3, 4
}
```

## Return in Loops

You can return from a function inside a loop:

```clish
function findItem(items, target) {
    for (local item in items) {
        if (item == target) {
            return true
        }
    }
    return false
}
```
