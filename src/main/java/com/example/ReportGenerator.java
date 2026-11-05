package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Formats execution statistics and failed-test details as a plain-text report.
 */
public class ReportGenerator {

    private final TestResultAnalyzer analyzer;

    /**
     * Creates a report generator for the supplied analyzer.
     *
     * @param analyzer source of execution statistics
     */
    public ReportGenerator(TestResultAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    /**
     * Generates a report with an execution timestamp in Mexico City time.
     *
     * @return the formatted execution report
     */
    public String generateReport() {
        LocalDateTime executionDateTime = LocalDateTime.now(
            ZoneId.of("America/Mexico_City")
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder report = new StringBuilder();

        report.append("========================================\n");
        report.append("       QA TEST EXECUTION REPORT         \n");
        report.append("========================================\n\n");
        report.append("Execution Date: ")
            .append(executionDateTime.format(formatter))
            .append("\n\n");
        report.append("Total Tests: ").append(analyzer.getTotalTests()).append("\n");
        report.append("Passed: ").append(analyzer.getPassedTests()).append("\n");
        report.append("Failed: ").append(analyzer.getFailedTests()).append("\n");
        report.append("Skipped: ").append(analyzer.getSkippedTests()).append("\n\n");
        report.append("Pass Rate: ")
            .append(String.format("%.2f", analyzer.getPassRate()))
            .append("%\n");
        report.append("Average Response Time: ")
            .append(String.format("%.2f", analyzer.getAverageResponseTime()))
            .append(" ms\n\n");

        if (analyzer.getTotalTests() > 0) {
            if (analyzer.getSlowestTest().isEmpty()) {
                report.append("Slowest Test: N/A\n\n");
            } else {
                analyzer.getSlowestTest().ifPresent(result ->
                    report.append("Slowest Test: ")
                        .append(result.testId())
                        .append(" - ")
                        .append(result.testName())
                        .append(" (")
                        .append(result.responseTime())
                        .append(" ms)\n\n")
                );
            }

            report.append("Failed Tests:\n");
            if (analyzer.getFailedTestResults().isEmpty()) {
                report.append("None\n");
            } else {
                analyzer.getFailedTestResults().forEach(result ->
                    report.append(result.testId())
                        .append(" - ")
                        .append(result.testName())
                        .append("\n")
                );
            }
        } else {
            report.append("Slowest Test: N/A\n\n");
            report.append("Failed Tests:\n");
            report.append("None\n");
        }

        report.append("\n========================================\n");
        return report.toString();
    }

    /**
     * Writes the generated report to a text file.
     *
     * @param outputPath destination path
     * @throws IOException if the report cannot be written
     */
    public void saveReport(Path outputPath) throws IOException {
        Files.writeString(outputPath, generateReport());
    }
}
