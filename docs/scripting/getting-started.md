# Getting Started with Clish

Clish is a shell-like scripting language for Minecraft 1.21.11 Fabric that allows players to create and execute automation scripts.

## Installation

1. Download the mod JAR from the releases page
2. Place it in your Minecraft `mods` folder
3. Launch Minecraft with Fabric 1.21.11

## Quick Start

### Running Your First Script

Use the `/clish` command to execute scripts:

```
/clish run <script-name>
/clish run hello
```

### Creating Scripts

Scripts are stored in `config/clish/scripts/`. Create a file named `hello.clish`:

```clish
echo("Hello, World!")
```

Run it with `/clish run hello`.

### Using the In-Game Editor

Open the text editor with:

```
/clish edit <script-name>
/clish edit myscript
```

## Script Structure

```clish
# Comments start with #
function main() {
    echo("Starting script...")
    # Your code here
    echo("Done!")
}

# Call the main function
main()
```

## Basic Examples

### Example: Health Check

```clish
function checkHealth() {
    local health = player.health()
    if (health < 10) {
        echo("Low health!")
    } else {
        echo("Health is OK")
    }
}

checkHealth()
```

### Example: Auto-Mine Loop

```clish
function mineArea() {
    for (local i = 0; i < 10; i = i + 1) {
        input.leftClick()
        time.sleep(100)
    }
}

mineArea()
```

## Next Steps

- Read [Syntax Reference](syntax.md) for basic language constructs
- Explore [Control Flow](control-flow.md) for loops and conditionals
- Check [Libraries](../libraries/math.md) for built-in functions
- Learn about [Game APIs](../api/player.md) for interacting with Minecraft
