package net.clish.ast;

import java.util.function.Consumer;

/**
 * Main script engine that ties together the lexer, parser, and interpreter.
 */
public class ScriptEngine {
    private final Interpreter interpreter;
    private long timeoutMs = 60000;
    private Consumer<String> outputConsumer;

    public ScriptEngine() {
        this.interpreter = new Interpreter();
        interpreter.setTimeout(timeoutMs);
    }

    /**
     * Execute a script from source code.
     *
     * @param source The script source code
     * @return The result of the last expression
     */
    public Object execute(String source) {
        Parser parser = new Parser(source);
        ProgramNode program = parser.parse();
        return interpreter.execute(program);
    }

    /**
     * Parse and validate a script without executing it.
     *
     * @param source The script source code
     * @return The parsed program AST
     */
    public ProgramNode parse(String source) {
        Parser parser = new Parser(source);
        return parser.parse();
    }

    /**
     * Set the execution timeout in milliseconds.
     */
    public void setTimeout(long timeoutMs) {
        this.timeoutMs = timeoutMs;
        interpreter.setTimeout(timeoutMs);
    }

    /**
     * Set the output consumer for library functions.
     * @param outputConsumer consumer that receives output strings
     */
    public void setOutputConsumer(Consumer<String> outputConsumer) {
        this.outputConsumer = outputConsumer;
        interpreter.setOutputConsumer(outputConsumer);
    }

    /**
     * Interrupt the currently running script.
     */
    public void interrupt() {
        interpreter.interrupt();
    }

    /**
     * Register a built-in library function.
     */
    public void registerLibrary(String name, ClishLibrary library) {
        interpreter.registerLibrary(name, library);
    }

    /**
     * Get the interpreter's global variables.
     */
    public java.util.Map<String, Object> getGlobalVariables() {
        return interpreter.getGlobalVariables();
    }
}
