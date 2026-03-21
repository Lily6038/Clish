# Concurrency

## Philosophy

Clish follows a **shell-like concurrency model** inspired by traditional Unix pipelines:

- **Channels** instead of threads directly - like Unix pipes
- **Spawn/wait** for background jobs - like `&` and `wait` in shells
- **Co-processes** for bidirectional communication - like named pipes
- **Pipes** for data flow composition - like `|` in shells

This model is simpler than full threads while being powerful enough for concurrent operations.

## Channels

Channels are typed conduits for passing values between concurrent tasks.

### Creating Channels

```clish
# Unbuffered channel (sends block until received)
let ch = channel()

# Buffered channel with specified capacity
let ch = channel(10)
```

### Sending Values

```clish
send(ch, "hello")
send(ch, {a: 1, b: 2})
send(ch, 42)
```

### Receiving Values

```clish
# Blocking receive - waits until value available
let msg = receive(ch)
echo msg

# Non-blocking receive - returns nil if empty
let msg = tryReceive(ch)
if (msg != nil) {
    echo "Got: " + msg
}
```

### Channel Example

```clish
let ch = channel()

spawn {
    echo "Sending message"
    send(ch, "Hello from spawn!")
}

let msg = receive(ch)
echo msg  # Prints: Hello from spawn!
```

## Pipes

Pipes create data flow pipelines, similar to Unix shell pipes.

### Basic Pipe

```clish
let numbers = [1, 2, 3, 4, 5]

let result = numbers | map(fn(x) { x * 2 }) | filter(fn(x) { x > 4 })

echo result  # [6, 8, 10]
```

### Pipe with Error Propagation

```clish
function riskyDouble(x) {
    if (x < 0) {
        return err("Negative not allowed")
    }
    return ok(x * 2)
}

let result = [1, 2, -3] | flatMap(fn(x) { riskyDouble(x) })
# Result will contain ok values and propagate errors
```

## Spawn & Jobs

### Spawning Background Tasks

```clish
# Spawn a simple background task
spawn {
    echo "Running in background"
    time.sleep(1000)
    echo "Done"
}

# Spawn multiple jobs
spawn { processChunk(1) }
spawn { processChunk(2) }
spawn { processChunk(3) }
```

### Waiting for Jobs

```clish
# Wait for all spawned jobs to complete
wait

# Wait for specific job ID
let jobId = $!
wait(jobId)

# Wait for specific job with timeout (ms)
let success = wait(jobId, 5000)
```

### Job ID ($!)

`$!` returns the ID of the last spawned background job:

```clish
spawn { longTask() }
let lastJob = $!
wait(lastJob)
```

### Managing Multiple Jobs

```clish
let job1 = spawn { task1() }
let job2 = spawn { task2() }
let job3 = spawn { task3() }

wait(job1)
wait(job2)
wait(job3)

echo "All tasks completed"
```

## Co-Processes

Co-processes are persistent concurrent tasks with bidirectional channels.

### Creating a Co-Process

```clish
coproc adder {
    while (true) {
        let msg = receive(self.in)
        send(self.out, msg.a + msg.b)
    }
}
```

### Accessing Co-Process Channels

```clish
# self.in - input channel (receive from this in the coproc)
# self.out - output channel (send to this from the coproc)
# self.pid - process ID
```

### Using a Co-Process

```clish
# Send work to the co-process
send(adder.in, {a: 10, b: 20})

# Receive result
let sum = receive(adder.out)
echo sum  # 30

# Can keep sending more work
send(adder.in, {a: 5, b: 7})
let sum2 = receive(adder.out)  # 12
```

### Multiple Co-Processes

```clish
# Create worker pool
for (i in range(3)) {
    let workers[i] = coproc worker {
        while (true) {
            let job = receive(self.in)
            let result = process(job)
            send(self.out, result)
        }
    }
}

# Distribute work
for (work in workItems) {
    send(workers[work.id % 3].in, work)
}

# Collect results
for (work in workItems) {
    let result = receive(workers[work.id % 3].out)
    echo result
}
```

## Patterns

### Producer/Consumer

```clish
let ch = channel()

# Producer
spawn {
    for (item in items) {
        send(ch, item)
    }
    send(ch, nil)  # Sentinel
}

# Consumer
let count = 0
while (true) {
    let item = receive(ch)
    if (item == nil) {
        break
    }
    count = count + 1
}
echo "Processed " + count
```

### Parallel Entity Scanning

```clish
# Scan different areas concurrently
let areas = [
    {min: [0, 0, 0], max: [100, 64, 100]},
    {min: [100, 0, 0], max: [200, 64, 100]},
    {min: [200, 0, 0], max: [300, 64, 100]}
]

let channels = []

for (area in areas) {
    let ch = channel()
    channels.push(ch)

    spawn {
        let entities = @e[distance=..50]
        send(ch, entities)
    }
}

# Collect all results
let allEntities = []
for (ch in channels) {
    let entities = receive(ch)
    allEntities = allEntities + entities
}
```

### Concurrent NBT Reads

```clish
let files = ["a.dat", "b.dat", "c.dat", "d.dat"]
let channels = []

# Read all files concurrently
for (file in files) {
    let ch = channel()
    channels.push(ch)

    spawn {
        let nbt = nbt.read(file)
        send(ch, nbt)
    }
}

# Gather results
let results = []
for (ch in channels) {
    let nbt = receive(ch)
    results.push(nbt)
}
```

### Fan-Out / Fan-In

```clish
let workChannel = channel()
let resultChannel = channel()

# Create workers
for (i in range(5)) {
    spawn {
        while (true) {
            let job = receive(workChannel)
            if (job == nil) {
                break
            }
            let result = process(job)
            send(resultChannel, result)
        }
    }
}

# Send work
for (item in workItems) {
    send(workChannel, item)
}

# Send termination signals
for (i in range(5)) {
    send(workChannel, nil)
}

# Collect results
let results = []
for (item in workItems) {
    results.push(receive(resultChannel))
}
```

### Pipeline with Buffers

```clish
# Create pipeline stages with buffers
let stage1 = channel(10)  # Buffer size 10
let stage2 = channel(20)

spawn { stage1Producer(stage1) }
spawn { stage1Consumer(stage1, stage2) }
spawn { stage2Consumer(stage2) }
```

## Error Handling in Concurrency

### Propagating Errors from Spawn

```clish
let ch = channel()

spawn {
    let result = riskyOperation()
    if (isError(result)) {
        send(ch, result)
        return
    }
    send(ch, ok(result.value * 2))
}

let result = receive(ch)
if (isError(result)) {
    echo "Error: " + result.message
} else {
    echo "Success: " + result.value
}
```

### Timeout Pattern

```clish
function waitWithTimeout(ch, timeoutMs) {
    let start = time.now()
    while (time.now() - start < timeoutMs) {
        let result = tryReceive(ch)
        if (result != nil) {
            return ok(result)
        }
        time.sleep(10)
    }
    return err("Timeout", -1)
}
```

## Best Practices

1. **Always wait or close channels** - Don't let spawned tasks run forever
2. **Use buffered channels** for high-throughput scenarios
3. **Send nil as sentinel** to signal completion
4. **Keep co-processes alive** - They're designed for persistent workloads
5. **Handle errors in spawn** - Errors won't automatically propagate

## Limitations

- No shared memory between spawned tasks (use channels)
- No thread优先级 control
- Maximum concurrent jobs limited by configuration
- Channels are untyped (any value can be sent)
