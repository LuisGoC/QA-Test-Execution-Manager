import java.util.List;

public class Main {
    public static void main(String[] args) {
        try{
            List<TestResult> results = List.of(
                new TestResult("TC001", "Login Test", TestStatus.PASS, 200, 125),
                new TestResult("TC002", "Checkout Test", TestStatus.FAIL, 500, 780),
                new TestResult("TC003", "Logout Test", TestStatus.SKIPPED, 0, 0)
            );
            results.forEach(result -> System.out.println(result));
        }
        catch(IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        try{
            TestResult invalidResult = new TestResult("TC004", "Register Test", TestStatus.SKIPPED, 202, 0);
            System.out.println(invalidResult);
        }
        catch(IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}