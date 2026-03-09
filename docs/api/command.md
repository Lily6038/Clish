# Command API

The Command API allows scripts to execute Minecraft commands.

## Functions

### command.execute

Execute a Minecraft command.

```clish
command.execute("/give @p minecraft:diamond 1")
```

### command.run

Alias for `command.execute`.

```clish
command.run("/say Hello from Clish!")
```

## Examples

### Give Items

```clish
# Give player 1 diamond
command.execute("/give @p minecraft:diamond 1")

# Give player 64 diamonds
command.execute("/give @p minecraft:diamond 64")

# Give player stack of items
command.execute("/give @p minecraft:enchanted_golden_apple 3")
```

### Teleport

```clish
# Teleport to coordinates
command.execute("/tp 100 64 0")

# Teleport to spawn
command.execute("/tp @s 0 64 0")

# Teleport to another player
command.execute("/tp @s @p")
```

### Set Time

```clish
# Set to day
command.execute("/time set day")

# Set to night
command.execute("/time set night")

# Set specific time
command.execute("/time set 6000")
```

### Weather

```clish
# Clear weather
command.execute("/weather clear")

# Set rain
command.execute("/weather rain")

# Set thunder
command.execute("/weather thunder")
```

### Gamemode

```clish
# Creative mode
command.execute("/gamemode creative")

# Survival mode
command.execute("/gamemode survival")

# Spectator mode
command.execute("/gamemode spectator")
```

### Using Variables

```clish
function giveItems(itemId, amount) {
    local cmd = "/give @p " + itemId + " " + amount
    command.execute(cmd)
}

giveItems("minecraft:diamond", 10)
giveItems("minecraft:torch", 64)
```

### Dynamic Commands

```clish
function teleportTo(x, y, z) {
    command.execute("/tp @s " + x + " " + y + " " + z)
}

teleportTo(player.x(), player.y(), player.z() + 10)
```

### Command with Selector

```clish
# Kill all creepers in 10 block radius
command.execute("/kill @e[type=minecraft:creeper,distance=..10]")

# Give all players diamond
command.execute("/give @a minecraft:diamond 1")

# Message all players
command.execute("/tellraw @a {\"text\":\"Hello everyone!\"}")
```

## Common Commands Reference

### World

| Command | Description |
|---------|-------------|
| `/time set <time>` | Set time (0-24000) |
| `/weather <type>` | Set weather |
| `/gamerule <rule> <value>` | Set game rule |

### Player

| Command | Description |
|---------|-------------|
| `/tp [x] [y] [z]` | Teleport |
| `/give <target> <item> [count]` | Give items |
| `/effect give <target> <effect>` | Give effects |
| `/xp <amount> [player]` | Give experience |

### Blocks

| Command | Description |
|---------|-------------|
| `/setblock <x> <y> <z> <block>` | Set block |
| `/fill <x1> <y1> <z1> <x2> <y2> <z2> <block>` | Fill area |

### Entity

| Command | Description |
|---------|-------------|
| `/summon <entity> [x] [y] [z]` | Spawn entity |
| `/kill <target>` | Kill entity |
| `/data get entity <target>` | Get entity NBT |

## Combining with Other APIs

### Get Player Position Then Teleport

```clish
function goUp() {
    local y = player.y() + 10
    command.execute("/tp @s " + player.x() + " " + y + " " + player.z())
}
```

### Check Entity Then Execute

```clish
function checkAndGive() {
    local entities = @e[type=minecraft:creeper,limit=1,distance=..5]
    if (len(entities) > 0) {
        command.execute("/give @p minecraft:gunpowder 1")
    }
}
```

## Tips

- Commands must start with `/`
- Use `@p` for nearest player, `@s` for self
- Use string concatenation for dynamic commands
- Some commands require permissions
- Long commands may be truncated in chat
