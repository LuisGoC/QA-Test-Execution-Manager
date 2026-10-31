import java.io.IOException;
import java.util.List;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path testResultsPath = Path.of("test_results.csv");
        try{
            TestResultReader reader = new TestResultReader();
            List<TestResult> testResults = reader.readResults(testResultsPath);
            TestResultAnalyzer testResultAnalyzer = new TestResultAnalyzer(testResults);
            System.out.println("Total Tests: " + testResultAnalyzer.getTotalTests());
            System.out.println("Passed: " + testResultAnalyzer.getPassedTests());
            System.out.println("Failed: " + testResultAnalyzer.getFailedTests());
            System.out.println("Skipped: " + testResultAnalyzer.getSkippedTests());
            System.out.printf("Pass Rate: %.2f%%\n", testResultAnalyzer.getPassRate());
            System.out.printf("Average Response Time: %.2f ms\n", testResultAnalyzer.getAverageResponseTime());
            testResultAnalyzer.getSlowestTest().ifPresent(result -> System.out.println("Slowest Test: " + result.testId() + " - " + result.testName() + " (" + result.responseTime() + " ms)"));
            
            System.out.println("");
            System.out.println("Failed Tests:");
            testResultAnalyzer.getFailedTestResults().forEach(result -> System.out.println(result.testId() + " - " + result.testName()));
            
            System.out.println("");
            System.out.println("Tests Slower Than 150 ms:");
            testResultAnalyzer.getSlowTests(150).forEach(result -> System.out.println(result.testId() + " - " + result.testName() + " (" + result.responseTime() + " ms)"));
            
        }
        catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}