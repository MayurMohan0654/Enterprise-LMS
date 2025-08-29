package com.library.controller;

import com.library.model.Admin;
import com.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Integer id) {
        return adminService.getAdminById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        try {
            Admin newAdmin = adminService.createAdmin(admin);
            return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody Admin admin) {
        try {
            Admin updatedAdmin = adminService.updateAdmin(id, admin);
            return ResponseEntity.ok(updatedAdmin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok(Map.of("message", "Admin deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/register-librarian")
    public ResponseEntity<?> registerLibrarian(@RequestBody Admin admin) {
        try {
            // Ensure the role is set to Librarian
            admin.setRole(Admin.AdminRole.Librarian);
            
            Admin newLibrarian = adminService.createAdmin(admin);
            return new ResponseEntity<>(newLibrarian, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAdmin(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            if (username == null || password == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
            }
            
            Admin admin = adminService.authenticateAdmin(username, password);
            
            if (admin != null) {
                return ResponseEntity.ok(admin);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
