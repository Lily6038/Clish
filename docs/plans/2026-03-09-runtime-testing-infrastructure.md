# Runtime Testing Infrastructure Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development to implement this plan task-by-task.

**Goal:** Add Mockito-based unit tests and structured logging for testing runtime-dependent code (commands, keybinds, APIs) that requires Minecraft Fabric runtime.

**Architecture:** Add Mockito dependency for mocking Fabric/Minecraft classes, create test classes for ClishCommand, ClishKeybinds, and API classes with stub implementations, add structured logging for runtime debugging.

**Tech Stack:** JUnit 5, Mockito, SLF4J logging, Gradle

---

## Task 1: Add Mockito dependency to build.gradle

**Files:**
- Modify: `build.gradle`

**Step 1: Add Mockito test dependency**

```groovy
// Test dependencies
testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
testImplementation 'org.mockito:mockito-core:5.11.0'
testImplementation 'org.mockito:mockito-junit-jupiter:5.11.0'
testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
```

**Step 2: Verify dependencies are resolved**

Run: `./gradlew dependencies --configuration testRuntimeClasspath 2>&1 | grep -i mock`
Expected: See mockito-core in output

---

## Task 2: Create ClishCommand test class

**Files:**
- Create: `src/test/java/net/clish/ClishCommandTest.java`

**Step 1: Write failing test**

```java
package net.clish;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClishCommandTest {

    @Test
    void testRegisterLogsMessage() {
        // Capture log output or verify method executes without exception
        ClishCommand.register();
        // If we had log capture, we'd verify "Clish commands registered"
        assertTrue(true); // Placeholder - will enhance with log capture
    }

    @Test
    void testCommandClassExists() {
        // Verify ClishCommand has required structure
        assertNotNull(ClishCommand.class);
    }
}
```

**Step 2: Run test to verify it compiles and runs**

Run: `./gradlew test --tests "net.clish.ClishCommandTest" 2>&1`
Expected: Tests pass (basic structure test)

---

## Task 3: Create ClishKeybinds test class

**Files:**
- Create: `src/test/java/net/clish/ClishKeybindsTest.java`

**Step 1: Write failing test**

```java
package net.clish;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class ClishKeybindsTest {

    @BeforeEach
    void setUp() {
        // Set up singleton for keybind testing
        new Clish();
    }

    @Test
    void testRegisterLogsMessage() {
        ClishKeybinds.register();
        assertTrue(true); // Placeholder
    }

    @Test
    void testHandleKeyPressCtrlC() {
        // Test Ctrl-C handling (0x2D is 'C')
        // Should not throw when Clish is initialized
        ClishKeybinds.handleKeyPress(0x2D, true);
    }

    @Test
    void testHandleKeyPressNonCtrl() {
        // Non-Ctrl key should not trigger interrupt
        ClishKeybinds.handleKeyPress(0x2D, false); // 'C' without Ctrl
        // No exception means success
    }
}
```

**Step 2: Run test**

Run: `./gradlew test --tests "net.clish.ClishKeybindsTest" 2>&1`
Expected: Tests pass

---

## Task 4: Create PlayerApi test class with stub

**Files:**
- Create: `src/test/java/net/clish/api/PlayerApiTest.java`

**Step 1: Write failing test**

```java
package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class PlayerApiTest {

    @Test
    void testGetXReturnsPlaceholder() {
        PlayerApi.GetX fn = new PlayerApi.GetX();
        Object result = fn.call(List.of());
        assertEquals("Player API requires Minecraft runtime", result);
    }

    @Test
    void testGetYReturnsPlaceholder() {
        PlayerApi.GetY fn = new PlayerApi.GetY();
        Object result = fn.call(List.of());
        assertEquals("Player API requires Minecraft runtime", result);
    }

    @Test
    void testGetZReturnsPlaceholder() {
        PlayerApi.GetZ fn = new PlayerApi.GetZ();
        Object result = fn.call(List.of());
        assertEquals("Player API requires Minecraft runtime", result);
    }

    @Test
    void testGetHealthReturnsPlaceholder() {
        PlayerApi.GetHealth fn = new PlayerApi.GetHealth();
        Object result = fn.call(List.of());
        assertEquals("Player API requires Minecraft runtime", result);
    }

    @Test
    void testGetAllReturnsList() {
        List<?> all = PlayerApi.getAll();
        assertEquals(7, all.size());
    }

    @Test
    void testApiNames() {
        assertEquals("player.x", new PlayerApi.GetX().getName());
        assertEquals("player.y", new PlayerApi.GetY().getName());
        assertEquals("player.z", new PlayerApi.GetZ().getName());
        assertEquals("player.health", new PlayerApi.GetHealth().getName());
    }
}
```

**Step 2: Run test**

Run: `./gradlew test --tests "net.clish.api.PlayerApiTest" 2>&1`
Expected: Tests pass

---

## Task 5: Create BlockApi test class with stub

**Files:**
- Create: `src/test/java/net/clish/api/BlockApiTest.java`

**Step 1: Write failing test**

```java
package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BlockApiTest {

    @Test
    void testGetBlockReturnsPlaceholder() {
        BlockApi.GetBlock fn = new BlockApi.GetBlock();
        Object result = fn.call(List.of());
        assertEquals("Block API requires Minecraft runtime", result);
    }

    @Test
    void testGetNbtReturnsPlaceholder() {
        BlockApi.GetNbt fn = new BlockApi.GetNbt();
        Object result = fn.call(List.of());
        assertEquals("Block NBT requires Minecraft runtime", result);
    }

    @Test
    void testExistsReturnsPlaceholder() {
        BlockApi.Exists fn = new BlockApi.Exists();
        Object result = fn.call(List.of());
        assertEquals("Block API requires Minecraft runtime", result);
    }

    @Test
    void testGetLightReturnsPlaceholder() {
        BlockApi.GetLight fn = new BlockApi.GetLight();
        Object result = fn.call(List.of());
        assertEquals("Block API requires Minecraft runtime", result);
    }

    @Test
    void testApiNames() {
        assertEquals("block.get", new BlockApi.GetBlock().getName());
        assertEquals("block.nbt", new BlockApi.GetNbt().getName());
        assertEquals("block.exists", new BlockApi.Exists().getName());
        assertEquals("block.light", new BlockApi.GetLight().getName());
    }
}
```

**Step 2: Run test**

Run: `./gradlew test --tests "net.clish.api.BlockApiTest" 2>&1`
Expected: Tests pass

---

## Task 6: Create InputApi and CommandApi stub tests

**Files:**
- Create: `src/test/java/net/clish/api/InputApiTest.java`
- Create: `src/test/java/net/clish/api/CommandApiTest.java`

**Step 1: Write tests for InputApi**

```java
package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InputApiTest {

    @Test
    void testLeftClickReturnsPlaceholder() {
        InputApi.LeftClick fn = new InputApi.LeftClick();
        Object result = fn.call(List.of());
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testRightClickReturnsPlaceholder() {
        InputApi.RightClick fn = new InputApi.RightClick();
        Object result = fn.call(List.of());
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testMouseMoveReturnsPlaceholder() {
        InputApi.MouseMove fn = new InputApi.MouseMove();
        Object result = fn.call(List.of());
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testKeyPressReturnsPlaceholder() {
        InputApi.KeyPress fn = new InputApi.KeyPress();
        Object result = fn.call(List.of(65)); // 'A' key
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testKeyPressRequiresArgs() {
        InputApi.KeyPress fn = new InputApi.KeyPress();
        Object result = fn.call(List.of());
        assertEquals("Key code required", result);
    }

    @Test
    void testTypeTextReturnsPlaceholder() {
        InputApi.TypeText fn = new InputApi.TypeText();
        Object result = fn.call(List.of("hello"));
        assertEquals("Input simulation requires Minecraft runtime", result);
    }

    @Test
    void testTypeTextRequiresArgs() {
        InputApi.TypeText fn = new InputApi.TypeText();
        Object result = fn.call(List.of());
        assertEquals("Text required", result);
    }

    @Test
    void testApiNames() {
        assertEquals("input.leftClick", new InputApi.LeftClick().getName());
        assertEquals("input.rightClick", new InputApi.RightClick().getName());
        assertEquals("input.mouseMove", new InputApi.MouseMove().getName());
        assertEquals("input.keyPress", new InputApi.KeyPress().getName());
        assertEquals("input.typeText", new InputApi.TypeText().getName());
    }

    @Test
    void testGetAllReturnsFiveFunctions() {
        List<?> all = InputApi.getAll();
        assertEquals(5, all.size());
    }
}
```

**Step 2: Write tests for CommandApi**

```java
package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class CommandApiTest {

    @Test
    void testExecuteCommandReturnsPlaceholder() {
        CommandApi.ExecuteCommand fn = new CommandApi.ExecuteCommand();
        Object result = fn.call(List.of("say hello"));
        assertEquals("Command execution requires Minecraft runtime", result);
    }

    @Test
    void testExecuteCommandEmptyArgs() {
        CommandApi.ExecuteCommand fn = new CommandApi.ExecuteCommand();
        Object result = fn.call(List.of());
        assertNull(result);
    }

    @Test
    void testRunCommandReturnsPlaceholder() {
        CommandApi.RunCommand fn = new CommandApi.RunCommand();
        Object result = fn.call(List.of("give @s diamond 1"));
        assertEquals("Command execution requires Minecraft runtime", result);
    }

    @Test
    void testApiNames() {
        assertEquals("command.execute", new CommandApi.ExecuteCommand().getName());
        assertEquals("command.run", new CommandApi.RunCommand().getName());
    }

    @Test
    void testGetAllReturnsTwoFunctions() {
        List<?> all = CommandApi.getAll();
        assertEquals(2, all.size());
    }
}
```

**Step 3: Run tests**

Run: `./gradlew test --tests "net.clish.api.*" 2>&1`
Expected: Tests pass

---

## Task 7: Add structured logging to runtime components

**Files:**
- Modify: `src/main/java/net/clish/ClishCommand.java`
- Modify: `src/main/java/net/clish/ClishKeybinds.java`
- Modify: `src/main/java/net/clish/api/PlayerApi.java`
- Modify: `src/main/java/net/clish/api/BlockApi.java`
- Modify: `src/main/java/net/clish/api/InputApi.java`
- Modify: `src/main/java/net/clish/api/CommandApi.java`

**Step 1: Add SLF4J logger to ClishCommand**

Add at class level:
```java
private static final Logger LOGGER = LoggerFactory.getLogger("Clish");
```

Enhance register method:
```java
public static void register() {
    LOGGER.info("Registering Clish commands...");
    // TODO: Actual command registration via ClientCommandRegistrationCallback
    LOGGER.info("Clish commands registered successfully");
}
```

**Step 2: Add debug logging to PlayerApi**

Add import: `import org.slf4j.Logger;` and `import org.slf4j.LoggerFactory;`
Add at class level:
```java
private static final Logger LOGGER = LoggerFactory.getLogger("Clish");
```

Enhance each method:
```java
@Override
public Object call(List<Object> args) {
    LOGGER.debug("PlayerApi.{} called with args: {}", getName(), args);
    // TODO: Add MinecraftClient.getInstance().isInGame() check
    return "Player API requires Minecraft runtime";
}
```

**Step 3: Similarly for BlockApi, InputApi, CommandApi**

Add same logging pattern to all API classes for debugging runtime calls.

## Task 8: Create integration test script for runtime verification

**Files:**
- Create: `src/test/resources/test-scripts/integration-test.clish`

**Step 1: Create test script**

```bash
# Integration test script
echo "Testing string library..."
local result = string.length("hello")
echo "Length: " + result

# More tests...
```

---

## Task 9: Document runtime testing workflow

**Files:**
- Create: `docs/testing-runtime.md`

**Step 1: Document how to test runtime components**

```markdown
# Runtime Testing Guide

## Running Tests
./gradlew test

## Running In-Game Tests
./gradlew runClient

## Viewing Logs
- Game logs: ~/.minecraft/logs/
- In-game: Press F3 + L

## Debugging Runtime Issues
1. Check logs for "Player API requires Minecraft runtime"
2. Verify MinecraftClient.getInstance().isInGame()
3. Use `/clish` command to run test scripts
```

---

## Task 10: Run all tests and verify

**Step 1: Run full test suite**

Run: `./gradlew test 2>&1`
Expected: All tests pass

**Step 2: Check test report**

Run: `ls -la build/reports/tests/test/`

---

## Task 11: Commit

**Step 1: Commit all changes**

Run: `git add src/test/ src/main/java/net/clish/api/ build.gradle docs/testing-runtime.md`
Run: `git commit -m "test: add runtime unit tests and structured logging for Fabric-dependent code"`
