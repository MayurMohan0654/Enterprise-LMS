package com.library.client.ui;

import com.library.client.model.Admin;
import com.library.client.service.AuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LibrarianHomeFrame extends JFrame {

    private final AuthService authService;
    private final Admin librarian;
    private JPanel contentPanel;
    
    public LibrarianHomeFrame(Admin librarian) {
        this.authService = new AuthService();
        this.librarian = librarian;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library Management System - Librarian Portal");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 600));
        
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
        
        // Create sidebar navigation and content panel
        JSplitPane splitPane = createSplitPane();
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Create status bar
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(51, 102, 153));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Create logo and title
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.WEST);
        
        // Create user info and logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("Librarian: " + librarian.getUsername());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        
        userPanel.add(userLabel);
        userPanel.add(logoutButton);
        
        panel.add(userPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JSplitPane createSplitPane() {
        // Create navigation panel
        JPanel navigationPanel = createNavigationPanel();
        
        // Create content panel
        contentPanel = new JPanel(new CardLayout());
        
        // Add pages to content panel
        contentPanel.add(new BorrowBookPanel(), "borrowBook");
        contentPanel.add(new ReturnBookPanel(), "returnBook");
        contentPanel.add(new ManageMembersPanel(), "manageMembers");
        contentPanel.add(new ManageBooksPanel(), "manageBooks");
        contentPanel.add(new ManageFinesPanel(), "manageFines");
        contentPanel.add(new ReportsPanel(), "reports");
        
        // Show default panel
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, "borrowBook");
        
        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigationPanel, contentPanel);
        splitPane.setDividerLocation(220);
        splitPane.setDividerSize(1);
        splitPane.setContinuousLayout(true);
        
        return splitPane;
    }
    
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Create navigation buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        
        // Add navigation buttons
        addNavButton(buttonsPanel, "Borrow Book", "borrowBook");
        addNavButton(buttonsPanel, "Return Book", "returnBook");
        addNavButton(buttonsPanel, "Manage Members", "manageMembers");
        addNavButton(buttonsPanel, "Manage Books", "manageBooks");
        addNavButton(buttonsPanel, "Manage Fines", "manageFines");
        addNavButton(buttonsPanel, "Reports", "reports");
        
        panel.add(buttonsPanel, BorderLayout.NORTH);
        
        return panel;
    }
    
    private void addNavButton(JPanel panel, String text, String panelName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        button.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
            cardLayout.show(contentPanel, panelName);
        });
        
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private JPanel createStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
            new EmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel statusLabel = new JLabel("Ready");
        panel.add(statusLabel, BorderLayout.WEST);
        
        JLabel dateLabel = new JLabel(java.time.LocalDate.now().toString());
        panel.add(dateLabel, BorderLayout.EAST);
        
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
    
    // Inner panel classes for each tab
    
    // Borrow Book Panel
    private class BorrowBookPanel extends JPanel {
        public BorrowBookPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Borrow Book", JLabel.LEFT);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Member search
            formPanel.add(new JLabel("Member ID/Email:"), gbc);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            JTextField memberSearchField = new JTextField(20);
            formPanel.add(memberSearchField, gbc);
            
            gbc.gridx = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0.0;
            JButton searchMemberButton = new JButton("Search");
            formPanel.add(searchMemberButton, gbc);
            
            // Book search
            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(new JLabel("Book ID/ISBN:"), gbc);
            
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JTextField bookSearchField = new JTextField(20);
            formPanel.add(bookSearchField, gbc);
            
            gbc.gridx = 2;
            gbc.fill = GridBagConstraints.NONE;
            JButton searchBookButton = new JButton("Search");
            formPanel.add(searchBookButton, gbc);
            
            // Borrow duration
            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanel.add(new JLabel("Borrow Duration (days):"), gbc);
            
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(14, 1, 30, 1));
            formPanel.add(durationSpinner, gbc);
            
            // Issue button
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(20, 5, 5, 5);
            JButton issueButton = new JButton("Issue Book");
            formPanel.add(issueButton, gbc);
            
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(formPanel, BorderLayout.NORTH);
            
            // Preview panels
            JPanel previewPanel = new JPanel(new GridLayout(1, 2, 20, 0));
            
            // Member preview
            JPanel memberPanel = new JPanel(new BorderLayout());
            memberPanel.setBorder(BorderFactory.createTitledBorder("Member Details"));
            JTextArea memberDetails = new JTextArea(10, 20);
            memberDetails.setEditable(false);
            JScrollPane memberScroll = new JScrollPane(memberDetails);
            memberPanel.add(memberScroll, BorderLayout.CENTER);
            
            // Book preview
            JPanel bookPanel = new JPanel(new BorderLayout());
            bookPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
            JTextArea bookDetails = new JTextArea(10, 20);
            bookDetails.setEditable(false);
            JScrollPane bookScroll = new JScrollPane(bookDetails);
            bookPanel.add(bookScroll, BorderLayout.CENTER);
            
            previewPanel.add(memberPanel);
            previewPanel.add(bookPanel);
            
            centerPanel.add(previewPanel, BorderLayout.CENTER);
            
            add(centerPanel, BorderLayout.CENTER);
        }
    }
    
    // Return Book Panel
    private class ReturnBookPanel extends JPanel {
        public ReturnBookPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Return Book", JLabel.LEFT);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);
            
            // Implementation for return book functionality
            JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
            
            // Search panel
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchPanel.add(new JLabel("Issue ID or Member ID:"));
            JTextField searchField = new JTextField(15);
            searchPanel.add(searchField);
            JButton searchButton = new JButton("Search");
            searchPanel.add(searchButton);
            
            mainPanel.add(searchPanel, BorderLayout.NORTH);
            
            // Borrowed books table
            String[] columns = {"Issue ID", "Book Title", "Member", "Issue Date", "Due Date", "Status"};
            Object[][] data = {}; // This would be populated from the database
            
            JTable borrowedBooksTable = new JTable(data, columns);
            JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            
            // Return button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton returnButton = new JButton("Return Selected Book");
            buttonPanel.add(returnButton);
            
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            add(mainPanel, BorderLayout.CENTER);
        }
    }
    
    // Placeholder panel classes for the remaining tabs
    private class ManageMembersPanel extends JPanel {
        public ManageMembersPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Manage Members", JLabel.LEFT);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);
            
            // Placeholder content
            add(new JLabel("Member management interface will be implemented here", JLabel.CENTER), BorderLayout.CENTER);
        }
    }
    
    private class ManageBooksPanel extends JPanel {
        public ManageBooksPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Manage Books", JLabel.LEFT);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);
            
            // Placeholder content
            add(new JLabel("Book management interface will be implemented here", JLabel.CENTER), BorderLayout.CENTER);
        }
    }
    
    private class ManageFinesPanel extends JPanel {
        public ManageFinesPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Manage Fines", JLabel.LEFT);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);
            
            // Placeholder content
            add(new JLabel("Fine management interface will be implemented here", JLabel.CENTER), BorderLayout.CENTER);
        }
    }
    
    private class ReportsPanel extends JPanel {
        public ReportsPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("Reports", JLabel.LEFT);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            add(titleLabel, BorderLayout.NORTH);
            
            // Placeholder content
            add(new JLabel("Reports interface will be implemented here", JLabel.CENTER), BorderLayout.CENTER);
        }
    }
}
