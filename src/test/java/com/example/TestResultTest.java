package com.example;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestResultTest {

    @Nested
    @DisplayName("Input Validation")
    class InputValidationTests {

        @Test
        @Tag("negative")
        void shouldRejectBlankTestId() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TestResult("", "Login Test", TestStatus.PASS, 200, 150)
            );

            assertEquals("testId cannot be null or blank", exception.getMessage());
        }

        @Test
        @Tag("negative")
        void shouldRejectNullTestName() {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TestResult("TC001", null, TestStatus.PASS, 200, 150)
            );

            assertEquals("testName cannot be null or blank", exception.getMessage());
        }

        @ParameterizedTest
        @MethodSource("com.example.TestResultTest#invalidTestNames")
        @Tag("negative")
        void shouldRejectBlankTestNames(String testName) {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TestResult("TC001", testName, TestStatus.PASS, 200, 150)
            );

            assertEquals("testName cannot be null or blank", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("HTTP Code Validation")
    class HttpCodeValidationTests {

        @ParameterizedTest(name = "HTTP code {0} should be rejected")
        @ValueSource(ints = {0, 99, 600, 700})
        @Tag("boundary")
        void shouldRejectHttpCodesOutsideValidRange(int httpCode) {
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new TestResult("TC001", "Login Test", TestStatus.PASS, httpCode, 150)
            );

            assertEquals("HTTP Code must be between 100 and 599", exception.getMessage());
        }

        @ParameterizedTest
        @CsvSource({
            "PASS, 100",
            "PASS, 200",
            "FAIL, 404",
            "FAIL, 599"
        })
        void shouldAcceptValidHttpCodes(String status, int httpCode) {
            TestStatus testStatus = TestStatus.valueOf(status.trim());

            assertDoesNotThrow(
                () -> new TestResult("TC001", "Login Test", testStatus, httpCode, 150)
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {100, 599})
        @Tag("boundary")
        void shouldAcceptHttpCodeBoundaries(int httpCode) {
            assertDoesNotThrow(
                () -> new TestResult("TC001", "Login Test", TestStatus.PASS, httpCode, 250)
            );
        }
    }

    @Test
    void shouldRejectNullStatus() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestResult("TC001", "Login Test", null, 200, 150)
        );

        assertEquals("status cannot be null", exception.getMessage());
    }

    @Test
    void shouldRejectNegativeResponseTime() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestResult("TC001", "Login Test", TestStatus.PASS, 200, -50)
        );

        assertEquals("responseTime cannot be negative", exception.getMessage());
    }

    @Test
    void shouldRejectSkippedTestWithHttpCode() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestResult("TC001", "Login Test", TestStatus.SKIPPED, 200, 0)
        );

        assertEquals("HTTP Code must be 0 for SKIPPED status", exception.getMessage());
    }

    @Test
    void shouldRejectSkippedTestWithResponseTime() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new TestResult("TC001", "Login Test", TestStatus.SKIPPED, 0, 100)
        );

        assertEquals("responseTime must be 0 for SKIPPED status", exception.getMessage());
    }

    @Test
    @Tag("smoke")
    void shouldStoreAllTestResultFields() {
        TestResult result = new TestResult(
            "TC010", "Payment Test", TestStatus.PASS, 201, 250
        );

        assertAll(
            () -> assertEquals("TC010", result.testId(), "testId should be 'TC010'"),
            () -> assertEquals("Payment Test", result.testName(), "testName should be 'Payment Test'"),
            () -> assertEquals(TestStatus.PASS, result.status(), "status should be PASS"),
            () -> assertEquals(201, result.httpCode(), "httpCode should be 201"),
            () -> assertEquals(250, result.responseTime(), "responseTime should be 250")
        );
    }

    static Stream<String> invalidTestNames() {
        return Stream.of("", " ", "   ");
    }
}
