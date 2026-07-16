package locators;

import org.openqa.selenium.By;

public interface CheckoutPageLocators {
    By checkoutButton();
    By firstNameField();
    By lastNameField();
    By postalCodeField();
    By continueButton();
    By finishButton();
    By summaryItemSubTotal();
    By summaryItemTotal();
    By completeHeader();
}
