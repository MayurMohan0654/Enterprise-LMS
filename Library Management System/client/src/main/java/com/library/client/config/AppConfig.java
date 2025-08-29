package com.library.client.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
    
    /**
     * Tests the connection to the backend server
     * @return true if connection is successful, false otherwise
     */
    public static boolean testBackendConnection() {
        try {
            URL url = new URL(getApiBaseUrl() + "/health");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(getConnectionTimeout());
            connection.setReadTimeout(getReadTimeout());
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
        } catch (IOException e) {
            System.err.println("Failed to connect to backend: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Main method to run a simple test of the configuration
     */
    public static void main(String[] args) {
        System.out.println("API Base URL: " + getApiBaseUrl());
        System.out.println("Connection Timeout: " + getConnectionTimeout() + "ms");
        System.out.println("Read Timeout: " + getReadTimeout() + "ms");
        System.out.println("Backend Connection Test: " + (testBackendConnection() ? "SUCCESS" : "FAILED"));
    }
}
