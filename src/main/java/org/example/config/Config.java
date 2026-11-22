package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Файл config.properties не найден. Используются значения по умолчанию.");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getDefaultTtlDays() {
        return Integer.parseInt(properties.getProperty("link.default.ttl.days", "1"));
    }

    public static int getDefaultMaxClicks() {
        return Integer.parseInt(properties.getProperty("link.default.max.clicks", "5"));
    }
}