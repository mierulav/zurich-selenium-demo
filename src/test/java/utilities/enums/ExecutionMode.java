package utilities.enums;

public enum ExecutionMode {
    LOCAL,
    EMULATOR,
    BROWSERSTACK;

    public static ExecutionMode fromString(String value) {
        if (value == null || value.isBlank()) {
            return LOCAL;
        }
        try {
            return ExecutionMode.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported executionMode: " + value);
        }
    }
}