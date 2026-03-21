# Syntax Reference

This document covers basic syntax elements in Clish.

## Variables

### Declaration

Use `local` to declare variables:

```clish
local x = 10
local name = "Hello"
local isActive = true
local numbers = [1, 2, 3]
local data = {key: "value"}
```

### Assignment

Assign new values to existing variables:

```clish
x = 20
name = "World"
```

## Identifiers

Variable and function names:
- Start with a letter or underscore
- Contain letters, digits, and underscores
- Are case-sensitive

```clish
local _private = 1
local camelCase = 2
local snake_case = 3
```

## Comments

Use `#` for single-line comments:

```clish
# This is a comment
local x = 10  # Inline comment
```

## Statements

### Expression Statements

```clish
x + y
"hello"
player.health()
```

### Block Statements

Group statements in braces:

```clish
{
    local x = 10
    local y = 20
    echo(x + y)
}
```

### Empty Statement

```clish
;  # Does nothing
```

## Scoping

Variables are scoped to their block:

```clish
{
    local x = 10
}
# x is not accessible here
```

## Reserved Words

The following cannot be used as identifiers:

```
if, elif, else, for, while, function, return, local, true, false, null,
try, catch, finally, ok, err, spawn, wait, channel, send, receive,
coproc
```

## Line Structure

- Statements are separated by newlines
- Semicolons are optional but supported
- Multiple statements on one line require semicolons:

```clish
local x = 10; local y = 20
```
