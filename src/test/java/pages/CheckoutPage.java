package pages;

import locators.CheckoutPageLocators;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utilities.LocatorFactory;
import utilities.enums.Platform;

import java.math.BigDecimal;

public class CheckoutPage extends Common {

    private final CheckoutPageLocators locators;

    public CheckoutPage(WebDriver driver, Platform platform) {
        super(driver);
        this.locators = LocatorFactory.getCheckoutPageLocators(platform);
    }

    public void proceedToCheckout() {
        click(locators.checkoutButton(), longWait);
    }

    public void fillShippingInfo(String firstName, String lastName, String postalCode) {
        type(locators.firstNameField(), firstName, longWait);
        type(locators.lastNameField(), lastName, longWait);
        type(locators.postalCodeField(), postalCode, longWait);
        logStepWithScreenshot("Filled shipping information");
        click(locators.continueButton(), longWait);
    }

    /** Text Displayed as "Total: $xx.xx" - strip the label before parsing as a number. */
    public BigDecimal getItemTotal() {
        String rawText = getText(locators.summaryItemTotal(), longWait);
        String numericPart = rawText.replaceAll("[^0-9.]", "");
        return new BigDecimal(numericPart).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /** Text Displayed as "Item total: $xx.xx" - strip the label before parsing as a number. */
    public BigDecimal getItemSubTotal() {
        String rawText = getText(locators.summaryItemSubTotal(), longWait);
        String numericPart = rawText.replaceAll("[^0-9.]", "");
        return new BigDecimal(numericPart).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public void verifyItemTotalNotIncludecTaxMatchesExpected(BigDecimal expectedTotal) {
        BigDecimal actualTotal = getItemSubTotal();
        Assert.assertEquals(actualTotal, expectedTotal,
                "Checkout item total did not match the DB-expected total");
    }

    public void finishOrder() {
        click(locators.finishButton(), longWait);
    }

    public void verifyOrderConfirmed() {
        Assert.assertTrue(isDisplayed(locators.completeHeader(), longWait),
                "Order completion header was not displayed");
        String header = getText(locators.completeHeader(), longWait);
        Assert.assertTrue(header.toLowerCase().contains("thank you"),
                "Expected order confirmation header, got: " + header);
        logStepWithScreenshot("Order confirmed: " + header);
    }
}
