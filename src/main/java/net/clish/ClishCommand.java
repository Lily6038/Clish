package net.clish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main command registration for /clish command.
 */
public class ClishCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger("Clish");

    /**
     * Register the /clish command.
     */
    public static void register() {
        LOGGER.info("Registering Clish commands...");
        // TODO: Actual command registration via ClientCommandRegistrationCallback
        LOGGER.info("Clish commands registered successfully");
    }
}
