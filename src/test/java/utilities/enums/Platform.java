package utilities.enums;

public enum Platform {
    DESKTOP_WEB,
    MOBILE_WEB,
    ANDROID;

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
