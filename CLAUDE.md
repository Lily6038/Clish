# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Clish is a **Minecraft 1.21.11 Fabric client-side mod** that allows players to create, edit, and execute shell-like scripts.

Reference project for learning Fabric development: `../clientcommands/` (sibling directory)

## Architecture

The mod follows a layered architecture:

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

### Key Design Decisions
- **AST Walker interpreter** (not VM/bytecode) - Shell-like behavior
- **Timeout-based loop protection** - Configurable, no instruction counting needed
- **Custom shell parser** - Not embedding JS/Lua engines

## Project Structure

```
clish/
├── src/main/java/net/clish/
│   └── Clish.java           # Mod entry point
├── src/main/resources/
│   ├── fabric.mod.json      # Mod metadata
│   └── clish.aw             # Access widener
├── build.gradle             # Fabric Loom configuration
├── gradle.properties        # Version settings
└── settings.gradle          # Project settings
```

## Key Components

### Script Engine
- **Lexer** + **Parser**: Custom shell-like syntax parser (not using existing JS/Lua engines)
- **VM**: Stack-based virtual machine with instruction counting to prevent infinite loops
- Scripts stored in `config/clish/scripts/`

### Game API
- Player data APIs (position, health, inventory)
- Entity selection API (Minecraft selector syntax like `@e[distance=..5, limit=2, type=item]`)
- Block selection API (position syntax like `254 64 -128`)
- Input simulation (left/right click, mouse movement) via Mixin
- Uses `ClientCommandRegistrationCallback` for command registration

### Libraries
- NBT library, string library, regex, math, time libraries

## Dependencies

### Required
- fabric-loader >= 0.18.1
- fabric-api 0.139.4+1.21.11
- minecraft 1.21.11
- Java 21

### Recommended
- clientarguments 1.11.x+ (enhanced command arguments)
- betterconfig 2.4.x+ (configuration)
- modmenu 6.x+ (config UI integration)

## Development Notes

- Script execution must be async to avoid blocking the main thread
- Implement timeout protection and instruction count limits
- Use WatchService for hot-reloading scripts from disk
- Reference ../clientcommands for Fabric patterns

## Building & Running

```bash
# Download Minecraft sources
./gradlew genSourcesWithVineflower

# Build the mod JAR
./gradlew build

# Run development client
./gradlew runClient
```

Output JAR: `build/libs/clish-<version>.jar`

## Planning Documents

- `Goals.md` - Project goals and feature overview
- `RequirementsAnalysis.md` - Detailed requirements, feasibility analysis, and architecture design
- `docs/plans/` - Implementation plans
