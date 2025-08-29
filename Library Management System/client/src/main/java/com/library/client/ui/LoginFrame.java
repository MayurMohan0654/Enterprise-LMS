package com.library.client.ui;

import com.library.client.service.AuthService;
import com.library.client.util.ThemeUtil;
import com.library.client.model.Admin;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private final AuthService authService;
    
    public LoginFrame() {
        this.authService = new AuthService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library Management System - Librarian Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Library Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        JLabel subHeaderLabel = new JLabel("Librarian Access Portal", JLabel.CENTER);
        subHeaderLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        headerPanel.add(subHeaderLabel, BorderLayout.SOUTH);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Login panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField = new JTextField(15);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField = new JPasswordField(15);
        
        loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register as Librarian");
        
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(registerButton);
        loginPanel.add(loginButton);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(loginPanel, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JLabel footerLabel = new JLabel("© 2025 Library Management System", JLabel.CENTER);
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        mainPanel.add(footerLabel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Add event handlers
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegistrationDialog());
        
        // Allow login on Enter key
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Disable login button to prevent multiple attempts
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        
        // Use SwingWorker to perform login in a background thread
        SwingWorker<Admin, Void> worker = new SwingWorker<>() {
            @Override
            protected Admin doInBackground() throws Exception {
                return authService.loginAsLibrarian(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    Admin librarian = get();
                    if (librarian != null) {
                        openLibrarianHome(librarian);
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "Invalid username or password",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE);
                        loginButton.setEnabled(true);
                        loginButton.setText("Login");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Error connecting to server: " + ex.getMessage(),
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                    loginButton.setEnabled(true);
                    loginButton.setText("Login");
                }
            }
        };
        
        worker.execute();
    }
    
    private void openRegistrationDialog() {
        RegistrationDialog dialog = new RegistrationDialog(this);
        dialog.setVisible(true);
    }
    
    private void openLibrarianHome(Admin librarian) {
        // Hide login window
        setVisible(false);
        
        // Open librarian home window
        LibrarianHomeFrame librarianFrame = new LibrarianHomeFrame(librarian);
        librarianFrame.setVisible(true);
        
        // Dispose login window
        dispose();
    }
}
