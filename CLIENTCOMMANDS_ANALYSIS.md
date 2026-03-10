# How ClientCommands Works & Clish Fix Guide

## Table of Contents
1. [How ClientCommands Registers Commands](#how-clientcommands-registers-commands)
2. [How Commands Call Minecraft Vanilla Functions](#how-commands-call-minecraft-vanilla-functions)
3. [How Commands Implement Custom Features via Mixins](#how-commands-implement-custom-features-via-mixins)
4. [Key Dependencies Used](#key-dependencies-used)
5. [What's Wrong with Clish](#whats-wrong-with-clish)
6. [Required Fixes](#required-fixes)

---

## How ClientCommands Registers Commands

ClientCommands uses **Fabric's Client Command API** (`fabric-api`) to register client-side commands. Here's the pattern:

### 1. Main Entry Point

In `ClientCommands.java` (line 85):

```java
ClientCommandRegistrationCallback.EVENT.register(ClientCommands::registerCommands);
```

This registers a callback that fires when the client is ready to register commands.

### 2. Command Registration Method

```java
public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext context) {
    // Each command has a static `register` method
    ChatCommand.register(dispatcher);
    CGiveCommand.register(dispatcher, context);
    GetDataCommand.register(dispatcher);
    // ... etc
}
```

### 3. Individual Command Pattern

Each command follows this structure (from `ChatCommand.java`):

```java
public class ChatCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        // Use brigadier DSL to define command
        dispatcher.register(literal("chat").executes(ctx -> execute()));
    }

    private static int execute() {
        Minecraft.getInstance().schedule(() -> {
            // Do work on main thread
            Minecraft.getInstance().openChatScreen(ChatComponent.ChatMethod.MESSAGE);
            sendFeedback("commands.chat.success");
        });
        return Command.SINGLE_SUCCESS;
    }
}
```

### Key Points:
- Commands are registered in a **static `register` method** that receives the `CommandDispatcher`
- Use **Brigadier** (`com.mojang.brigadier`) for command building
- Execute methods return an `int` (success code) using `Command.SINGLE_SUCCESS`
- For Minecraft operations, wrap in `Minecraft.getInstance().schedule()` to run on main thread

---

## How Commands Call Minecraft Vanilla Functions

### 1. Sending Chat Messages

From `ClientCommandHelper.java`:

```java
public static void sendFeedback(Component message) {
    Minecraft.getInstance().gui.getChat().addMessage(message);
}
```

Or directly via `FabricClientCommandSource`:

```java
source.sendFeedback(Component.translatable("commands.cgive.success", count, stack.getDisplayName()));
```

### 2. Accessing Player Data

From `CGiveCommand.java`:

```java
LocalPlayer player = source.getPlayer();  // Get the client player
if (!player.isCreative()) {
    throw NOT_CREATIVE_EXCEPTION.create();
}
MultiPlayerGameMode interactionManager = source.getClient().gameMode;
```

### 3. Reading NBT Data

From `GetDataCommand.java`:

```java
// Entity NBT
DataAccessor entityAccessor = new EntityDataAccessor(getEntity(ctx, argName));
Tag nbt = accessor.getData();

// Block NBT
BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(pos);
DataAccessor blockAccessor = new BlockDataAccessor(blockEntity, pos);
```

### 4. Getting Entities via Selectors

Using `clientarguments` library:

```java
import static dev.xpple.clientarguments.arguments.CEntityArgument.*;

CEntitySelector selector = ctx.getArgument("targets", CEntitySelector.class);
List<? extends Entity> entities = selector.findEntities(source);
```

### 5. Sending Packets

From `CTeleportCommand.java`:

```java
ClientPacketListener packetListener = source.getClient().getConnection();
packetListener.send(new ServerboundTeleportToEntityPacket(uuid));
```

---

## How Commands Implement Custom Features via Mixins

ClientCommands uses **Mixin** to inject code into Minecraft classes.

### 1. Define an Interface

From `IEntity_Glowable.java`:

```java
public interface IEntity_Glowable {
    void clientcommands_addGlowingTicket(int ticks, int color);
    boolean clientcommands_hasGlowingTicket();
    void clientcommands_tickGlowingTickets();
}
```

### 2. Create a Mixin

From `EntityMixin.java`:

```java
@Mixin(Entity.class)
public class EntityMixin implements IEntity_Glowable {
    @Unique
    private final List<EntityGlowingTicket> glowingTickets = new ArrayList<>(0);

    @Override
    public void clientcommands_addGlowingTicket(int ticks, int color) {
        glowingTickets.add(new EntityGlowingTicket(ticks, color));
    }

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void overrideIsCurrentlyGlowing(CallbackInfoReturnable<Boolean> ci) {
        if (!glowingTickets.isEmpty()) {
            ci.setReturnValue(Boolean.TRUE);
        }
    }
}
```

### 3. Register Mixin in mixins.json

```json
{
  "required": true,
  "minVersion": "0.8",
  "package": "net.earthcomputer.clientcommands.mixin",
  "compatibilityLevel": "JAVA_17",
  "mixins": [
    "commands.glow.EntityMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}
```

---

## Key Dependencies Used

ClientCommands uses these libraries (from `build.gradle`):

```groovy
// Fabric API - for client commands registration
modImplementation "net.fabricmc.fabric-api:fabric-api:${project.fabric_version}"

// ClientArguments - for enhanced argument types (entity selectors, block pos, etc.)
include modImplementation("dev.xpple:clientarguments:${project.clientarguments_version}")

// BetterConfig - for configuration
include modImplementation("dev.xpple:betterconfig-fabric:${project.betterconfig_version}")
```

---

## What's Wrong with Clish

Based on my analysis, here are the main issues:

### 1. **Reflection Instead of Direct Imports**

In `ClishCommand.java` (lines 30-54):

```java
// Using reflection to access Minecraft classes - BAD!
static {
    try {
        textClass = Class.forName("net.minecraft.class_2597"); // Text in Mojang mappings
        literalMethod = textClass.getMethod("literal", String.class);
    } catch (Exception e) {
        LOGGER.warn("Could not find Text class: {}", e.getMessage());
    }
}
```

**Problem**: This uses Mojang mappings (obfuscated names) which can change between versions. Should use Parchment mappings and direct imports.

### 2. **PlayerApi is Stubbed**

In `PlayerApi.java` (lines 28-32):

```java
@Override
public Object call(List<Object> args) {
    LOGGER.debug("PlayerApi.{} called with args: {}", getName(), args);
    return "Player API requires Minecraft runtime";  // Always returns this!
}
```

**Problem**: The API returns a string instead of actually calling Minecraft APIs. All PlayerApi functions return placeholder text.

### 3. **InputApi is Stubbed**

In `InputApi.java` (lines 26-29):

```java
@Override
public Object call(List<Object> args) {
    LOGGER.debug("InputApi.{} called with args: {}", getName(), args);
    // TODO: Implement via Mixin
    return "Input simulation requires Minecraft runtime";
}
```

**Problem**: No Mixin implementation exists for input simulation.

### 4. **NbtLibrary is Not Connected to Real NBT**

In `NbtLibrary.java`:

```java
public static class CreateFunction implements ClishLibrary {
    @Override
    public Object call(List<Object> args) {
        return new java.util.HashMap<String, Object>();  // Plain Map, not real NBT!
    }
}
```

**Problem**: Uses Java HashMaps instead of Minecraft's `CompoundTag`. Cannot actually read/write block entity NBT.

### 5. **Missing Dependencies**

In `clish/build.gradle`:

```groovy
// This is commented out!
 // include modImplementation("dev.xpple:clientarguments:${project.clientarguments_version}")
```

**Problem**: Missing `clientarguments` library for entity selectors and block position arguments.

### 6. **Not Using Parchment Mappings Properly**

The code uses Mojang mappings (`class_2597`) instead of Parchment mappings (`Component`, `Text`).

### 7. **Missing Access Widener**

The `clish.aw` file may not properly expose needed methods.

---

## Required Fixes

### Fix 1: Use Proper Imports and Mappings

Replace reflection with direct imports using Parchment mappings:

```java
// Instead of reflection:
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;

// Use directly:
source.sendFeedback(Component.literal("Hello"));
```

### Fix 2: Implement PlayerApi Properly

```java
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public static class GetX implements ClishLibrary {
    @Override
    public Object call(List<Object> args) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return null;
        return player.getX();
    }
}
```

### Fix 3: Add clientarguments Dependency

In `build.gradle`:

```groovy
include modImplementation("dev.xpple:clientarguments:${project.clientarguments_version}")
```

Then use in commands:

```java
import static dev.xpple.clientarguments.arguments.CEntityArgument.*;
import static dev.xpple.clientarguments.arguments.CBlockPosArgument.*;
```

### Fix 4: Implement Real NBT Reading

```java
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

public static class GetBlockNbt implements ClishLibrary {
    @Override
    public Object call(List<Object> args) {
        if (args.isEmpty()) return null;

        int x = ((Number)args.get(0)).intValue();
        int y = ((Number)args.get(1)).intValue();
        int z = ((Number)args.get(2)).intValue();

        BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(BlockPos.of(x, y, z));
        if (blockEntity == null) return null;

        return blockEntity.getTileData();  // Returns CompoundTag
    }
}
```

### Fix 5: Create Input Mixin

1. Define interface in `api/InputSimulator.java`
2. Create mixin to `MinecraftClient` or `MouseHandler`:
```java
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Inject(method = "onPress", at = @At("HEAD"))
    private void onPress(CallbackInfo ci) {
        // Track or simulate clicks
    }
}
```
3. Register in `mixins.json`

### Fix 6: Update Access Widener

In `clish.aw`:

```
accessible class net/minecraft/class_*
method * * (Lnet/minecraft/class_2597;)V # sendFeedback
```

---

## Summary

Clish has a good architecture but is **not actually integrated with Minecraft**. The APIs return placeholder strings instead of calling real Minecraft code. The main fixes are:

1. **Use direct imports** instead of reflection
2. **Implement actual API functions** in PlayerApi, BlockApi, InputApi
3. **Add clientarguments** dependency for entity selectors
4. **Use real NBT** (CompoundTag) instead of HashMaps
5. **Create Mixins** for input simulation and other custom features

The script engine (lexer, parser, interpreter) appears to be complete and functional - the issue is just the missing Minecraft integration layer.
