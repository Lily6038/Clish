# Time Library

The time library provides functions for time manipulation and formatting.

## Current Time

### time.now

Returns current timestamp in milliseconds.

```clish
time.now()  # 1704067200000 (example)
```

### time.timestamp

Returns current timestamp in seconds.

```clish
time.timestamp()  # 1704067200 (example)
```

### time.utc

Returns current UTC date/time as ISO string.

```clish
time.utc()  # "2024-01-01T00:00:00Z"
```

## Sleep

### time.sleep

Pauses execution for specified milliseconds.

```clish
time.sleep(1000)  # Sleep 1 second
time.sleep(500)   # Sleep 500ms
```

## Formatting

### time.format

Formats a timestamp as a string.

```clish
time.format(timestamp, "yyyy-MM-dd")
time.format(timestamp, "yyyy-MM-dd HH:mm:ss")
time.format(timestamp, "HH:mm:ss")
```

**Format patterns:**

| Pattern | Description | Example |
|---------|-------------|---------|
| `yyyy` | 4-digit year | 2024 |
| `yy` | 2-digit year | 24 |
| `MM` | Month (2-digit) | 01-12 |
| `MMM` | Month name short | Jan |
| `MMMM` | Month name full | January |
| `dd` | Day (2-digit) | 01-31 |
| `HH` | Hour (24h) | 00-23 |
| `hh` | Hour (12h) | 01-12 |
| `mm` | Minute | 00-59 |
| `ss` | Second | 00-59 |
| `SSS` | Millisecond | 000-999 |
| `a` | AM/PM | AM/PM |
| `z` | Timezone | UTC |

### Examples

```clish
local now = time.now()
time.format(now, "yyyy-MM-dd")           # "2024-01-15"
time.format(now, "HH:mm:ss")             # "14:30:45"
time.format(now, "MMMM dd, yyyy")         # "January 15, 2024"
time.format(now, "MMM dd yyyy HH:mm")    # "Jan 15 2024 14:30"
```

## Parsing

### time.parse

Parses a date string to timestamp.

```clish
time.parse("2024-01-15", "yyyy-MM-dd")     # timestamp
time.parse("2024-01-15 10:30:00", "yyyy-MM-dd HH:mm:ss")
```

## Date Components

### time.date

Returns date components as object.

```clish
time.date()
# {
#   year: 2024,
#   month: 1,
#   day: 15,
#   hour: 14,
#   minute: 30,
#   second: 45,
#   dayOfWeek: 1,
#   timestamp: 1705318245000
# }
```

## Time Arithmetic

### time.add

Adds time to a timestamp.

```clish
local now = time.now()
time.add(now, 1, "hour")    # +1 hour
time.add(now, 30, "minute") # +30 minutes
time.add(now, 1, "day")     # +1 day
time.add(now, 1, "week")    # +1 week
time.add(now, 500, "ms")   # +500 milliseconds
```

**Units:** `ms/millis`, `s/sec`, `m/min`, `h/hour`, `d/day`, `w/week`

### time.subtract

Subtracts time from a timestamp.

```clish
local now = time.now()
time.subtract(now, 1, "hour")    # -1 hour
time.subtract(now, 30, "minute") # -30 minutes
```

### time.diff

Returns difference between two timestamps.

```clish
local t1 = time.now()
time.sleep(1000)
local t2 = time.now()

time.diff(t1, t2, "ms")   # ~1000
time.diff(t1, t2, "s")   # ~1
```

## Examples

### Timer/Stopwatch

```clish
function timer(fn) {
    local start = time.now()
    fn()
    local end = time.now()
    local duration = time.diff(start, end, "ms")
    echo("Took " + duration + "ms")
}

timer(function() {
    # Your code here
    time.sleep(500)
})
```

### Countdown

```clish
function countdown(seconds) {
    for (local i = seconds; i > 0; i = i - 1) {
        echo(i)
        time.sleep(1000)
    }
    echo("Time's up!")
}

countdown(10)
```

### Format Relative Time

```clish
function relativeTime(timestamp) {
    local now = time.now()
    local diff = time.diff(timestamp, now, "s")

    if (diff < 60) {
        return diff + " seconds ago"
    } elif (diff < 3600) {
        return (diff / 60) + " minutes ago"
    } elif (diff < 86400) {
        return (diff / 3600) + " hours ago"
    } else {
        return (diff / 86400) + " days ago"
    }
}
```

### Schedule Event

```clish
function scheduleEvent(delaySeconds, callback) {
    local target = time.add(time.now(), delaySeconds, "s")

    while (time.now() < target) {
        time.sleep(100)
    }

    callback()
}

scheduleEvent(5, function() {
    echo("Event triggered!")
})
```

### Unix Timestamp to Date

```clish
function unixToDate(unixTimestamp) {
    # Unix timestamps are in seconds, convert to ms
    local ms = unixTimestamp * 1000
    return time.date(ms)
}

local date = unixToDate(1704067200)
echo(date.year + "-" + date.month + "-" + date.day)
```

### Current Time String

```clish
function currentTimeString() {
    return time.format(time.now(), "yyyy-MM-dd HH:mm:ss")
}

echo(currentTimeString())  # "2024-01-15 14:30:45"
```
