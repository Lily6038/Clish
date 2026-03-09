package net.clish.builtin;

import net.clish.ast.ClishLibrary;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Regex library for Clish.
 */
public class RegexLibrary {

    /**
     * Test if a string matches a regex pattern.
     */
    public static class TestFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.test";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return false;
            try {
                String str = args.get(0).toString();
                String regex = args.get(1).toString();
                Pattern pattern = Pattern.compile(regex);
                return pattern.matcher(str).matches();
            } catch (PatternSyntaxException e) {
                return false;
            }
        }
    }

    /**
     * Check if a pattern matches anywhere in the string.
     */
    public static class ContainsFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.contains";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return false;
            try {
                String str = args.get(0).toString();
                String regex = args.get(1).toString();
                Pattern pattern = Pattern.compile(regex);
                return pattern.matcher(str).find();
            } catch (PatternSyntaxException e) {
                return false;
            }
        }
    }

    /**
     * Find all matches and return as array.
     */
    public static class FindFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.find";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return List.of();
            try {
                String str = args.get(0).toString();
                String regex = args.get(1).toString();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);

                List<String> matches = new java.util.ArrayList<>();
                while (matcher.find()) {
                    matches.add(matcher.group());
                }
                return matches;
            } catch (PatternSyntaxException e) {
                return List.of();
            }
        }
    }

    /**
     * Find and return match groups.
     */
    public static class FindGroupsFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.findGroups";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return List.of();
            try {
                String str = args.get(0).toString();
                String regex = args.get(1).toString();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);

                if (matcher.find()) {
                    List<String> groups = new java.util.ArrayList<>();
                    for (int i = 0; i <= matcher.groupCount(); i++) {
                        groups.add(matcher.group(i));
                    }
                    return groups;
                }
                return List.of();
            } catch (PatternSyntaxException e) {
                return List.of();
            }
        }
    }

    /**
     * Replace all matches.
     */
    public static class ReplaceFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.replace";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return args.isEmpty() ? "" : args.get(0).toString();
            try {
                String str = args.get(0).toString();
                String regex = args.get(1).toString();
                String replacement = args.get(2).toString();
                Pattern pattern = Pattern.compile(regex);
                return pattern.matcher(str).replaceAll(replacement);
            } catch (PatternSyntaxException e) {
                return args.get(0).toString();
            }
        }
    }

    /**
     * Replace first match only.
     */
    public static class ReplaceFirstFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.replaceFirst";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return args.isEmpty() ? "" : args.get(0).toString();
            try {
                String str = args.get(0).toString();
                String regex = args.get(1).toString();
                String replacement = args.get(2).toString();
                Pattern pattern = Pattern.compile(regex);
                return pattern.matcher(str).replaceFirst(replacement);
            } catch (PatternSyntaxException e) {
                return args.get(0).toString();
            }
        }
    }

    /**
     * Split string by regex.
     */
    public static class SplitFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.split";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return List.of();
            try {
                String str = args.get(0).toString();
                String regex = args.get(1).toString();
                int limit = args.size() > 2 ? ((Number) args.get(2)).intValue() : 0;

                if (limit > 0) {
                    return Arrays.asList(str.split(regex, limit));
                }
                return Arrays.asList(str.split(regex));
            } catch (PatternSyntaxException e) {
                return args.isEmpty() ? List.of() : List.of(args.get(0).toString());
            }
        }
    }

    /**
     * Get match count.
     */
    public static class CountFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.count";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return 0;
            try {
                String str = args.get(0).toString();
                String regex = args.get(1).toString();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(str);

                int count = 0;
                while (matcher.find()) {
                    count++;
                }
                return count;
            } catch (PatternSyntaxException e) {
                return 0;
            }
        }
    }

    /**
     * Escape regex special characters.
     */
    public static class EscapeFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "regex.escape";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "";
            return Pattern.quote(args.get(0).toString());
        }
    }

    /**
     * Get all regex functions.
     */
    public static List<ClishLibrary> getAll() {
        return Arrays.asList(
            new TestFunction(),
            new ContainsFunction(),
            new FindFunction(),
            new FindGroupsFunction(),
            new ReplaceFunction(),
            new ReplaceFirstFunction(),
            new SplitFunction(),
            new CountFunction(),
            new EscapeFunction()
        );
    }
}
