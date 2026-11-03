package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

public class TestResultTest {
    
    @Test
    void shouldRejectBlankTestId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("", "Login Test", TestStatus.PASS, 200, 150));
        assertEquals("testId cannot be null or blank", exception.getMessage());
    }
    
    @Test
    void shouldRejectNullTestName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("TC001", null, TestStatus.PASS, 200, 150));
        assertEquals("testName cannot be null or blank", exception.getMessage());
    }
    
    @Test
    void shouldRejectNullStatus() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("TC001", "Login Test", null, 200, 150));
        assertEquals("status cannot be null", exception.getMessage());
    }
    
    @Test
    void shouldRejectInvalidHttpCode() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("TC001", "Login Test", TestStatus.PASS, 700, 150));
        assertEquals("HTTP Code must be between 100 and 599", exception.getMessage());
    }
    
    @Test
    void shouldRejectNegativeResponseTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("TC001", "Login Test", TestStatus.PASS, 200, -50));
        assertEquals("responseTime cannot be negative", exception.getMessage());
    }
    
    @Test
    void shouldRejectSkippedTestWithHttpCode() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("TC001", "Login Test", TestStatus.SKIPPED, 200, 0));
        assertEquals("HTTP Code must be 0 for SKIPPED status", exception.getMessage());
    }
    
    @Test
    void shouldRejectSkippedTestWithResponseTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("TC001", "Login Test", TestStatus.SKIPPED, 0, 100));
        assertEquals("responseTime must be 0 for SKIPPED status", exception.getMessage());
    }
    
    //BVA
    @Test
    @DisplayName("Should reject HTTP code 99")
    void shouldRejectHttpCodeBelowMinimum() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("TC001", "Login Test", TestStatus.PASS, 99, 150));
        assertEquals("HTTP Code must be between 100 and 599", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should accept HTTP code 100")
    void shouldAcceptHttpCodeExactlyOnMinimum() {
        assertDoesNotThrow(() -> new TestResult("TC001", "Login Test", TestStatus.PASS, 100, 150));
    }
    
    @Test
    @DisplayName("Should accept HTTP code 599")
    void shouldAcceptHttpCodeExactlyOnMaximum() {
        assertDoesNotThrow(() -> new TestResult("TC001", "Login Test", TestStatus.PASS, 599, 150));
    }
    
    @Test
    @DisplayName("Should reject HTTP code 600")
    void shouldRejectHttpCodeAboveMaximum() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestResult("TC001", "Login Test", TestStatus.PASS, 600, 150));
        assertEquals("HTTP Code must be between 100 and 599", exception.getMessage());
    }
}