import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

public class TestResultReader {
    public List<TestResult> readResults(Path filePath) throws IOException {
        List<TestResult> results = new ArrayList<>();
        List<String> lines = Files.readAllLines(filePath);
        int lineNumber = 0;
        
        for(String line : lines) {
            String[] parts = line.split(",", -1);
            lineNumber++;
            
            if(parts.length != 5) {
                System.out.println("Warning: Invalid data at line " + lineNumber);
                continue;
            }
            
            try {
                String testId = parts[0].trim();
                String testName = parts[1].trim();
                TestStatus status = TestStatus.valueOf(parts[2].trim());
                int httpCode = Integer.parseInt(parts[3].trim());
                long responseTime = Long.parseLong(parts[4].trim());
                TestResult result = new TestResult(testId, testName, status, httpCode, responseTime);
                results.add(result);
            }
            catch(IllegalArgumentException e) {
                System.out.println("Warning: " + e.getMessage() + " at line " + lineNumber);
            }
        }
        
        return results;
    }
}