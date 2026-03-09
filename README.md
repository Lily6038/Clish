# Clish

A Minecraft 1.21.11 Fabric client-side mod that allows players to create, edit, and execute shell-like scripts.

## Features

- Shell-like scripting language for Minecraft
- Custom lexer, parser, and AST walker interpreter
- Built-in libraries: string, math, regex, time, nbt
- Player data APIs (position, health, inventory)
- Entity selection API (Minecraft selector syntax like `@e[distance=..5, limit=2, type=item]`)
- Block selection API (position syntax like `254 64 -128`)
- Input simulation support
- Hot-reloading scripts from disk

## Requirements

- Java 21
- Gradle 9.2.1 (included via wrapper)

## Building

```bash
# Clone the repository
git clone https://github.com/clish/clish.git
cd clish

# Download Minecraft sources (first time only)
./gradlew genSourcesWithVineflower

# Build the mod JAR
./gradlew build
```

The built JAR will be at `build/libs/clish-<version>.jar`

## Running

### Development Client

```bash
./gradlew runClient
```

This launches Minecraft 1.21.11 with the mod installed. The game data will be stored in the `run/` directory.

### Production Build

Copy the built JAR from `build/libs/clish-<version>.jar` to your Minecraft mods folder (`~/.minecraft/mods` for Fabric).

## Project Structure

```
src/main/java/net/clish/
└── Clish.java           # Mod entry point

src/main/resources/
├── fabric.mod.json      # Mod metadata
└── clish.aw             # Access widener
```

## Architecture

The mod follows a layered architecture:

- **Layer 0**: Entry Points & Commands (Mod init, /clish command)
- **Layer 1**: Script Engine (Lexer, Parser, AST Walker)
- **Layer 2**: Built-in Libraries (string, math, regex, time, nbt)
- **Layer 3**: Game API Bridge (Player data, input simulation)
- **Layer 4**: Configuration & UI

## Dependencies

- [Fabric Loader](https://fabricmc.net/) 0.18.1+
- [Fabric API](https://fabricmc.net/) 0.139.4+
- Minecraft 1.21.11

## Debugging

This section covers the debugging workflow for developing the Clish mod using VS Code.

### VS Code Setup

#### Required Extensions

Install these extensions from the VS Code Marketplace:

1. **Extension Pack for Java** - Java language support, debugging, and project management
2. **Debugger for Java** - Advanced Java debugging features
3. **Minecraft Development** (optional) - Fabric modding support, Mixin highlighting

#### Project Import

1. Open VS Code in the project directory
2. Install Java 21 JDK if not already installed
3. Set Java home in VS Code settings:
   ```json
   "java.configuration.runtimes": [
     {
       "name": "JavaSE-21",
       "path": "/path/to/jdk-21",
       "default": true
     }
   ]
   ```
4. Wait for Java Language Server to index the project
5. Run `./gradlew genSourcesWithVineflower` to download Minecraft sources

The project includes `com.demonwav.mcdev:annotations` which provides:
- `@Mixin` annotation highlighting
- Side-aware method suggestions
- NBT tag auto-completion

### Debugging Methods

#### Method 1: VS Code Debug Configuration (Recommended)

Create `.vscode/launch.json`:
```json
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Debug runClient",
            "request": "launch",
            "mainClass": "net.fabricmc.loader.impl.launch.knot.KnotClient",
            "projectName": "clish",
            "console": "integratedTerminal",
            "vmArgs": "-Xmx2G"
        },
        {
            "type": "java",
            "name": "Attach to Minecraft",
            "request": "attach",
            "projectName": "clish",
            "hostName": "localhost",
            "port": 5005
        }
    ]
}
```

Create `.vscode/tasks.json`:
```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "runClient",
            "type": "shell",
            "command": "./gradlew runClient",
            "group": "build",
            "problemMatcher": []
        },
        {
            "label": "genSources",
            "type": "shell",
            "command": "./gradlew genSourcesWithVineflower",
            "problemMatcher": []
        }
    ]
}
```

**To debug:**
1. First, add debug arguments to `gradle.properties`:
   ```properties
   org.gradle.jvmargs=-Xmx2G -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
   ```
2. Run `./gradlew runClient` in terminal
3. Press F5 and select "Attach to Minecraft"
4. Set breakpoints in your code

#### Method 2: Terminal + VS Code Debug

1. Modify `gradle.properties` to enable debugging:
   ```properties
   org.gradle.jvmargs=-Xmx2G -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
   ```

2. Run the game:
   ```bash
   ./gradlew runClient
   ```

3. In VS Code, press F5 → "Attach to Java Process" → Select the Minecraft process

#### Debug Console

When debugging:
- **VARIABLES** panel - View local variables and watch expressions
- **CALL STACK** panel - See method call hierarchy
- **BREAKPOINTS** panel - Manage all breakpoints
- Hover over variables to see their values
- Right-click → "Watch" to add expressions

### Logging

Use Fabric's logger in your code:
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Example {
    private static final Logger LOGGER = LoggerFactory.getLogger("clish");

    public void debugMethod() {
        LOGGER.info("Method called");
        LOGGER.debug("Debug info: {}", variable);
        LOGGER.warn("Warning: {}", warning);
        LOGGER.error("Error occurred", exception);
    }
}
```

- View dev client logs in `run/logs/` directory
- Log levels: `info`, `debug`, `warn`, `error`

### Common Debugging Issues

| Issue | Solution |
|-------|----------|
| "Cannot find Mixin target" | Run `./gradlew genSourcesWithVineflower` |
| "Port already in use" | Kill existing Java processes or change debug port in launch.json |
| "ClassNotFoundException" | Rebuild with `./gradlew build` |
| Mod not loading | Check `run/logs/` for errors |
| No Minecraft sources | Run `./gradlew genSourcesWithVineflower` |
| Java not found | Set `java.configuration.runtimes` in VS Code settings |

### Quick Reference

| Action | Command/Shortcut |
|--------|------------------|
| Start debugging | F5 |
| Toggle breakpoint | F9 |
| Step over | F10 |
| Step into | F11 |
| Step out | Shift+F11 |
| Stop debugging | Shift+F5 |
| Open debug console | Debug → Debug Console |

### Hot Reloading

Scripts in `config/clish/scripts/` can be edited externally and reloaded via command—no full game restart needed for script changes.

## License

MIT
