package net.clish.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configuration management for Clish.
 */
public class ClishConfig {
    private static final Path CONFIG_DIR = Paths.get("config", "clish");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("clish.properties");

    private static ClishConfig instance;

    private long timeoutMs = 60000;        // Script execution timeout (1 minute)
    private boolean autoReload = true;     // Auto-reload scripts on file change
    private int maxLoopIterations = 10000; // Maximum loop iterations
    private String scriptDirectory = "config/clish/scripts";

    private ClishConfig() {
        load();
    }

    /**
     * Get singleton instance.
     */
    public static ClishConfig getInstance() {
        if (instance == null) {
            instance = new ClishConfig();
        }
        return instance;
    }

    /**
     * Load configuration from file.
     */
    public void load() {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            System.err.println("Failed to create config directory: " + e.getMessage());
            return;
        }

        if (!Files.exists(CONFIG_FILE)) {
            save(); // Create default config
            return;
        }

        try {
            Properties props = new Properties();
            props.load(Files.newInputStream(CONFIG_FILE));

            timeoutMs = Long.parseLong(props.getProperty("timeout", "60000"));
            autoReload = Boolean.parseBoolean(props.getProperty("autoReload", "true"));
            maxLoopIterations = Integer.parseInt(props.getProperty("maxLoopIterations", "10000"));
            scriptDirectory = props.getProperty("scriptDirectory", "config/clish/scripts");

        } catch (Exception e) {
            System.err.println("Failed to load config: " + e.getMessage());
        }
    }

    /**
     * Save configuration to file.
     */
    public void save() {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            System.err.println("Failed to create config directory: " + e.getMessage());
            return;
        }

        Properties props = new Properties();
        props.setProperty("timeout", String.valueOf(timeoutMs));
        props.setProperty("autoReload", String.valueOf(autoReload));
        props.setProperty("maxLoopIterations", String.valueOf(maxLoopIterations));
        props.setProperty("scriptDirectory", scriptDirectory);

        try {
            props.store(Files.newOutputStream(CONFIG_FILE), "Clish Configuration");
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    // Getters and Setters

    public long getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
        save();
    }

    public boolean isAutoReload() {
        return autoReload;
    }

    public void setAutoReload(boolean autoReload) {
        this.autoReload = autoReload;
        save();
    }

    public int getMaxLoopIterations() {
        return maxLoopIterations;
    }

    public void setMaxLoopIterations(int maxLoopIterations) {
        this.maxLoopIterations = maxLoopIterations;
        save();
    }

    public String getScriptDirectory() {
        return scriptDirectory;
    }

    public void setScriptDirectory(String scriptDirectory) {
        this.scriptDirectory = scriptDirectory;
        save();
    }
}
