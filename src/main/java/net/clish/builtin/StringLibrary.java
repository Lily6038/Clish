package net.clish.builtin;

import net.clish.ast.ClishLibrary;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * String manipulation library for Clish.
 */
public class StringLibrary {

    /**
     * Returns the length of a string.
     */
    public static class LengthFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.length";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            return args.get(0).toString().length();
        }
    }

    /**
     * Returns a substring of the given string.
     */
    public static class SubstringFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.substring";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "";
            String str = args.get(0).toString();
            int start = args.size() > 1 ? ((Number) args.get(1)).intValue() : 0;
            int end = args.size() > 2 ? ((Number) args.get(2)).intValue() : str.length();
            if (start < 0) start = 0;
            if (end > str.length()) end = str.length();
            if (start >= end) return "";
            return str.substring(start, end);
        }
    }

    /**
     * Returns the index of a substring.
     */
    public static class IndexOfFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.indexOf";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return -1;
            String str = args.get(0).toString();
            String search = args.get(1).toString();
            return str.indexOf(search);
        }
    }

    /**
     * Returns the last index of a substring.
     */
    public static class LastIndexOfFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.lastIndexOf";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return -1;
            String str = args.get(0).toString();
            String search = args.get(1).toString();
            return str.lastIndexOf(search);
        }
    }

    /**
     * Replaces occurrences of a substring with another.
     */
    public static class ReplaceFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.replace";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return args.isEmpty() ? "" : args.get(0).toString();
            String str = args.get(0).toString();
            String search = args.get(1).toString();
            String replacement = args.get(2).toString();
            return str.replace(search, replacement);
        }
    }

    /**
     * Replaces occurrences matching a regex pattern.
     */
    public static class ReplaceAllFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.replaceAll";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return args.isEmpty() ? "" : args.get(0).toString();
            String str = args.get(0).toString();
            String regex = args.get(1).toString();
            String replacement = args.get(2).toString();
            return str.replaceAll(regex, replacement);
        }
    }

    /**
     * Trims whitespace from both ends of a string.
     */
    public static class TrimFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.trim";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "";
            return args.get(0).toString().trim();
        }
    }

    /**
     * Converts string to uppercase.
     */
    public static class ToUpperCaseFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.toUpperCase";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "";
            return args.get(0).toString().toUpperCase();
        }
    }

    /**
     * Converts string to lowercase.
     */
    public static class ToLowerCaseFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.toLowerCase";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "";
            return args.get(0).toString().toLowerCase();
        }
    }

    /**
     * Splits a string by a delimiter.
     */
    public static class SplitFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.split";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return List.of();
            String str = args.get(0).toString();
            String delimiter = args.size() > 1 ? args.get(1).toString() : ",";
            int limit = args.size() > 2 ? ((Number) args.get(2)).intValue() : 0;
            if (limit > 0) {
                return Arrays.asList(str.split(Pattern.quote(delimiter), limit));
            }
            return Arrays.asList(str.split(Pattern.quote(delimiter)));
        }
    }

    /**
     * Joins array elements with a delimiter.
     */
    public static class JoinFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.join";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "";
            String delimiter = args.get(0).toString();
            if (args.size() < 2) return "";
            if (!(args.get(1) instanceof List)) {
                // Try to convert to list
                return String.join(delimiter, args.subList(1, args.size()).stream()
                    .map(Object::toString)
                    .toList());
            }
            List<?> list = (List<?>) args.get(1);
            return String.join(delimiter, list.stream()
                .map(Object::toString)
                .toList());
        }
    }

    /**
     * Checks if string starts with a prefix.
     */
    public static class StartsWithFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.startsWith";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return false;
            String str = args.get(0).toString();
            String prefix = args.get(1).toString();
            return str.startsWith(prefix);
        }
    }

    /**
     * Checks if string ends with a suffix.
     */
    public static class EndsWithFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.endsWith";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return false;
            String str = args.get(0).toString();
            String suffix = args.get(1).toString();
            return str.endsWith(suffix);
        }
    }

    /**
     * Checks if string contains a substring.
     */
    public static class ContainsFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.contains";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return false;
            String str = args.get(0).toString();
            String search = args.get(1).toString();
            return str.contains(search);
        }
    }

    /**
     * Returns character at specified index.
     */
    public static class CharAtFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.charAt";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return "";
            String str = args.get(0).toString();
            int index = ((Number) args.get(1)).intValue();
            if (index < 0 || index >= str.length()) return "";
            return String.valueOf(str.charAt(index));
        }
    }

    /**
     * Returns a repeated string.
     */
    public static class RepeatFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "string.repeat";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return "";
            String str = args.get(0).toString();
            int count = ((Number) args.get(1)).intValue();
            return str.repeat(Math.max(0, count));
        }
    }

    /**
     * Get all string functions.
     */
    public static List<ClishLibrary> getAll() {
        return Arrays.asList(
            new LengthFunction(),
            new SubstringFunction(),
            new IndexOfFunction(),
            new LastIndexOfFunction(),
            new ReplaceFunction(),
            new ReplaceAllFunction(),
            new TrimFunction(),
            new ToUpperCaseFunction(),
            new ToLowerCaseFunction(),
            new SplitFunction(),
            new JoinFunction(),
            new StartsWithFunction(),
            new EndsWithFunction(),
            new ContainsFunction(),
            new CharAtFunction(),
            new RepeatFunction()
        );
    }
}
