package com.github.argherna.preftool.runtime.ui.demos;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.github.argherna.preftool.runtime.ui.PreferencesValuesTableFactory;
import com.github.argherna.preftool.runtime.ui.PreferencesValuesTableModelFactory;

/**
 * Demo GUI for the component created by PreferencesValuesTableFactory.
 * 
 * @see PreferencesValuesTableFactory
 */
class PreferencesValuesTableUIDemo extends JPanel {

    private static final Object[][] DEMO_VALUES = {
            { "key0", "java.util.String", "value0" },
            { "key1", "int", Integer.valueOf(1024) },
            { "key2", "boolean", Boolean.TRUE }
    };

    PreferencesValuesTableUIDemo() {
        super(new BorderLayout());

        // Demonstrates how to create the Preferences values JTable.
        var factory = new PreferencesValuesTableFactory();
        var preferencesValuesTable = factory.create();

        // Fill with DEMO_VALUES
        var tableModelFactory = new PreferencesValuesTableModelFactory();
        var tableModel = tableModelFactory.create();

        for (int i = 0; i < DEMO_VALUES.length; i++) {
            tableModel.insertRow(i, DEMO_VALUES[i]);
        }
        
        preferencesValuesTable.setModel(tableModel);

        // Demonstrate clicking a row and getting the data from it.
        preferencesValuesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                var keyName = preferencesValuesTable.getValueAt(preferencesValuesTable.getSelectedRow(), 0);
                var keyType = preferencesValuesTable.getValueAt(preferencesValuesTable.getSelectedRow(), 1);
                var value = preferencesValuesTable.getValueAt(preferencesValuesTable.getSelectedRow(), 2);

                System.out.printf("Selected: %s %s %s%n", keyName, keyType, value);
            }
        });

        // Put the table in a JScrollPane
        var scrollPane = new JScrollPane(preferencesValuesTable);
        scrollPane.setName("preferencesValuesTableScrollPaneUIDemo");
        scrollPane.setOpaque(true);

        // Put the JScrollPane on the JPanel.
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Shows the demo UI accessible by the main method.
     */
    private static void showUI() {
        var frame = new JFrame("Test UI - PreferencesValuesTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new PreferencesValuesTableUIDemo());
        frame.pack();
        frame.setSize(300, 400);
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
