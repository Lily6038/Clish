package net.clish.builtin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class TimeLibraryTest {

    @Test
    void testNow() {
        var fn = new TimeLibrary.NowFunction();
        long result = (Long) fn.call(List.of());
        long now = System.currentTimeMillis();
        assertTrue(Math.abs(result - now) < 1000);
    }

    @Test
    void testTimestamp() {
        var fn = new TimeLibrary.TimestampFunction();
        long result = (Long) fn.call(List.of());
        long now = System.currentTimeMillis() / 1000;
        assertTrue(Math.abs(result - now) < 2);
    }

    @Test
    void testFormat() {
        var fn = new TimeLibrary.FormatFunction();
        String result = (String) fn.call(List.of(0L, "yyyy-MM-dd"));
        assertEquals("1970-01-01", result);
    }

    @Test
    void testParse() {
        var fn = new TimeLibrary.ParseFunction();
        long result = (Long) fn.call(List.of("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
        assertTrue(result < 1000);
    }

    @Test
    void testDate() {
        var fn = new TimeLibrary.DateFunction();
        Map<?, ?> result = (Map<?, ?>) fn.call(List.of(0L));
        assertEquals(1970, result.get("year"));
        assertEquals(1, result.get("month"));
        assertEquals(1, result.get("day"));
    }

    @Test
    void testAdd() {
        var fn = new TimeLibrary.AddFunction();
        long result = (Long) fn.call(List.of(1000L, 1L, "second"));
        assertEquals(2000L, result);
    }

    @Test
    void testSubtract() {
        var fn = new TimeLibrary.SubtractFunction();
        long result = (Long) fn.call(List.of(2000L, 1L, "second"));
        assertEquals(1000L, result);
    }

    @Test
    void testDiff() {
        var fn = new TimeLibrary.DiffFunction();
        long result = (Long) fn.call(List.of(1000L, 2000L, "second"));
        assertEquals(1L, result);
    }
}
