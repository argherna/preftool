package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.ERROR;

import java.awt.Component;
import java.util.Enumeration;
import java.util.StringJoiner;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.IntStream;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * User interface utilities.
 */
class UIUtilities {

    private static final System.Logger LOGGER = System.getLogger(UIUtilities.class.getName());

    private UIUtilities() {}

    static DefaultMutableTreeNode newTopNode() {
        return new DefaultMutableTreeNode("Preferences");
    }

    static DefaultTreeModel createPreferencesTreeModel() {
        var model = new DefaultTreeModel(newTopNode());
        eagerlyCreatePreferencesNodeTreeNodes((DefaultMutableTreeNode) model.getRoot());
        return model;
    }

    /**
     * Creates the TreeNodes for the GUI's JTree.
     *
     * <P>
     * Each Preferences root node (user and system) are recursed through fully.
     *
     * @param top the top TreeNode to add the User and System TreeNodes.
     */
    static void eagerlyCreatePreferencesNodeTreeNodes(DefaultMutableTreeNode top) {
        var category = new DefaultMutableTreeNode("User");
        var prefRoot = Preferences.userRoot();
        try {
            eagerlyAddChildren(category, prefRoot);
        } catch (BackingStoreException e) {
            e.printStackTrace(System.err);
        }
        if (!category.isLeaf()) {
            top.add(category);
        }

        category = new DefaultMutableTreeNode("System");
        prefRoot = Preferences.systemRoot();
        try {
            eagerlyAddChildren(category, prefRoot);
        } catch (BackingStoreException e) {
            e.printStackTrace(System.err);
        }
        top.add(category);
    }

    /**
     * Adds Preferences node names as TreeNodes to the given parent TreeNode.
     *
     * <P>
     * This method will recurse through the entire tree. That is, if the given Preferences have
     * several children, and they have several children themselves, this method will recurse all the
     * way through each level building the tree.
     *
     * @param parentTreeNode the parent TreeNode.
     * @param preferences the Preferences node.
     * @throws BackingStoreException if a BackingStoreException is thrown.
     */
    static void eagerlyAddChildren(DefaultMutableTreeNode parentTreeNode, Preferences preferences)
            throws BackingStoreException {
        for (var childName : preferences.childrenNames()) {
            var childTreeNode = new DefaultMutableTreeNode(childName);
            if (!parentTreeNode.isNodeDescendant(childTreeNode)) {
                parentTreeNode.add(childTreeNode);
                eagerlyAddChildren(childTreeNode, preferences.node(childName));
            }
        }
    }

    static DefaultMutableTreeNode getMatchingChild(DefaultMutableTreeNode parentTreeNode,
            Preferences preferences) throws BackingStoreException {
        DefaultMutableTreeNode matchingChild = null;
        if (preferences.childrenNames().length == 0) {
            return parentTreeNode;
        }
        Enumeration<TreeNode> childTreeNodes = parentTreeNode.preorderEnumeration();
        while (childTreeNodes.hasMoreElements()) {
            var childTreeNode = (DefaultMutableTreeNode) childTreeNodes.nextElement();
            var userObj = childTreeNode.getUserObject();
            if (!(userObj instanceof Preferences)) {
                continue;
            }
            var pref = (Preferences) userObj;
            if (pref.equals(preferences) || pref.equals(preferences.parent())) {
                matchingChild = childTreeNode;
                break;
            }
        }
        return matchingChild;
    }

    /**
     * @return a DefaultTableModel to hold the Preferences key, value and type data.
     */
    static DefaultTableModel createPreferencesDataTableModel() {
        var tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Type");
        tableModel.addColumn("Value");
        return tableModel;
    }

    /**
     * Converts the given TreePath to a String of the form {@code /q1/q2/...}.
     *
     * <P>
     * If the TreePath contains a root node like User or System, a simple {@code /} is returned.
     *
     * @param tp the TreePath with preference node names in hierarchical order.
     * @return String representation of the Preferences node name.
     */
    static String renderPreferencesNodeAddress(final TreePath tp) {
        if (tp.getPathCount() <= 1) {
            return "";
        }

        var location = new StringJoiner(":").add(tp.getPathComponent(1).toString());
        if (tp.getPathCount() == 2) {
            return location.add("/").toString();
        }

        var ndnm = new StringJoiner("/").add("");

        /*
         * For each path component in the TreePath: map it to a DefaultMutableTreeNode, get the user
         * object from the node, cast it to a Preferences object, get its name, and add it to the
         * StringJoiner.
         */
        try {
            for (int i = 2; i < tp.getPathCount(); i++) {
                var dmtn = (DefaultMutableTreeNode) tp.getPathComponent(i);
                var nodename = dmtn.getUserObject().toString();
                ndnm.add(nodename);
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        // IntStream.range(2, tp.getPathCount())
        // .mapToObj(i -> (DefaultMutableTreeNode) tp.getPathComponent(i))
        // .map(n -> (String) n.getUserObject()).forEach(s -> ndnm.add(s));
        return location.add(ndnm.toString()).toString();
    }

    static String renderPreferencesNodeAddress(final DefaultMutableTreeNode[] treeNodes) {
        if (treeNodes.length <= 1) {
            return "";
        }

        var location = new StringJoiner(":").add(treeNodes[1].toString());
        if (treeNodes.length == 2) {
            return location.add("/").toString();
        }

        var ndnm = new StringJoiner("/").add("");

        try {
            IntStream.range(2, treeNodes.length).mapToObj(i -> treeNodes[i])
                    .map(n -> (String) n.getUserObject().toString()).forEach(s -> ndnm.add(s));
            return location.add(ndnm.toString()).toString();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Log and display error messages.
     *
     * <P>
     * This has the same effect as calling
     * {@link AbstractPreferenceUIAction#handleUIError(Exception, String, Component)
     * handlUIError(Exception, String, null)}.
     */
    static void handleUIError(Exception ex, String errTitle) {
        handleUIError(ex, errTitle, null);
    }

    /**
     * Log and display error messages.
     *
     * @param ex cause of the error.
     * @param errTitle title for an error dialog.
     * @param parentComponent the parent Component.
     */
    static void handleUIError(Exception ex, String errTitle, Component parentComponent) {
        var message = String.format("An error occurred: %s", ex.getMessage());
        LOGGER.log(ERROR, message, ex);
        JOptionPane.showMessageDialog(parentComponent, message, errTitle,
                JOptionPane.ERROR_MESSAGE);
    }
}
