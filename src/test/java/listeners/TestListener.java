package listeners;

import base.BasePage;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import utilities.EmailUtils;
import utilities.ExtentReportManager;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.getReporter();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentReportManager.createTestIfNotExists(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test Passed");
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logFailureWithScreenshot(result);
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.createTestIfNotExists(result.getMethod().getMethodName());
        if (result.getThrowable() instanceof SkipException) {
            ExtentReportManager.getTest().log(Status.SKIP, "Skipped");
            ExtentReportManager.removeTest();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReport();

        int passed = context.getPassedTests().size();
        int failed = context.getFailedTests().size();
        int skipped = context.getSkippedTests().size();
        EmailUtils.sendReport(ExtentReportManager.getReportPath(), passed, failed, skipped);
    }

    private void logFailureWithScreenshot(ITestResult result) {
        Throwable throwable = result.getThrowable();
        String errorMessage = (throwable != null) ? throwable.getMessage() : "No error message.";
        Object testInstance = result.getInstance();

        if (testInstance instanceof BasePage basePage && basePage.getWebDriver() != null) {
            try {
                // Any Common subclass exposes getScreenshotBase64(); reuse via a throwaway page-less call
                String base64 = ((org.openqa.selenium.TakesScreenshot) basePage.getWebDriver())
                        .getScreenshotAs(org.openqa.selenium.OutputType.BASE64);
                String imgTag = "<br><b>Failure Screenshot</b><br>"
                        + "<img src='data:image/png;base64," + base64 + "' style='max-width:25%; border:2px solid red;'/><br>";
                ExtentReportManager.getTest().log(Status.FAIL, imgTag + errorMessage,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
            } catch (Exception e) {
                ExtentReportManager.getTest().log(Status.FAIL, "Failed to capture screenshot: " + e.getMessage());
            }
        } else {
            ExtentReportManager.getTest().log(Status.FAIL, errorMessage);
        }
    }
}
