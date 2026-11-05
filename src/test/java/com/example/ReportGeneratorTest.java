package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldSaveExecutionReport() throws IOException {
        List<TestResult> results = List.of(
            new TestResult("TC001", "Login Test", TestStatus.PASS, 200, 150)
        );
        TestResultAnalyzer analyzer = new TestResultAnalyzer(results);
        ReportGenerator generator = new ReportGenerator(analyzer);
        Path reportFile = tempDir.resolve("execution_report.txt");

        generator.saveReport(reportFile);

        assertTrue(Files.exists(reportFile), "The report file should exist");
        String report = Files.readString(reportFile);

        // The execution timestamp changes between runs; assert stable report content only.
        assertAll(
            () -> assertTrue(report.contains("Total Tests: 1")),
            () -> assertTrue(report.contains("Passed: 1"))
        );
    }

    @Test
    void shouldGenerateReportForEmptyResults() {
        TestResultAnalyzer analyzer = new TestResultAnalyzer(List.of());
        ReportGenerator generator = new ReportGenerator(analyzer);

        String report = generator.generateReport();

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
