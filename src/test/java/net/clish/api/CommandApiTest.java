package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class CommandApiTest {

    @Test
    void testExecuteCommandReturnsPlaceholder() {
        CommandApi.ExecuteCommand fn = new CommandApi.ExecuteCommand();
        Object result = fn.call(List.of("say hello"));
        assertEquals("Command execution requires Minecraft runtime", result);
    }

    @Test
    void testExecuteCommandEmptyArgs() {
        CommandApi.ExecuteCommand fn = new CommandApi.ExecuteCommand();
        Object result = fn.call(List.of());
        assertNull(result);
    }

    @Test
    void testRunCommandReturnsPlaceholder() {
        CommandApi.RunCommand fn = new CommandApi.RunCommand();
        Object result = fn.call(List.of("give @s diamond 1"));
        assertEquals("Command execution requires Minecraft runtime", result);
    }

    @Test
    void testApiNames() {
        assertEquals("command.execute", new CommandApi.ExecuteCommand().getName());
        assertEquals("command.run", new CommandApi.RunCommand().getName());
    }

    @Test
    void testGetAllReturnsTwoFunctions() {
        List<?> all = CommandApi.getAll();
        assertEquals(2, all.size());
    }
}
