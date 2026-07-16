package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utilities.ConfigReader;
import utilities.DriverFactory;
import utilities.enums.Platform;

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
    @Parameters({"browser", "platform"})
    public void setUpMethod(@Optional("chrome") String browser, @Optional("desktop_web") String platformParam) {
        Platform platformEnum = Platform.fromString(platformParam);
        platform.set(platformEnum);
        driver.set(DriverFactory.getDriver(browser, platformEnum));
        getWebDriver().get(ConfigReader.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMethod(ITestResult result) {
        DriverFactory.quitDriver();
        driver.remove();
        platform.remove();
    }
}
