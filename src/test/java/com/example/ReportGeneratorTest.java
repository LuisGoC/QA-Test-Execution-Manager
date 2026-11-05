package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldSaveExecutionReport() throws IOException {

        // Arrange
        List<TestResult> results = List.of(
            new TestResult("TC001", "Login Test", TestStatus.PASS, 200, 150)
        );

        TestResultAnalyzer analyzer = new TestResultAnalyzer(results);

        ReportGenerator generator = new ReportGenerator(analyzer);

        Path reportFile = tempDir.resolve("execution_report.txt");

        // Act
        // Call the method that saves the report
        generator.saveReport(reportFile);

        // Assert
        // Verify that the file exists
        assertTrue(Files.exists(reportFile));
        // Read the saved file as a String
        String report = Files.readString(reportFile);
        
        /* We avoid comparing an entire report containing the current date
         * Because the date changes between executions
         * Dynamic content makes exact string comparisons britle unless time is controlled.
         */
        assertAll(
            () -> assertTrue(report.contains("Total Tests: 1")), // Verify that it contains "Total Tests: 1"
            () -> assertTrue(report.contains("Passed: 1")) // Verify that it contains "Passed: 1"
        );
    }
    
    @Test
    void shouldGenerateReportForEmptyResults() {
        // Create a TestResultAnalyzer using an empty list
        TestResultAnalyzer analyzer = new TestResultAnalyzer(List.of());
        // Create a ReportGenerator
        ReportGenerator generator = new ReportGenerator(analyzer);
        // Call generateareport()
        String report = generator.generateReport();
        // Assertions
        assertAll(
            () -> assertTrue(report.contains("Total Tests: 0")),
            () -> assertTrue(report.contains("Passed: 0")),
            () -> assertTrue(report.contains("Failed: 0")),
            () -> assertTrue(report.contains("Skipped: 0")),
            () -> assertTrue(report.contains("Slowest Test: N/A")),
            () -> assertTrue(report.contains("Failed Tests:\nNone"))
        );
    }
}