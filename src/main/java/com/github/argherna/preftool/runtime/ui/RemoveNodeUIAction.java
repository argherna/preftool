package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import com.github.argherna.preftool.RemovePreferencesNode;

/**
 * An Action that removes a Preferences node.
 */
class RemoveNodeUIAction extends AbstractPreferenceUIAction {

    private static final System.Logger LOGGER = System.getLogger(RemoveNodeUIAction.class.getName());

    private final JTree tree;

    /**
     * Constructs a new RemoveNodeUIAction instance.
     *
     * @param tree JTree that holds the node names and structure.
     */
    RemoveNodeUIAction(JTree tree) {
        this.tree = tree;
    }

    /**
     * Processes the given ActionEvent.
     *
     * <P>
     * Preferences will be removed if the Preferences node can be determined from
     * the ActionEvent.
     * Errors are logged and feedback is given to the user. If the Preferences
     * cannot be deleted,
     * the UI will not be updated.
     *
     * @param e the ActionEvent.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        var preferences = getPreferencesFromNodeAddress();
        var parent = preferences.parent();
        parent.addNodeChangeListener(new TreeModelNodeChangeListener(tree.getModel()));
        var nodename = preferences.name();
        LOGGER.log(INFO, "Removing {0}", nodename);
        if (Objects.nonNull(preferences)) {
            try {
                // TODO: NOTHING WORKS CONSISTENTLY WHEN REMOVING PREF NODES!!! WHY????
                // preferences.removeNode();
                var removeNodeAction = new RemovePreferencesNode(preferences);
                removeNodeAction.call();

                if (Objects.nonNull(parent)) {
                    parent.sync();
                }
            } catch (Exception ex) {
                handleException(ex);
                return;
            }
        } else {
            LOGGER.log(WARNING, "Unable to retrieve preferences for delete!");
        }

        // TODO: Temporary check
        var nodepath = getPreferencesNodeAddress().split(":/")[1];
        try {
            LOGGER.log(INFO, "Checking for existence of {0}", nodepath);
            if (Preferences.userRoot().nodeExists(nodepath)) {
                LOGGER.log(WARNING, "{0} was not deleted!", nodepath);
            }
        } catch (BackingStoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        var model = (DefaultTreeModel) tree.getModel();
        var paths = tree.getSelectionPaths();
        if (Objects.nonNull(paths)) {
            for (TreePath treePath : paths) {
                var node = (MutableTreeNode) treePath.getLastPathComponent();
                if (Objects.nonNull(node.getParent())) {
                    model.removeNodeFromParent(node);
                }
            }
        }
    }

    private void handleException(Exception ex) {
        var message = String.format("An error occurred: %s", ex.getMessage());
        LOGGER.log(ERROR, message, ex);
        JOptionPane.showMessageDialog(null, message, "Remove error", JOptionPane.ERROR_MESSAGE);
    }
}
