package net.clish;

import net.fabricmc.api.ModInitializer;
import net.clish.ast.ClishLibrary;
import net.clish.builtin.Builtins;
import net.clish.builtin.StringLibrary;
import net.clish.builtin.MathLibrary;
import net.clish.builtin.RegexLibrary;
import net.clish.builtin.TimeLibrary;
import net.clish.builtin.NbtLibrary;
import net.clish.api.PlayerApi;
import net.clish.api.BlockApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Clish implements ModInitializer {
    private static Clish instance;

    private final net.clish.ast.ScriptEngine scriptEngine;
    private ExecutorService executor;
    private Future<?> runningScript;

    public Clish() {
        instance = this;
        this.scriptEngine = new net.clish.ast.ScriptEngine();
        this.executor = Executors.newSingleThreadExecutor();

        // Register built-in libraries
        for (ClishLibrary lib : Builtins.getAll()) {
            scriptEngine.registerLibrary(lib.getName(), lib);
        }
        for (ClishLibrary lib : StringLibrary.getAll()) {
            scriptEngine.registerLibrary(lib.getName(), lib);
        }
        for (ClishLibrary lib : MathLibrary.getAll()) {
            scriptEngine.registerLibrary(lib.getName(), lib);
        }
        for (ClishLibrary lib : RegexLibrary.getAll()) {
            scriptEngine.registerLibrary(lib.getName(), lib);
        }
        for (ClishLibrary lib : TimeLibrary.getAll()) {
            scriptEngine.registerLibrary(lib.getName(), lib);
        }
        for (ClishLibrary lib : NbtLibrary.getAll()) {
            scriptEngine.registerLibrary(lib.getName(), lib);
        }
        for (ClishLibrary lib : PlayerApi.getAll()) {
            scriptEngine.registerLibrary(lib.getName(), lib);
        }
        for (ClishLibrary lib : BlockApi.getAll()) {
            scriptEngine.registerLibrary(lib.getName(), lib);
        }
    }

    @Override
    public void onInitialize() {
        System.out.println("Clish mod initialized!");
    }

    /**
     * Get the singleton instance.
     */
    public static Clish getInstance() {
        return instance;
    }

    /**
     * Execute a script asynchronously.
     */
    public void executeScript(String source, String[] args) {
        // TODO: Pass args to script
        runningScript = executor.submit(() -> {
            try {
                scriptEngine.execute(source);
            } catch (Exception e) {
                System.err.println("Script error: " + e.getMessage());
            }
        });
    }

    /**
     * Stop the running script.
     */
    public void stopScript() {
        if (runningScript != null) {
            runningScript.cancel(true);
            scriptEngine.interrupt();
        }
    }

    /**
     * Get the script engine.
     */
    public net.clish.ast.ScriptEngine getScriptEngine() {
        return scriptEngine;
    }
}
