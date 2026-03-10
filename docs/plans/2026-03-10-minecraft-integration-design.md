# Clish Minecraft Integration Layer Design

**Date:** 2026-03-10
**Status:** Design - Awaiting Approval

---

## 1. Overview

This design covers the complete Minecraft integration layer for Clish, enabling the script engine to interact with Minecraft's client-side APIs. The integration includes chat output, input simulation, NBT reading/writing, and player/block data access.

### Goals
- Fix broken chat output (high priority)
- Implement InputApi with Mixin-based input simulation
- Implement real NbtLibrary using Minecraft's CompoundTag
- Implement PlayerApi for player data access
- Implement BlockApi for block queries
- Add clientarguments dependency for entity selectors

---

## 2. Architecture

### 2.1 Current State
```
ClishClient
    └── ScriptEngine
          ├── Builtins (echo, sleep, etc.)
          ├── StringLibrary
          ├── MathLibrary
          ├── RegexLibrary
          ├── TimeLibrary
          └── NbtLibrary (stubbed - HashMap)

    ClishCommand (uses reflection for chat)

    PlayerApi (stubbed - returns placeholder strings)
    BlockApi (stubbed - returns placeholder strings)
    InputApi (stubbed - returns placeholder strings)
```

### 2.2 Target State
```
ClishClient
    └── ScriptEngine
          ├── Builtins, StringLibrary, MathLibrary, etc.
          └── NbtLibrary (real CompoundTag)

    ClishCommand (direct Component.literal() calls)

    MinecraftClient (singleton access)
    ├── PlayerApi (LocalPlayer access)
    ├── BlockApi (Level access)
    └── InputApi (Mixin-enabled input simulation)
```

### 2.3 Key Design Decisions
- **Parchment mappings** - Use `net.minecraft.network.chat.Component` not Mojang `class_2597`
- **Direct imports** - No reflection for Minecraft API access
- **Mixin for input** - Input simulation requires Mixin; other APIs use `Minecraft.getInstance()`
- **Null safety** - Always check `Minecraft.getInstance().player != null`

---

## 3. Chat Output Fix (High Priority)

### Problem
ClishCommand.java uses reflection to find `Text.literal()` method with Mojang mappings. This is fragile and may fail silently.

### Solution
Replace reflection with direct Parchment imports:

```java
// ClishCommand.java - BEFORE (broken)
import java.lang.reflect.Method;
// ...
static {
    try {
        textClass = Class.forName("net.minecraft.class_2597");
        literalMethod = textClass.getMethod("literal", String.class);
    } catch (Exception e) { ... }
}

// ClishCommand.java - AFTER (working)
import net.minecraft.network.chat.Component;
// ...
private static void sendToChat(CommandContext<FabricClientCommandSource> context, String message) {
    context.getSource().sendFeedback(Component.literal(message));
}
```

### Implementation
1. Replace reflection code with direct `Component.literal()` imports
2. Use `FabricClientCommandSource.sendFeedback()` for chat output
3. Remove the static initialization block for textClass

---

## 4. InputApi Implementation

### 4.1 Overview
Input simulation requires Mixin injection into Minecraft's input handling classes.

### 4.2 Mixin Targets (Minecraft 1.21.11)
- **Mouse input:** `net.minecraft.client.MouseHandler` or `net.minecraft.client.gui.screen.Mouse`
- **Keyboard input:** `net.minecraft.client.Keyboard`

### 4.3 Interface Design
```java
// net.clish.mixin/IInputSimulator.java
public interface IInputSimulator {
    void clish_leftClick();
    void clish_rightClick();
    void clish_mouseMove(double dx, double dy);
    void clish_keyPress(int keyCode);
    void clish_typeText(String text);
}
```

### 4.4 Mixin Implementation
```java
// net.clish.mixin.InputSimulatorMixin.java
@Mixin(MouseHandler.class)
public class InputSimulatorMixin implements IInputSimulator {
    @Inject(method = "onPress", at = @At("HEAD"))
    private void onPress(CallbackInfo ci) { ... }

    @Override
    public void clish_leftClick() {
        // Simulate left mouse button press
    }
}
```

### 4.5 API Functions to Implement
| Function | Description |
|----------|-------------|
| `input.leftClick()` | Simulate left click |
| `input.rightClick()` | Simulate right click |
| `input.mouseMove(dx, dy)` | Move mouse by delta |
| `input.keyPress(keyCode)` | Press a key |
| `input.typeText(text)` | Type text into chat/gui |

### 4.6 Access Widener
```
accessible class net/minecraft/client/MouseHandler
accessible class net/minecraft/client/Keyboard
method net/minecraft/client/MouseHandler clish_leftClick ()V
method net/minecraft/client/Keyboard clish_keyPress (IZ)V
```

---

## 5. NbtLibrary Implementation

### 5.1 Overview
Replace HashMap-based NBT with real Minecraft `CompoundTag`.

### 5.2 New Functions
| Function | Description |
|----------|-------------|
| `nbt.fromBlock(x, y, z)` | Get block entity NBT |
| `nbt.fromEntity(entitySelector)` | Get entity NBT |
| `nbt.fromItem(itemStack)` | Get item NBT |
| `nbt.write(nbt)` | Serialize to SNBT string |
| `nbt.read(snbt)` | Parse SNBT to NBT |

### 5.3 Implementation
```java
public static class FromBlock implements ClishLibrary {
    @Override
    public Object call(List<Object> args) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return null;

        int x = ((Number)args.get(0)).intValue();
        int y = ((Number)args.get(1)).intValue();
        int z = ((Number)args.get(2)).intValue();

        BlockPos pos = new BlockPos(x, y, z);
        BlockEntity be = mc.level.getBlockEntity(pos);
        if (be == null) return null;

        return be.getTileData(); // Returns CompoundTag
    }
}
```

### 5.4 Script Engine Integration
The Interpreter must handle `CompoundTag` objects:
- Convert to/from String for script operations
- Support nested path access: `nbt.get(nbt, "Inventory.0.tag.display.Name")`

---

## 6. PlayerApi Implementation

### 6.1 Functions
| Function | Returns |
|----------|---------|
| `player.x` | double |
| `player.y` | double |
| `player.z` | double |
| `player.health` | float |
| `player.food` | int |
| `player.dimension` | String |
| `player.inventory` | List<ItemStack> |
| `player.heldItem` | ItemStack |
| `player.yaw` | float |
| `player.pitch` | float |

### 6.2 Implementation
```java
public static class GetX implements ClishLibrary {
    @Override
    public Object call(List<Object> args) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return null;
        return player.getX();
    }
}
```

---

## 7. BlockApi Implementation

### 7.1 Functions
| Function | Returns |
|----------|---------|
| `block.get(x, y, z)` | BlockState |
| `block.nbt(x, y, z)` | CompoundTag |
| `block.exists(x, y, z)` | boolean |
| `block.light(x, y, z)` | int |
| `block.sky(x, y, z)` | int |

### 7.2 Implementation
```java
public static class GetBlock implements ClishLibrary {
    @Override
    public Object call(List<Object> args) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;

        int x = ((Number)args.get(0)).intValue();
        int y = ((Number)args.get(1)).intValue();
        int z = ((Number)args.get(2)).intValue();

        BlockPos pos = new BlockPos(x, y, z);
        return mc.level.getBlockState(pos);
    }
}
```

---

## 8. Dependencies

### 8.1 Required Changes to build.gradle
```groovy
// Uncomment clientarguments
include modImplementation("dev.xpple:clientarguments:${project.clientarguments_version}")

// Optional: for better config
include modImplementation("dev.xpple:betterconfig-fabric:${project.betterconfig_version}")
```

### 8.2 gradle.properties
```
clientarguments_version=1.11.0+1.21
betterconfig_version=2.4.0
```

---

## 9. Testing Strategy

### 9.1 Unit Tests (MockMinecraft)
- Mock `Minecraft.getInstance()` using Mockito
- Test PlayerApi functions return expected values
- Test NbtLibrary functions handle null gracefully
- Test BlockApi functions with mocked Level

### 9.2 In-Game Tests
1. **Chat output:** Run `/clish run test.clish` and verify message appears
2. **PlayerApi:** Script `echo player.x` should print coordinates
3. **BlockApi:** Script `echo block.get(0, 64, 0)` should print block state
4. **NbtLibrary:** Script reading chest NBT at spawn
5. **InputApi:** Script that clicks automatically

### 9.3 Test Scripts
```bash
# config/clish/scripts/test_player.clish
echo "Player position:"
echo player.x
echo player.y
echo player.z
```

---

## 10. Implementation Order

1. **Phase 1: Chat Output Fix** (High Priority)
   - Fix ClishCommand to use Component.literal()

2. **Phase 2: Setup Infrastructure**
   - Add clientarguments dependency
   - Create mixins.json
   - Update access widener

3. **Phase 3: InputApi**
   - Create IInputSimulator interface
   - Create InputSimulatorMixin
   - Implement all input functions

4. **Phase 4: NbtLibrary**
   - Add fromBlock, fromEntity, fromItem functions
   - Add write (to SNBT), read (from SNBT) functions
   - Update Interpreter to handle CompoundTag

5. **Phase 5: PlayerApi**
   - Implement all player data functions

6. **Phase 6: BlockApi**
   - Implement all block query functions

7. **Phase 7: Testing**
   - Write unit tests
   - In-game validation

---

## 11. Risks & Mitigations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Mixin targets change in MC updates | High | Use version-specific mixin targets, test on each MC version |
| Input simulation conflicts with player | High | Add cooldown, require explicit enable in config |
| NullPointerException when not in game | Medium | Always check `Minecraft.getInstance().player != null` |
| Thread safety | Medium | All Minecraft API calls must run on main thread via `Minecraft.getInstance().execute()` |

---

## 12. Approval

- [ ] Architecture and approach approved
- [ ] Phase 1 (Chat Output) - Ready to implement
- [ ] Phase 2-7 - Ready after previous phases complete
