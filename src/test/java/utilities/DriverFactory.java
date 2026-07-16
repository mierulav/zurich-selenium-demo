package utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utilities.enums.Platform;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe driver creation/teardown.
 * ThreadLocal keeps each TestNG worker thread on its own WebDriver instance,
 * which is what allows this suite to run with parallel="methods"/"classes"
 * in emulator-test.xml without threads stepping on each other's browser sessions.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
        // static-access only
    }

    public static WebDriver getDriver(String browser, Platform platform) {
        if (driver.get() == null) {
            driver.set(createDriver(browser, platform));
        }
        return driver.get();
    }

    private static WebDriver createDriver(String browser, Platform platform) {
        WebDriver newDriver;

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                newDriver = new FirefoxDriver(new FirefoxOptions());
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                newDriver = new ChromeDriver(buildChromeOptions(platform));
                break;
        }

        int implicitWait = Integer.parseInt(ConfigReader.get("implicitWaitSeconds", "5"));
        newDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        if (platform == Platform.DESKTOP_WEB) {
            newDriver.manage().window().maximize();
        }
        return newDriver;
    }

    /**
     * MOBILE_WEB uses Chrome's device-emulation metrics so the same suite can
     * exercise the responsive mobile layout without needing a real device/Appium.
     */
    private static ChromeOptions buildChromeOptions(Platform platform) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-features=PasswordLeakDetection,PasswordManagerOnboarding,AutofillServerCommunication");
        options.addArguments("--disable-save-password-bubble");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        prefs.put("autofill.profile_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches", new String[] {"enable-automation"});

        if (platform == Platform.MOBILE_WEB) {
            Map<String, Object> deviceMetrics = new HashMap<>();
            deviceMetrics.put("width", 390);
            deviceMetrics.put("height", 844);
            deviceMetrics.put("pixelRatio", 3.0);

            Map<String, Object> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceMetrics", deviceMetrics);
            mobileEmulation.put("userAgent",
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 "
                            + "(KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1");

            options.setExperimentalOption("mobileEmulation", mobileEmulation);
        }
        return options;
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
