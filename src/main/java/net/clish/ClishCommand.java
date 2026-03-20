package net.clish;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Main command registration for /clish command.
 */
public class ClishCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger("Clish");
    private static final Path SCRIPTS_DIR = Paths.get("config/clish/scripts");
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static Future<?> runningScript = null;

    // Queue for output messages to be sent from background thread to main thread
    private static final ConcurrentLinkedQueue<Component> outputQueue = new ConcurrentLinkedQueue<>();

    /**
     * Poll and send any queued output messages. Call this from the main thread tick.
     */
    public static void flushOutputQueue() {
        Component msg;
        while ((msg = outputQueue.poll()) != null) {
            // Send directly to game - this must be called from main thread
            ClishCommand.sendQueuedMessage(msg);
        }
    }

    /**
     * Stored command source for sending queued messages from main thread.
     */
    private static FabricClientCommandSource storedSource;

    /**
     * Set the command source for sending queued messages.
     */
    public static void setCommandSource(FabricClientCommandSource source) {
        storedSource = source;
    }

    /**
     * Send a queued message via the stored command source.
     */
    private static void sendQueuedMessage(Component msg) {
        if (storedSource != null) {
            storedSource.sendFeedback(msg);
        }
    }

    /**
     * Send message to player chat using direct Component API.
     */
    private static void sendToChat(CommandContext<FabricClientCommandSource> context, String message) {
        context.getSource().sendFeedback(Component.literal(message));
    }

    /**
     * Register the /clish command.
     */
    public static void register() {
        try {
            LOGGER.info("Registering Clish commands...");

            ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                dispatcher.register(
                    ClientCommandManager.literal("clish")
                        .then(ClientCommandManager.literal("run")
                            .then(ClientCommandManager.argument("script", StringArgumentType.string())
                                .executes(ClishCommand::runScript)))
                        .then(ClientCommandManager.literal("list")
                            .executes(ClishCommand::listScripts))
                        .then(ClientCommandManager.literal("stop")
                            .executes(ClishCommand::stopScript))
                        .then(ClientCommandManager.literal("help")
                            .executes(ClishCommand::showHelp))
                        .executes(ClishCommand::showHelp)
                );
            });

            LOGGER.info("Clish commands registered successfully");
        } catch (Exception e) {
            LOGGER.error("Failed to register Clish commands: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private static int runScript(CommandContext<FabricClientCommandSource> context) {
        String scriptName = StringArgumentType.getString(context, "script");

        // Add .clish extension if not present
        if (!scriptName.endsWith(".clish")) {
            scriptName = scriptName + ".clish";
        }

        Path scriptPath = SCRIPTS_DIR.resolve(scriptName);

        if (!Files.exists(scriptPath)) {
            sendToChat(context, "§cScript not found: " + scriptName);
            return 0;
        }

        try {
            String scriptSource = Files.readString(scriptPath);
            sendToChat(context, "§aRunning script: " + scriptName + "...");

            // Store command source for output queue processing on main thread
            setCommandSource(context.getSource());

            // Create output consumer that queues messages for main thread processing
            Consumer<String> outputConsumer = (output) -> {
                outputQueue.offer(Component.literal("§7" + output));
            };

            // Run script asynchronously with output consumer
            net.clish.ast.ScriptEngine engine = ClishClient.getInstance().getScriptEngine();
            engine.setOutputConsumer(outputConsumer);

            runningScript = executor.submit(() -> {
                try {
                    engine.execute(scriptSource);
                } catch (Exception e) {
                    outputQueue.offer(Component.literal("§cError: " + e.getMessage()));
                } finally {
                    engine.setOutputConsumer(null);
                }
            });

            return 1;
        } catch (Exception e) {
            sendToChat(context, "§cError: " + e.getMessage());
            return 0;
        }
    }

    private static int listScripts(CommandContext<FabricClientCommandSource> context) {
        File scriptsDir = SCRIPTS_DIR.toFile();

        if (!scriptsDir.exists()) {
            sendToChat(context, "§7No scripts found. Create scripts in config/clish/scripts/");
            return 1;
        }

        File[] scripts = scriptsDir.listFiles((dir, name) -> name.endsWith(".clish"));

        if (scripts == null || scripts.length == 0) {
            sendToChat(context, "§7No scripts found. Create scripts in config/clish/scripts/");
            return 1;
        }

        StringBuilder sb = new StringBuilder("§6=== Available scripts ===\n");
        for (File script : scripts) {
            sb.append("§b- ").append(script.getName()).append("\n");
        }

        sendToChat(context, sb.toString());
        return 1;
    }

    private static int stopScript(CommandContext<FabricClientCommandSource> context) {
        if (runningScript == null || runningScript.isDone()) {
            sendToChat(context, "§7No script is currently running.");
            return 1;
        }

        runningScript.cancel(true);

        net.clish.ast.ScriptEngine engine = ClishClient.getInstance().getScriptEngine();
        engine.interrupt();

        sendToChat(context, "§aScript stopped.");
        return 1;
    }

    private static int showHelp(CommandContext<FabricClientCommandSource> context) {
        String help = "§6=== Clish Commands ===\n" +
            "§b/clish run <script> §f- Run a script\n" +
            "§b/clish list §f- List available scripts\n" +
            "§b/clish stop §f- Stop running script\n" +
            "§b/clish help §f- Show this help\n\n" +
            "§7Scripts: config/clish/scripts/";

        sendToChat(context, help);
        return 1;
    }
}
