package locators;

import org.openqa.selenium.By;

/**
 * Contract for the Login page. Concrete strategies (desktop/mobile) implement
 * this so ProductsPage/LoginPage never care which viewport strategy is active.
 */
public interface LoginPageLocators {
    By usernameField();
    By passwordField();
    By loginButton();
    By errorMessage();
}
