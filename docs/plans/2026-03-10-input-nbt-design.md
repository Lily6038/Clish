# Clish Minecraft Integration - Phase 3 & 4 Implementation Design

**Date:** 2026-03-10
**Status:** Design - Approved

---

## 1. Overview

Implement InputApi (Phase 3) and complete NbtLibrary (Phase 4) by mirroring the clientcommands mod patterns which are proven to work with Minecraft 1.21.11.

### Goals
- Implement InputApi with Mixin-based input simulation
- Implement real NbtLibrary using Minecraft's CompoundTag
- Add clientarguments dependency (version 1.11.6)
- Test both in-game and with unit tests

---

## 2. Architecture

### 2.1 Current State (After Phases 1, 5, 6)
- ClishCommand: Fixed chat output using Component.literal()
- PlayerApi: Implemented with real Minecraft API
- BlockApi: Implemented with real Minecraft API
- NbtLibrary: HashMap-based (not connected to real NBT)
- InputApi: Stubbed

### 2.2 Target State
```
ClishClient
    └── ScriptEngine
          ├── Builtins, StringLibrary, MathLibrary, etc.
          └── NbtLibrary (real CompoundTag via BlockDataAccessor)

    ClishCommand (Component.literal())

    MinecraftClient (singleton)
    ├── PlayerApi (LocalPlayer access) ✓ DONE
    ├── BlockApi (Level access) ✓ DONE
    └── InputApi (Mixin-enabled input simulation)
```

---

## 3. InputApi Implementation

### 3.1 Mixin Setup
Reference: clientcommands mixins.json and EntityMixin patterns

```json
// mixins.clish.json
{
  "required": true,
  "package": "net.clish.mixin",
  "compatibilityLevel": "JAVA_21",
  "mixins": [
    "InputSimulatorMixin"
  ]
}
```

### 3.2 Interface Design
```java
public interface IInputSimulator {
    void clish_leftClick();
    void clish_rightClick();
    void clish_mouseMove(double dx, double dy);
    void clish_keyPress(int keyCode);
    void clish_typeText(String text);
}
```

### 3.3 Mixin Implementation
Target: `net.minecraft.client.MouseHandler` for mouse input

### 3.4 API Functions
| Function | Description |
|----------|-------------|
| `input.leftClick()` | Simulate left click |
| `input.rightClick()` | Simulate right click |
| `input.mouseMove(dx, dy)` | Move mouse by delta |
| `input.keyPress(keyCode)` | Press a key |
| `input.typeText(text)` | Type text into chat/gui |

---

## 4. NbtLibrary Implementation

### 4.1 Dependencies
- clientarguments 1.11.6 (from clientcommands)
- Use `net.minecraft.server.commands.data.BlockDataAccessor` for block NBT

### 4.2 New Functions
| Function | Description |
|----------|-------------|
| `nbt.fromBlock(x, y, z)` | Get block entity NBT using BlockDataAccessor |
| `nbt.fromEntity(entitySelector)` | Get entity NBT (requires clientarguments) |
| `nbt.fromItem(itemStack)` | Get item NBT |
| `nbt.write(nbt)` | Serialize to SNBT string |
| `nbt.read(snbt)` | Parse SNBT to NBT |

### 4.3 Implementation
```java
// Reference: clientcommands GetDataCommand.java
import net.minecraft.server.commands.data.BlockDataAccessor;

// For block NBT:
BlockEntity blockEntity = mc.level.getBlockEntity(pos);
BlockDataAccessor accessor = new BlockDataAccessor(blockEntity, pos);
CompoundTag nbt = accessor.getData();
```

---

## 5. Dependencies

### 5.1 build.gradle
```groovy
// Use clientarguments 1.11.6 (proven with MC 1.21.11)
include modImplementation("dev.xpple:clientarguments:1.11.6")
```

### 5.2 gradle.properties
```properties
clientarguments_version=1.11.6
```

---

## 6. Testing Strategy

### 6.1 Unit Tests
- Mock Minecraft.getInstance() using Mockito
- Test InputApi functions with mocked input
- Test NbtLibrary functions with mocked CompoundTag

### 6.2 In-Game Tests
1. Run `/clish run test.clish` and verify functionality
2. Test input simulation scripts
3. Test NBT reading from blocks/items

---

## 7. Implementation Order

1. Add clientarguments 1.11.6 dependency
2. Create mixins.clish.json and register in fabric.mod.json
3. Create IInputSimulator interface
4. Create InputSimulatorMixin
5. Implement InputApi functions
6. Update NbtLibrary with real CompoundTag
7. Run unit tests
8. Test in-game

---

## 8. Approval

- [x] Architecture and approach approved
- [ ] Implementation ready to proceed
