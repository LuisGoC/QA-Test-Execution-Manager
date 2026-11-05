package com.example;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Calculates execution counts, pass rates, and response-time statistics.
 * Skipped tests are excluded from response-time calculations.
 */
public class TestResultAnalyzer {

    private final Predicate<TestResult> isPassed =
        result -> result.status().equals(TestStatus.PASS);
    private final Predicate<TestResult> isFailed =
        result -> result.status().equals(TestStatus.FAIL);
    private final Predicate<TestResult> isSkipped =
        result -> result.status().equals(TestStatus.SKIPPED);
    private final Predicate<TestResult> isNotSkipped =
        result -> !result.status().equals(TestStatus.SKIPPED);

    private final List<TestResult> results;

    /**
     * Creates an analyzer for the supplied test results.
     *
     * @param results results to analyze
     */
    public TestResultAnalyzer(List<TestResult> results) {
        this.results = results;
    }

    /** @return the number of test results */
    public long getTotalTests() {
        return results.size();
    }

    /** @return the number of passed tests */
    public long getPassedTests() {
        return results.stream().filter(isPassed).count();
    }

    /** @return the number of failed tests */
    public long getFailedTests() {
        return results.stream().filter(isFailed).count();
    }

    /** @return the number of skipped tests */
    public long getSkippedTests() {
        return results.stream().filter(isSkipped).count();
    }

    /**
     * Calculates the percentage of passed tests among all results.
     *
     * @return pass percentage, or 0.0 if there are no results
     */
    public double getPassRate() {
        long total = getTotalTests();

        if (total == 0) {
            return 0.0;
        }

        return (double) getPassedTests() / total * 100.0;
    }

    /**
     * Calculates the mean response time of non-skipped tests.
     *
     * @return average response time in milliseconds, or 0.0 if none were executed
     */
    public double getAverageResponseTime() {
        return results.stream()
            .filter(isNotSkipped)
            .mapToLong(result -> result.responseTime())
            .average()
            .orElse(0.0);
    }

    /**
     * Finds the non-skipped test with the longest response time.
     *
     * @return the slowest executed test, or an empty Optional if none exists
     */
    public Optional<TestResult> getSlowestTest() {
        return results.stream()
            .filter(isNotSkipped)
            .max(Comparator.comparingLong(result -> result.responseTime()));
    }

    /** @return the failed test results in their original order */
    public List<TestResult> getFailedTestResults() {
        return results.stream().filter(isFailed).toList();
    }

    /**
     * Finds non-skipped tests whose response time exceeds a threshold.
     *
     * @param thresholdMs exclusive response-time threshold in milliseconds
     * @return test results exceeding the threshold
     * @throws IllegalArgumentException if the threshold is negative
     */
    public List<TestResult> getSlowTests(long thresholdMs) {
        if (thresholdMs < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }

        return results.stream()
            .filter(isNotSkipped)
            .filter(result -> result.responseTime() > thresholdMs)
            .toList();
    }
}
