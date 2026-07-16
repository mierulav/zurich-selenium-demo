package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utilities.ConfigReader;
import utilities.DriverFactory;
import utilities.enums.Platform;

import java.util.Map;

public abstract class BasePage {

    private final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private final ThreadLocal<Platform> platform = new ThreadLocal<>();

    public WebDriver getWebDriver() {
        return driver.get();
    }

    public Platform getPlatform() {
        return platform.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpMethod(ITestContext context) {
        Map<String, String> params = context.getCurrentXmlTest().getAllParameters();
        Platform platformEnum = Platform.fromString(params.get("platform"));
        platform.set(platformEnum);
        // using switch to determine driver
        switch (platformEnum) {
            case ANDROID:
                driver.set(DriverFactory.createAndroidDriver(params));
                break;
            case DESKTOP_WEB:
            case MOBILE_WEB:
                driver.set(DriverFactory.getDriver(params.get("browser"), platformEnum));
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platformEnum);
        }
        getWebDriver().get(ConfigReader.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMethod(ITestResult result) {
        DriverFactory.quitDriver();
        driver.remove();
        platform.remove();
    }
}
