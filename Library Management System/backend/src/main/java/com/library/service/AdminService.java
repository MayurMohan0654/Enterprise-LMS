package com.library.service;

import com.library.model.Admin;
import com.library.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    public Optional<Admin> getAdminById(Integer id) {
        return adminRepository.findById(id);
    }
    
    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
    
    public Admin createAdmin(Admin admin) {
        if (adminRepository.existsByUsername(admin.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Encrypt the password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        return adminRepository.save(admin);
    }
    
    public Admin updateAdmin(Integer id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Admin not found with id " + id));
        
        // Only update username if it's changed and not already taken
        if (!admin.getUsername().equals(adminDetails.getUsername())) {
            if (adminRepository.existsByUsername(adminDetails.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            admin.setUsername(adminDetails.getUsername());
        }
        
        // Only update password if provided
        if (adminDetails.getPassword() != null && !adminDetails.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
        }
        
        admin.setRole(adminDetails.getRole());
        
        return adminRepository.save(admin);
    }
    
    public void deleteAdmin(Integer id) {
        Admin admin = adminRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Admin not found with id " + id));
        adminRepository.delete(admin);
    }
}
