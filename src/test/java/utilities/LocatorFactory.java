package utilities;

import locators.CheckoutPageLocators;
import locators.LoginPageLocators;
import locators.ProductsPageLocators;
import utilities.enums.Platform;

/**
 * Central place that decides which locator-strategy implementation to hand
 * back for a given Platform. Page/Module classes never instantiate a
 * locators.desktop.* or locators.mobile.* class directly - they only depend
 * on the interfaces, so adding a new platform (e.g. TABLET_WEB) later means
 * touching this factory + adding one impl package, nothing else.
 */
public class LocatorFactory {

    private LocatorFactory() {
        // static-access only
    }

    public static LoginPageLocators getLoginPageLocators(Platform platform) {
        switch (platform) {
            case DESKTOP_WEB:
            case ANDROID:
                return new locators.desktop.LoginPageLocatorsImpl();
            case MOBILE_WEB:
                return new locators.mobile.LoginPageLocatorsImpl();
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    public static ProductsPageLocators getProductsPageLocators(Platform platform) {
        switch (platform) {
            case DESKTOP_WEB:
            case ANDROID:
                return new locators.desktop.ProductsPageLocatorsImpl();
            case MOBILE_WEB:
                return new locators.mobile.ProductsPageLocatorsImpl();
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    public static CheckoutPageLocators getCheckoutPageLocators(Platform platform) {
        switch (platform) {
            case DESKTOP_WEB:
            case ANDROID:
                return new locators.desktop.CheckoutPageLocatorsImpl();
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
}
