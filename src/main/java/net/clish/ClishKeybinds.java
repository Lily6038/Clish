package net.clish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keyboard input handling for Ctrl-C emergency break.
 */
public class ClishKeybinds {
    private static final Logger LOGGER = LoggerFactory.getLogger("Clish");

    /**
     * Initialize keybind handlers.
     * This will be called from the client initializer.
     */
    public static void register() {
        // TODO: Register keybind for emergency stop
        // Using Fabric's keybind API:
        // KeyBinding keyStop = ...;
        // KeyCallback.EVENT.register((key, action) -> {
        //     if (key == keyStop && action == KeyAction.PRESS) {
        //         Clish.getInstance().stopScript();
        //     }
        // });

        LOGGER.info("Clish keybinds registered (Ctrl-C handler coming soon)");
    }

    /**
     * Handle keyboard input for script interruption.
     */
    public static void handleKeyPress(int keyCode, boolean isCtrlPressed) {
        // TODO: Implement actual key handling
        if (isCtrlPressed && keyCode == 0x2D) { // 'C' key
            Clish.getInstance().stopScript();
            LOGGER.info("Script interrupted by user");
        }
    }
}
