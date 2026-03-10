package net.clish.api;

import net.clish.ast.ClishLibrary;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.List;

/**
 * Player API for accessing player data from Minecraft.
 */
public class PlayerApi {
    private static final Minecraft mc = Minecraft.getInstance();

    /**
     * Get player X position.
     */
    public static class GetX implements ClishLibrary {
        @Override
        public String getName() {
            return "player.x";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;
            return player.getX();
        }
    }

    /**
     * Get player Y position.
     */
    public static class GetY implements ClishLibrary {
        @Override
        public String getName() {
            return "player.y";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;
            return player.getY();
        }
    }

    /**
     * Get player Z position.
     */
    public static class GetZ implements ClishLibrary {
        @Override
        public String getName() {
            return "player.z";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;
            return player.getZ();
        }
    }

    /**
     * Get player health.
     */
    public static class GetHealth implements ClishLibrary {
        @Override
        public String getName() {
            return "player.health";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;
            return player.getHealth();
        }
    }

    /**
     * Get player food level.
     */
    public static class GetFood implements ClishLibrary {
        @Override
        public String getName() {
            return "player.food";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;
            return player.getFoodData().getFoodLevel();
        }
    }

    /**
     * Get player dimension.
     */
    public static class GetDimension implements ClishLibrary {
        @Override
        public String getName() {
            return "player.dimension";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;
            return player.level().dimension().toString();
        }
    }

    /**
     * Get player inventory as list of item names.
     */
    public static class GetInventory implements ClishLibrary {
        @Override
        public String getName() {
            return "player.inventory";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;

            List<String> items = new java.util.ArrayList<>();
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                var item = player.getInventory().getItem(i);
                if (!item.isEmpty()) {
                    items.add(item.getItem().toString());
                }
            }
            return items;
        }
    }

    /**
     * Get player's held item name.
     */
    public static class GetHeldItem implements ClishLibrary {
        @Override
        public String getName() {
            return "player.heldItem";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;

            var item = player.getMainHandItem();
            if (item.isEmpty()) return "";
            return item.getItem().toString();
        }
    }

    /**
     * Get player yaw rotation.
     */
    public static class GetYaw implements ClishLibrary {
        @Override
        public String getName() {
            return "player.yaw";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;
            return player.getYRot();
        }
    }

    /**
     * Get player pitch rotation.
     */
    public static class GetPitch implements ClishLibrary {
        @Override
        public String getName() {
            return "player.pitch";
        }

        @Override
        public Object call(List<Object> args) {
            LocalPlayer player = mc.player;
            if (player == null) return null;
            return player.getXRot();
        }
    }

    /**
     * Get all player API functions.
     */
    public static List<ClishLibrary> getAll() {
        return List.of(
            new GetX(),
            new GetY(),
            new GetZ(),
            new GetHealth(),
            new GetFood(),
            new GetDimension(),
            new GetInventory(),
            new GetHeldItem(),
            new GetYaw(),
            new GetPitch()
        );
    }
}
