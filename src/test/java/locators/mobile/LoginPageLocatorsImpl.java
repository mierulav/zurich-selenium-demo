package locators.mobile;

import locators.LoginPageLocators;
import org.openqa.selenium.By;

/**
 * SauceDemo's DOM/IDs don't actually change between viewports (it's a
 * CSS-only responsive layout), so today this mirrors the desktop strategy.
 * The point of keeping a dedicated implementation class (rather than reusing
 * desktop's) is scalability: the moment a real app under test diverges its
 * markup for mobile (a common occurrence), only this class needs to change -
 * LoginPage/LocatorFactory/callers stay untouched.
 */
public class LoginPageLocatorsImpl implements LoginPageLocators {

    @Override
    public By usernameField() {
        return By.id("user-name");
    }

    @Override
    public By passwordField() {
        return By.id("password");
    }

    @Override
    public By loginButton() {
        return By.id("login-button");
    }

    @Override
    public By errorMessage() {
        return By.cssSelector("[data-test='error']");
    }
}
