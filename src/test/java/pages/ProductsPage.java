package pages;

import locators.ProductsPageLocators;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utilities.LocatorFactory;
import utilities.enums.Platform;

import java.math.BigDecimal;

public class ProductsPage extends Common {

    private final ProductsPageLocators locators;

    public ProductsPage(WebDriver driver, Platform platform) {
        super(driver);
        this.locators = LocatorFactory.getProductsPageLocators(platform);
    }

    public void addProductToCart(String productName) {
        waitForVisibility(locators.productNameByText(productName), longWait);
        click(locators.addToCartButtonByProduct(productName), longWait);
        logStepWithScreenshot("Added to cart: " + productName);
    }

    public BigDecimal getDisplayedPrice(String productName) {
        String priceText = getText(locators.priceByProduct(productName), longWait);
        return parsePrice(priceText);
    }

    public void verifyDisplayedPriceMatchesExpected(String productName, BigDecimal expectedPrice) {
        BigDecimal actualPrice = getDisplayedPrice(productName);
        Assert.assertEquals(actualPrice, expectedPrice,
                "Displayed price for '" + productName + "' did not match expected DB price");
    }

    public void sortBy(String sortOption) {
        selectByVisibleText(locators.sortDropdown(), sortOption, longWait);
        logStepWithScreenshot("Sorted products by: " + sortOption);
    }

    public void goToCart() {
        click(locators.cartIcon(), longWait);
    }

    public int getCartItemCount() {
        if (!isDisplayed(locators.cartBadge(), shortWait)) {
            return 0;
        }
        return Integer.parseInt(getText(locators.cartBadge(), shortWait));
    }
}
