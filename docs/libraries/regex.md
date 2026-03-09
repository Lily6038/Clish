# Regex Library

The regex library provides regular expression pattern matching.

## Pattern Syntax

Uses Java regex syntax:
- `.` - Any character
- `\d` - Digit [0-9]
- `\w` - Word character [a-zA-Z0-9_]
- `\s` - Whitespace
- `*` - Zero or more
- `+` - One or more
- `?` - Zero or one
- `^` - Start of string
- `$` - End of string
- `[]` - Character class
- `()` - Capture group

## Match Testing

### regex.test

Tests if entire string matches pattern.

```clish
regex.test("hello", "^hel")   # true
regex.test("hello", "^wor")    # false
regex.test("123", "^\\d+$")   # true
regex.test("abc", "^\\d+$")   # false
```

### regex.contains

Tests if pattern is found anywhere in string.

```clish
regex.contains("hello", "l")     # true
regex.contains("hello", "x")      # false
regex.contains("abc123", "\\d+")  # true
```

## Finding Matches

### regex.find

Returns all matches as array.

```clish
regex.find("abc123def456", "\\d+")  # ["123", "456"]
regex.find("hello", "\\d+")         # []
```

### regex.findGroups

Returns capture groups from first match.

```clish
regex.findGroups("hello123world", "(\\w+)(\\d+)(\\w+)")
# ["hello123world", "hello", "123", "world"]

regex.findGroups("2024-01-15", "(\\d{4})-(\\d{2})-(\\d{2})")
# ["2024-01-15", "2024", "01", "15"]
```

### regex.count

Returns number of matches.

```clish
regex.count("abc123def456", "\\d+")  # 2
regex.count("hello", "\\d+")          # 0
```

## Replacement

### regex.replace

Replaces all matches.

```clish
regex.replace("hello", "l", "r")           # "herro"
regex.replace("a1b2c3", "\\d", "X")        # "aXbXcX"
regex.replace("hello world", "world", "there")  # "hello there"
```

### regex.replaceFirst

Replaces only first match.

```clish
regex.replaceFirst("hello l l", "l", "r")   # "herro l"
regex.replaceFirst("a1b2c3", "\\d", "X")   # "aXb2c3"
```

## Splitting

### regex.split

Splits string by regex pattern.

```clish
regex.split("a,b;c,d", "[,;]")      # ["a", "b", "c", "d"]
regex.split("a:b:c", ":", 2)        # ["a", "b:c"]
```

## Utility

### regex.escape

Escapes special regex characters.

```clish
regex.escape("a.b*c?")  # "a\\.b\\*c\\?"
```

Useful when searching for literal strings that may contain regex special characters.

## Examples

### Validate Email

```clish
function isValidEmail(email) {
    local pattern = "^[\\w.-]+@[\\w.-]+\\.\\w+$"
    return regex.test(email, pattern)
}

isValidEmail("user@example.com")    # true
isValidEmail("invalid-email")       # false
```

### Extract Numbers

```clish
function extractNumbers(text) {
    return regex.find(text, "-?\\d+\\.?\\d*")
}

extractNumbers("abc 123 def 45.6 ghi")  # ["123", "45.6"]
```

### Replace Multiple Patterns

```clish
function sanitize(input) {
    local result = input
    result = regex.replace(result, "<script", "&lt;script")
    result = regex.replace(result, ">", "&gt;")
    return result
}
```

### Parse Log Line

```clish
function parseLogLine(line) {
    # Example: [2024-01-15 10:30:45] ERROR: Something went wrong
    local pattern = "\\[([^\\]]+)\\] (\\w+): (.*)"
    local groups = regex.findGroups(line, pattern)

    if (len(groups) > 0) {
        return {
            timestamp: groups[1],
            level: groups[2],
            message: groups[3]
        }
    }
    return null
}
```

### Validate Minecraft Username

```clish
function isValidUsername(name) {
    # 3-16 characters, alphanumeric and underscores
    local pattern = "^[a-zA-Z0-9_]{3,16}$"
    return regex.test(name, pattern)
}

isValidUsername("Steve")        # true
isValidUsername("Player_123")   # true
isValidUsername("ab")           # false
```

### Find All URLs

```clish
function findUrls(text) {
    local pattern = "https?://[\\w.-]+(?:/[\\w.-]*)*"
    return regex.find(text, pattern)
}

findUrls("Visit http://example.com or https://site.org today!")
# ["http://example.com", "https://site.org"]
```

### Remove HTML Tags

```clish
function stripHtml(html) {
    return regex.replace(html, "<[^>]+>", "")
}

stripHtml("<p>Hello <b>World</b>!</p>")  # "Hello World!"
```
