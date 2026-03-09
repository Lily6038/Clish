package net.clish.builtin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class MathLibraryTest {

    @Test
    void testAbs() {
        var fn = new MathLibrary.AbsFunction();
        assertEquals(5.0, fn.call(List.of(-5)));
        assertEquals(5.0, fn.call(List.of(5)));
    }

    @Test
    void testFloor() {
        var fn = new MathLibrary.FloorFunction();
        assertEquals(3, fn.call(List.of(3.7)));
        assertEquals(-4, fn.call(List.of(-3.7)));
    }

    @Test
    void testCeil() {
        var fn = new MathLibrary.CeilFunction();
        assertEquals(4, fn.call(List.of(3.2)));
        assertEquals(-3, fn.call(List.of(-3.7)));
    }

    @Test
    void testRound() {
        var fn = new MathLibrary.RoundFunction();
        assertEquals(4, fn.call(List.of(3.7)));
        assertEquals(4, fn.call(List.of(3.5)));
    }

    @Test
    void testMin() {
        var fn = new MathLibrary.MinFunction();
        assertEquals(1.0, fn.call(List.of(1, 2, 3)));
    }

    @Test
    void testMax() {
        var fn = new MathLibrary.MaxFunction();
        assertEquals(3.0, fn.call(List.of(1, 2, 3)));
    }

    @Test
    void testSqrt() {
        var fn = new MathLibrary.SqrtFunction();
        assertEquals(4.0, fn.call(List.of(16)));
        assertEquals(2.0, fn.call(List.of(4)));
    }

    @Test
    void testPow() {
        var fn = new MathLibrary.PowFunction();
        assertEquals(8.0, fn.call(List.of(2, 3)));
        assertEquals(1.0, fn.call(List.of(5, 0)));
    }

    @Test
    void testPi() {
        var fn = new MathLibrary.PiFunction();
        assertEquals(Math.PI, fn.call(List.of()));
    }

    @Test
    void testE() {
        var fn = new MathLibrary.EFunction();
        assertEquals(Math.E, fn.call(List.of()));
    }

    @Test
    void testClamp() {
        var fn = new MathLibrary.ClampFunction();
        assertEquals(5.0, fn.call(List.of(5, 0, 10)));
        assertEquals(0.0, fn.call(List.of(-5, 0, 10)));
        assertEquals(10.0, fn.call(List.of(15, 0, 10)));
    }

    @Test
    void testLog() {
        var fn = new MathLibrary.LogFunction();
        assertEquals(1.0, fn.call(List.of(Math.E)));
    }

    @Test
    void testLog10() {
        var fn = new MathLibrary.Log10Function();
        assertEquals(1.0, fn.call(List.of(10)));
        assertEquals(2.0, fn.call(List.of(100)));
    }

    @Test
    void testToRadians() {
        var fn = new MathLibrary.ToRadiansFunction();
        assertEquals(Math.PI, fn.call(List.of(180)));
    }

    @Test
    void testToDegrees() {
        var fn = new MathLibrary.ToDegreesFunction();
        assertEquals(180.0, fn.call(List.of(Math.PI)));
    }
}
