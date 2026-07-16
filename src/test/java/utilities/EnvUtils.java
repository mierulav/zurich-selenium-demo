package utilities;

public class EnvUtils {

    private EnvUtils() {
        // static-access only
    }

    public static String optionalEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    /** Secrets/credentials should never have a baked-in fallback - missing means fail loudly. */
    public static String requiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable '" + key + "' is not set. "
                            + "Set it in your shell/.env or IDE run configuration before running.");
        }
        return value;
    }
}