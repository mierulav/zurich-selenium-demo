package locators.desktop;

import locators.LoginPageLocators;
import org.openqa.selenium.By;

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
