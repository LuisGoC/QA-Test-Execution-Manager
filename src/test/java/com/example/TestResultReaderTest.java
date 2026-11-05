package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestResultReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldReadValidCsvFile() throws IOException {
        Path csvFile = tempDir.resolve("test_results.csv");
        String csvContent = """
            TC001,Login Test,PASS,200,150
            TC002,Checkout Test,FAIL,500,300
            TC003,Logout Test,SKIPPED,0,0
            """;

        Files.writeString(csvFile, csvContent);
        TestResultReader reader = new TestResultReader();

        List<TestResult> results = reader.readResults(csvFile);

        assertAll(
            () -> assertEquals(3, results.size(), "Three results should be loaded"),
            () -> assertEquals("TC001", results.get(0).testId(), "First test ID should be TC001"),
            () -> assertEquals(TestStatus.FAIL, results.get(1).status(), "Second test should fail"),
            () -> assertEquals(0, results.get(2).responseTime(), "Skipped test should take zero time")
        );
    }

    @Test
    void shouldSkipInvalidCsvRows() throws IOException {
        Path csvFile = tempDir.resolve("test_results.csv");
        String csvContent = """
            TC001,Login Test,PASS,200,150
            TC002,Checkout Test,INVALID,500,300
            TC003,Logout Test,SKIPPED,0,0
            TC004,Search Test,PASS,abc,100
            TC005,Profile Test,PASS,200,120
            """;

        Files.writeString(csvFile, csvContent);
        TestResultReader reader = new TestResultReader();

        List<TestResult> results = reader.readResults(csvFile);
        List<String> actualIds = results.stream()
            .map(result -> result.testId())
            .toList();

        // Comparing IDs verifies valid-row retention, invalid-row exclusion, and ordering.
        assertEquals(List.of("TC001", "TC003", "TC005"), actualIds);
    }

    @Test
    void shouldReturnEmptyListForEmptyCsv() throws IOException {
        Path csvFile = tempDir.resolve("empty.csv");
        Files.writeString(csvFile, "");
        TestResultReader reader = new TestResultReader();

        List<TestResult> results = reader.readResults(csvFile);

        assertTrue(results.isEmpty());
    }

    @Test
    void shouldThrowExceptionForMissingCsvFile() {
        Path missingFile = tempDir.resolve("missing.csv");
        TestResultReader reader = new TestResultReader();

        assertThrows(IOException.class, () -> reader.readResults(missingFile));
    }
}
