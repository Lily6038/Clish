# Architecture Overview

Clish is a Minecraft 1.21.11 Fabric mod that provides a shell-like scripting language. This document describes the overall architecture.

## Layer Overview

```
┌─────────────────────────────────────────────────┐
│  Layer 0: Entry Points & Commands             │
│  (Mod init, /clish command, keybinds)          │
├─────────────────────────────────────────────────┤
│  Layer 1: Script Engine                        │
│  (Lexer → Parser → AST → Interpreter)         │
├─────────────────────────────────────────────────┤
│  Layer 2: Built-in Libraries                  │
│  (string, math, regex, time, nbt)             │
├─────────────────────────────────────────────────┤
│  Layer 3: Game API Bridge                      │
│  (Player, entity, block, input, command)       │
├─────────────────────────────────────────────────┤
│  Layer 4: Configuration & UI                  │
│  (Config, text editor)                        │
└─────────────────────────────────────────────────┘
```

## Key Components

### Layer 0: Entry Points

| Component | File | Description |
|-----------|------|-------------|
| Mod Entry | `Clish.java` | Main mod class, initializes everything |
| Commands | `ClishCommand.java` | `/clish` command registration |
| Keybinds | `ClishKeybinds.java` | Keyboard shortcut handling |

### Layer 1: Script Engine

| Component | File | Description |
|-----------|------|-------------|
| Lexer | `lexer/Lexer.java` | Tokenizes source code |
| Parser | `ast/Parser.java` | Builds AST from tokens |
| AST Nodes | `ast/Nodes.java` | AST node definitions |
| Script Engine | `ast/ScriptEngine.java` | Orchestrates parsing/execution |
| Interpreter | `ast/Interpreter.java` | Executes AST |

### Layer 2: Built-in Libraries

| Library | File | Description |
|---------|------|-------------|
| Math | `builtin/MathLibrary.java` | Math functions |
| String | `builtin/StringLibrary.java` | String manipulation |
| Regex | `builtin/RegexLibrary.java` | Regular expressions |
| Time | `builtin/TimeLibrary.java` | Time/date functions |
| NBT | `builtin/NbtLibrary.java` | NBT data handling |
| Builtins | `builtin/Builtins.java` | Core built-in functions |

### Layer 3: Game API

| API | File | Description |
|-----|------|-------------|
| Player | `api/PlayerApi.java` | Player position, health, inventory |
| Block | `api/BlockApi.java` | Block selection and querying |
| Entity | (part of PlayerApi) | Entity selection |
| Input | `api/InputApi.java` | Mouse/keyboard simulation |
| Command | `api/CommandApi.java` | Execute Minecraft commands |

### Layer 4: Configuration & UI

| Component | File | Description |
|-----------|------|-------------|
| Config | `config/ClishConfig.java` | Mod configuration |
| Text Editor | `ui/TextEditor.java` | In-game script editor |

## Data Flow

```
Script Source
    ↓
[Lexer] → Tokens
    ↓
[Parser] → AST
    ↓
[Interpreter] → Execute AST
    ↓
Library/API Calls → Minecraft
```

## Threading

- Scripts execute in a separate thread via `ExecutorService`
- This prevents blocking the main Minecraft thread
- Script execution can be interrupted via `Future.cancel()`

## Extension Points

### Adding New Built-in Functions

1. Create a new class in `builtin/` or extend existing library
2. Implement `ClishLibrary` interface
3. Register in `Clish.java`

### Adding New APIs

1. Create API class in `api/`
2. Implement `ClishLibrary` for each function
3. Register in `Clish.java`

## Configuration

- Scripts stored in: `config/clish/scripts/`
- Mod config: `config/clish.json`

## References

- Reference project: `../clientcommands/` (Fabric patterns)
- Fabric API documentation
- Minecraft 1.21.11 client API
