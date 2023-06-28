package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.ERROR;

import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.StringJoiner;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.github.argherna.preftool.AddPreferencesNode;
import com.github.argherna.preftool.PreferencesUtilities;

/**
 * An Action that creates a child Preferences node on a parent.
 */
public class NewNodeUIAction extends AbstractPreferenceUIAction {

    private final JTree preferencesNodeTree;

    /**
     * Construct a new NewNodeUIAction.
     *
     * @param preferencesNodeTree JTree to be updated after a successful add.
     */
    public NewNodeUIAction(JTree preferencesNodeTree) {
        this.preferencesNodeTree = preferencesNodeTree;
    }

    /**
     * Adds a new Preferences node, then updates the user interface.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        var nodeAddress = getPreferencesNodeAddress();
        if (Objects.isNull(nodeAddress) || nodeAddress.isEmpty()) {
            return;
        }

        var newNodeName = (String) JOptionPane.showInputDialog(null,
                "Enter new preferences node name:", "New Node", JOptionPane.PLAIN_MESSAGE);
        if (Objects.isNull(newNodeName) || newNodeName.isEmpty() || newNodeName.isBlank()) {
            return;
        }

        var nodeAddressArray = nodeAddress.split(":");
        var newNodePath = createNodeNameFrom(nodeAddressArray[1], newNodeName);

        try {
            var adder = new AddPreferencesNode(
                    PreferencesUtilities.getPreferencesRoot(nodeAddressArray[0]), newNodePath);
            var addedPreferences = adder.call();
            var model = (DefaultTreeModel) preferencesNodeTree.getModel();

            var parent = (DefaultMutableTreeNode) preferencesNodeTree.getSelectionPath()
                    .getLastPathComponent();
            model.insertNodeInto(new DefaultMutableTreeNode(addedPreferences.name()), parent,
                    parent.getChildCount());
        } catch (Exception ex) {
            var message = String.format("An error occurred: %s", ex.getMessage());
            LOGGER.log(ERROR, message, ex);
            JOptionPane.showMessageDialog(null, message, "Add error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @param parent parent node name.
     * @param newNodeName new node name to add to the parent.
     * @return full path name of the new node.
     */
    private String createNodeNameFrom(String parent, String newNodeName) {
        return parent.endsWith("/") ? new StringJoiner("").add(parent).add(newNodeName).toString()
                : new StringJoiner("/").add(parent).add(newNodeName).toString();
    }
}
