package net.clish.builtin;

import net.clish.ast.ClishLibrary;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Time library for Clish.
 */
public class TimeLibrary {

    /**
     * Get current timestamp in milliseconds.
     */
    public static class NowFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.now";
        }

        @Override
        public Object call(List<Object> args) {
            return System.currentTimeMillis();
        }
    }

    /**
     * Get current timestamp in seconds.
     */
    public static class TimestampFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.timestamp";
        }

        @Override
        public Object call(List<Object> args) {
            return System.currentTimeMillis() / 1000;
        }
    }

    /**
     * Sleep for specified milliseconds.
     */
    public static class SleepFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.sleep";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return null;
            long millis = ((Number) args.get(0)).longValue();
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return null;
        }
    }

    /**
     * Format timestamp to string.
     */
    public static class FormatFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.format";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return "";

            long timestamp;
            String pattern;
            String timezone = "UTC";

            if (args.get(0) instanceof Number) {
                timestamp = ((Number) args.get(0)).longValue();
                pattern = args.size() > 1 ? args.get(1).toString() : "yyyy-MM-dd HH:mm:ss";
                timezone = args.size() > 2 ? args.get(2).toString() : "UTC";
            } else {
                timestamp = System.currentTimeMillis();
                pattern = args.get(0).toString();
                timezone = args.size() > 1 ? args.get(1).toString() : "UTC";
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                if (!timezone.equals("UTC")) {
                    sdf.setTimeZone(TimeZone.getTimeZone(timezone));
                }
                return sdf.format(new Date(timestamp));
            } catch (Exception e) {
                return new Date(timestamp).toString();
            }
        }
    }

    /**
     * Parse string to timestamp.
     */
    public static class ParseFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.parse";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0L;

            String dateStr = args.get(0).toString();
            String pattern = args.size() > 1 ? args.get(1).toString() : "yyyy-MM-dd HH:mm:ss";

            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                return sdf.parse(dateStr).getTime();
            } catch (Exception e) {
                // Try parsing as epoch milliseconds
                try {
                    return Long.parseLong(dateStr);
                } catch (NumberFormatException ex) {
                    return 0L;
                }
            }
        }
    }

    /**
     * Get current date/time as components.
     */
    public static class DateFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.date";
        }

        @Override
        public Object call(List<Object> args) {
            long timestamp = args.isEmpty() ? System.currentTimeMillis() : ((Number) args.get(0)).longValue();
            Date date = new Date(timestamp);

            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("year", date.getYear() + 1900);
            result.put("month", date.getMonth() + 1);
            result.put("day", date.getDate());
            result.put("hour", date.getHours());
            result.put("minute", date.getMinutes());
            result.put("second", date.getSeconds());
            result.put("dayOfWeek", date.getDay());
            result.put("timestamp", timestamp);

            return result;
        }
    }

    /**
     * Add time to a timestamp.
     */
    public static class AddFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.add";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return 0L;

            long timestamp = ((Number) args.get(0)).longValue();
            long amount = ((Number) args.get(1)).longValue();
            String unit = args.get(2).toString().toLowerCase();

            return switch (unit) {
                case "ms", "millis", "millisecond" -> timestamp + amount;
                case "s", "sec", "second" -> timestamp + (amount * 1000);
                case "m", "min", "minute" -> timestamp + (amount * 60 * 1000);
                case "h", "hour" -> timestamp + (amount * 60 * 60 * 1000);
                case "d", "day" -> timestamp + (amount * 24 * 60 * 60 * 1000);
                case "w", "week" -> timestamp + (amount * 7 * 24 * 60 * 60 * 1000);
                default -> timestamp;
            };
        }
    }

    /**
     * Subtract time from a timestamp.
     */
    public static class SubtractFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.subtract";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return 0L;

            long timestamp = ((Number) args.get(0)).longValue();
            long amount = ((Number) args.get(1)).longValue();
            String unit = args.get(2).toString().toLowerCase();

            return switch (unit) {
                case "ms", "millis", "millisecond" -> timestamp - amount;
                case "s", "sec", "second" -> timestamp - (amount * 1000);
                case "m", "min", "minute" -> timestamp - (amount * 60 * 1000);
                case "h", "hour" -> timestamp - (amount * 60 * 60 * 1000);
                case "d", "day" -> timestamp - (amount * 24 * 60 * 60 * 1000);
                case "w", "week" -> timestamp - (amount * 7 * 24 * 60 * 60 * 1000);
                default -> timestamp;
            };
        }
    }

    /**
     * Get time unit difference between two timestamps.
     */
    public static class DiffFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.diff";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return 0L;

            long timestamp1 = ((Number) args.get(0)).longValue();
            long timestamp2 = ((Number) args.get(1)).longValue();
            String unit = args.get(2).toString().toLowerCase();

            long diff = Math.abs(timestamp2 - timestamp1);

            return switch (unit) {
                case "ms", "millis", "millisecond" -> diff;
                case "s", "sec", "second" -> diff / 1000;
                case "m", "min", "minute" -> diff / (60 * 1000);
                case "h", "hour" -> diff / (60 * 60 * 1000);
                case "d", "day" -> diff / (24 * 60 * 60 * 1000);
                case "w", "week" -> diff / (7 * 24 * 60 * 60 * 1000);
                default -> diff;
            };
        }
    }

    /**
     * Get current UTC date/time formatted.
     */
    public static class UtcFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "time.utc";
        }

        @Override
        public Object call(List<Object> args) {
            return Instant.now().atZone(ZoneId.of("UTC"))
                .format(DateTimeFormatter.ISO_INSTANT);
        }
    }

    /**
     * Get all time functions.
     */
    public static List<ClishLibrary> getAll() {
        return Arrays.asList(
            new NowFunction(),
            new TimestampFunction(),
            new SleepFunction(),
            new FormatFunction(),
            new ParseFunction(),
            new DateFunction(),
            new AddFunction(),
            new SubtractFunction(),
            new DiffFunction(),
            new UtcFunction()
        );
    }
}
