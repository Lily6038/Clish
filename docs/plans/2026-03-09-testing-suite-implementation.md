# Testing Suite Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development to implement this plan task-by-task.

**Goal:** Create comprehensive JUnit 5 test suite for the clish Minecraft mod in `src/test/java/`

**Architecture:** Add JUnit 5 dependency to build.gradle, create test classes mirroring the main source structure in src/test/java/

**Tech Stack:** JUnit 5, Gradle (built-in test support)

---

## Task 1: Add JUnit 5 dependency to build.gradle

**Files:**
- Modify: `build.gradle`

**Step 1: Add JUnit 5 test dependency**

```groovy
dependencies {
    // ... existing dependencies ...

    // Test dependencies
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

**Step 2: Verify build.gradle is valid**

Run: `./gradlew dependencies --configuration testRuntimeClasspath 2>&1 | head -30`
Expected: See junit-jupiter in output

---

## Task 2: Create Lexer test class

**Files:**
- Create: `src/test/java/net/clish/lexer/LexerTest.java`

**Step 1: Write failing tests**

```java
package net.clish.lexer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void testKeywords() {
        Lexer lexer = new Lexer("if while for");
        var tokens = lexer.tokenize();
        assertEquals(3, tokens.size());
        assertEquals(TokenType.IF, tokens.get(0).getType());
        assertEquals(TokenType.WHILE, tokens.get(1).getType());
        assertEquals(TokenType.FOR, tokens.get(2).getType());
    }

    @Test
    void testOperators() {
        Lexer lexer = new Lexer("+ - * / = == != < <= > >= && ||");
        var tokens = lexer.tokenize();
        assertEquals(12, tokens.size());
    }

    @Test
    void testStringLiteral() {
        Lexer lexer = new Lexer("\"hello world\"");
        var tokens = lexer.tokenize();
        assertEquals(1, tokens.size());
        assertEquals(TokenType.STRING, tokens.get(0).getType());
        assertEquals("hello world", tokens.get(0).getLiteral());
    }

    @Test
    void testNumberLiteral() {
        Lexer lexer = new Lexer("42 3.14");
        var tokens = lexer.tokenize();
        assertEquals(2, tokens.size());
        assertEquals("42", tokens.get(0).getLiteral());
        assertEquals("3.14", tokens.get(1).getLiteral());
    }

    @Test
    void testIdentifier() {
        Lexer lexer = new Lexer("myVar _private count1");
        var tokens = lexer.tokenize();
        assertEquals(3, tokens.size());
        assertEquals(TokenType.IDENTIFIER, tokens.get(0).getType());
    }
}
```

**Step 2: Run tests to verify they fail**

Run: `./gradlew test --tests "net.clish.lexer.LexerTest" 2>&1`
Expected: Tests run and pass (or fail if implementation has bugs)

**Step 3: Commit**

---

## Task 3: Create Parser test class

**Files:**
- Create: `src/test/java/net/clish/ast/ParserTest.java`

**Step 1: Write failing tests**

```java
package net.clish.ast;

import net.clish.lexer.Lexer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void testParseNumberLiteral() {
        Parser parser = new Parser("42");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
        assertTrue(program.getStatements().get(0) instanceof ExpressionStatementNode);
    }

    @Test
    void testParseStringLiteral() {
        Parser parser = new Parser("\"hello\"");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseVariableDeclaration() {
        Parser parser = new Parser("local x = 5");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseIfStatement() {
        Parser parser = new Parser("if (x == 1) { echo hello }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseWhileStatement() {
        Parser parser = new Parser("while (x < 10) { x = x + 1 }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseForStatement() {
        Parser parser = new Parser("for (local i = 0; i < 10; i = i + 1) { echo i }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseFunctionDeclaration() {
        Parser parser = new Parser("function add(a, b) { return a + b }");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }

    @Test
    void testParseBinaryExpression() {
        Parser parser = new Parser("a + b * c");
        ProgramNode program = parser.parse();
        assertEquals(1, program.getStatements().size());
    }
}
```

**Step 2: Run tests**

Run: `./gradlew test --tests "net.clish.ast.ParserTest" 2>&1`

**Step 3: Commit**

---

## Task 4: Create Interpreter test class

**Files:**
- Create: `src/test/java/net/clish/ast/InterpreterTest.java`

**Step 1: Write failing tests**

```java
package net.clish.ast;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InterpreterTest {

    private Object runScript(String script) {
        Parser parser = new Parser(script);
        ProgramNode program = parser.parse();
        Interpreter interpreter = new Interpreter();
        return interpreter.execute(program);
    }

    @Test
    void testVariableDeclaration() {
        Parser parser = new Parser("local x = 5");
        ProgramNode program = parser.parse();
        Interpreter interpreter = new Interpreter();
        interpreter.execute(program);
    }

    @Test
    void testArithmeticOperations() {
        assertEquals(10.0, runScript("5 + 5"));
        assertEquals(2.0, runScript("10 - 8"));
        assertEquals(6.0, runScript("3 * 2"));
        assertEquals(2.0, runScript("10 / 5"));
    }

    @Test
    void testComparisonOperations() {
        assertEquals(true, runScript("5 == 5"));
        assertEquals(false, runScript("5 != 5"));
        assertEquals(true, runScript("3 < 5"));
        assertEquals(true, runScript("5 > 3"));
    }

    @Test
    void testStringConcatenation() {
        Object result = runScript("\"hello\" + \" world\"");
        assertEquals("hello world", result);
    }

    @Test
    void testIfStatement() {
        Object result = runScript("if (true) { 1 } else { 2 }");
        assertEquals(1, result);
    }

    @Test
    void testWhileLoop() {
        Object result = runScript("local i = 0; local sum = 0; while (i < 5) { sum = sum + i; i = i + 1 }; sum");
    }
}
```

**Step 2: Run tests**

Run: `./gradlew test --tests "net.clish.ast.InterpreterTest" 2>&1`

**Step 3: Commit**

---

## Task 5: Create StringLibrary test class

**Files:**
- Create: `src/test/java/net/clish/builtin/StringLibraryTest.java`

**Step 1: Write failing tests**

```java
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
        "hello, 1, hel",
        "hello, 0, hel",
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
        assertEquals("hello world", fn.call(List.of("hello world", "world", "there")));
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
```

**Step 2: Run tests**

Run: `./gradlew test --tests "net.clish.builtin.StringLibraryTest" 2>&1`

**Step 3: Commit**

---

## Task 6: Create MathLibrary test class

**Files:**
- Create: `src/test/java/net/clish/builtin/MathLibraryTest.java`

**Step 1: Write failing tests**

```java
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
```

**Step 2: Run tests**

Run: `./gradlew test --tests "net.clish.builtin.MathLibraryTest" 2>&1`

**Step 3: Commit**

---

## Task 7: Create RegexLibrary test class

**Files:**
- Create: `src/test/java/net/clish/builtin/RegexLibraryTest.java`

**Step 1: Write failing tests**

```java
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
        assertEquals("a1 b2 c1", fn.call(List.of("a1 b1 c1", "1", "2")));
    }

    @Test
    void testSplit() {
        var fn = new RegexLibrary.SplitFunction();
        var result = (List<?>) fn.call(List.of("a1b1c1", "\\d"));
        assertEquals(4, result.size());
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
```

**Step 2: Run tests**

Run: `./gradlew test --tests "net.clish.builtin.RegexLibraryTest" 2>&1`

**Step 3: Commit**

---

## Task 8: Create TimeLibrary test class

**Files:**
- Create: `src/test/java/net/clish/builtin/TimeLibraryTest.java`

**Step 1: Write failing tests**

```java
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
```

**Step 2: Run tests**

Run: `./gradlew test --tests "net.clish.builtin.TimeLibraryTest" 2>&1`

**Step 3: Commit**

---

## Task 9: Run all tests and verify

**Step 1: Run full test suite**

Run: `./gradlew test 2>&1`

**Step 2: Check test report**

Run: `ls -la build/reports/tests/test/`

---

## Task 10: Final commit

**Step 1: Commit all test files**

Run: `git add src/test/` and commit with message: "test: add JUnit 5 test suite for lexer, parser, interpreter, and libraries"
