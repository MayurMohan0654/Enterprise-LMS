package com.library.client;

import javax.swing.*;
import java.awt.*;

public class LibraryManagementClient {
    
    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and display the main application window
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Library Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);
            frame.setLocationRelativeTo(null);
            
            // TODO: Add main panel content here
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(new JLabel("Library Management System", JLabel.CENTER), BorderLayout.CENTER);
            
            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
