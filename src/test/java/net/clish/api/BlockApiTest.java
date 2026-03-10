package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BlockApiTest {

    @Test
    void testGetBlockRequiresThreeArgs() {
        BlockApi.GetBlock fn = new BlockApi.GetBlock();
        // With no args, should return null (no Minecraft)
        Object result = fn.call(List.of());
        assertNull(result);
    }

    @Test
    void testGetNbtRequiresThreeArgs() {
        BlockApi.GetNbt fn = new BlockApi.GetNbt();
        // With no args, should return null
        Object result = fn.call(List.of());
        assertNull(result);
    }

    @Test
    void testExistsRequiresThreeArgs() {
        BlockApi.Exists fn = new BlockApi.Exists();
        // With no args, should return false
        Object result = fn.call(List.of());
        assertEquals(false, result);
    }

    @Test
    void testGetLightRequiresThreeArgs() {
        BlockApi.GetLight fn = new BlockApi.GetLight();
        // With no args, should return 0
        Object result = fn.call(List.of());
        assertEquals(0, result);
    }

    @Test
    void testApiNames() {
        assertEquals("block.get", new BlockApi.GetBlock().getName());
        assertEquals("block.nbt", new BlockApi.GetNbt().getName());
        assertEquals("block.exists", new BlockApi.Exists().getName());
        assertEquals("block.light", new BlockApi.GetLight().getName());
        assertEquals("block.sky", new BlockApi.GetSkyLight().getName());
    }
}
