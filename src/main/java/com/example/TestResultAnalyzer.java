import java.util.List;
import java.util.function.Predicate;
import java.util.Optional;
import java.util.Comparator;

public class TestResultAnalyzer {
    
    private Predicate<TestResult> isPassed = result -> result.status().equals(TestStatus.PASS);
    private Predicate<TestResult> isFailed = result -> result.status().equals(TestStatus.FAIL);
    private Predicate<TestResult> isSkipped = result -> result.status().equals(TestStatus.SKIPPED);
    private Predicate<TestResult> isNotSkipped = result -> !result.status().equals(TestStatus.SKIPPED);
    
    private final List<TestResult> results;
    
    public TestResultAnalyzer(List<TestResult> results) {
        this.results = results;
    }
    
    public long getTotalTests() {
        return results.size();
    }
    
    public long getPassedTests() {
        //Predicate<TestResult> isPassedPredicate = result -> result.status().equals(TestStatus.PASS);
        return results.stream().filter(isPassed).count();
    }
    
    public long getFailedTests() {
        //Predicate<TestResult> isFailedPredicate = result -> result.status().equals(TestStatus.FAIL);
        return results.stream().filter(isFailed).count();
    }
    
    public long getSkippedTests() {
        //Predicate<TestResult> isSkippedPredicate = result -> result.status().equals(TestStatus.SKIPPED);
        return results.stream().filter(isSkipped).count();
    }
    
    public double getPassRate() {
        long total = getTotalTests();
        
        if (total == 0) {
            return 0.0;
        }
        
        return (double) getPassedTests() / total * 100.0;
    }
    
    public double getAverageResponseTime() {
        //Predicate<TestResult> isNotSkippedPredicate = result -> !result.status().equals(TestStatus.SKIPPED);
        return results.stream().filter(isNotSkipped).mapToLong(result -> result.responseTime()).average().orElse(0.0);
    }
    
    public Optional<TestResult> getSlowestTest() {
        //Predicate<TestResult> isNotSkippedPredicate = result -> !result.status().equals(TestStatus.SKIPPED);
        return results.stream().filter(isNotSkipped).max(Comparator.comparingLong(result -> result.responseTime()));
    }
    
    public List<TestResult> getFailedTestResults() {
        //Predicate<TestResult> isFailedPredicate = result -> result.status().equals(TestStatus.FAIL);
        return results.stream().filter(isFailed).toList();
    }
    
    public List<TestResult> getSlowTests(long thresholdMs) {
        if (thresholdMs < 0) {
            throw new 
            IllegalArgumentException("Threshold cannot be negative");
        }
        return results.stream().filter(isNotSkipped).filter(result -> result.responseTime() > thresholdMs).toList();
    }
}