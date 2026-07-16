package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utilities.ConfigReader;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 1;
    private int retryCount = Integer.parseInt(ConfigReader.get("retryCount", "1"));

    @Override
    public boolean retry(ITestResult result) {
        Throwable throwable = result.getThrowable();
        String testName = result.getName();

        if (throwable instanceof AssertionError) {
            System.out.println("[RetryAnalyzer] " + testName + " - Assertion failure, not retrying.");
            return false;
        }

        if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException) {
            System.out.println("[RetryAnalyzer] " + testName + " - Coding error (NPE/IAE), not retrying.");
            return false;
        }

        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            System.out.println("[RetryAnalyzer] " + testName + " - Retrying (" + retryCount + "/" + MAX_RETRY_COUNT + ")");
            return true;
        }

        System.out.println("[RetryAnalyzer] " + testName + " - Failed permanently after " + MAX_RETRY_COUNT + " retries.");
        return false;
    }
}
