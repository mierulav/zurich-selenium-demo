package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wraps ExtentReports as a singleton and keeps each thread's "current test"
 * node separate via a ThreadLocal-backed map, so parallel TestNG execution
 * doesn't cross-log results between test methods.
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static String reportPath;
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();
    private static final Map<String, ExtentTest> testMap = new ConcurrentHashMap<>();

    private ExtentReportManager() {
        // static-access only
    }

    public static synchronized ExtentReports getReporter() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            reportPath = "test-output/ExtentReport_" + timestamp + ".html";
            new File("test-output").mkdirs();

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Zurich QA Automation Demo");
            sparkReporter.config().setReportName("Selenium + TestNG Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Framework", "Selenium 4 / TestNG / ExtentReports");
        }
        return extent;
    }

    public static String getReportPath() {
        return reportPath;
    }

    public static void createTestIfNotExists(String testName) {
        testMap.computeIfAbsent(testName, name -> getReporter().createTest(name));
        currentTest.set(testMap.get(testName));
    }

    public static ExtentTest getTest() {
        return currentTest.get();
    }

    public static void removeTest() {
        currentTest.remove();
    }

    public static synchronized void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}