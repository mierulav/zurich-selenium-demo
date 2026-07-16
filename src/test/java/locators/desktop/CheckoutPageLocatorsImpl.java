package locators.desktop;

import locators.CheckoutPageLocators;
import org.openqa.selenium.By;

/**
 * Checkout markup on SauceDemo is identical for desktop and mobile viewports,
 * so LocatorFactory intentionally returns this single implementation for both
 * Platform values instead of duplicating an identical mobile class -
 * demonstrating that the strategy pattern is applied where it adds value,
 * not applied mechanically everywhere.
 */
public class CheckoutPageLocatorsImpl implements CheckoutPageLocators {

    @Override
    public By checkoutButton() {
        return By.id("checkout");
    }

    @Override
    public By firstNameField() {
        return By.id("first-name");
    }

    @Override
    public By lastNameField() {
        return By.id("last-name");
    }

    @Override
    public By postalCodeField() {
        return By.id("postal-code");
    }

    @Override
    public By continueButton() {
        return By.id("continue");
    }

    @Override
    public By finishButton() {
        return By.id("finish");
    }

    @Override
    public By summaryItemSubTotal() {
        return By.className("summary_subtotal_label");
    }

    @Override
    public By summaryItemTotal() {
        return By.className("summary_total_label");
    }

    @Override
    public By completeHeader() {
        return By.className("complete-header");
    }
}
