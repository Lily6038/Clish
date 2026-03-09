# Development Workflow

This guide covers how to develop and extend the Clish mod.

## Prerequisites

- Java 21
- Gradle (included via wrapper)
- IntelliJ IDEA (recommended) or VS Code

## Setup

### 1. Clone and Setup

```bash
git clone <repository>
cd clish
./gradlew genSourcesWithVineflower  # Download Minecraft sources
```

### 2. IDE Setup

**IntelliJ IDEA:**
1. Open project directory
2. Import as Gradle project
3. Wait for dependencies to resolve

**VS Code:**
1. Install Java extensions
2. Open project folder
3. Use `./gradlew` commands

## Building

### Build Mod JAR

```bash
./gradlew build
```

Output: `build/libs/clish-<version>.jar`

### Run Development Client

```bash
./gradlew runClient
```

This launches Minecraft with the mod installed.

## Project Structure

```
clish/
├── src/main/java/net/clish/
│   ├── Clish.java              # Mod entry point
│   ├── ClishCommand.java       # /clish command
│   ├── ClishKeybinds.java     # Keyboard shortcuts
│   ├── lexer/                 # Lexer components
│   ├── ast/                   # Parser & Interpreter
│   ├── builtin/               # Built-in libraries
│   ├── api/                   # Game APIs
│   ├── config/                # Configuration
│   └── ui/                    # UI components
├── src/main/resources/
│   ├── fabric.mod.json        # Mod metadata
│   └── clish.aw              # Access widener
└── docs/                      # Documentation
```

## Development Tasks

### Adding a New Built-in Function

1. Choose or create library file in `builtin/`
2. Add new class implementing `ClishLibrary`:

```java
public class MyFunction implements ClishLibrary {
    @Override
    public String getName() {
        return "my.function";
    }

    @Override
    public Object call(List<Object> args) {
        // Validate arguments
        if (args.isEmpty()) {
            throw new RuntimeException("Expected argument");
        }

        // Implementation
        Object input = args.get(0);

        // Return result
        return result;
    }
}
```

3. Register in `Clish.java`:

```java
for (ClishLibrary lib : MyLibrary.getAll()) {
    scriptEngine.registerLibrary(lib.getName(), lib);
}
```

### Adding a New Game API

1. Create API class in `api/`
2. Implement functions similar to built-ins
3. Register in `Clish.java`

### Modifying the Parser

Parser is in `ast/Parser.java`. To add new syntax:

1. Add token type in `lexer/TokenType.java`
2. Update lexer to recognize new tokens
3. Add AST node type in `ast/Nodes.java`
4. Update parser to build new node
5. Update interpreter to handle new node

## Debugging

### VS Code Debugging

A debugging workflow is configured in `.vscode/launch.json`.

1. Set breakpoints in code
2. Run the "Debug Client" configuration
3. Minecraft will launch with debugging enabled

### Logging

Use `System.out.println()` for basic logging:

```java
System.out.println("[Clish] Processing script...");
```

For errors:

```java
System.err.println("[Clish] Error: " + e.getMessage());
```

## Testing

### Manual Testing

1. Build the mod: `./gradlew build`
2. Copy JAR to `~/.minecraft/mods/`
3. Launch Minecraft
4. Test functionality in-game

### Script Testing

Create test scripts in `config/clish/scripts/`:

```clish
# test.clish
echo("Testing...")
math.sqrt(16)
```

Run with `/clish run test`

## Common Issues

### Mod Not Loading

- Check Minecraft version matches (1.21.11)
- Verify Fabric and Fabric API are installed
- Check console for error messages

### Script Errors

- Use `echo()` to debug values
- Check syntax against documentation
- Verify function arguments

### Build Failures

- Ensure Java 21 is installed
- Run `./gradlew clean build`
- Check for dependency conflicts

## Code Style

- Follow existing code patterns
- Use meaningful variable names
- Add JavaDoc for public APIs
- Keep functions focused and small

## Resources

- [Fabric Wiki](https://fabricmc.net/wiki/documentation:fabric_mod_json)
- [Fabric API](https://modmuss50.me/fabric-docs/)
- [Minecraft Wiki](https://minecraft.wiki/)
