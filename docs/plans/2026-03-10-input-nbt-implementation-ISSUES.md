# InputApi & NbtLibrary Implementation Plan - Issues Document

> **Status:** BLOCKED - Implementation attempt failed
> **Created:** 2026-03-10
> **Original Plan:** `docs/plans/2026-03-10-input-nbt-implementation.md`

---

## Summary

The implementation attempt was **unsuccessful**. Non-functional code has been removed. This document details the blockers that prevented completion.

---

## Blocker 1: clientarguments Dependency Not Available

**Problem:** The plan required `clientarguments` version 1.11.6 (or similar) to access `BlockDataAccessor` for reading block NBT data.

**Attempted versions:**
- `1.11.6` - Not found in Maven
- `1.11.0+1.21` - Not found in Maven

**Search locations checked:**
- `https://maven.fabricmc.net/`
- `https://repo.maven.apache.org/maven2/`
- `https://maven.parchmentmc.org/`
- Fabric Loom cache

**Impact:** Without `clientarguments`, we cannot access `BlockDataAccessor` which was needed for:
- Reading real NBT from block entities (`nbt.fromBlock`)
- Converting between Minecraft `CompoundTag` and script representations

---

## Blocker 2: Input Simulation Mixin Incomplete

**Problem:** The `InputSimulatorMixin` was created with empty stub methods. Even if the Mixin infrastructure worked, the actual input simulation methods were not implemented.

**Methods that need implementation:**
```java
void clish_leftClick();
void clish_rightClick();
void clish_mouseMove(double dx, double dy);
void clish_keyPress(int keyCode);
void clish_typeText(String text);
```

**Root cause:** The plan assumed we could use `Screen.mouseClicked()` but:
1. The `Screen` class was not accessible in the Mixin context (client vs server side)
2. No clear path to actually inject mouse/keyboard events in Minecraft 1.21.11

**Impact:** Input simulation (`input.*` APIs) is completely non-functional.

---

## Blocker 3: NBT API Requires Real Minecraft Runtime

**Problem:** Even with `clientarguments`, the NbtLibrary changes required:
- `CompoundTag.valueOf(String)` - Not available in Minecraft 1.21.11
- `BlockEntity.saveWithMetadata()` - Method doesn't exist

**Partial solution implemented (then removed):**
- `nbt.fromBlock` returned only block entity type identifier, not actual NBT
- `nbt.write` serialized HashMap to JSON, not SNBT format
- `nbt.read` returned raw string without parsing

**Impact:** The NBT functions would give misleading results to users.

---

## What Was Removed

The following non-functional code was removed:

| File | Reason |
|------|--------|
| `src/main/resources/mixins.clish.json` | Mixin infrastructure not working |
| `src/main/java/net/clish/mixin/IInputSimulator.java` | Interface with no implementors |
| `src/main/java/net/clish/mixin/InputSimulatorMixin.java` | Empty stub methods |
| `NbtLibrary.FromBlock` class | Returned wrong data type |
| `NbtLibrary.Write` class | Wrong serialization format |
| `NbtLibrary.Read` class | No actual SNBT parsing |
| `Clish.java` InputApi registration | Not functional without mixin |

---

## Remaining Work

To complete this plan, the following research/investigation is needed:

### 1. Find Working clientarguments Version
- [ ] Search Maven Central for actual available versions
- [ ] Check clientarguments GitHub releases for Maven coordinates
- [ ] Or find alternative approach to access BlockDataAccessor

### 2. Implement Input Simulation
- [ ] Research how other mods (ClientCommands, ClickPress) implement input simulation in 1.21.11
- [ ] Determine if there's a public API for injecting input events
- [ ] Or consider alternative architecture (hook into keybind system)

### 3. NBT Integration Research
- [ ] Find correct Minecraft 1.21.11 APIs for NBT parsing
- [ ] Determine how to get CompoundTag from BlockEntity
- [ ] Verify serialization approach

### 4. Testing
- [ ] Set up in-game test environment
- [ ] Cannot verify functionality without runtime testing

---

## Original Plan Tasks - Final Status

| Task | Planned | Actual Status |
|------|---------|---------------|
| 1. Add clientarguments Dependency | gradle.properties → 1.11.6 | ❌ BLOCKED - Dependency not available |
| 2. Create Mixin Infrastructure | mixins.json, fabric.mod.json | ❌ REMOVED - Not functional |
| 3. Create InputSimulatorMixin | Full implementation | ❌ REMOVED - Was empty stubs |
| 4. Implement InputApi Functions | Mixin-based calls | ❌ REMOVED - Mixin doesn't work |
| 5. Update NbtLibrary | Real CompoundTag | ❌ REMOVED - Partial/broken |
| 6. Register APIs in Clish | Register InputApi | ❌ REMOVED - Not functional |
| 7. Update Unit Tests | InputApiTest | ⚠️ WEAK - Only tests names |
| 8. In-Game Testing | Test in Minecraft | ❌ NOT DONE |

---

## Recommendations

1. **Do not attempt this implementation** until:
   - A working `clientarguments` version is confirmed available
   - Input simulation approach is researched and validated

2. **Consider alternative approaches:**
   - Instead of Mixin-based input simulation, explore using Minecraft's built-in keybind system
   - Instead of clientarguments, check if Fabric API provides similar functionality

3. **Break into smaller research tasks:**
   - First validate that required dependencies exist
   - Then prototype in isolation before full implementation

---

## Files Modified During Failed Attempt

These files were reverted to original state:
- `gradle.properties` - clientarguments_version unchanged
- `build.gradle` - clientarguments dependency still commented out
- `src/main/resources/fabric.mod.json` - mixins entry removed
- `src/main/java/net/clish/api/InputApi.java` - reverted to stub
- `src/main/java/net/clish/builtin/NbtLibrary.java` - reverted to original
- `src/main/java/net/clish/Clish.java` - InputApi registration removed
