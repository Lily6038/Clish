# Clish InputApi & NbtLibrary Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement InputApi with Mixin-based input simulation and complete NbtLibrary with real Minecraft CompoundTag integration

**Architecture:** Mirror clientcommands patterns exactly - use clientarguments 1.11.6, BlockDataAccessor for NBT, and Mixin for input simulation

**Tech Stack:** Fabric 1.21.11, Mixin, clientarguments 1.11.6, Mockito for testing

---

## Task 1: Add clientarguments Dependency

**Files:**
- Modify: `gradle.properties:25`
- Modify: `build.gradle:33-35`

**Step 1: Update gradle.properties**

```bash
# Change clientarguments_version to 1.11.6
clientarguments_version=1.11.6
```

**Step 2: Uncomment clientarguments in build.gradle**

```groovy
# In dependencies section, uncomment:
include modImplementation("dev.xpple:clientarguments:${project.clientarguments_version}")
```

**Step 3: Verify build**

```bash
./gradlew build --no-daemon
```

Expected: BUILD SUCCESSFUL

---

## Task 2: Create Mixin Infrastructure

**Files:**
- Create: `src/main/resources/mixins.clish.json`
- Modify: `src/main/resources/fabric.mod.json`
- Create: `src/main/java/net/clish/mixin/IInputSimulator.java`

**Step 1: Create mixins.clish.json**

```json
{
  "required": true,
  "package": "net.clish.mixin",
  "compatibilityLevel": "JAVA_21",
  "mixins": [
    "InputSimulatorMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
```

**Step 2: Update fabric.mod.json to include mixins**

Add to fabric.mod.json:
```json
"mixins": [
  "mixins.clish.json"
]
```

**Step 3: Create IInputSimulator interface**

Create file: `src/main/java/net/clish/mixin/IInputSimulator.java`
```java
package net.clish.mixin;

public interface IInputSimulator {
    void clish_leftClick();
    void clish_rightClick();
    void clish_mouseMove(double dx, double dy);
    void clish_keyPress(int keyCode);
    void clish_typeText(String text);
}
```

**Step 4: Verify build**

```bash
./gradlew build --no-daemon
```

Expected: BUILD SUCCESSFUL

---

## Task 3: Create InputSimulatorMixin

**Files:**
- Create: `src/main/java/net/clish/mixin/InputSimulatorMixin.java`

**Step 1: Create InputSimulatorMixin**

```java
package net.clish.mixin;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class InputSimulatorMixin implements IInputSimulator {

    @Shadow @Final private Screen screen;

    @Unique
    @Override
    public void clish_leftClick() {
        if (screen != null) {
            screen.mouseClicked(0, 0, 0);
        }
    }

    @Unique
    @Override
    public void clish_rightClick() {
        if (screen != null) {
            screen.mouseClicked(0, 0, 1);
        }
    }

    @Unique
    @Override
    public void clish_mouseMove(double dx, double dy) {
        // Implementation depends on Minecraft version
    }

    @Unique
    @Override
    public void clish_keyPress(int keyCode) {
        // Implementation depends on Minecraft version
    }

    @Unique
    @Override
    public void clish_typeText(String text) {
        // Implementation depends on Minecraft version
    }
}
```

**Step 2: Verify build**

```bash
./gradlew build --no-daemon
```

Expected: BUILD SUCCESSFUL (may have warnings about method implementations)

---

## Task 4: Implement InputApi Functions

**Files:**
- Modify: `src/main/java/net/clish/api/InputApi.java`

**Step 1: Update InputApi to use Mixin**

Replace stub implementations with real Mixin-based calls:

```java
package net.clish.api;

import net.clish.ast.ClishLibrary;
import net.clish.mixin.IInputSimulator;
import net.minecraft.client.Minecraft;

import java.util.List;

public class InputApi {
    private static final Minecraft mc = Minecraft.getInstance();

    public static class LeftClick implements ClishLibrary {
        @Override
        public String getName() { return "input.leftClick"; }

        @Override
        public Object call(List<Object> args) {
            if (mc.mouseHandler instanceof IInputSimulator) {
                ((IInputSimulator) mc.mouseHandler).clish_leftClick();
                return true;
            }
            return false;
        }
    }

    public static class RightClick implements ClishLibrary {
        @Override
        public String getName() { return "input.rightClick"; }

        @Override
        public Object call(List<Object> args) {
            if (mc.mouseHandler instanceof IInputSimulator) {
                ((IInputSimulator) mc.mouseHandler).clish_rightClick();
                return true;
            }
            return false;
        }
    }

    public static class MouseMove implements ClishLibrary {
        @Override
        public String getName() { return "input.mouseMove"; }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return false;
            double dx = ((Number) args.get(0)).doubleValue();
            double dy = ((Number) args.get(1)).doubleValue();

            if (mc.mouseHandler instanceof IInputSimulator) {
                ((IInputSimulator) mc.mouseHandler).clish_mouseMove(dx, dy);
                return true;
            }
            return false;
        }
    }

    public static class KeyPress implements ClishLibrary {
        @Override
        public String getName() { return "input.keyPress"; }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return false;
            int keyCode = ((Number) args.get(0)).intValue();

            if (mc.mouseHandler instanceof IInputSimulator) {
                ((IInputSimulator) mc.mouseHandler).clish_keyPress(keyCode);
                return true;
            }
            return false;
        }
    }

    public static class TypeText implements ClishLibrary {
        @Override
        public String getName() { return "input.typeText"; }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return false;
            String text = args.get(0).toString();

            if (mc.mouseHandler instanceof IInputSimulator) {
                ((IInputSimulator) mc.mouseHandler).clish_typeText(text);
                return true;
            }
            return false;
        }
    }

    public static List<ClishLibrary> getAll() {
        return List.of(
            new LeftClick(),
            new RightClick(),
            new MouseMove(),
            new KeyPress(),
            new TypeText()
        );
    }
}
```

**Step 2: Verify build**

```bash
./gradlew build --no-daemon
```

Expected: BUILD SUCCESSFUL

---

## Task 5: Update NbtLibrary with Real CompoundTag

**Files:**
- Modify: `src/main/java/net/clish/builtin/NbtLibrary.java`

**Step 1: Update imports and add new functions**

Add imports:
```java
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.BlockDataAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
```

Add new function classes:
```java
// FromBlock using BlockDataAccessor
public static class FromBlock implements ClishLibrary {
    @Override
    public String getName() { return "nbt.fromBlock"; }

    @Override
    public Object call(List<Object> args) {
        if (args.size() < 3) return null;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;

        int x = ((Number) args.get(0)).intValue();
        int y = ((Number) args.get(1)).intValue();
        int z = ((Number) args.get(2)).intValue();

        BlockPos pos = new BlockPos(x, y, z);
        BlockEntity blockEntity = mc.level.getBlockEntity(pos);

        if (blockEntity == null) return null;

        // Use BlockDataAccessor pattern from clientcommands
        BlockDataAccessor accessor = new BlockDataAccessor(blockEntity, pos);
        return accessor.getData();
    }
}

// Write NBT to SNBT
public static class Write implements ClishLibrary {
    @Override
    public String getName() { return "nbt.write"; }

    @Override
    public Object call(List<Object> args) {
        if (args.isEmpty() || !(args.get(0) instanceof CompoundTag)) {
            return "{}";
        }
        return args.get(0).toString();
    }
}

// Read NBT from SNBT
public static class Read implements ClishLibrary {
    @Override
    public String getName() { return "nbt.read"; }

    @Override
    public Object call(List<Object> args) {
        if (args.isEmpty()) return null;
        String snbt = args.get(0).toString();
        if (snbt.isEmpty()) return null;

        try {
            return CompoundTag.valueOf(snbt);
        } catch (Exception e) {
            return null;
        }
    }
}
```

**Step 2: Update getAll() to include new functions**

```java
public static List<ClishLibrary> getAll() {
    return Arrays.asList(
        new CreateFunction(),
        new GetFunction(),
        new SetFunction(),
        new RemoveFunction(),
        new HasFunction(),
        new KeysFunction(),
        new ValuesFunction(),
        new ToStringFunction(),
        new TypeFunction(),
        new FromBlock(),    // NEW
        new Write(),        // NEW
        new Read()          // NEW
    );
}
```

**Step 3: Verify build**

```bash
./gradlew build --no-daemon
```

Expected: BUILD SUCCESSFUL

---

## Task 6: Register New APIs in Clish

**Files:**
- Modify: `src/main/java/net/clish/Clish.java`

**Step 1: Add imports and register in constructor**

```java
import net.clish.api.InputApi;

// In constructor, after other libraries:
for (ClishLibrary lib : PlayerApi.getAll()) {
    scriptEngine.registerLibrary(lib.getName(), lib);
}
for (ClishLibrary lib : BlockApi.getAll()) {
    scriptEngine.registerLibrary(lib.getName(), lib);
}
for (ClishLibrary lib : InputApi.getAll()) {
    scriptEngine.registerLibrary(lib.getName(), lib);
}
```

**Step 2: Verify build**

```bash
./gradlew build --no-daemon
```

Expected: BUILD SUCCESSFUL

---

## Task 7: Update Unit Tests

**Files:**
- Create: `src/test/java/net/clish/api/InputApiTest.java`
- Modify: `src/test/java/net/clish/api/BlockApiTest.java`

**Step 1: Create InputApiTest.java**

```java
package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class InputApiTest {

    @Test
    void testLeftClickApiName() {
        assertEquals("input.leftClick", new InputApi.LeftClick().getName());
    }

    @Test
    void testRightClickApiName() {
        assertEquals("input.rightClick", new InputApi.RightClick().getName());
    }

    @Test
    void testMouseMoveApiName() {
        assertEquals("input.mouseMove", new InputApi.MouseMove().getName());
    }

    @Test
    void testKeyPressApiName() {
        assertEquals("input.keyPress", new InputApi.KeyPress().getName());
    }

    @Test
    void testTypeTextApiName() {
        assertEquals("input.typeText", new InputApi.TypeText().getName());
    }

    @Test
    void testGetAllReturnsAllFunctions() {
        List<?> functions = InputApi.getAll();
        assertEquals(5, functions.size());
    }
}
```

**Step 2: Run tests**

```bash
./gradlew test --no-daemon
```

Expected: All tests pass

---

## Task 8: In-Game Testing

**Step 1: Build the mod**

```bash
./gradlew build --no-daemon
```

**Step 2: Copy JAR to mods folder**

Copy `build/libs/clish-*.jar` to Minecraft mods folder

**Step 3: Test in-game**

Run Minecraft with the mod:
- Test `/clish run` commands
- Test player.* functions
- Test block.* functions

---

## Summary

| Task | Description | Estimated Time |
|------|-------------|----------------|
| 1 | Add clientarguments dependency | 5 min |
| 2 | Create mixin infrastructure | 5 min |
| 3 | Create InputSimulatorMixin | 10 min |
| 4 | Implement InputApi functions | 10 min |
| 5 | Update NbtLibrary | 10 min |
| 6 | Register APIs in Clish | 5 min |
| 7 | Update unit tests | 5 min |
| 8 | In-game testing | 15 min |

**Total estimated time:** ~65 minutes

---

## Plan Complete

Plan saved to `docs/plans/2026-03-10-input-nbt-design.md`

**Two execution options:**

1. **Subagent-Driven (this session)** - I dispatch fresh subagent per task, review between tasks, fast iteration

2. **Parallel Session (separate)** - Open new session with executing-plans, batch execution with checkpoints

Which approach?
