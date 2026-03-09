package net.clish.builtin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class StringLibraryTest {

    @Test
    void testLength() {
        var fn = new StringLibrary.LengthFunction();
        assertEquals(5, fn.call(List.of("hello")));
        assertEquals(0, fn.call(List.of("")));
    }

    @ParameterizedTest
    @CsvSource({
        "hello, 0, 3, hel",
        "hello, 1, 4, ell",
        "hello, 0, 3, hel",
        "hello, 0, 10, hello"
    })
    void testSubstring(String input, int start, int end, String expected) {
        var fn = new StringLibrary.SubstringFunction();
        assertEquals(expected, fn.call(List.of(input, start, end)));
    }

    @Test
    void testIndexOf() {
        var fn = new StringLibrary.IndexOfFunction();
        assertEquals(0, fn.call(List.of("hello", "hel")));
        assertEquals(-1, fn.call(List.of("hello", "xyz")));
    }

    @Test
    void testReplace() {
        var fn = new StringLibrary.ReplaceFunction();
        assertEquals("hello there", fn.call(List.of("hello world", "world", "there")));
    }

    @Test
    void testTrim() {
        var fn = new StringLibrary.TrimFunction();
        assertEquals("hello", fn.call(List.of("  hello  ")));
    }

    @Test
    void testToUpperCase() {
        var fn = new StringLibrary.ToUpperCaseFunction();
        assertEquals("HELLO", fn.call(List.of("hello")));
    }

    @Test
    void testToLowerCase() {
        var fn = new StringLibrary.ToLowerCaseFunction();
        assertEquals("hello", fn.call(List.of("HELLO")));
    }

    @Test
    void testSplit() {
        var fn = new StringLibrary.SplitFunction();
        assertArrayEquals(new String[]{"a", "b", "c"},
            ((List<?>)fn.call(List.of("a,b,c", ","))).toArray());
    }

    @Test
    void testContains() {
        var fn = new StringLibrary.ContainsFunction();
        assertTrue((Boolean) fn.call(List.of("hello world", "world")));
        assertFalse((Boolean) fn.call(List.of("hello", "xyz")));
    }

    @Test
    void testStartsWith() {
        var fn = new StringLibrary.StartsWithFunction();
        assertTrue((Boolean) fn.call(List.of("hello", "hel")));
        assertFalse((Boolean) fn.call(List.of("hello", "world")));
    }

    @Test
    void testEndsWith() {
        var fn = new StringLibrary.EndsWithFunction();
        assertTrue((Boolean) fn.call(List.of("hello", "llo")));
        assertFalse((Boolean) fn.call(List.of("hello", "hel")));
    }
}
