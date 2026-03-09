# Math Library

The math library provides mathematical functions and constants.

## Constants

### math.pi

Returns the value of Pi.

```clish
math.pi  # 3.141592653589793
```

### math.e

Returns Euler's number.

```clish
math.e  # 2.718281828459045
```

## Basic Functions

### math.abs

Absolute value.

```clish
math.abs(-5)     # 5
math.abs(3.14)   # 3.14
```

### math.floor

Largest integer less than or equal to value.

```clish
math.floor(3.7)  # 3
math.floor(-2.3) # -3
```

### math.ceil

Smallest integer greater than or equal to value.

```clish
math.ceil(3.2)   # 4
math.ceil(-2.3)  # -2
```

### math.round

Nearest integer.

```clish
math.round(3.5)  # 4
math.round(3.4)  # 3
```

## Min/Max

### math.min

Returns the minimum of values.

```clish
math.min(3, 1, 4, 1, 5)  # 1
math.min(-1, -5)         # -5
```

### math.max

Returns the maximum of values.

```clish
math.max(3, 1, 4, 1, 5)  # 5
math.max(-1, -5)         # -1
```

### math.clamp

Clamps a value between min and max.

```clish
math.clamp(5, 0, 10)   # 5
math.clamp(-5, 0, 10)  # 0
math.clamp(15, 0, 10)  # 10
```

## Power/Roots

### math.sqrt

Square root.

```clish
math.sqrt(16)  # 4
math.sqrt(2)   # 1.414...
```

### math.pow

Power - x raised to y.

```clish
math.pow(2, 3)   # 8
math.pow(5, 2)   # 25
math.pow(2, 0.5) # 1.414... (square root of 2)
```

## Trigonometric Functions

### math.sin

Sine (input in radians).

```clish
math.sin(0)        # 0
math.sin(math.pi)  # ~0
```

### math.cos

Cosine (input in radians).

```clish
math.cos(0)        # 1
math.cos(math.pi)  # ~-1
```

### math.tan

Tangent (input in radians).

```clish
math.tan(0)  # 0
```

### math.asin

Arc sine (returns radians).

```clish
math.asin(0)   # 0
math.asin(1)   # 1.570... (pi/2)
```

### math.acos

Arc cosine (returns radians).

```clish
math.acos(1)   # 0
math.acos(0)   # 1.570... (pi/2)
```

### math.atan

Arc tangent (returns radians).

```clish
math.atan(0)  # 0
math.atan(1)  # 0.785... (pi/4)
```

## Logarithms

### math.log

Natural logarithm (base e).

```clish
math.log(math.e)  # 1
math.log(1)       # 0
```

### math.log10

Base 10 logarithm.

```clish
math.log10(100)  # 2
math.log10(10)   # 1
```

## Angle Conversion

adians

Converts### math.toR degrees to radians.

```clish
math.toRadians(180)  # 3.141... (pi)
math.toRadians(90)   # 1.570... (pi/2)
```

### math.toDegrees

Converts radians to degrees.

```clish
math.toDegrees(math.pi)    # 180
math.toDegrees(math.pi/2)  # 90
```

## Random

### math.random

Returns a random float between 0 and 1.

```clish
math.random()  # 0.723...
```

### math.randomInt

Returns a random integer between min and max (inclusive).

```clish
math.randomInt(1, 10)   # Random between 1 and 10
math.randomInt(0, 100)   # Random between 0 and 100
```

## Examples

### Calculate Distance

```clish
function distance(x1, y1, z1, x2, y2, z2) {
    local dx = x2 - x1
    local dy = y2 - y1
    local dz = z2 - z1
    return math.sqrt(dx*dx + dy*dy + dz*dz)
}

local dist = distance(0, 0, 0, 3, 4, 0)  # 5
```

### Random Choice

```clish
function randomChoice(items) {
    local index = math.floor(math.random() * len(items))
    return items[index]
}

local items = ["sword", "shield", "potion"]
local choice = randomChoice(items)
```

### Degrees to Direction

```clish
function angleToDirection(angleDegrees) {
    local radians = math.toRadians(angleDegrees)
    return {
        x: math.cos(radians),
        z: math.sin(radians)
    }
}
```
