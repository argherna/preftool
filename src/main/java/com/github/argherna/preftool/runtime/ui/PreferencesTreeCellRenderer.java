package com.github.argherna.preftool.runtime.ui;

import java.awt.Component;
import java.util.prefs.Preferences;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * TreeCellRenderer for Preferences data.
 */
public class PreferencesTreeCellRenderer extends DefaultTreeCellRenderer {

    /**
     * Renders a node in the tree with Preferences data.
     */
    @Override
    public Component getTreeCellRendererComponent​(JTree tree, Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        var treeNode = (DefaultMutableTreeNode) value;

        /*
         * You can't rely on Preferences#toString to show just the name of the Preferences object if
         * you have one in the TreeNode. Instead, get the object and get the name. If the object in
         * the TreeNode isn't a Preferences, then use toString.
         */
        if (treeNode.getUserObject() instanceof Preferences) {
            var preferences = (Preferences) treeNode.getUserObject();
            setText(preferences.name());
        } else {
            setText(value.toString());
        }
        return this;
    }
}
