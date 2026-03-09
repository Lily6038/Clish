package net.clish.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class BlockApiTest {

    @Test
    void testGetBlockReturnsPlaceholder() {
        BlockApi.GetBlock fn = new BlockApi.GetBlock();
        Object result = fn.call(List.of());
        assertEquals("Block API requires Minecraft runtime", result);
    }

    @Test
    void testGetNbtReturnsPlaceholder() {
        BlockApi.GetNbt fn = new BlockApi.GetNbt();
        Object result = fn.call(List.of());
        assertEquals("Block NBT requires Minecraft runtime", result);
    }

    @Test
    void testExistsReturnsPlaceholder() {
        BlockApi.Exists fn = new BlockApi.Exists();
        Object result = fn.call(List.of());
        assertEquals("Block API requires Minecraft runtime", result);
    }

    @Test
    void testGetLightReturnsPlaceholder() {
        BlockApi.GetLight fn = new BlockApi.GetLight();
        Object result = fn.call(List.of());
        assertEquals("Block API requires Minecraft runtime", result);
    }

    @Test
    void testApiNames() {
        assertEquals("block.get", new BlockApi.GetBlock().getName());
        assertEquals("block.nbt", new BlockApi.GetNbt().getName());
        assertEquals("block.exists", new BlockApi.Exists().getName());
        assertEquals("block.light", new BlockApi.GetLight().getName());
    }
}
