package net.clish.builtin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class RegexLibraryTest {

    @Test
    void testMatch() {
        var fn = new RegexLibrary.TestFunction();
        assertTrue((Boolean) fn.call(List.of("hello", "hel.*")));
        assertFalse((Boolean) fn.call(List.of("hello", "xyz.*")));
    }

    @Test
    void testContains() {
        var fn = new RegexLibrary.ContainsFunction();
        assertTrue((Boolean) fn.call(List.of("hello world", "world")));
        assertFalse((Boolean) fn.call(List.of("hello", "xyz")));
    }

    @Test
    void testFind() {
        var fn = new RegexLibrary.FindFunction();
        var result = (List<?>) fn.call(List.of("a1 b2 c3", "\\d"));
        assertEquals(3, result.size());
    }

    @Test
    void testReplace() {
        var fn = new RegexLibrary.ReplaceFunction();
        assertEquals("hello XYZ", fn.call(List.of("hello world", "world", "XYZ")));
    }

    @Test
    void testReplaceFirst() {
        var fn = new RegexLibrary.ReplaceFirstFunction();
        // replaceFirst replaces only the first match
        assertEquals("a2 b1 c1", fn.call(List.of("a1 b1 c1", "1", "2")));
    }

    @Test
    void testSplit() {
        var fn = new RegexLibrary.SplitFunction();
        // Java's split removes trailing empty strings by default
        var result = (List<?>) fn.call(List.of("a1b1c1", "\\d"));
        assertEquals(3, result.size());
    }

    @Test
    void testCount() {
        var fn = new RegexLibrary.CountFunction();
        assertEquals(3, fn.call(List.of("a1 b2 c3", "\\d")));
    }

    @Test
    void testEscape() {
        var fn = new RegexLibrary.EscapeFunction();
        String result = (String) fn.call(List.of("a.b"));
        assertTrue(result.startsWith("\\Q"));
    }
}
