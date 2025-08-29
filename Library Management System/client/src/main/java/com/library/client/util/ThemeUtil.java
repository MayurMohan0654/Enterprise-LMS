package com.library.client.util;

import javax.swing.*;
import java.awt.*;

public class ThemeUtil {

    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void applyCommonStyles() {
        // Set common UI properties
        UIManager.put("Button.arc", 15);
        UIManager.put("Component.arc", 15);
        UIManager.put("ProgressBar.arc", 15);
        UIManager.put("TextComponent.arc", 15);
        
        // Font settings
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("Table.font", defaultFont);
    }
}
