package locators.mobile;

import locators.ProductsPageLocators;
import org.openqa.selenium.By;

public class ProductsPageLocatorsImpl implements ProductsPageLocators {

    private String slugify(String productName) {
        return productName.toLowerCase().replace(" ", "-").replace(".", "");
    }

    @Override
    public By menuButton() {
        return By.id("react-burger-menu-btn");
    }

    @Override
    public By sortDropdown() {
        return By.className("product_sort_container");
    }

    @Override
    public By productNameByText(String productName) {
        return By.xpath("//div[@class='inventory_item_name' and text()='" + productName + "']");
    }

    @Override
    public By addToCartButtonByProduct(String productName) {
        return By.id("add-to-cart-" + slugify(productName));
    }

    @Override
    public By priceByProduct(String productName) {
        return By.xpath("//div[@class='inventory_item_name' and text()='" + productName
                + "']/ancestor::div[@class='inventory_item']//div[@class='inventory_item_price']");
    }

    @Override
    public By cartIcon() {
        return By.className("shopping_cart_link");
    }

    @Override
    public By cartBadge() {
        return By.className("shopping_cart_badge");
    }
}
