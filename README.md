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

This section covers the debugging workflow for developing the Clish mod.

### IDE Setup

**Recommended IDE**: IntelliJ IDEA (Community or Ultimate)

1. Open the project in IntelliJ IDEA
2. Select "Open" and choose the `build.gradle` file
3. Wait for Fabric Loom to configure automatically
4. Ensure Java 21 is set in Project Structure → SDKs

The project includes `com.demonwav.mcdev:annotations` which provides:
- `@Mixin` annotation highlighting
- Side-aware method suggestions
- NBT tag auto-completion

### Debugging Methods

#### Local Debugging (Recommended)

1. **Create Run Configuration**:
   - Run → Edit Configurations → Add "Gradle"
   - Task: `runClient`
   - Working directory: Project root

2. **Enable Debugging**:
   - In the same configuration, check "Enable debug"
   - Port: 5005 (default)

3. **Start Debugging**:
   - Set breakpoints in code
   - Run the configuration in debug mode

#### Remote Debugging

Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx2G -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

Then run `./gradlew runClient` and attach IntelliJ's remote debugger to `localhost:5005`.

#### Logging

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
| "Port already in use" | Kill existing Java processes or change debug port |
| "ClassNotFoundException" | Rebuild with `./gradlew build` |
| Mod not loading | Check `run/logs/` for errors |

### Hot Reloading

Scripts in `config/clish/scripts/` can be edited externally and reloaded via command—no full game restart needed for script changes.

## License

MIT
