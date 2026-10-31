package com.mycompany.virtualpetgame.ui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Mitchell
 */
public class PetGameApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // the default look and feel is fine
            }
            try {
                new MainFrame().setVisible(true);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null,
                        "The game could not start: " + e.getMessage(),
                        "Scrappymon", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
