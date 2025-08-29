package com.library.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Admin {
    
    @JsonProperty("admin_id")
    private Integer id;
    
    private String username;
    private String password;
    
    @JsonProperty("role")
    private Role role;
    
    public enum Role {
        ADMIN, LIBRARIAN
    }
    
    // Getters and setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    // Add special method for handling role string from server
    @JsonProperty("role")
    public void setRoleFromString(String role) {
        if (role != null) {
            if (role.equalsIgnoreCase("Admin")) {
                this.role = Role.ADMIN;
            } else if (role.equalsIgnoreCase("Librarian")) {
                this.role = Role.LIBRARIAN;
            }
        }
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
