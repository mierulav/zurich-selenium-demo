package testcases;

import base.BasePage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CheckoutPage;
import pages.LoginPage;
import pages.ProductsPage;
import utilities.DBUtils;

import java.math.BigDecimal;
import java.util.List;

public class TC002_CheckoutFlow extends BasePage {

    private static final String PRODUCT_1 = "Sauce Labs Backpack";
    private static final String PRODUCT_2 = "Sauce Labs Bike Light";

    @BeforeClass(alwaysRun = true)
    public void seedDatabase() {
        // Setup db connection
        DBUtils.verifyDatabaseReady();
    }

    @Test(description = "End-to-end checkout flow validating UI prices/total against SQL-sourced expected values")
    public void TC002_Checkout_PriceValidatedAgainstDB() {
        LoginPage loginPage = new LoginPage(getWebDriver(), getPlatform());
        loginPage.login("standard_user", "secret_sauce");
        ProductsPage productsPage = new ProductsPage(getWebDriver(), getPlatform());

        // Validate displayed prices against SQL-sourced expected prices BEFORE adding to cart
        productsPage.verifyDisplayedPriceMatchesExpected(PRODUCT_1, DBUtils.getExpectedPrice(PRODUCT_1));
        productsPage.verifyDisplayedPriceMatchesExpected(PRODUCT_2, DBUtils.getExpectedPrice(PRODUCT_2));

        // Add products to Cart
        productsPage.addProductToCart(PRODUCT_1);
        productsPage.addProductToCart(PRODUCT_2);
        productsPage.goToCart();

        // Proceed Checkout
        CheckoutPage checkoutPage = new CheckoutPage(getWebDriver(), getPlatform());
        checkoutPage.proceedToCheckout();
        checkoutPage.fillShippingInfo("Amirul", "QA", "63000");

        // Validate Total Price not including tax against SQL-sourced expected total
        BigDecimal expectedTotal = DBUtils.getExpectedTotal(List.of(PRODUCT_1, PRODUCT_2));
        checkoutPage.verifyItemTotalNotIncludecTaxMatchesExpected(expectedTotal);

        // Complete Order
        checkoutPage.finishOrder();
        checkoutPage.verifyOrderConfirmed();
    }
}
