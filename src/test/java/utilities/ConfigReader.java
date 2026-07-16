package utilities;

import utilities.enums.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class ConfigReader {

    private static final Properties defaultProps = new Properties();
    private static volatile boolean defaultLoaded = false;

    private static final Map<Platform, Properties> platformPropsCache = new ConcurrentHashMap<>();

    private ConfigReader() {
        // static-access only
    }

    private static synchronized void loadDefault() {
        if (defaultLoaded) {
            return;
        }
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            defaultProps.load(is);
            defaultLoaded = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private static Properties loadPlatformProps(Platform platform) {
        return platformPropsCache.computeIfAbsent(platform, p -> {
            String fileName = (p == Platform.ANDROID) ? "androidConfig.properties" : "iosConfig.properties";
            Properties properties = new Properties();
            try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
                if (is == null) {
                    throw new RuntimeException(fileName + " not found on classpath");
                }
                properties.load(is);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load " + fileName, e);
            }
            return properties;
        });
    }

    public static String get(String key) {
        loadDefault();
        return defaultProps.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        loadDefault();
        return defaultProps.getProperty(key, defaultValue);
    }

    /** Platform-specific settings (deviceName, platformVersion, etc.) - never mixed with config.properties. */
    public static String get(String key, Platform platform) {
        return loadPlatformProps(platform).getProperty(key);
    }

    public static String get(String key, String defaultValue, Platform platform) {
        return loadPlatformProps(platform).getProperty(key, defaultValue);
    }
}