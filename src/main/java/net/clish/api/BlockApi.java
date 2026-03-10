package net.clish.api;

import net.clish.ast.ClishLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Block selection API for querying block data from Minecraft.
 */
public class BlockApi {
    private static final Minecraft mc = Minecraft.getInstance();

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
            if (args.size() < 3) return null;

            if (mc.level == null) return null;

            int x = ((Number) args.get(0)).intValue();
            int y = ((Number) args.get(1)).intValue();
            int z = ((Number) args.get(2)).intValue();

            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = mc.level.getBlockState(pos);

            return state.getBlock().toString();
        }
    }

    /**
     * Get block NBT at position.
     */
    public static class GetNbt implements ClishLibrary {
        @Override
        public String getName() {
            return "block.nbt";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return null;

            if (mc.level == null) return null;

            int x = ((Number) args.get(0)).intValue();
            int y = ((Number) args.get(1)).intValue();
            int z = ((Number) args.get(2)).intValue();

            BlockPos pos = new BlockPos(x, y, z);
            BlockEntity blockEntity = mc.level.getBlockEntity(pos);

            if (blockEntity == null) return null;
            return "BlockEntity at " + x + "," + y + "," + z;
        }
    }

    /**
     * Check if block exists (is loaded).
     */
    public static class Exists implements ClishLibrary {
        @Override
        public String getName() {
            return "block.exists";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return false;

            if (mc.level == null) return false;

            int x = ((Number) args.get(0)).intValue();
            int y = ((Number) args.get(1)).intValue();
            int z = ((Number) args.get(2)).intValue();

            BlockPos pos = new BlockPos(x, y, z);
            return !mc.level.getBlockState(pos).isAir();
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
            if (args.size() < 3) return 0;

            if (mc.level == null) return 0;

            int x = ((Number) args.get(0)).intValue();
            int y = ((Number) args.get(1)).intValue();
            int z = ((Number) args.get(2)).intValue();

            BlockPos pos = new BlockPos(x, y, z);
            return mc.level.getLightEngine().getRawBrightness(pos, 0);
        }
    }

    /**
     * Get block sky light level.
     */
    public static class GetSkyLight implements ClishLibrary {
        @Override
        public String getName() {
            return "block.sky";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return 0;

            if (mc.level == null) return 0;

            int x = ((Number) args.get(0)).intValue();
            int y = ((Number) args.get(1)).intValue();
            int z = ((Number) args.get(2)).intValue();

            BlockPos pos = new BlockPos(x, y, z);
            return mc.level.getLightEngine().getRawBrightness(pos, 0);
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
            new GetLight(),
            new GetSkyLight()
        );
    }
}
