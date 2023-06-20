package com.github.argherna.preftool.runtime.ui;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 * Creates instances of {@link DefaultTreeModel} whose data is populated by the
 * names of {@link Preferences} nodes.
 */
public class PreferencesTreeModelFactory {

    /**
     * Creates the TreeModel used for the JTree view in the application.
     * 
     * @return TreeModel populated with Preferences node names.
     * @throws BackingStoreException if a BackingStoreException is thrown.
     */
    public TreeModel create() throws BackingStoreException {
        var topNode = createTopNode();
        var treeModel = new DefaultTreeModel(topNode);
        addTreeNodes(topNode);
        return treeModel;
    }

    /**
     * 
     * @return top node for the TreeModel.
     */
    private DefaultMutableTreeNode createTopNode() {
        return new DefaultMutableTreeNode("Preferences");
    }

    /**
     * Adds the User and System Preferences node names as TreeNodes to the given
     * root node.
     * 
     * @param root root node to add the user and system tree nodes to.
     * @throws BackingStoreException if a BackingStoreException is thrown.
     */
    private void addTreeNodes(DefaultMutableTreeNode root) throws BackingStoreException {
        var user = createCategoryNode("User");
        var userRoot = Preferences.userRoot();
        addChildren(user, userRoot);
        root.add(user);

        var system = createCategoryNode("System");
        var systemRoot = Preferences.systemRoot();
        addChildren(system, systemRoot);
        root.add(system);
    }

    /**
     * A category Node is simply the root to add children to.
     * 
     * @param name Name for the category node.
     * @return DefaultMutableTreeNode with the given name.
     */
    private DefaultMutableTreeNode createCategoryNode(String name) {
        return new DefaultMutableTreeNode(name);
    }

    /**
     * Adds DefaultMutableTreeNodes representing the children of the given
     * Preferences node to the given parent.
     * 
     * <P>
     * This method is called recursively to account for child Preferences of the
     * given Preferences node.
     * 
     * @param parent      parent DefaultMutableTreeNode to add children to.
     * @param preferences Preferences node to add child data to.
     * @throws BackingStoreException if a BackingStoreException is thrown.
     */
    private void addChildren(DefaultMutableTreeNode parent, Preferences preferences) throws BackingStoreException {
        for (var childName : preferences.childrenNames()) {
            var childTreeNode = new DefaultMutableTreeNode(childName);
            if (!parent.isNodeDescendant(childTreeNode)) {
                parent.add(childTreeNode);
                addChildren(childTreeNode, preferences.node(childName));
            }
        }
    }
}
