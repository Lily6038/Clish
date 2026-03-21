package net.clish.builtin;

import net.clish.ast.ClishLibrary;
import net.clish.runtime.ResultType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Built-in shell commands for Clish.
 */
public class Builtins {

    private static Consumer<String> outputConsumer;

    /**
     * Set the output consumer for built-in print functions.
     * @param consumer the consumer that receives output strings
     */
    public static void setOutputConsumer(Consumer<String> consumer) {
        outputConsumer = consumer;
    }

    /**
     * Echo command - prints arguments to output.
     */
    public static class EchoFunction implements ClishLibrary {
        private Consumer<String> outputConsumer;

        @Override
        public String getName() {
            return "echo";
        }

        @Override
        public Object call(List<Object> args) {
            String output = args.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
            sendOutput(output);
            return output;  // Return output like unix echo
        }

        @Override
        public void setOutputConsumer(Consumer<String> consumer) {
            this.outputConsumer = consumer;
        }

        private void sendOutput(String output) {
            if (outputConsumer != null) {
                outputConsumer.accept(output);
            } else if (Builtins.outputConsumer != null) {
                Builtins.outputConsumer.accept(output);
            } else {
                System.out.println(output);
            }
        }
    }

    /**
     * Print command - like echo but for debugging.
     */
    public static class PrintFunction implements ClishLibrary {
        private Consumer<String> outputConsumer;

        @Override
        public String getName() {
            return "print";
        }

        @Override
        public Object call(List<Object> args) {
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(arg);
            }
            sendOutput(sb.toString());
            return null;
        }

        @Override
        public void setOutputConsumer(Consumer<String> consumer) {
            this.outputConsumer = consumer;
        }

        private void sendOutput(String output) {
            if (outputConsumer != null) {
                outputConsumer.accept(output);
            } else if (Builtins.outputConsumer != null) {
                Builtins.outputConsumer.accept(output);
            } else {
                System.out.println(output);
            }
        }
    }

    /**
     * Printf command - formatted printing.
     */
    public static class PrintfFunction implements ClishLibrary {
        private Consumer<String> outputConsumer;

        @Override
        public String getName() {
            return "printf";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return null;
            String format = args.get(0).toString();
            Object[] formatArgs = args.subList(1, args.size()).toArray();
            String output = String.format(format, formatArgs);
            sendOutput(output);
            return null;
        }

        @Override
        public void setOutputConsumer(Consumer<String> consumer) {
            this.outputConsumer = consumer;
        }

        private void sendOutput(String output) {
            if (outputConsumer != null) {
                outputConsumer.accept(output);
            } else if (Builtins.outputConsumer != null) {
                Builtins.outputConsumer.accept(output);
            } else {
                System.out.println(output);
            }
        }
    }

    /**
     * Len function - returns length of string/array/map.
     */
    public static class LenFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "len";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            Object arg = args.get(0);
            if (arg instanceof String s) return s.length();
            if (arg instanceof List l) return l.size();
            if (arg instanceof java.util.Map m) return m.size();
            if (arg instanceof Object[] a) return a.length;
            return 0;
        }
    }

    /**
     * Type function - returns type of value.
     */
    public static class TypeFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "type";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "null";
            Object arg = args.get(0);
            if (arg == null) return "null";
            if (arg instanceof Integer) return "int";
            if (arg instanceof Double) return "float";
            if (arg instanceof Boolean) return "bool";
            if (arg instanceof String) return "string";
            if (arg instanceof List) return "array";
            if (arg instanceof java.util.Map) return "object";
            return arg.getClass().getSimpleName().toLowerCase();
        }
    }

    /**
     * ToString function - converts value to string.
     */
    public static class ToStringFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "toString";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "";
            return args.get(0).toString();
        }
    }

    /**
     * ToInt function - converts value to integer.
     */
    public static class ToIntFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "toInt";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            Object arg = args.get(0);
            if (arg instanceof Number n) return n.intValue();
            try {
                return Integer.parseInt(arg.toString());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    /**
     * ToFloat function - converts value to float.
     */
    public static class ToFloatFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "toFloat";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0.0;
            Object arg = args.get(0);
            if (arg instanceof Number n) return n.doubleValue();
            try {
                return Double.parseDouble(arg.toString());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }

    /**
     * IsOk function - returns true if result is Ok.
     */
    public static class IsOkFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "isOk";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return false;
            Object arg = args.get(0);
            if (arg instanceof ResultType rt) {
                return rt.isOk();
            }
            return false;
        }
    }

    /**
     * IsError function - returns true if result is Error.
     */
    public static class IsErrorFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "isError";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return false;
            Object arg = args.get(0);
            if (arg instanceof ResultType rt) {
                return rt.isError();
            }
            return false;
        }
    }

    /**
     * Unwrap function - returns value or throws if error.
     */
    public static class UnwrapFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "unwrap";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) throw new RuntimeException("unwrap requires 1 argument");
            Object arg = args.get(0);
            if (arg instanceof ResultType rt) {
                if (rt.isError()) {
                    throw new RuntimeException("Cannot unwrap error: " + rt.getMessage());
                }
                return rt.getValue();
            }
            return arg;
        }
    }

    /**
     * UnwrapOr function - returns value or default if error.
     */
    public static class UnwrapOrFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "unwrapOr";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) throw new RuntimeException("unwrapOr requires 2 arguments");
            Object arg = args.get(0);
            Object defaultValue = args.get(1);
            if (arg instanceof ResultType rt) {
                if (rt.isOk()) {
                    return rt.getValue();
                }
                return defaultValue;
            }
            return arg;
        }
    }

    /**
     * Exit function - exits the script.
     */
    public static class ExitFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "exit";
        }

        @Override
        public Object call(List<Object> args) {
            int code = args.isEmpty() ? 0 : ((Number) args.get(0)).intValue();
            throw new ExitException(code);
        }
    }

    /**
     * Exception for script exit.
     */
    public static class ExitException extends RuntimeException {
        private final int exitCode;

        public ExitException(int exitCode) {
            super(null, null, true, false);
            this.exitCode = exitCode;
        }

        public int getExitCode() {
            return exitCode;
        }
    }

    /**
     * Get all built-in functions.
     */
    public static List<ClishLibrary> getAll() {
        return Arrays.asList(
            new EchoFunction(),
            new PrintFunction(),
            new PrintfFunction(),
            new LenFunction(),
            new TypeFunction(),
            new ToStringFunction(),
            new ToIntFunction(),
            new ToFloatFunction(),
            new IsOkFunction(),
            new IsErrorFunction(),
            new UnwrapFunction(),
            new UnwrapOrFunction(),
            new ExitFunction()
        );
    }
}
