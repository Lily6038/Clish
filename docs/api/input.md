# Input API

The Input API allows scripts to simulate player input including clicks and mouse movement.

**Note:** These functions require Minecraft runtime to be fully functional.

## Mouse Input

### input.leftClick

Simulate a left mouse button click (attack).

```clish
input.leftClick()
```

### input.rightClick

Simulate a right mouse button click (use item/place block).

```clish
input.rightClick()
```

### input.mouseMove

Move the mouse by specified offsets.

```clish
input.mouseMove(dx, dy)
# dx - horizontal movement
# dy - vertical movement
```

## Keyboard Input

### input.keyPress

Simulate pressing a keyboard key.

```clish
input.keyPress("keycode")
```

Common key codes:
- `key.keyboard.w` - W
- `key.keyboard.a` - A
- `key.keyboard.s` - S
- `key.keyboard.d` - D
- `key.keyboard.space` - Space
- `key.keyboard.shift` - Shift
- `key.keyboard.control` - Ctrl
- `key.keyboard.1` through `key.keyboard.9` - Number keys

### input.typeText

Type text as if the player was typing.

```clish
input.typeText("Hello, World!")
```

## Examples

### Click Multiple Times

```clish
function clickTimes(count) {
    for (local i = 0; i < count; i = i + 1) {
        input.leftClick()
        time.sleep(100)  # Wait between clicks
    }
}

clickTimes(5)
```

### Continuous Mining

```clish
function mineBlock() {
    for (local i = 0; i < 10; i = i + 1) {
        input.leftClick()
        time.sleep(150)
    }
}

mineBlock()
```

### Move Mouse in Circle

```clish
function moveMouseCircle() {
    for (local i = 0; i < 360; i = i + 10) {
        local angle = i * 3.14159 / 180
        local dx = math.cos(angle) * 10
        local dy = math.sin(angle) * 10
        input.mouseMove(dx, dy)
        time.sleep(50)
    }
}
```

### Type a Message

```clish
function sendMessage() {
    input.typeText("/msg Hello from Clish!")
}
```

### WASD Movement Simulation

```clish
function moveForward() {
    input.keyPress("key.keyboard.w")
    time.sleep(100)
}

function strafeLeft() {
    input.keyPress("key.keyboard.a")
    time.sleep(100)
}

function walkSquare() {
    for (local i = 0; i < 4; i = i + 1) {
        moveForward()
        moveForward()
        strafeLeft()
        strafeLeft()
    }
}
```

## Advanced Patterns

### Click Until Success

```clish
function clickUntil(fn) {
    local maxClicks = 20
    for (local i = 0; i < maxClicks; i = i + 1) {
        input.leftClick()
        time.sleep(200)
        if (fn()) {
            echo("Success at click " + i)
            return
        }
    }
    echo("Failed after " + maxClicks + " clicks")
}

# Usage: click until player health is full
clickUntil(function() { return player.health() >= 20 })
```

### Mouse Follow Entity

```clish
function lookAt(target) {
    # This would require actual rotation calculations
    # Simplified example
    echo("Looking at entity at: " + target.x + "," + target.y + "," + target.z)
}
```

## Safety Considerations

- Input simulation can interfere with normal gameplay
- Use delays between actions to avoid detection
- Be mindful of server-side anti-cheat systems
- Some actions may require the mod to have proper permissions

## Troubleshooting

**Input not working?**
- Ensure Minecraft is in focus
- Check that the mod is properly loaded
- Some servers may block input simulation

**Mouse movement not accurate?**
- Minecraft uses normalized device coordinates
- Movement scales with mouse sensitivity
