# Player API

The Player API provides access to player data including position, health, inventory, and more.

## Position

### player.x

Returns the player's X coordinate.

```clish
local x = player.x()
```

### player.y

Returns the player's Y coordinate.

```clish
local y = player.y()
```

### player.z

Returns the player's Z coordinate.

```clish
local z = player.z()
```

### Get Full Position

```clish
function getPosition() {
    local x = player.x()
    local y = player.y()
    local z = player.z()
    echo("Position: " + x + ", " + y + ", " + z)
}
```

## Health

### player.health

Returns the player's current health.

```clish
local health = player.health()
```

### Check Health Status

```clish
function checkHealth() {
    local hp = player.health()
    if (hp < 10) {
        echo("Warning: Low health!")
    } elif (hp < 5) {
        echo("Critical: Very low health!")
    }
}
```

## Food

### player.food

Returns the player's food level (0-20).

```clish
local food = player.food()
```

## Dimension

### player.dimension

Returns the current dimension name.

```clish
local dim = player.dimension()
# Returns: "minecraft:overworld", "minecraft:the_nether", or "minecraft:the_end"
```

## Inventory

### player.inventory

Returns the player's inventory as an array.

```clish
local inv = player.inventory()
# Returns array of item objects
```

### Access Inventory Slot

```clish
local inv = player.inventory()
local firstItem = inv[0]  # First slot
```

### Example: Find Item

```clish
function hasItem(itemId) {
    local inv = player.inventory()
    for (local item in inv) {
        if (item.id == itemId) {
            return true
        }
    }
    return false
}

if (hasItem("minecraft:diamond")) {
    echo("Has diamonds!")
}
```

## Complete Example

```clish
function playerStatus() {
    echo("=== Player Status ===")
    echo("Position: " + player.x() + ", " + player.y() + ", " + player.z())
    echo("Health: " + player.health())
    echo("Food: " + player.food())
    echo("Dimension: " + player.dimension())
    echo("=====================")
}

playerStatus()
```
