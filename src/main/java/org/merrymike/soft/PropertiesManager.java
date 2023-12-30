package org.merrymike.soft;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager extends Properties{
    private static final Properties properties = new Properties();
    static {
        try (InputStream input = PropertiesManager.class.getResourceAsStream("/application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties getProperties() {
        return properties;
    }

}
