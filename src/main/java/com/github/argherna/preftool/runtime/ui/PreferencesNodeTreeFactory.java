package com.github.argherna.preftool.runtime.ui;

import java.util.prefs.BackingStoreException;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

/**
 * Creates instances of JTrees with a model that is populated by Preferences
 * Nodes.
 * 
 * @see PreferencesTreeModelFactory
 */
public class PreferencesNodeTreeFactory {

    /**
     * Return a JTree.
     * 
     * @return JTree populated with Preferences nodes.
     * @throws BackingStoreException if a BackingStoreException is thrown.
     */
    public JTree create() throws BackingStoreException {
        var preferencesNodeTreeModel = new PreferencesTreeModelFactory().create();
        var preferencesNodeTree = new JTree(preferencesNodeTreeModel);
        preferencesNodeTree.getSelectionModel()
                .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        preferencesNodeTree.setCellRenderer(new PreferencesTreeCellRenderer());
        preferencesNodeTree.setName("preferencesNodeTree");
        preferencesNodeTree.setRootVisible(true);

        return preferencesNodeTree;
    }
}
