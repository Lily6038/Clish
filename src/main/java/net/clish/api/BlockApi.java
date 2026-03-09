package net.clish.api;

import net.clish.ast.ClishLibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Block selection API for querying block data.
 */
public class BlockApi {
    private static final Logger LOGGER = LoggerFactory.getLogger("Clish");

    /**
     * Get block at position.
     */
    public static class GetBlock implements ClishLibrary {
        @Override
        public String getName() {
            return "block.get";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("BlockApi.{} called with args: {}", getName(), args);
            // TODO: Add MinecraftClient.getInstance().isInGame() check
            return "Block API requires Minecraft runtime";
        }
    }

    /**
     * Get block NBT.
     */
    public static class GetNbt implements ClishLibrary {
        @Override
        public String getName() {
            return "block.nbt";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("BlockApi.{} called with args: {}", getName(), args);
            // TODO: Add MinecraftClient.getInstance().isInGame() check
            return "Block NBT requires Minecraft runtime";
        }
    }

    /**
     * Check if block exists.
     */
    public static class Exists implements ClishLibrary {
        @Override
        public String getName() {
            return "block.exists";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("BlockApi.{} called with args: {}", getName(), args);
            // TODO: Add MinecraftClient.getInstance().isInGame() check
            return "Block API requires Minecraft runtime";
        }
    }

    /**
     * Get block light level.
     */
    public static class GetLight implements ClishLibrary {
        @Override
        public String getName() {
            return "block.light";
        }

        @Override
        public Object call(List<Object> args) {
            LOGGER.debug("BlockApi.{} called with args: {}", getName(), args);
            // TODO: Add MinecraftClient.getInstance().isInGame() check
            return "Block API requires Minecraft runtime";
        }
    }

    /**
     * Get all block API functions.
     */
    public static List<ClishLibrary> getAll() {
        return List.of(
            new GetBlock(),
            new GetNbt(),
            new Exists(),
            new GetLight()
        );
    }
}
