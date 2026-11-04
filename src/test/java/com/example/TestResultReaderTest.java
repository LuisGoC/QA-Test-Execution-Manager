package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Predicate;

class TestResultReaderTest {
    
    @TempDir
    Path tempDir;
    
    @Test
    void shouldReadValidCsvFile() throws IOException {
        //Arrange
        Path csvFile = tempDir.resolve("test_results.csv");
        
        String csvContent =
            "TC001,Login Test,PASS,200,150\n" +
            "TC002,Checkout Test,FAIL,500,300\n" +
            "TC003,Logout Test,SKIPPED,0,0\n";
        
        Files.writeString(csvFile, csvContent);
        
        TestResultReader reader = new TestResultReader();
        
        // Act
        // Call reader.readResults(csvFile)
        List<TestResult> results = reader.readResults(csvFile);

        // Assert
        assertAll(
            () -> assertEquals(3, results.size(), "Results loaded should be 3"), // Verify that exactly 3 results were loaded
            () -> assertEquals("TC001", results.get(0).testId(), "First test ID should be 'TC001'"), // Verify that the first test has ID TC001
            () -> assertEquals(TestStatus.FAIL, results.get(1).status(), "Second test status should be FAIL"), // Verify that the second test has status FAIL
            () -> assertEquals(0, results.get(2).responseTime(), "Third test response time should be 0") // Verify that the third test has responseTime 0
        );
    }
    
    @Test
    void shouldSkipInvalidCsvRows() throws IOException {
        
        Predicate<TestResult> isFirstInvalidRow = result -> result.testId().equals("TC002");
        Predicate<TestResult> isSecondInvalidRow = result -> result.testId().equals("TC004");
        Predicate<TestResult> isFirstValidRow = result -> result.testId().equals("TC001");
        Predicate<TestResult> isSecondValidRow = result -> result.testId().equals("TC003");
        Predicate<TestResult> isThirdValidRow = result -> result.testId().equals("TC005");
        
        Path csvFile = tempDir.resolve("test_results.csv");
        
        String csvContent = 
            "TC001,Login Test,PASS,200,150\n" +
            "TC002,Checkout Test,INVALID,500,300\n" +
            "TC003,Logout Test,SKIPPED,0,0\n" +
            "TC004,Search Test,PASS,abc,100\n" +
            "TC005,Profile Test,PASS,200,120\n";
        
        Files.writeString(csvFile, csvContent);
        
        TestResultReader reader = new TestResultReader();
        
        List<TestResult> results = reader.readResults(csvFile);
        
        assertAll(
            () -> assertEquals(3, results.size()), // Verify that exactly 3 results were loaded
            () -> assertFalse(results.stream().anyMatch(isFirstInvalidRow)), // Verify that TC002 were excluded
            () -> assertFalse(results.stream().anyMatch(isSecondInvalidRow)), // Verify that TC004 were excluded
            () -> assertTrue(results.stream().anyMatch(isFirstValidRow)), // Verify that TC001 were included
            () -> assertTrue(results.stream().anyMatch(isSecondValidRow)), // Verify that TC003 were included
            () -> assertTrue(results.stream().anyMatch(isThirdValidRow)) // Verify that TC005 were included
        );
    }
    
    @Test
    void shouldReturnEmptyListForEmptyCsv() throws IOException {
        Path csvFile = tempDir.resolve("empty.csv");
        
        Files.writeString(csvFile, "");
        
        TestResultReader reader = new TestResultReader();
        
        List<TestResult> results = reader.readResults(csvFile);
        
        assertTrue(results.isEmpty()); // Verify that the returned list is empty
    }
    
    @Test
    void shouldThrowExceptionForMissingCsvFile() {
        Path missingFile = tempDir.resolve("missing.csv");
        TestResultReader reader = new TestResultReader();
        assertThrows(IOException.class, () -> reader.readResults(missingFile));
    }
}