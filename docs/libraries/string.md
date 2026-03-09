# String Library

The string library provides functions for string manipulation.

## Length

### string.length

Returns the length of a string.

```clish
string.length("hello")      # 5
string.length("")           # 0
```

## Substring

### string.substring

Returns a substring.

```clish
string.substring("hello", 1, 3)   # "el" (characters 1-2)
string.substring("hello", 2)       # "llo" (from index 2 to end)
```

## Search

### string.indexOf

Returns the index of the first occurrence.

```clish
string.indexOf("hello", "l")     # 2
string.indexOf("hello", "x")     # -1 (not found)
```

### string.lastIndexOf

Returns the index of the last occurrence.

```clish
string.lastIndexOf("hello", "l")  # 3
```

### string.contains

Checks if string contains a substring.

```clish
string.contains("hello", "ell")  # true
string.contains("hello", "world") # false
```

## Case Conversion

### string.toUpperCase

Converts to uppercase.

```clish
string.toUpperCase("hello")  # "HELLO"
```

### string.toLowerCase

Converts to lowercase.

```clish
string.toLowerCase("HELLO")  # "hello"
```

## Trim

### string.trim

Removes whitespace from both ends.

```clish
string.trim("  hello  ")  # "hello"
```

## Replace

### string.replace

Replaces literal substrings.

```clish
string.replace("hello", "l", "r")  # "herro"
```

### string.replaceAll

Replaces using regex patterns.

```clish
string.replaceAll("hello123world", "\\d+", "X")  # "helloXworld"
```

## Split/Join

### string.split

Splits string into array.

```clish
string.split("a,b,c", ",")          # ["a", "b", "c"]
string.split("a:b:c", ":", 2)        # ["a", "b:c"] (limit 2)
```

### string.join

Joins array elements with delimiter.

```clish
string.join(", ", ["a", "b", "c"])  # "a, b, c"
```

## Prefix/Suffix

### string.startsWith

Checks if string starts with prefix.

```clish
string.startsWith("hello", "hel")  # true
string.startsWith("hello", "wor")  # false
```

### string.endsWith

Checks if string ends with suffix.

```clish
string.endsWith("hello", "llo")  # true
string.endsWith("hello", "world") # false
```

## Character Access

### string.charAt

Returns character at index.

```clish
string.charAt("hello", 0)   # "h"
string.charAt("hello", 4)   # "o"
```

## Repeat

### string.repeat

Returns repeated string.

```clish
string.repeat("ha", 3)  # "hahaha"
string.repeat("x", 0)   # ""
```

## Examples

### Parse CSV Line

```clish
function parseCsv(line) {
    return string.split(line, ",")
}

local data = parseCsv("name,age,city")
# ["name", "age", "city"]
```

### Format Number

```clish
function formatNumber(num) {
    local str = toString(num)
    # Add thousands separators
    local parts = string.split(str, ".")
    local intPart = parts[0]
    local result = ""
    local count = 0

    for (local i = len(intPart) - 1; i >= 0; i = i - 1) {
        if (count > 0 && count % 3 == 0) {
            result = "," + result
        }
        result = intPart[i] + result
        count = count + 1
    }

    if (len(parts) > 1) {
        result = result + "." + parts[1]
    }

    return result
}
```

### Capitalize First Letter

```clish
function capitalize(str) {
    if (len(str) == 0) {
        return str
    }
    return string.toUpperCase(string.charAt(str, 0)) + string.substring(str, 1)
}

capitalize("hello")  # "Hello"
```

### Check Valid Number

```clish
function isNumeric(str) {
    # Try to convert and check if it worked
    local num = toFloat(str)
    return type(num) == "float" || type(num) == "int"
}
```

### Word Wrap

```clish
function wordWrap(text, maxWidth) {
    local words = string.split(text, " ")
    local lines = [""]
    local currentLine = 0

    for (local word in words) {
        local current = lines[currentLine]
        if (len(current) + len(word) + 1 <= maxWidth) {
            if (len(current) > 0) {
                lines[currentLine] = current + " " + word
            } else {
                lines[currentLine] = word
            }
        } else {
            currentLine = currentLine + 1
            lines[currentLine] = word
        }
    }

    return lines
}
```
