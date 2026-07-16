package pages;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ExtentReportManager;

import java.time.Duration;
import java.util.Base64;

/**
 * Abstract base for every Module/Page class. Holds the one thing every page
 * needs (a driver reference + a wait) and exposes the small set of reusable
 * element operations so individual Page classes stay focused on workflow
 * logic instead of re-implementing waits/clicks everywhere.
 */
public abstract class Common {

    protected final WebDriver driver;

    protected final Duration shortWait = Duration.ofSeconds(5);
    protected final Duration longWait = Duration.ofSeconds(15);

    protected Common(WebDriver driver) {
        this.driver = driver;
    }

    // ---------- Logging ----------

    protected void logStep(String message, Status status) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(status, message);
        }
    }

    protected void logStepWithScreenshot(String stepName) {
        if (ExtentReportManager.getTest() != null) {
            String base64 = getScreenshotBase64();
            String imgTag = "<br><b>" + stepName + "</b><br>"
                    + "<img src='data:image/png;base64," + base64 + "' style='max-width:25%; border:1px solid #ddd;'/><br>";
            ExtentReportManager.getTest().log(Status.INFO, imgTag,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
        }
    }

    public String getScreenshotBase64() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    // ---------- Element operations ----------

    public void click(By locator, Duration timeout) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void type(By locator, String text, Duration timeout) {
        WebElement element = new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator))
                .getText();
    }

    public boolean isDisplayed(By locator, Duration timeout) {
        try {
            return new WebDriverWait(driver, timeout)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForVisibility(By locator, Duration timeout) {
        new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void selectByVisibleText(By locator, String visibleText, Duration timeout) {
        WebElement dropdown = new WebDriverWait(driver, timeout)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        new org.openqa.selenium.support.ui.Select(dropdown).selectByVisibleText(visibleText);
    }

    /** Strips currency symbols (e.g. "$29.99" -> 29.99) so price text can be compared as a number. */
    protected java.math.BigDecimal parsePrice(String priceText) {
        String numeric = priceText.replace("$", "").trim();
        return new java.math.BigDecimal(numeric).setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
