package net.clish.builtin;

import net.clish.ast.ClishLibrary;

import java.util.Arrays;
import java.util.List;

/**
 * NBT (Named Binary Tag) library for Clish.
 * Provides functions to read and manipulate NBT data.
 *
 * Note: This is a basic implementation. Full NBT support requires
 * integration with Minecraft's NBT system.
 */
public class NbtLibrary {

    /**
     * Create a new empty NBT compound.
     */
    public static class CreateFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.create";
        }

        @Override
        public Object call(List<Object> args) {
            return new java.util.HashMap<String, Object>();
        }
    }

    /**
     * Get a value from NBT by path.
     */
    public static class GetFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.get";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return null;

            Object nbt = args.get(0);
            String path = args.get(1).toString();

            if (!(nbt instanceof java.util.Map)) return null;

            java.util.Map<String, Object> compound = (java.util.Map<String, Object>) nbt;
            return getPath(compound, path);
        }

        private Object getPath(java.util.Map<String, Object> compound, String path) {
            String[] parts = path.split("\\.", 2);
            String key = parts[0];

            if (!compound.containsKey(key)) return null;

            if (parts.length == 1) {
                return compound.get(key);
            }

            // Navigate deeper
            Object value = compound.get(key);
            if (value instanceof java.util.Map) {
                return getPath((java.util.Map<String, Object>) value, parts[1]);
            }

            return null;
        }
    }

    /**
     * Set a value in NBT by path.
     */
    public static class SetFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.set";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return null;

            Object nbt = args.get(0);
            String path = args.get(1).toString();
            Object value = args.get(2);

            if (!(nbt instanceof java.util.Map)) return null;

            java.util.Map<String, Object> compound = (java.util.Map<String, Object>) nbt;
            setPath(compound, path, value);
            return compound;
        }

        private void setPath(java.util.Map<String, Object> compound, String path, Object value) {
            String[] parts = path.split("\\.", 2);
            String key = parts[0];

            if (parts.length == 1) {
                compound.put(key, value);
                return;
            }

            // Navigate or create nested compound
            java.util.Map<String, Object> nested;
            if (compound.containsKey(key) && compound.get(key) instanceof java.util.Map) {
                nested = (java.util.Map<String, Object>) compound.get(key);
            } else {
                nested = new java.util.HashMap<>();
                compound.put(key, nested);
            }

            setPath(nested, parts[1], value);
        }
    }

    /**
     * Remove a value from NBT by path.
     */
    public static class RemoveFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.remove";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return null;

            Object nbt = args.get(0);
            String path = args.get(1).toString();

            if (!(nbt instanceof java.util.Map)) return null;

            java.util.Map<String, Object> compound = (java.util.Map<String, Object>) nbt;
            removePath(compound, path);
            return compound;
        }

        private void removePath(java.util.Map<String, Object> compound, String path) {
            String[] parts = path.split("\\.", 2);
            String key = parts[0];

            if (parts.length == 1) {
                compound.remove(key);
                return;
            }

            if (compound.containsKey(key) && compound.get(key) instanceof java.util.Map) {
                removePath((java.util.Map<String, Object>) compound.get(key), parts[1]);
            }
        }
    }

    /**
     * Check if NBT contains a path.
     */
    public static class HasFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.has";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return false;

            Object nbt = args.get(0);
            String path = args.get(1).toString();

            if (!(nbt instanceof java.util.Map)) return false;

            java.util.Map<String, Object> compound = (java.util.Map<String, Object>) nbt;
            return hasPath(compound, path);
        }

        private boolean hasPath(java.util.Map<String, Object> compound, String path) {
            String[] parts = path.split("\\.", 2);
            String key = parts[0];

            if (!compound.containsKey(key)) return false;

            if (parts.length == 1) return true;

            Object value = compound.get(key);
            return value instanceof java.util.Map && hasPath((java.util.Map<String, Object>) value, parts[1]);
        }
    }

    /**
     * Get keys of NBT compound.
     */
    public static class KeysFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.keys";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return List.of();

            Object nbt = args.get(0);
            if (!(nbt instanceof java.util.Map)) return List.of();

            return new java.util.ArrayList<>(((java.util.Map<?, ?>) nbt).keySet());
        }
    }

    /**
     * Get values of NBT compound.
     */
    public static class ValuesFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.values";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return List.of();

            Object nbt = args.get(0);
            if (!(nbt instanceof java.util.Map)) return List.of();

            return new java.util.ArrayList<>(((java.util.Map<?, ?>) nbt).values());
        }
    }

    /**
     * Get NBT as JSON string.
     */
    public static class ToStringFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.toString";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "{}";

            Object nbt = args.get(0);
            if (!(nbt instanceof java.util.Map)) return "{}";

            // Simple JSON-like serialization
            return toJsonString((java.util.Map<String, Object>) nbt, 0);
        }

        private String toJsonString(java.util.Map<String, Object> map, int indent) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            String indentStr = "  ".repeat(indent);

            int i = 0;
            for (var entry : map.entrySet()) {
                if (i > 0) sb.append(", ");
                sb.append("\"").append(entry.getKey()).append("\": ");
                sb.append(valueToString(entry.getValue(), indent + 1));
                i++;
            }

            sb.append("}");
            return sb.toString();
        }

        private String valueToString(Object value, int indent) {
            if (value == null) return "null";
            if (value instanceof Number || value instanceof Boolean) return value.toString();
            if (value instanceof java.util.Map) {
                return toJsonString((java.util.Map<String, Object>) value, indent);
            }
            if (value instanceof List) {
                return listToString((List<?>) value, indent);
            }
            return "\"" + escapeString(value.toString()) + "\"";
        }

        private String listToString(List<?> list, int indent) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(valueToString(list.get(i), indent + 1));
            }
            sb.append("]");
            return sb.toString();
        }

        private String escapeString(String s) {
            return s.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
        }
    }

    /**
     * Get NBT type as string.
     */
    public static class TypeFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "nbt.type";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "null";

            Object nbt = args.get(0);
            if (nbt == null) return "null";
            if (nbt instanceof java.util.Map) return "compound";
            if (nbt instanceof List) return "list";
            if (nbt instanceof Number) {
                Number num = (Number) nbt;
                if (num instanceof Byte || num instanceof Short || num instanceof Integer) return "int";
                if (num instanceof Long) return "long";
                if (num instanceof Float || num instanceof Double) return "double";
                return "number";
            }
            if (nbt instanceof String) return "string";
            if (nbt instanceof byte[]) return "byteArray";
            if (nbt instanceof int[]) return "intArray";
            if (nbt instanceof long[]) return "longArray";

            return "unknown";
        }
    }

    /**
     * Get all NBT functions.
     */
    public static List<ClishLibrary> getAll() {
        return Arrays.asList(
            new CreateFunction(),
            new GetFunction(),
            new SetFunction(),
            new RemoveFunction(),
            new HasFunction(),
            new KeysFunction(),
            new ValuesFunction(),
            new ToStringFunction(),
            new TypeFunction()
        );
    }
}
