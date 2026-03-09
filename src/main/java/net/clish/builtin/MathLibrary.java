package net.clish.builtin;

import net.clish.ast.ClishLibrary;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Math library for Clish.
 */
public class MathLibrary {
    private static final Random random = new Random();

    /**
     * Absolute value.
     */
    public static class AbsFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.abs";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.abs(value);
        }
    }

    /**
     * Floor - largest integer less than or equal to value.
     */
    public static class FloorFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.floor";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return (int) Math.floor(value);
        }
    }

    /**
     * Ceil - smallest integer greater than or equal to value.
     */
    public static class CeilFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.ceil";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return (int) Math.ceil(value);
        }
    }

    /**
     * Round - nearest integer.
     */
    public static class RoundFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.round";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return (int) Math.round(value);
        }
    }

    /**
     * Minimum of values.
     */
    public static class MinFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.min";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double min = Double.MAX_VALUE;
            for (Object arg : args) {
                double value = ((Number) arg).doubleValue();
                if (value < min) min = value;
            }
            return min;
        }
    }

    /**
     * Maximum of values.
     */
    public static class MaxFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.max";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double max = Double.MIN_VALUE;
            for (Object arg : args) {
                double value = ((Number) arg).doubleValue();
                if (value > max) max = value;
            }
            return max;
        }
    }

    /**
     * Random value between 0 and 1.
     */
    public static class RandomFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.random";
        }

        @Override
        public Object call(List<Object> args) {
            return random.nextDouble();
        }
    }

    /**
     * Random integer between min and max (inclusive).
     */
    public static class RandomIntFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.randomInt";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return random.nextInt();
            int min = ((Number) args.get(0)).intValue();
            int max = ((Number) args.get(1)).intValue();
            return random.nextInt(max - min + 1) + min;
        }
    }

    /**
     * Square root.
     */
    public static class SqrtFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.sqrt";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.sqrt(value);
        }
    }

    /**
     * Power - x raised to y.
     */
    public static class PowFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.pow";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 2) return 0;
            double base = ((Number) args.get(0)).doubleValue();
            double exponent = ((Number) args.get(1)).doubleValue();
            return Math.pow(base, exponent);
        }
    }

    /**
     * Sine (radians).
     */
    public static class SinFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.sin";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.sin(value);
        }
    }

    /**
     * Cosine (radians).
     */
    public static class CosFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.cos";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.cos(value);
        }
    }

    /**
     * Tangent (radians).
     */
    public static class TanFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.tan";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.tan(value);
        }
    }

    /**
     * Arc sine (radians).
     */
    public static class AsinFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.asin";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.asin(value);
        }
    }

    /**
     * Arc cosine (radians).
     */
    public static class AcosFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.acos";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.acos(value);
        }
    }

    /**
     * Arc tangent (radians).
     */
    public static class AtanFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.atan";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.atan(value);
        }
    }

    /**
     * Natural logarithm.
     */
    public static class LogFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.log";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.log(value);
        }
    }

    /**
     * Base 10 logarithm.
     */
    public static class Log10Function implements ClishLibrary {
        @Override
        public String getName() {
            return "math.log10";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.log10(value);
        }
    }

    /**
     * Converts degrees to radians.
     */
    public static class ToRadiansFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.toRadians";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.toRadians(value);
        }
    }

    /**
     * Converts radians to degrees.
     */
    public static class ToDegreesFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.toDegrees";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.isEmpty()) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            return Math.toDegrees(value);
        }
    }

    /**
     * Pi constant.
     */
    public static class PiFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.pi";
        }

        @Override
        public Object call(List<Object> args) {
            return Math.PI;
        }
    }

    /**
     * E constant.
     */
    public static class EFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.e";
        }

        @Override
        public Object call(List<Object> args) {
            return Math.E;
        }
    }

    /**
     * Clamp value between min and max.
     */
    public static class ClampFunction implements ClishLibrary {
        @Override
        public String getName() {
            return "math.clamp";
        }

        @Override
        public Object call(List<Object> args) {
            if (args.size() < 3) return 0;
            double value = ((Number) args.get(0)).doubleValue();
            double min = ((Number) args.get(1)).doubleValue();
            double max = ((Number) args.get(2)).doubleValue();
            return Math.max(min, Math.min(max, value));
        }
    }

    /**
     * Get all math functions.
     */
    public static List<ClishLibrary> getAll() {
        return Arrays.asList(
            new AbsFunction(),
            new FloorFunction(),
            new CeilFunction(),
            new RoundFunction(),
            new MinFunction(),
            new MaxFunction(),
            new RandomFunction(),
            new RandomIntFunction(),
            new SqrtFunction(),
            new PowFunction(),
            new SinFunction(),
            new CosFunction(),
            new TanFunction(),
            new AsinFunction(),
            new AcosFunction(),
            new AtanFunction(),
            new LogFunction(),
            new Log10Function(),
            new ToRadiansFunction(),
            new ToDegreesFunction(),
            new PiFunction(),
            new EFunction(),
            new ClampFunction()
        );
    }
}
