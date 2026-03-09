# Debugging Workflow Design

## Overview

This document describes the complete debugging workflow for developing the Clish Minecraft Fabric mod using VS Code.

## 1. VS Code Setup

### Required Extensions

- **Extension Pack for Java** - Java language support and debugging
- **Debugger for Java** - Advanced Java debugging features
- **Minecraft Development** (optional) - Fabric modding support with Mixin highlighting

### Java Configuration

1. Install Java 21 JDK
2. Add to VS Code settings (`settings.json`):
   ```json
   {
     "java.configuration.runtimes": [
       {
         "name": "JavaSE-21",
         "path": "/path/to/jdk-21",
         "default": true
       }
     ]
   }
   ```

3. Open the project folder in VS Code
4. Wait for Java Language Server to index

### Project Configuration

Run to download Minecraft sources:
```bash
./gradlew genSourcesWithVineflower
```

The project includes `com.demonwav.mcdev:annotations` which provides:
- `@Mixin` annotation highlighting
- Side-aware method suggestions
- NBT tag auto-completion

## 2. Debugging Methods

### 2.1 VS Code Debug Configuration (Recommended)

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

### Debugging Steps

1. **Enable Debug Mode**:
   Modify `gradle.properties`:
   ```properties
   org.gradle.jvmargs=-Xmx2G -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
   ```

2. **Run the Game**:
   ```bash
   ./gradlew runClient
   ```

3. **Attach Debugger**:
   - Press F5 in VS Code
   - Select "Attach to Minecraft"
   - Set breakpoints in your code

### 2.2 Attach to Running Process

1. Run `./gradlew runClient` in terminal
2. Press F5 → "Attach to Java Process"
3. Select the Minecraft client process from the list

### 2.3 Debug Features

- **Variables Panel**: View local variables and their values
- **Watch Expressions**: Right-click → "Watch" to monitor expressions
- **Call Stack**: See the method call hierarchy
- **Breakpoints Panel**: Manage all breakpoints (line, method, conditional)
- **Debug Console**: Execute code in the current context

## 3. Logging

### Using Fabric's Logger

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

### Log Levels

- `info` - General information
- `debug` - Detailed debug info (hidden by default)
- `warn` - Warning messages
- `error` - Error messages with stack traces

### Viewing Logs

- Development client: `run/logs/` directory
- Production: `.minecraft/logs/`

## 4. Common Debugging Scenarios

### 4.1 Mod Not Loading
- Check `run/logs/` for loading errors
- Verify mod JAR is in `build/libs/`
- Check for missing dependencies

### 4.2 Mixin Errors
- Ensure target class exists: Run `./gradlew genSourcesWithVineflower`
- Check Mixin annotations are correct
- Verify target class is decompiled

### 4.3 Command Registration Issues
- Use `ClientCommandRegistrationCallback` for client commands
- Check Brigadier error messages in logs

### 4.4 Port Already in Use
- Kill existing Java processes: `pkill java` or Task Manager
- Change debug port in `launch.json`

### 4.5 No Minecraft Sources
```bash
./gradlew genSourcesWithVineflower
```

## 5. Keyboard Shortcuts

| Action | Windows/Linux | macOS |
|--------|---------------|-------|
| Start Debug | F5 | F5 |
| Stop Debug | Shift+F5 | Shift+F5 |
| Toggle Breakpoint | F9 | F9 |
| Step Over | F10 | F10 |
| Step Into | F11 | F11 |
| Step Out | Shift+F11 | Shift+F11 |
| Debug Console | Ctrl+Shift+Y | Cmd+Shift+Y |

## 6. Troubleshooting Reference

| Issue | Solution |
|-------|----------|
| "Cannot find Mixin target" | Run `./gradlew genSourcesWithVineflower` |
| "Port already in use" | Kill Java processes or change port |
| "ClassNotFoundException" | Rebuild with `./gradlew build` |
| "NoClassDefFoundError" | Check dependencies in `build.gradle` |
| Java not configured | Set `java.configuration.runtimes` in settings |
| Debugger not attaching | Check firewall, use `localhost:5005` |
| No sources indexed | Run genSourcesWithVineflower task |

## 7. Build & Test Cycle

1. Make code changes in VS Code
2. Build: `./gradlew build`
3. Run: `./gradlew runClient` (or use debug config)
4. Test in Minecraft
5. Use debug console to inspect variables
6. Repeat

### Hot Reloading

For the script engine, scripts in `config/clish/scripts/` can be:
- Edited externally and reloaded via command
- No full game restart needed for script changes

## 8. Recommended Workflow

1. Set breakpoints in code
2. Run `./gradlew runClient` with debug args enabled
3. Press F5 → Attach to Minecraft
4. Trigger the code path in-game
5. Inspect variables in VS Code
6. Modify code and rebuild as needed

---

This design provides a complete VS Code debugging workflow for Clish development.
