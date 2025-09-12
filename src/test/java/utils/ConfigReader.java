package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties P = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            P.load(fis);
        } catch (IOException e) {
            // defaults
            P.setProperty("baseUrl", "https://demoblaze.com");
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        return P.getProperty(key);
    }
}
