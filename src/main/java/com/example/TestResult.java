package com.example;

/**
 * Stores a single test execution result and validates its fields.
 *
 * @param testId unique identifier of the test
 * @param testName descriptive name of the test
 * @param status execution outcome
 * @param httpCode HTTP response code, or zero for a skipped test
 * @param responseTime response time in milliseconds, or zero for a skipped test
 */
public record TestResult(
    String testId,
    String testName,
    TestStatus status,
    int httpCode,
    long responseTime
) {

    /**
     * Validates the components of a test result.
     *
     * @throws IllegalArgumentException if any component violates its validation rules
     */
    public TestResult {
        if (testId == null || testId.isBlank()) {
            throw new IllegalArgumentException("testId cannot be null or blank");
        }

        if (testName == null || testName.isBlank()) {
            throw new IllegalArgumentException("testName cannot be null or blank");
        }

        if (responseTime < 0) {
            throw new IllegalArgumentException("responseTime cannot be negative");
        }

        switch (status) {
            case PASS, FAIL -> {
                if (httpCode < 100 || httpCode > 599) {
                    throw new IllegalArgumentException(
                        "HTTP Code must be between 100 and 599"
                    );
                }
            }
            case SKIPPED -> {
                if (httpCode != 0) {
                    throw new IllegalArgumentException(
                        "HTTP Code must be 0 for SKIPPED status"
                    );
                }

                if (responseTime != 0) {
                    throw new IllegalArgumentException(
                        "responseTime must be 0 for SKIPPED status"
                    );
                }
            }
            case null -> throw new IllegalArgumentException("status cannot be null");
            default -> throw new IllegalArgumentException("Invalid status");
        }
    }
}
