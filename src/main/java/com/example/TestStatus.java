package com.example;

public enum TestStatus {
    PASS("Test passed successfully"),
    FAIL("Test failed"),
    SKIPPED("Test was not executed");
    
    private final String description;
    
    TestStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}