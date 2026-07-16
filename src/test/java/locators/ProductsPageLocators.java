package locators;

import org.openqa.selenium.By;

public interface ProductsPageLocators {
    By menuButton();
    By sortDropdown();
    By productNameByText(String productName);
    By addToCartButtonByProduct(String productName);
    By priceByProduct(String productName);
    By cartIcon();
    By cartBadge();
}
