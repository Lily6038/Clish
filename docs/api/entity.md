# Entity API

The Entity API allows you to select and query entities using Minecraft's selector syntax.

## Entity Selection

Use Minecraft selector syntax to find entities:

```clish
@e[...]  # All entities
@p       # Nearest player
@a       # All players
@r       # Random player
@s       # Executing entity (the player running the script)
```

## Selector Criteria

| Criterion | Description | Example |
|-----------|-------------|---------|
| `type` | Entity type | `type=minecraft:creeper` |
| `distance` | Distance from player | `distance=..5` (within 5 blocks) |
| `level` | Experience level | `level=10..` |
| `x`, `y`, `z` | Position | `x=0,y=64,z=0` |
| `dx`, `dy`, `dz` | Volume | `dx=5,dy=1,dz=5` |
| `tag` | Has tag | `tag=mytag` |
| `nbt` | NBT match | `nbt={Health:10s}` |
| `limit` | Max results | `limit=5` |
| `sort` | Sort order | `sort=nearest` |
| `scores` | Scoreboard | `scores={myScore=10}` |

## Distance Operators

- `..5` - Less than or equal to 5
- `5..` - Greater than or equal to 5
- `3..7` - Between 3 and 7 (inclusive)

## Examples

### Get All Nearby Mobs

```clish
local mobs = @e[type=minecraft:!player,distance=..10]
```

### Get Nearest Player

```clish
local target = @p
```

### Get 5 Closest Creepers

```clish
local creepers = @e[type=minecraft:creeper,limit=5,sort=nearest]
```

### Get Entities in a Region

```clish
# Entities in a 5x5x5 cube centered at player
local entities = @e[x=player.x,y=player.y,z=player.z,dx=5,dy=5,dz=5]
```

### Get Entities with Specific Tag

```clish
local tagged = @e[tag=mytag]
```

## Entity Properties

Once you have entities, you can access their properties:

### Common Properties

| Property | Description |
|----------|-------------|
| `x`, `y`, `z` | Position |
| `id` | Entity type ID |
| `uuid` | Unique identifier |
| `health` | Entity health |
| `customName` | Custom name |
| `nbt` | Full NBT data |

### Example: Iterate Entities

```clish
function listNearbyMobs() {
    local mobs = @e[type=minecraft:!player,distance=..20]
    echo("Found " + len(mobs) + " mobs")
    for (local mob in mobs) {
        echo("- " + mob.id + " at " + mob.x + "," + mob.y + "," + mob.z)
    }
}
```

### Example: Find Specific Entity

```clish
function findCreeper() {
    local creepers = @e[type=minecraft:creeper,limit=1]
    if (len(creepers) > 0) {
        local creeper = creepers[0]
        echo("Found creeper at: " + creeper.x + ", " + creeper.y + ", " + creeper.z)
    } else {
        echo("No creepers found")
    }
}
```

## Filtering Examples

### All Hostile Mobs

```clish
local hostiles = @e[type=minecraft:zombie,type=minecraft:skeleton,type=minecraft:creeper]
```

### All Items on Ground

```clish
local items = @e[type=minecraft:item]
```

### All Tamed Wolves

```clish
local wolves = @e[type=minecraft:wolf,tag=owner]
```

## Tips

- Use `limit` to avoid processing too many entities
- Use `sort=nearest` for closest entities first
- Use `distance` to filter by range from player
- Entity selection returns an array (may be empty)
