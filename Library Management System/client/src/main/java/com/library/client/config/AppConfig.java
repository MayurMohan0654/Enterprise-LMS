package com.library.client.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";
    
    static {
        try {
            // Try to load from classpath first
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
        } catch (Exception e) {
            // If not found in classpath, try to load from file system
            try {
                properties.load(new FileInputStream(CONFIG_FILE));
            } catch (IOException ex) {
                System.err.println("Could not load configuration file: " + ex.getMessage());
                // Set default values
                properties.setProperty("api.base.url", "http://localhost:8080/api");
                properties.setProperty("connection.timeout", "5000");
                properties.setProperty("read.timeout", "5000");
            }
        }
    }
    
    public static String getApiBaseUrl() {
        return properties.getProperty("api.base.url");
    }
    
    public static int getConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("connection.timeout"));
    }
    
    public static int getReadTimeout() {
        return Integer.parseInt(properties.getProperty("read.timeout"));
    }
}
