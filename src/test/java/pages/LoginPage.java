package pages;

import locators.LoginPageLocators;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utilities.LocatorFactory;
import utilities.enums.Platform;

public class LoginPage extends Common {

    private final LoginPageLocators locators;

    public LoginPage(WebDriver driver, Platform platform) {
        super(driver);
        this.locators = LocatorFactory.getLoginPageLocators(platform);
    }

    public void login(String username, String password) {
        type(locators.usernameField(), username, longWait);
        type(locators.passwordField(), password, longWait);
        logStepWithScreenshot("Entered credentials for user: " + username);
        click(locators.loginButton(), longWait);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(locators.errorMessage(), shortWait);
    }

    public void verifyErrorMessageContains(String expectedMessageFragment) {
        Assert.assertTrue(isErrorDisplayed(), "Expected Error Message but None Displayed");
        String actualMessage = getText(locators.errorMessage(), shortWait);
        Assert.assertTrue(actualMessage.contains(expectedMessageFragment),
                "Error Message -> Expected: '" + expectedMessageFragment + "' Actual: '" + actualMessage + "'");
        logStepWithScreenshot("Verified error message: " + actualMessage);
    }
}
