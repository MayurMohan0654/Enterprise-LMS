package com.library.client.ui;

import com.library.client.model.Admin;
import com.library.client.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    
    private final AuthService authService;
    private JTabbedPane tabbedPane;
    
    public MainFrame() {
        this.authService = new AuthService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        // Handle window close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed content
        tabbedPane = new JTabbedPane();
        createTabs();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create logo and title
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Create user info and logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        Admin currentUser = authService.getCurrentUser();
        JLabel userLabel = new JLabel("User: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        
        userPanel.add(userLabel);
        userPanel.add(logoutButton);
        
        panel.add(userPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void createTabs() {
        // Books tab
        JPanel booksPanel = new BooksPanel();
        tabbedPane.addTab("Books", new ImageIcon(), booksPanel, "Manage Books");
        
        // Members tab
        JPanel membersPanel = new MembersPanel();
        tabbedPane.addTab("Members", new ImageIcon(), membersPanel, "Manage Members");
        
        // Borrowing tab
        JPanel borrowingPanel = new BorrowingPanel();
        tabbedPane.addTab("Borrowing", new ImageIcon(), borrowingPanel, "Manage Book Borrowing");
        
        // Fines tab
        JPanel finesPanel = new FinesPanel();
        tabbedPane.addTab("Fines", new ImageIcon(), finesPanel, "Manage Fines");
        
        // Admin tab - only show for admin role
        if (authService.getCurrentUser().getRole() == Admin.Role.ADMIN) {
            JPanel adminPanel = new AdminPanel();
            tabbedPane.addTab("Admin", new ImageIcon(), adminPanel, "Admin Settings");
        }
    }
    
    private JPanel createStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel statusLabel = new JLabel("Ready");
        panel.add(statusLabel, BorderLayout.WEST);
        
        JLabel versionLabel = new JLabel("Version 1.0");
        panel.add(versionLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void handleLogout() {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            authService.logout();
            
            // Open login window
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            
            // Close this window
            dispose();
        }
    }
    
    private void handleExit() {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    // Placeholder panel classes for the tabs
    private class BooksPanel extends JPanel {
        public BooksPanel() {
            setLayout(new BorderLayout());
            // TODO: Implement books management UI
            add(new JLabel("Books Management", JLabel.CENTER));
        }
    }
    
    private class MembersPanel extends JPanel {
        public MembersPanel() {
            setLayout(new BorderLayout());
            // TODO: Implement members management UI
            add(new JLabel("Members Management", JLabel.CENTER));
        }
    }
    
    private class BorrowingPanel extends JPanel {
        public BorrowingPanel() {
            setLayout(new BorderLayout());
            // TODO: Implement borrowing management UI
            add(new JLabel("Borrowing Management", JLabel.CENTER));
        }
    }
    
    private class FinesPanel extends JPanel {
        public FinesPanel() {
            setLayout(new BorderLayout());
            // TODO: Implement fines management UI
            add(new JLabel("Fines Management", JLabel.CENTER));
        }
    }
    
    private class AdminPanel extends JPanel {
        public AdminPanel() {
            setLayout(new BorderLayout());
            // TODO: Implement admin settings UI
            add(new JLabel("Admin Settings", JLabel.CENTER));
        }
    }
}
