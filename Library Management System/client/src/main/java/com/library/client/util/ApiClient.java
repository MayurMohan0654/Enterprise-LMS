package com.library.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.client.config.AppConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {
    
    private final String baseUrl;
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private String token;
    
    public ApiClient() {
        this.baseUrl = AppConfig.getApiBaseUrl();
        System.out.println("API Base URL: " + baseUrl); // Debug log
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(AppConfig.getConnectionTimeout()))
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    public <T> T get(String endpoint, Class<T> responseType) throws IOException, InterruptedException {
        HttpRequest request = createGetRequest(endpoint);
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return objectMapper.readValue(response.body(), responseType);
        } else {
            handleErrorResponse(response);
            return null;
        }
    }
    
    public <T> T post(String endpoint, Object requestBody, Class<T> responseType) throws IOException, InterruptedException {
        String jsonBody = objectMapper.writeValueAsString(requestBody);
        String fullUrl = baseUrl + endpoint;
        System.out.println("Making POST request to: " + fullUrl);
        
        HttpRequest request = createPostRequest(endpoint, jsonBody);
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response body: " + (response.body() != null ? response.body() : "empty"));
        
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            // Success response
            if (response.body() == null || response.body().isEmpty()) {
                // Some endpoints return empty body on success - this is not an error
                System.out.println("Successful request with empty response body");
                
                // If the expected return type is Object, return an empty HashMap to indicate success
                if (responseType == Object.class) {
                    return (T) new HashMap<>();
                }
                return null;
            }
            
            try {
                return objectMapper.readValue(response.body(), responseType);
            } catch (Exception e) {
                System.err.println("Error parsing response: " + e.getMessage());
                throw new IOException("Response parsing error: " + e.getMessage() + 
                                     ". Status code was: " + response.statusCode() + 
                                     " (this was a successful HTTP request)");
            }
        } else {
            handleErrorResponse(response);
            return null;
        }
    }
    
    private void handleErrorResponse(HttpResponse<String> response) throws IOException {
        String errorBody = response.body();
        String errorMessage = "HTTP error " + response.statusCode();
        
        if (errorBody != null && !errorBody.isEmpty()) {
            try {
                Map<String, Object> errorMap = objectMapper.readValue(errorBody, HashMap.class);
                if (errorMap.containsKey("error")) {
                    errorMessage += ": " + errorMap.get("error");
                } else if (errorMap.containsKey("message")) {
                    errorMessage += ": " + errorMap.get("message");
                }
            } catch (Exception e) {
                // If we can't parse the error JSON, just use the raw body
                errorMessage += ": " + errorBody;
            }
        }
        
        throw new IOException(errorMessage);
    }
    
    private HttpRequest createGetRequest(String endpoint) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .GET()
                .header("Content-Type", "application/json");
        
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        
        return builder.build();
    }
    
    private HttpRequest createPostRequest(String endpoint, String jsonBody) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json");
        
        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }
        
        return builder.build();
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}
