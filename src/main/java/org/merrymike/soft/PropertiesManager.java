package org.merrymike.soft;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class PropertiesManager extends Properties{
    private static final Properties properties = new Properties();
    static {
        try {
            properties.load(new FileInputStream(Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                    .getResource("")).getPath() + "application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties getProperties() {
        return properties;
    }

}
