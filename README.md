# QA Test Execution Manager

A Java command-line application designed to **read, validate, analyze, and report software test execution results** from CSV files.

This project demonstrates the practical application of **Core Java, Object-Oriented Programming (OOP), data validation, exception handling, file I/O, and the Stream API** in a Quality Assurance (QA) environment.

## Project Overview

The **QA Test Execution Manager** simulates a simplified test execution reporting workflow commonly used in software testing.

The application reads test execution data from a CSV file, validates individual records, calculates execution statistics, analyzes response times, and generates a formatted text report.

This project was developed to strengthen my Java programming skills and demonstrate foundational software engineering practices relevant to **QA Automation Engineering and Software Development Engineering in Test (SDET)**.

## Key Features

- **CSV File Processing:** Reads test execution results from an external CSV file.
- **Data Validation:** Validates test identifiers, test names, execution statuses, HTTP status codes, and response times.
- **Error Handling:** Skips invalid CSV records, displays warnings with line numbers, and continues processing valid records.
- **Execution Statistics:** Calculates total, passed, failed, and skipped tests.
- **Pass Rate Calculation:** Calculates the percentage of passed tests.
- **Performance Analysis:** Calculates average response times, identifies the slowest test, and filters tests exceeding a specified response-time threshold.
- **Report Generation:** Generates a formatted text report containing execution statistics and failed-test details.
- **Edge-Case Handling:** Supports empty CSV files, skipped-only executions, passed-only executions, and missing input files.

## Technologies & Tools

| Technology | Purpose |
|---|---|
| Java 21 | Core application development |
| Gradle | Build automation and project configuration |
| Git | Version control |
| GitHub | Source code hosting |
| JStudio | Java development environment on Android |

### Java Concepts Applied

- Object-Oriented Programming (OOP)
- Java Records and Enums
- Constructors and Data Validation
- Collections Framework
- Stream API
- Lambda Expressions and Predicates
- Optional
- Exception Handling
- Java NIO File Handling
- Java Date and Time API
- StringBuilder
- Separation of Responsibilities

## Project Structure

The project follows the standard Gradle directory structure, with Java source files organized under the `com.example` package.

```text
QA_Test_Execution_Manager/
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   ├── Main.java
│                   ├── ReportGenerator.java
│                   ├── TestResult.java
│                   ├── TestResultAnalyzer.java
│                   ├── TestResultReader.java
│                   └── TestStatus.java
├── test_results.csv
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── .gitignore
└── README.md
```

The `execution_report.txt` file is generated when the application runs.

## Application Architecture

The application uses a modular design, where each class has a clearly defined responsibility.

| Component | Responsibility |
|---|---|
| `TestStatus.java` | Defines the `PASS`, `FAIL`, and `SKIPPED` execution statuses |
| `TestResult.java` | Represents an individual test result and validates its data |
| `TestResultReader.java` | Reads and parses CSV files into validated test result objects |
| `TestResultAnalyzer.java` | Calculates execution statistics and performance metrics |
| `ReportGenerator.java` | Generates and saves the execution report |
| `Main.java` | Coordinates the application workflow |

### Application Workflow

```text
       test_results.csv
              |
              v
      TestResultReader
       (CSV Parsing)
              |
              v
         TestResult
      (Data Validation)
              |
              v
     TestResultAnalyzer
     (Metrics & Analysis)
              |
              v
       ReportGenerator
              |
              v
      execution_report.txt
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21
- Git (optional, for cloning the repository)

The project includes the Gradle Wrapper, so a separate Gradle installation is not required.

### Installation

Clone the repository:

```bash
git clone https://github.com/LuisGoC/QA_Test_Execution_Manager.git
```

Navigate to the project directory:

```bash
cd QA_Test_Execution_Manager
```

### Build the Project

On Linux or macOS:

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew.bat build
```

### Run the Application

On Linux or macOS:

```bash
./gradlew run
```

On Windows:

```powershell
.\gradlew.bat run
```

The application uses the following Gradle configuration to identify its entry point:

```kotlin
application {
    mainClass.set("com.example.Main")
}
```

**Note:** The `test_results.csv` file must be located in the project's working directory, normally the project root when running through Gradle.

### Expected Console Output

```text
Report successfully generated: execution_report.txt
```

After successful execution, the application creates or overwrites `execution_report.txt`.

## CSV Input Format

The application expects a CSV file named `test_results.csv`.

Each record must contain five comma-separated fields in the following order:

```text
testId,testName,status,httpCode,responseTime
```

**Important:** This line describes the field order. The current CSV reader does not support header rows, so the actual input file should contain only test records.

### Example CSV Input

```csv
TC001,Login Test,PASS,200,125
TC002,Registration Test,FAIL,400,340
TC003,Search Test,PASS,200,95
TC004,Checkout Test,FAIL,500,780
TC005,Logout Test,PASS,204,110
TC006,Profile Update Test,SKIPPED,0,0
TC007,Password Reset Test,PASS,200,215
TC008,Delete Account Test,FAIL,403,410
```

### Data Validation Rules

The application validates each test result using the following rules:

1. `testId` cannot be null or blank.
2. `testName` cannot be null or blank.
3. `status` must be `PASS`, `FAIL`, or `SKIPPED`.
4. HTTP status codes must be between 100 and 599 for `PASS` and `FAIL`.
5. HTTP status codes must be exactly `0` for `SKIPPED`.
6. Response times cannot be negative.
7. Skipped tests must have a response time of `0`.

If a CSV record violates these rules, the application displays a warning and continues processing the remaining records.

## Sample Execution Report

The following report was generated using the example CSV input:

```text
========================================
       QA TEST EXECUTION REPORT
========================================

Execution Date: 31/10/2026 23:31:27

Total Tests: 8
Passed: 4
Failed: 3
Skipped: 1

Pass Rate: 50.00%
Average Response Time: 296.43 ms

Slowest Test: TC004 - Checkout Test (780 ms)

Failed Tests:
TC002 - Registration Test
TC004 - Checkout Test
TC008 - Delete Account Test

========================================
```

### Metrics Explanation

**Total Tests**

The number of valid test records successfully processed from the CSV file.

**Pass Rate**

The percentage of passed tests relative to the total number of valid test records.

```text
Pass Rate = (Passed Tests / Total Tests) * 100
```

**Average Response Time**

The average response time of tests with `PASS` or `FAIL` status.

Skipped tests are excluded from this calculation.

**Slowest Test**

The non-skipped test with the highest response time.

If no executed tests are available, the report displays `N/A`.

**Failed Tests**

Lists the identifiers and names of all failed tests.

If no failed tests exist, the report displays `None`.

## Testing & Validation

The application was manually tested using six integration scenarios to verify its behavior under normal and exceptional conditions.

| Test Scenario | Expected Result | Status |
|---|---|---|
| Valid CSV | Generate accurate execution statistics | PASS |
| CSV with invalid records | Display warnings and process valid records | PASS |
| Empty CSV | Generate a report with zero statistics | PASS |
| Only skipped tests | Display zero average response time and no slowest test | PASS |
| Only passed tests | Display a 100% pass rate and no failed tests | PASS |
| Missing CSV file | Handle the file error without an unhandled exception | PASS |

**Testing Results: 6/6 scenarios passed.**

These scenarios were verified through application execution and output inspection.

Automated unit tests have not yet been implemented.

## Project Results

- Developed a functional Java application for processing test execution results.
- Implemented a modular architecture with clearly separated responsibilities.
- Applied data validation to prevent malformed records from affecting execution statistics.
- Used the Java Stream API to calculate metrics and analyze response times.
- Implemented exception handling for invalid input and file-processing errors.
- Generated readable execution reports containing test summaries and failed-test details.
- Verified application behavior across six integration scenarios.
- Organized the source code using the `com.example` Java package.
- Configured Gradle for application build and execution.

## Limitations

This project is intended as a Core Java learning application rather than a complete test management platform.

Current limitations include:

- CSV input must contain exactly five fields per row without a header.
- Quoted CSV fields containing commas are not supported.
- Test execution results are read from a local file rather than collected directly from a test automation framework.
- Reports are generated in plain-text format.
- Input and output paths are currently fixed in the application.
- Automated unit tests have not yet been implemented.

## Future Improvements

- Implement automated unit tests using JUnit 5.
- Add parameterized tests for validation rules and edge cases.
- Integrate test execution results from automated testing frameworks.
- Support additional report formats such as JSON and HTML.
- Improve CSV parsing and error reporting.
- Introduce logging for application events and invalid records.
- Add configurable input and output file paths.
- Integrate the application into a CI/CD workflow.

## What I Learned

Through this project, I strengthened my understanding of Java fundamentals and software engineering practices, including:

- Designing maintainable classes using object-oriented principles.
- Modeling data with records and enums.
- Validating application data through constructors and exceptions.
- Processing files using Java NIO.
- Using collections, predicates, and streams for data analysis.
- Handling empty results safely with `Optional`.
- Generating formatted reports using `StringBuilder`.
- Managing application workflows across multiple classes.
- Organizing Java source code using packages.
- Configuring and building Java applications with Gradle.
- Testing expected behavior and edge cases.

## Project Status

**Core Java implementation complete.**

All five development milestones have been completed, and the application has passed six manually executed integration scenarios.

Future development will focus on automated unit testing with JUnit 5 and integration with QA automation tools.

---

Developed as a portfolio project to demonstrate practical Java programming skills and preparation for a career in **QA Automation Engineering / SDET**.
