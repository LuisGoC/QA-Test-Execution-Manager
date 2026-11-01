package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ReportGenerator {

    private final TestResultAnalyzer analyzer;

    public ReportGenerator(TestResultAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public String generateReport() {
        LocalDateTime executionDateTime = LocalDateTime.now(ZoneId.of("America/Mexico_City"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder report = new StringBuilder();
        
        report.append("========================================\n");
        report.append("       QA TEST EXECUTION REPORT         \n");
        report.append("========================================\n\n");
        report.append("Execution Date: " + executionDateTime.format(formatter) + "\n\n");
        report.append("Total Tests: " + analyzer.getTotalTests() + "\n");
        report.append("Passed: " + analyzer.getPassedTests() + "\n");
        report.append("Failed: " + analyzer.getFailedTests() + "\n");
        report.append("Skipped: " + analyzer.getSkippedTests() + "\n\n");
        report.append("Pass Rate: " + String.format("%.2f", analyzer.getPassRate()) + "%\n");
        report.append("Average Response Time: " + String.format("%.2f", analyzer.getAverageResponseTime()) + " ms\n\n");
        if(analyzer.getTotalTests() > 0) {
            if(analyzer.getSlowestTest().isEmpty()) {
                report.append("Slowest Test: N/A\n\n");
            }
            else {
                analyzer.getSlowestTest().ifPresent(result -> report.append("Slowest Test: " + result.testId() + " - " + result.testName() + " (" + result.responseTime() + " ms)\n\n"));
            }
            report.append("Failed Tests:\n");
            if(analyzer.getFailedTestResults().isEmpty()) {
                report.append("None\n");
            }
            else {
                analyzer.getFailedTestResults().forEach(result -> report.append(result.testId() + " - " + result.testName() + "\n"));
            }
        }
        else {
            report.append("Slowest Test: N/A\n\n");
            report.append("Failed Tests:\n");
            report.append("None\n");
        }
        report.append("\n========================================\n");
        
        return report.toString();
    }
    
    public void saveReport(Path outputPath) throws IOException {
        Files.writeString(outputPath, generateReport());
    }
}