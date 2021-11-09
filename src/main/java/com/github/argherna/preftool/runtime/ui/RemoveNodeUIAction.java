package com.github.argherna.preftool.runtime.ui;

import java.awt.event.ActionEvent;
import java.lang.System.Logger.Level;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;


/**
 * An Action that removes a Preferences node.
 */
@SuppressWarnings("serial")
class RemoveNodeUIAction extends AbstractPreferenceUIAction {

    private static final System.Logger LOGGER =
            System.getLogger(RemoveNodeUIAction.class.getName());

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
     * Preferences will be removed if the Preferences node can be determined from the ActionEvent.
     * Errors are logged and feedback is given to the user. If the Preferences cannot be deleted,
     * the UI will not be updated.
     *
     * @param e the ActionEvent.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        var preferences = getPreferencesFromNodeAddress();
        if (Objects.nonNull(preferences)) {
            try {
                // TODO: NOTHING WORKS CONSISTENTLY WHEN REMOVING PREF NODES!!! WHY????
                preferences.removeNode();
            } catch (Exception ex) {
                handleException(ex);
                return;
            }
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
        LOGGER.log(Level.ERROR, message, ex);
        JOptionPane.showMessageDialog(null, message, "Remove error", JOptionPane.ERROR_MESSAGE);
    }
}
