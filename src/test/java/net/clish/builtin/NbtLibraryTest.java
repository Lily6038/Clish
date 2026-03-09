package net.clish.builtin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NbtLibraryTest {

    @Test
    void testCreate() {
        var fn = new NbtLibrary.CreateFunction();
        Object result = fn.call(List.of());
        assertTrue(result instanceof Map);
        assertTrue(((Map<?, ?>) result).isEmpty());
    }

    @Test
    void testGet() {
        var fn = new NbtLibrary.GetFunction();
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("name", "value");

        assertEquals("value", fn.call(List.of(nbt, "name")));
        assertNull(fn.call(List.of(nbt, "missing")));
    }

    @Test
    void testGetNested() {
        var fn = new NbtLibrary.GetFunction();
        Map<String, Object> inner = new HashMap<>();
        inner.put("key", "nestedValue");
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("outer", inner);

        assertEquals("nestedValue", fn.call(List.of(nbt, "outer.key")));
        assertNull(fn.call(List.of(nbt, "outer.missing")));
    }

    @Test
    void testSet() {
        var fn = new NbtLibrary.SetFunction();
        Map<String, Object> nbt = new HashMap<>();

        fn.call(List.of(nbt, "name", "value"));
        assertEquals("value", nbt.get("name"));
    }

    @Test
    void testSetNested() {
        var fn = new NbtLibrary.SetFunction();
        Map<String, Object> nbt = new HashMap<>();

        fn.call(List.of(nbt, "outer.inner", "nested"));
        assertTrue(nbt.get("outer") instanceof Map);
        assertEquals("nested", ((Map<String, Object>) nbt.get("outer")).get("inner"));
    }

    @Test
    void testRemove() {
        var fn = new NbtLibrary.RemoveFunction();
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("name", "value");

        fn.call(List.of(nbt, "name"));
        assertFalse(nbt.containsKey("name"));
    }

    @Test
    void testRemoveNested() {
        var fn = new NbtLibrary.RemoveFunction();
        Map<String, Object> inner = new HashMap<>();
        inner.put("key", "value");
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("outer", inner);

        fn.call(List.of(nbt, "outer.key"));
        assertFalse(((Map<String, Object>) nbt.get("outer")).containsKey("key"));
    }

    @Test
    void testHas() {
        var fn = new NbtLibrary.HasFunction();
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("name", "value");

        assertTrue((Boolean) fn.call(List.of(nbt, "name")));
        assertFalse((Boolean) fn.call(List.of(nbt, "missing")));
    }

    @Test
    void testHasNested() {
        var fn = new NbtLibrary.HasFunction();
        Map<String, Object> inner = new HashMap<>();
        inner.put("key", "value");
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("outer", inner);

        assertTrue((Boolean) fn.call(List.of(nbt, "outer.key")));
        assertFalse((Boolean) fn.call(List.of(nbt, "outer.missing")));
        assertFalse((Boolean) fn.call(List.of(nbt, "missing.key")));
    }

    @Test
    void testKeys() {
        var fn = new NbtLibrary.KeysFunction();
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("a", 1);
        nbt.put("b", 2);

        List<?> keys = (List<?>) fn.call(List.of(nbt));
        assertEquals(2, keys.size());
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
    }

    @Test
    void testKeysEmptyMap() {
        var fn = new NbtLibrary.KeysFunction();
        Map<String, Object> nbt = new HashMap<>();

        List<?> keys = (List<?>) fn.call(List.of(nbt));
        assertTrue(keys.isEmpty());
    }

    @Test
    void testValues() {
        var fn = new NbtLibrary.ValuesFunction();
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("a", 1);
        nbt.put("b", 2);

        List<?> values = (List<?>) fn.call(List.of(nbt));
        assertEquals(2, values.size());
    }

    @Test
    void testToString() {
        var fn = new NbtLibrary.ToStringFunction();
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("name", "value");

        String result = (String) fn.call(List.of(nbt));
        assertTrue(result.contains("name"));
        assertTrue(result.contains("value"));
    }

    @Test
    void testToStringEmpty() {
        var fn = new NbtLibrary.ToStringFunction();
        Map<String, Object> nbt = new HashMap<>();

        String result = (String) fn.call(List.of(nbt));
        assertEquals("{}", result);
    }

    @Test
    void testToStringNested() {
        var fn = new NbtLibrary.ToStringFunction();
        Map<String, Object> inner = new HashMap<>();
        inner.put("key", "value");
        Map<String, Object> nbt = new HashMap<>();
        nbt.put("outer", inner);

        String result = (String) fn.call(List.of(nbt));
        assertTrue(result.contains("outer"));
        assertTrue(result.contains("key"));
    }

    @Test
    void testTypeCompound() {
        var fn = new NbtLibrary.TypeFunction();
        Map<String, Object> nbt = new HashMap<>();

        assertEquals("compound", fn.call(List.of(nbt)));
    }

    @Test
    void testTypeString() {
        var fn = new NbtLibrary.TypeFunction();

        assertEquals("string", fn.call(List.of("hello")));
    }

    @Test
    void testTypeInt() {
        var fn = new NbtLibrary.TypeFunction();

        assertEquals("int", fn.call(List.of(42)));
    }

    @Test
    void testTypeDouble() {
        var fn = new NbtLibrary.TypeFunction();

        assertEquals("double", fn.call(List.of(3.14)));
    }

    @Test
    void testTypeBoolean() {
        var fn = new NbtLibrary.TypeFunction();

        // Boolean is not explicitly handled, returns "unknown"
        assertEquals("unknown", fn.call(List.of(true)));
    }

    @Test
    void testTypeList() {
        var fn = new NbtLibrary.TypeFunction();

        assertEquals("list", fn.call(List.of(List.of(1, 2, 3))));
    }

    @Test
    void testTypeNull() {
        var fn = new NbtLibrary.TypeFunction();

        assertEquals("null", fn.call(List.of()));
    }
}
