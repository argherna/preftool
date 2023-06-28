package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.ERROR;

import java.lang.System.Logger;
import java.util.StringJoiner;
import java.util.prefs.Preferences;

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Provides common functionality for the TreeSelectionListener implementations.
 * 
 */
public abstract class AbstractTreeSelectionListener implements TreeSelectionListener {

    /**
     * Logger for AbstractTreeSelectionListeners.
     * 
     * <P>
     * The name for this Logger is
     * {@code com.github.argherna.preftool.runtime.ui.treeSelectionListeners}.
     */
    protected static final Logger LOGGER = System
            .getLogger(AbstractTreeSelectionListener.class.getPackageName() + ".treeSelectionListeners");

    /**
     * Tests the given TreePath for a length less than 2. A length of less than 2
     * means the selection was not under the user or system root node.
     * 
     * @param treePath TreePath element
     * @return {@code true} if the length of the TreePath is less than 2.
     */
    protected boolean isTrivialPath(TreePath treePath) {
        return treePath.getPath().length < 2;
    }

    /**
     * Converts the given TreePath to a String of the form {@code root:/q1/q2/...}.
     *
     * <P>
     * If the TreePath contains a root node like User or System, a simple {@code /}
     * is returned.
     *
     * @param tp the TreePath with preference node names in hierarchical order.
     * @return String representation of the Preferences node name.
     */
    protected String renderPreferencesNodeAddress(final TreePath tp) {
        if (tp.getPathCount() <= 1) {
            return "";
        }

        var location = new StringJoiner(":").add(tp.getPathComponent(1).toString());
        if (tp.getPathCount() == 2) {
            return location.add("/").toString();
        }

        var ndnm = new StringJoiner("/").add("");

        /*
         * For each path component in the TreePath: map it to a DefaultMutableTreeNode,
         * get the user object from the node, cast it to a Preferences object, get its
         * name, and add it to the StringJoiner.
         */
        try {
            for (int i = 2; i < tp.getPathCount(); i++) {
                var dmtn = (DefaultMutableTreeNode) tp.getPathComponent(i);
                var nodename = dmtn.getUserObject().toString();
                ndnm.add(nodename);
            }
        } catch (Throwable t) {
            LOGGER.log(ERROR, t);
        }

        return location.add(ndnm.toString()).toString();
    }

    /**
     * Convert the given node address to a Preferences Object.
     * 
     * @param nodeAddress String of the form {@code [root]:/[path/to/node]}
     * @return Preferences object at the address represented by the given node
     *         address.
     */
    protected Preferences toPreferences(String nodeAddress) {
        var nameComponents = nodeAddress.split(":");
        var rootNode = nameComponents[0].equalsIgnoreCase("User") ? Preferences.userRoot()
                : Preferences.systemRoot();
        return rootNode.node(nameComponents[1]);
    }
}
