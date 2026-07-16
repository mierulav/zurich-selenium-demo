package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties props = new Properties();
    private static boolean loaded = false;

    private ConfigReader() {
        // static-access only
    }

    private static synchronized void load() {
        if (loaded) {
            return;
        }
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            props.load(is);
            loaded = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        load();
        return props.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        load();
        return props.getProperty(key, defaultValue);
    }
}
