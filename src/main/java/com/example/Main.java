package com.example;

import java.io.IOException;
import java.util.List;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path testResultsPath = Path.of("test_results.csv");
        Path executionReportPath = Path.of("execution_report.txt");
        
        try {
            TestResultReader reader = new TestResultReader();
            List<TestResult> testResults = reader.readResults(testResultsPath);
            TestResultAnalyzer analyzer = new TestResultAnalyzer(testResults);
            ReportGenerator reportGenerator = new ReportGenerator(analyzer);
            reportGenerator.saveReport(executionReportPath);
            System.out.println("Report successfully generated: " + executionReportPath.getFileName());
        }
        catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}