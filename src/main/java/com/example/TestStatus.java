package com.example;

/**
 * Represents the possible execution outcomes of a test.
 */
public enum TestStatus {
    PASS("Test passed successfully"),
    FAIL("Test failed"),
    SKIPPED("Test was not executed");

    private final String description;

    TestStatus(String description) {
        this.description = description;
    }

    /**
     * Returns a human-readable description of the status.
     *
     * @return the status description
     */
    public String getDescription() {
        return description;
    }
}
