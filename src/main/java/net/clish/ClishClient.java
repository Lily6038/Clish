package net.clish;

import net.fabricmc.api.ClientModInitializer;
import net.clish.ast.ClishLibrary;
import net.clish.builtin.Builtins;
import net.clish.builtin.StringLibrary;
import net.clish.builtin.MathLibrary;
import net.clish.builtin.RegexLibrary;
import net.clish.builtin.TimeLibrary;
import net.clish.builtin.NbtLibrary;

/**
 * Client-only initialization for Clish.
 */
public class ClishClient implements ClientModInitializer {
    private static ClishClient instance;
    private net.clish.ast.ScriptEngine scriptEngine;

    @Override
    public void onInitializeClient() {
        System.out.println("[ClishClient] Starting initialization...");

        instance = this;

        // Initialize script engine and register libraries (client-side only)
        this.scriptEngine = new net.clish.ast.ScriptEngine();

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

        System.out.println("[ClishClient] Script engine initialized, registering commands...");

        // Register commands on client side
        ClishCommand.register();
        // Register keybinds
        ClishKeybinds.register();

        System.out.println("Clish mod initialized!");
    }

    /**
     * Get the singleton instance.
     */
    public static ClishClient getInstance() {
        return instance;
    }

    /**
     * Get the script engine.
     */
    public net.clish.ast.ScriptEngine getScriptEngine() {
        return scriptEngine;
    }
}
