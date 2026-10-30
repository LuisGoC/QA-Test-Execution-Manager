import java.io.IOException;
import java.util.List;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path testResultsPath = Path.of("test_results.csv");
        try{
            TestResultReader reader = new TestResultReader();
            List<TestResult> testResults = reader.readResults(testResultsPath);
            testResults.forEach(result -> System.out.println(result));
        }
        catch(IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}