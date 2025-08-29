package com.library.client.service;

import com.library.client.model.Admin;
import com.library.client.util.ApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    
    private final ApiClient apiClient;
    private Admin currentUser;
    
    public AuthService() {
        this.apiClient = new ApiClient();
    }
    
    public Admin login(String username, String password) {
        try {
            // For now, we'll simulate authentication
            // In a real app, this would call the backend API
            
            if ("admin".equals(username) && "password".equals(password)) {
                currentUser = new Admin();
                currentUser.setUsername(username);
                currentUser.setRole(Admin.Role.ADMIN);
                return currentUser;
            } else if ("librarian".equals(username) && "password".equals(password)) {
                currentUser = new Admin();
                currentUser.setUsername(username);
                currentUser.setRole(Admin.Role.LIBRARIAN);
                return currentUser;
            }
            
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Admin loginAsLibrarian(String username, String password) {
        try {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);
            
            // Make actual API call to authenticate
            Admin admin = apiClient.post("/admins/authenticate", credentials, Admin.class);
            
            // Check if the admin has the librarian role
            if (admin != null && admin.getRole() == Admin.Role.LIBRARIAN) {
                this.currentUser = admin;
                return admin;
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public Admin getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        currentUser = null;
        // In a real app, you might want to invalidate the token on the server
    }
    
    public boolean register(String username, String password, String role) {
        try {
            // This would call the backend API in a real application
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean registerLibrarian(String username, String password) {
        try {
            // Create a map with the required fields
            Map<String, Object> registrationData = new HashMap<>();
            registrationData.put("username", username);
            registrationData.put("password", password);
            registrationData.put("role", "Librarian");
            
            // Send registration request
            try {
                Object response = apiClient.post("/admins/register-librarian", registrationData, Object.class);
                // If we get here, the request was successful (no exception thrown)
                return true;
            } catch (IOException e) {
                // Check if this is actually a success but with parsing issues
                if (e.getMessage().contains("201") || e.getMessage().contains("200")) {
                    // HTTP 201 Created or 200 OK - this is actually a success
                    return true;
                }
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
