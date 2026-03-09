# Clish Architecture Design

## Overview

Clish is a Minecraft 1.21.11 Fabric client-side mod that allows players to create, edit, and execute shell-like scripts.

## Design Decisions (Approved)

### Execution Model

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Interpreter type | AST Walker | Shell-like behavior, simpler than VM |
| Loop protection | Timeout-based | Avoids instruction counting complexity |
| Emergency break | Ctrl-C style keybind | Familiar shell behavior |
| Configuration | Timeout duration user-configurable | Flexibility for different scripts |

### Layer Architecture

```
┌─────────────────────────────────────────────────┐
│  Layer 0: Entry Points & Commands             │
│  (Mod init, /clish, Ctrl-C handler)           │
├─────────────────────────────────────────────────┤
│  Layer 1: Script Engine                       │
│  (Lexer → Parser → AST Walker)               │
├─────────────────────────────────────────────────┤
│  Layer 2: Built-in Libraries                  │
│  (string, math, regex, time, nbt)             │
├─────────────────────────────────────────────────┤
│  Layer 3: Game API Bridge                     │
│  (Player, entity, block, input simulation)    │
├─────────────────────────────────────────────────┤
│  Layer 4: Configuration & UI                   │
│  (Config, text editor, docs)                  │
└─────────────────────────────────────────────────┘
```

## To Be Designed (Future Sessions)

### Layer 1: Script Engine
- [ ] Lexer token types and rules
- [ ] Parser grammar (BNF or similar)
- [ ] AST node types
- [ ] Built-in commands (echo, cd, etc.)

### Layer 2: Built-in Libraries
- [ ] string library functions
- [ ] math library functions
- [ ] regex library
- [ ] time library
- [ ] NBT library

### Layer 3: Game API Bridge
- [ ] Player data APIs (position, health, inventory)
- [ ] Entity selection API (Minecraft selector syntax like `@e[distance=..5, limit=2, type=item]`)
- [ ] Block selection API (position syntax like `254 64 -128`)
- [ ] Input simulation APIs (click, mouse)
- [ ] Command execution integration

### Layer 4: Configuration & UI
- [ ] Config system (timeout settings)
- [ ] In-game text editor UI
- [ ] Documentation system

## Reference

- Reference project: `./clientcommands/`
- Requirements: `RequirementsAnalysis.md`
- Goals: `Goals.md`
