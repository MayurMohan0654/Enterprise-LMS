package com.library.client.ui;

import com.library.client.service.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegistrationDialog extends JDialog {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton cancelButton;
    private final AuthService authService;
    
    public RegistrationDialog(Frame parent) {
        super(parent, "Register as Librarian", true);
        this.authService = new AuthService();
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("Register as Librarian", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField(15);
        
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(confirmLabel);
        formPanel.add(confirmPasswordField);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        cancelButton = new JButton("Cancel");
        registerButton = new JButton("Register");
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(registerButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Add event handlers
        registerButton.addActionListener(e -> handleRegistration());
        cancelButton.addActionListener(e -> dispose());
        
        // Allow registration on Enter key
        getRootPane().setDefaultButton(registerButton);
    }
    
    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Basic validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Disable the register button to prevent multiple submissions
        registerButton.setEnabled(false);
        registerButton.setText("Registering...");
        
        // Use SwingWorker to perform the registration in a background thread
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authService.registerLibrarian(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(RegistrationDialog.this,
                                "Librarian registration successful",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(RegistrationDialog.this,
                                "Registration failed",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RegistrationDialog.this,
                            "Error connecting to server: " + ex.getMessage(),
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    registerButton.setEnabled(true);
                    registerButton.setText("Register");
                }
            }
        };
        
        worker.execute();
    }
}