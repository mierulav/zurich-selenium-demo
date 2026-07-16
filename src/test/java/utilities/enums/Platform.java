package utilities.enums;

/**
 * Represents the "form factor" the test should run against.
 * Mirrors the ANDROID/IOS split used in mobile frameworks, but here it
 * drives Chrome's responsive/device-emulation mode so the same LocatorFactory
 * + interface-based locator-strategy pattern can be showcased on web.
 */
public enum Platform {
    DESKTOP_WEB,
    MOBILE_WEB;

    public static Platform fromString(String value) {
        if (value == null || value.isBlank()) {
            return DESKTOP_WEB;
        }
        try {
            return Platform.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported platform: " + value);
        }
    }
}
