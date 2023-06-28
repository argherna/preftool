package com.github.argherna.preftool.runtime.ui.demos;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.argherna.preftool.runtime.ui.PreferencesTypesComboBoxFactory;

/**
 * Demo GUI for the component created by PreferencesTypesComboBoxFactory.
 * 
 * @see PreferencesTypesComboBoxFactory
 */
class PreferencesTypesComboBoxUIDemo extends JPanel {

    PreferencesTypesComboBoxUIDemo() {
        super(new BorderLayout());

        // Demonstrates how to create a JComboBox with the right model.
        var factory = new PreferencesTypesComboBoxFactory();
        var preferencesTypeComboBox = factory.create();

        // JPanel stuff
        add(preferencesTypeComboBox, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    /**
     * Shows the demo UI accessible by the main method.
     */
    private static void showUI() {
        var frame = new JFrame("Test UI - PreferencesTypesComboBox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        var contentPane = new PreferencesTypesComboBoxUIDemo();
        contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Shows the demo UI.
     * 
     * @param args N/A
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showUI();
            }
        });
    }
}
