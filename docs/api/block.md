# Block API

The Block API allows you to query block data using coordinate syntax.

## Block Position Syntax

Specify blocks using coordinates:

```clish
x y z
# Example: 0 64 0

# Relative coordinates (from player position)
~ ~ ~           # Player's position
~0 ~1 ~0       # 1 block above player
^ ^ ^           # Not supported

# Local coordinates (relative to facing)
# Use with /execute for advanced positioning
```

## Functions

### block.get

Get block data at a specific position.

```clish
local block = block.get(x, y, z)
```

Returns a block object with properties:
- `id` - Block type ID (e.g., "minecraft:stone")
- `x`, `y`, `z` - Position
- `state` - Block states
- `nbt` - Block entity NBT (if applicable)

### block.nbt

Get the NBT data of a block (for block entities like chests, signs).

```clish
local nbt = block.nbt(x, y, z)
```

### block.exists

Check if a block exists at the position.

```clish
local exists = block.exists(x, y, z)
# Returns true if block is not air
```

### block.light

Get the light level at a position.

```clish
local light = block.light(x, y, z)
# Returns 0-15
```

## Examples

### Get Block Type

```clish
function checkBlock() {
    local x = player.x()
    local y = player.y() - 1  # Block below player
    local z = player.z()

    local block = block.get(x, y, z)
    echo("Standing on: " + block.id)
}

checkBlock()
```

### Check for Specific Block

```clish
function isBlock(targetId) {
    local x = player.x()
    local y = player.y() - 1
    local z = player.z()

    local block = block.get(x, y, z)
    return block.id == targetId
}

if (isBlock("minecraft:diamond_block")) {
    echo("You're on diamond!")
}
```

### Check Multiple Blocks

```clish
function checkSurrounding() {
    local px = player.x()
    local py = player.y()
    local pz = player.z()

    # Check 3x3 area at foot level
    for (local dx = -1; dx <= 1; dx = dx + 1) {
        for (local dz = -1; dz <= 1; dz = dz + 1) {
            local block = block.get(px + dx, py - 1, pz + dz)
            echo("Block at " + (px + dx) + "," + (py - 1) + "," + (pz + dz) + ": " + block.id)
        }
    }
}
```

### Get Block Light Level

```clish
function checkLighting() {
    local x = player.x()
    local y = player.y()
    local z = player.z()

    local light = block.light(x, y, z)
    echo("Light level: " + light)

    if (light < 8) {
        echo("It's dark here!")
    }
}
```

### Working with Block Entities

```clish
# Example: Check chest contents (hypothetical)
function checkChest() {
    local x = 0  # Specify coordinates
    local y = 64
    local z = 0

    local block = block.get(x, y, z)
    if (block.id == "minecraft:chest") {
        local nbt = block.nbt(x, y, z)
        echo("Chest NBT: " + nbt)
    }
}
```

## Coordinate Helper Functions

### Get Position Below Player

```clish
function getBlockBelow() {
    return block.get(player.x(), player.y() - 1, player.z())
}
```

### Get Block in Front of Player

```clish
function getBlockInFront() {
    # Note: Need direction from player
    return block.get(player.x(), player.y(), player.z() + 1)
}
```

## Common Block IDs

| Block | ID |
|-------|-----|
| Air | `minecraft:air` |
| Stone | `minecraft:stone` |
| Dirt | `minecraft:dirt` |
| Grass Block | `minecraft:grass_block` |
| Water | `minecraft:water` |
| Oak Log | `minecraft:oak_log` |
| Diamond Block | `minecraft:diamond_block` |
| Chest | `minecraft:chest` |
| Furnace | `minecraft:furnace` |

## Tips

- Y coordinate: 0 (bottom) to 256 (top)
- Use `player.y() - 1` to get the block standing on
- Block entities (chests, furnaces) have NBT data
- Light level affects mob spawning
