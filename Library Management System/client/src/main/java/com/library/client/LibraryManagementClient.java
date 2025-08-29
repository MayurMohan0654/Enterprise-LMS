package com.library.client;

import com.library.client.ui.LoginFrame;
import com.library.client.util.ThemeUtil;

import javax.swing.*;

public class LibraryManagementClient {
    
    public static void main(String[] args) {
        // Set look and feel
        ThemeUtil.setSystemLookAndFeel();
        
        // Create and display the login window
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}