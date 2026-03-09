# Runtime Testing Guide

## Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests "net.clish.ClishCommandTest"
./gradlew test --tests "net.clish.api.PlayerApiTest"

# Run with verbose output
./gradlew test --info
```

## Running In-Game Tests

```bash
# Start development client
./gradlew runClient
```

In-game:
- Press `F3 + L` to view logs
- Use `/clish` command to run test scripts
- Check `~/.minecraft/logs/` for game logs

## Integration Test Scripts

Location: `src/test/resources/test-scripts/`

Run scripts with:
```
/clish run <script-name>
```

## Debugging Runtime Issues

1. **"Player API requires Minecraft runtime"**
   - This is expected when running outside Minecraft
   - For in-game testing, use `./gradlew runClient`

2. **Verify Minecraft runtime is available:**
   ```java
   if (MinecraftClient.getInstance().isInGame()) {
       // Safe to call API methods
   }
   ```

3. **Common log patterns to check:**
   - `PlayerApi.* called with args:`
   - `BlockApi.* called with args:`
   - `InputApi.* called with args:`
   - `CommandApi.* called with args:`

## Test Coverage

- **Unit Tests**: API stub tests verify placeholder behavior
- **Integration Tests**: Script execution with in-game runtime
- **Runtime Tests**: Full Minecraft environment testing

## CI/CD Integration

```bash
# Build and test
./gradlew build test

# Generate test report
./gradlew test --rerun-tasks
# View report at: build/reports/tests/test/index.html
```
