# Testing Suite Design - 2026-03-09

## Overview
Create a comprehensive JUnit 5 test suite for the clish Minecraft mod that tests all testable modules without requiring Minecraft runtime.

## Testable Modules

1. **Lexer** (`lexer/`) - Tokenizes script strings
2. **Parser/AST** (`ast/`) - Creates Abstract Syntax Tree
3. **Built-in Libraries** - Pure utility functions
   - StringLibrary, MathLibrary, RegexLibrary, TimeLibrary
4. **ScriptEngine** - Orchestrates Lexer → Parser → Interpreter

## Non-Testable Modules
- PlayerApi, BlockApi, InputApi, CommandApi (require Minecraft runtime)
- Config, UI components, Clish mod entry point

## Test Structure
```
src/test/java/net/clish/
├── lexer/
│   └── LexerTest.java
├── ast/
│   ├── ParserTest.java
│   └── InterpreterTest.java
└── builtin/
    ├── StringLibraryTest.java
    ├── MathLibraryTest.java
    ├── RegexLibraryTest.java
    └── TimeLibraryTest.java
```

## Framework Choice: JUnit 5
- Standard in Java/Minecraft community
- Native Gradle support
- Extensible with AssertJ if needed
