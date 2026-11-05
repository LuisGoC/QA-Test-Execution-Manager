package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

public class TestResultAnalyzerTest {
    
    private TestResultAnalyzer analyzer;
    
    @BeforeEach
    void setUp() {
        // Arrange: Prepare the test data
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
        // Act: Execute the method being tested
        long totalTests = analyzer.getTotalTests();

        // Assert: Verify the expected result
        assertEquals(3, totalTests, "Total test count should be 3");
    }
    
    @Test
    @Tag("smoke")
    void shouldReturnCorrectPassedTests() {
        long totalPassed = analyzer.getPassedTests();
        assertEquals(1, totalPassed, "Test passed count should be 1");
    }

    @Test
    void shouldReturnCorrectFailedTests() {
        long totalFailed = analyzer.getFailedTests();
        assertEquals(1, totalFailed, "Test failed count should be 1");
    }

    @Test
    void shouldReturnCorrectSkippedTests() {
        long totalSkipped = analyzer.getSkippedTests();
        assertEquals(1, totalSkipped, "Test skipped count should be 1");
    }
    
    @Test
    void shouldReturnZeroWhenListIsEmpty() {
        List<TestResult> emptyResults = List.of();
        TestResultAnalyzer emptyAnalyzer = new TestResultAnalyzer(emptyResults);
        
        long totalTests = emptyAnalyzer.getTotalTests();
        assertEquals(0, totalTests, "Total test count should be 0");
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
            () -> assertEquals(0, emptyAnalyzer.getTotalTests(), "Total tests should be zero for empty results"),
            () -> assertEquals(0, emptyAnalyzer.getPassedTests(), "Passed tests should be zero for empty results"),
            () -> assertEquals(0, emptyAnalyzer.getFailedTests(), "Failed tests should be zero for empty results"),
            () -> assertEquals(0, emptyAnalyzer.getSkippedTests(), "Skipped tests should be zero for empty results"),
            () -> assertEquals(0.0, emptyAnalyzer.getPassRate(), "Pass rate should be 0.0 for empty results"),
            () -> assertEquals(0.0, emptyAnalyzer.getAverageResponseTime(), "Average response time should be 0.0 for empty results"),
            () -> assertTrue(emptyAnalyzer.getSlowestTest().isEmpty(), "Slowest test should be empty for empty results")
        );
    }
    
    @Test
    void shouldExcludeSkippedTestsFromAverageResponseTime() {
        List<TestResult> responseResults = List.of(
            new TestResult("TC001", "Login Test", TestStatus.PASS, 200, 100),
            new TestResult("TC002", "Search Test", TestStatus.FAIL, 500, 300),
            new TestResult("TC003", "Checkout Test", TestStatus.SKIPPED, 0, 0)
        );
        
        TestResultAnalyzer responseAnalyzer = new TestResultAnalyzer(responseResults);
        
        assertEquals(200.0, responseAnalyzer.getAverageResponseTime(), "Average response time should be 200.0 for the given results");
    }
}