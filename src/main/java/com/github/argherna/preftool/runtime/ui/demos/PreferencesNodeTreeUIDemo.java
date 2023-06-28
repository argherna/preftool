package com.github.argherna.preftool.runtime.ui.demos;

import java.awt.BorderLayout;
import java.util.prefs.BackingStoreException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import com.github.argherna.preftool.runtime.ui.PreferencesTreeCellRenderer;
import com.github.argherna.preftool.runtime.ui.PreferencesTreeModelFactory;

/**
 * Demo GUI for the Preferences Node Tree.
 */
class PreferencesNodeTreeUIDemo extends JPanel {

    /**
     * Creates a new instance of PreferencesNodeTreeUIDemo.
     * 
     * @throws BackingStoreException if a BackingStoreException is thrown.
     * 
     * @see PreferencesTreeCellRenderer
     * @see PreferencesTreeModelFactory
     */
    PreferencesNodeTreeUIDemo() throws BackingStoreException {
        super(new BorderLayout());

        // Demonstrates how to create a JTree with the right model and renderer.
        var factory = new PreferencesTreeModelFactory();
        var preferencesTreeModel = factory.create();
        var preferencesNodeTree = new JTree(preferencesTreeModel);
        preferencesNodeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        preferencesNodeTree.setCellRenderer(new PreferencesTreeCellRenderer());
        preferencesNodeTree.setName("preferencesNodeTreeUIDemo");
        preferencesNodeTree.setRootVisible(true);

        // JPanel stuff
        add(preferencesNodeTree, BorderLayout.CENTER);
    }

    /**
     * Shows the demo UI accessible by the main method.
     * 
     * @throws BackingStoreException if a BackingStoreException is thrown.
     */
    static void showUI() throws BackingStoreException {
        var frame = new JFrame("Test UI - PreferencesNodeTree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var scrollPane = new JScrollPane(new PreferencesNodeTreeUIDemo());
        scrollPane.setName("preferencesNodeTreeScrollPaneUIDemo");
        scrollPane.setOpaque(true);

        frame.setContentPane(scrollPane);
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
                try {
                    showUI();
                } catch (BackingStoreException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
