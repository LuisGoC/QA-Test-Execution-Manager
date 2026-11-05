package com.example;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestResultAnalyzerTest {

    private TestResultAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        List<TestResult> results = List.of(
            new TestResult("TC001", "Login Test", TestStatus.PASS, 200, 343),
            new TestResult("TC002", "Add Product Test", TestStatus.FAIL, 404, 789),
            new TestResult("TC003", "Logout Test", TestStatus.SKIPPED, 0, 0)
        );

        analyzer = new TestResultAnalyzer(results);
    }

    @Test
    @Tag("smoke")
    void shouldReturnCorrectTotalTests() {
        long totalTests = analyzer.getTotalTests();

        assertEquals(3, totalTests, "Total test count should be 3");
    }

    @Test
    @Tag("smoke")
    void shouldReturnCorrectPassedTests() {
        long totalPassed = analyzer.getPassedTests();

        assertEquals(1, totalPassed, "Passed test count should be 1");
    }

    @Test
    void shouldReturnCorrectFailedTests() {
        long totalFailed = analyzer.getFailedTests();

        assertEquals(1, totalFailed, "Failed test count should be 1");
    }

    @Test
    void shouldReturnCorrectSkippedTests() {
        long totalSkipped = analyzer.getSkippedTests();

        assertEquals(1, totalSkipped, "Skipped test count should be 1");
    }

    @Test
    @Tag("performance")
    void shouldCalculateStatisticsWithinTimeLimit() {
        assertTimeout(Duration.ofSeconds(2), () -> {
            long result = analyzer.getPassedTests();
            assertEquals(1, result);
        });
    }

    @Test
    void shouldReturnZeroStatisticsForEmptyResults() {
        TestResultAnalyzer emptyAnalyzer = new TestResultAnalyzer(List.of());

        assertAll(
            () -> assertEquals(0, emptyAnalyzer.getTotalTests(), "Total count should be zero"),
            () -> assertEquals(0, emptyAnalyzer.getPassedTests(), "Passed count should be zero"),
            () -> assertEquals(0, emptyAnalyzer.getFailedTests(), "Failed count should be zero"),
            () -> assertEquals(0, emptyAnalyzer.getSkippedTests(), "Skipped count should be zero"),
            () -> assertEquals(0.0, emptyAnalyzer.getPassRate(), "Pass rate should be zero"),
            () -> assertEquals(0.0, emptyAnalyzer.getAverageResponseTime(), "Average time should be zero"),
            () -> assertTrue(emptyAnalyzer.getSlowestTest().isEmpty(), "No slowest test should exist")
        );
    }

    @Test
    void shouldExcludeSkippedTestsFromAverageResponseTime() {
        List<TestResult> results = List.of(
            new TestResult("TC001", "Login Test", TestStatus.PASS, 200, 100),
            new TestResult("TC002", "Search Test", TestStatus.FAIL, 500, 300),
            new TestResult("TC003", "Checkout Test", TestStatus.SKIPPED, 0, 0)
        );

        TestResultAnalyzer responseAnalyzer = new TestResultAnalyzer(results);

        assertEquals(
            200.0,
            responseAnalyzer.getAverageResponseTime(),
            "Skipped tests should not affect the average response time"
        );
    }
}
