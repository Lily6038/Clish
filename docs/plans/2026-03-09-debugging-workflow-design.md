# Debugging Workflow Design

## Overview

This document describes the complete debugging workflow for developing the Clish Minecraft Fabric mod. It covers IDE setup, debugging configurations, logging, and troubleshooting common issues.

## 1. IDE Setup

### Recommended IDE
- **IntelliJ IDEA** (Community or Ultimate) - Best Fabric/Loom support
- **VS Code** with Java extensions - Alternative option

### IntelliJ Configuration

#### Project Import
1. Open IntelliJ IDEA
2. Select "Open" and choose the `build.gradle` file
3. Select "Open as Project"
4. Wait for Fabric Loom to configure automatically

#### Java Version
- Ensure Java 21 is configured in Project Structure → SDKs
- Set module SDK to "Project SDK" (Java 21)

#### Annotations Support
The project includes `com.denonwav.mcdev:annotations` for IDE hints. This provides:
- `@Mixin` annotations highlighting
- Side-aware method suggestions
- NBT tag auto-completion

## 2. Debugging Methods

### 2.1 Local Debugging (Recommended)

Use IntelliJ's built-in debugger with the runClient task:

1. **Create Run Configuration**:
   - Run → Edit Configurations
   - Add "Gradle" configuration
   - Task: `runClient`
   - Working directory: Project root

2. **Enable Debugging**:
   - In the same configuration, check "Enable debug"
   - Port: 5005 (default)

3. **Start Debugging**:
   - Set breakpoints in code
   - Run the configuration in debug mode

### 2.2 Remote Debugging

For debugging a running instance:

#### Option A: IntelliJ Remote Debug
1. Create "Remote" run configuration
2. Host: `localhost`
3. Port: `5005`
4. Use module classpath: `clish`

#### Option B: Command Line
Add to `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx2G -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005
```

Then run `./gradlew runClient` and attach debugger.

### 2.3 Logging

#### Using Fabric's Logger
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

#### Log Levels
- `info` - General information
- `debug` - Detailed debug info (hidden by default)
- `warn` - Warning messages
- `error` - Error messages with stack traces

#### Viewing Logs
- Development client logs: `run/logs/` directory
- Minecraft game logs: `.minecraft/logs/` (production)

## 3. Common Debugging Scenarios

### 3.1 Mod Not Loading
- Check `run/logs/` for loading errors
- Verify mod JAR is in `build/libs/`
- Check for missing dependencies

### 3.2 Mixin Errors
- Ensure target class exists in Minecraft/decompiled sources
- Run `./gradlew genSourcesWithVineflower` to download sources
- Check Mixin annotations are correct

### 3.3 Command Registration Issues
- Use `ClientCommandRegistrationCallback` for client commands
- Ensure command is registered in `onClientCommandRegistration`
- Check Brigadier error messages in logs

### 3.4 Runtime Errors
- Enable debug logging in `gradle.properties`:
  ```properties
  # Add to existing org.gradle.jvmargs
  -Dforge.debug=true
  ```
- Use in-game `/reload` carefully (can cause issues)

## 4. Debugging Best Practices

### Breakpoints
- **Line breakpoints**: Stop at specific line
- **Method breakpoints**: Stop at method entry/exit
- **Conditional breakpoints**: Stop when condition is met
- **Watch expressions**: Monitor variable values

### Thread Inspection
- Use "Threads" panel in debugger to see current thread
- Filter by "Minecraft Client" thread for game code

### Variable Inspection
- Hover over variables in debug view
- Use "Watches" for expressions to evaluate

## 5. Build & Test Cycle

1. Make code changes
2. Build: `./gradlew build`
3. Run: `./gradlew runClient` (or use debug configuration)
4. Test in Minecraft
5. Repeat

### Hot Reloading
For the script engine, scripts in `config/clish/scripts/` can be:
- Edited externally and reloaded via command
- No full game restart needed for script changes

## 6. Troubleshooting Reference

| Issue | Solution |
|-------|----------|
| "Cannot find Mixin target" | Run `./gradlew genSourcesWithVineflower` |
| "Port already in use" | Kill existing Java processes or change debug port |
| "ClassNotFoundException" | Rebuild with `./gradlew build` |
| "NoClassDefFoundError" | Check dependencies in `build.gradle` |
| Debugger not attaching | Check firewall settings, use `localhost:5005` |

## 7. Tools & Resources

- **Fabric Loom**: Auto-generates run configs
- **Parchment MC**: Provides mappings for readable code
- **Minecraft Dev Plugin**: IntelliJ plugin for Fabric modding
- **Fabric Discord**: Community support for debugging

---

This design provides a comprehensive debugging workflow for Clish development.
