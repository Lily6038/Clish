# NBT Library

The NBT (Named Binary Tag) library provides functions for working with NBT data structures.

**Note:** This library provides basic NBT-like functionality using maps. Full Minecraft NBT integration requires runtime support.

## Create

### nbt.create

Creates a new empty NBT compound.

```clish
nbt.create()
# Returns {}
```

## Read

### nbt.get

Gets a value from NBT by path.

```clish
nbt.get(nbt, "key")
nbt.get(nbt, "outer.inner")
```

### nbt.has

Checks if a path exists.

```clish
nbt.has(nbt, "key")         # true/false
nbt.has(nbt, "outer.inner")
```

### nbt.keys

Returns all keys in the compound.

```clish
nbt.keys(nbt)  # ["key1", "key2", ...]
```

### nbt.values

Returns all values in the compound.

```clish
nbt.values(nbt)  # [value1, value2, ...]
```

### nbt.type

Returns the type of a value.

```clish
nbt.type(nbt)
nbt.type(value)

# Returns: "compound", "list", "string", "int", "long", "double", "byteArray", "intArray", "longArray"
```

## Write

### nbt.set

Sets a value at a path.

```clish
nbt.set(nbt, "key", value)
nbt.set(nbt, "outer.inner", value)
```

### nbt.remove

Removes a value at a path.

```clish
nbt.remove(nbt, "key")
nbt.remove(nbt, "outer.inner")
```

## Convert

### nbt.toString

Converts NBT to JSON-like string.

```clish
nbt.toString(nbt)
# Returns: "{\"key\": \"value\"}"
```

## Examples

### Create NBT Data

```clish
local nbt = nbt.create()
nbt.set(nbt, "name", "Steve")
nbt.set(nbt, "age", 30)
nbt.set(nbt, "isActive", true)

echo(nbt.toString(nbt))
# {"name": "Steve", "age": 30, "isActive": true}
```

### Nested Structures

```clish
local nbt = nbt.create()
nbt.set(nbt, "player.name", "Steve")
nbt.set(nbt, "player.stats.health", 20)
nbt.set(nbt, "player.stats.hunger", 20)

# Access nested values
local health = nbt.get(nbt, "player.stats.health")
```

### Working with Lists

```clish
local nbt = nbt.create()
nbt.set(nbt, "items", ["sword", "shield", "potion"])

local items = nbt.get(nbt, "items")
# Returns: ["sword", "shield", "potion"]
```

### Check and Update

```clish
function updateValue(nbt, path, newValue) {
    if (nbt.has(nbt, path)) {
        nbt.set(nbt, path, newValue)
        return true
    }
    return false
}

local nbt = nbt.create()
nbt.set(nbt, "count", 5)
updateValue(nbt, "count", 10)
```

### Iterate All Keys

```clish
function printAllKeys(nbt) {
    local keys = nbt.keys(nbt)
    for (local key in keys) {
        local value = nbt.get(nbt, key)
        echo(key + " = " + value)
    }
}
```

### Create Player Data

```clish
function createPlayerData(name, level, health) {
    local data = nbt.create()
    nbt.set(data, "playerName", name)
    nbt.set(data, "level", level)
    nbt.set(data, "health", health)
    nbt.set(data, "position.x", 0)
    nbt.set(data, "position.y", 64)
    nbt.set(data, "position.z", 0)
    return data
}

local player = createPlayerData("Steve", 30, 20)
```

### Merge NBT Data

```clish
function mergeNbt(target, source) {
    local sourceKeys = nbt.keys(source)
    for (local key in sourceKeys) {
        nbt.set(target, key, nbt.get(source, key))
    }
    return target
}

local base = nbt.create()
nbt.set(base, "a", 1)

local extra = nbt.create()
nbt.set(extra, "b", 2)

mergeNbt(base, extra)
# base now has both "a" and "b"
```

### Clone NBT

```clish
function cloneNbt(original) {
    local cloned = nbt.create()
    local keys = nbt.keys(original)
    for (local key in keys) {
        nbt.set(cloned, key, nbt.get(original, key))
    }
    return cloned
}
```

### Validate Structure

```clish
function validateNbt(nbt, requiredKeys) {
    for (local key in requiredKeys) {
        if (!nbt.has(nbt, key)) {
            return false
        }
    }
    return true
}

local required = ["name", "health", "position"]
local isValid = validateNbt(playerData, required)
```
