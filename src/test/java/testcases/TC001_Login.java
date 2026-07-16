package testcases;

import base.BasePage;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.LoginPage;
import utilities.CsvDataProvider;

public class TC001_Login extends BasePage {

    @Test(dataProvider = "loginData",
            dataProviderClass = CsvDataProvider.class,
            groups = {"data-driven"},
            description = "Check all possible login scenarios (data-driven)")
    public void TC001_Login_DataDriven(String username, String password, String expectedResult) {
        LoginPage loginPage = new LoginPage(getWebDriver(), getPlatform());
        loginPage.login(username, password);

        switch (expectedResult) {
            case "SUCCESS" ->
                    Assert.assertTrue(getWebDriver().getCurrentUrl().contains("inventory.html"),
                    "Expected successful login to land on inventory page for user: " + username);

            case "LOCKED_OUT" ->
                    loginPage.verifyErrorMessageContains("locked out");

            case "INVALID_CREDENTIALS" ->
                    loginPage.verifyErrorMessageContains("do not match");

            default ->
                    Assert.fail("Unknown expectedResult in test data: " + expectedResult);
        }
    }

    @Test(description = "Verify that login fails with empty credentials")
    public void TC001_Login_FailedAttempt() {
        LoginPage loginPage = new LoginPage(getWebDriver(), getPlatform());
        loginPage.login("Ayah", "Ibu");
        loginPage.verifyErrorMessageContains("do not match");
    }

    @Test(description = "Verify that login is successful with valid credentials")
    @Parameters({"username", "password"})
    public void TC001_Login_SuccessfulLogin(String username, String password) {
        LoginPage loginPage = new LoginPage(getWebDriver(), getPlatform());
        loginPage.login(username, password);
        Assert.assertTrue(getWebDriver().getCurrentUrl().contains("inventory.html"),
                "Expected successful login to land on inventory page");
    }
}
