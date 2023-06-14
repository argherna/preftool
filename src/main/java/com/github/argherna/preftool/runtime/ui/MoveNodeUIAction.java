package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.TRACE;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.lang.System.Logger.Level;
import java.util.Enumeration;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.github.argherna.preftool.AddPreferencesNode;
import com.github.argherna.preftool.PreferencesUtilities;
import com.github.argherna.preftool.RemovePreferencesNode;

class MoveNodeUIAction extends AbstractPreferenceUIAction {

    private final JTree preferencesNodeTree;

    MoveNodeUIAction(JTree preferencesNodeTree) {
        this.preferencesNodeTree = preferencesNodeTree;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var source = getPreferencesFromNodeAddress();
        if (Objects.isNull(source)) {
            return;
        }

        var moveNodeUI = (e.getActionCommand().equals("Rename")) ? new MoveNodeUI(source.parent())
                : new MoveNodeUI();
        moveNodeUI.showUI();

        if (moveNodeUI.isInputCanceled()) {
            return;
        }

        var destinationNodeName = moveNodeUI.getDestinationNodeName();
        if (Objects.isNull(destinationNodeName) || destinationNodeName.isEmpty()) {
            return;
        }

        // To move or rename a Preferences node:
        //
        // 1. Add the new node to the Preferences.
        // 2. Copy the source to the new node.
        // 3. Delete the source.
        Preferences destinationRoot = moveNodeUI.getDestinationPreferencesRoot();
        var adder = new AddPreferencesNode(destinationRoot, destinationNodeName);

        // Annoying try-catch blocks follow. This ensures operations are atomic and if something
        // fails, the method will exit before it does more damage.
        var errTitle = String.format("%s error", e.getActionCommand());
        try {
            adder.call();
        } catch (Exception ex) {
            handleUIError(ex, errTitle, (Component) e.getSource());
            return;
        }

        var destination = destinationRoot.node(destinationNodeName);

        try {
            PreferencesUtilities.copy(source, destination);
        } catch (BackingStoreException ex) {
            handleUIError(ex, errTitle, (Component) e.getSource());
            return;
        }

        if (!DISABLE_ACTIONS) {
            var remover = new RemovePreferencesNode(source);
            try {
                remover.call();
            } catch (Exception ex) {
                handleUIError(ex, errTitle, (Component) e.getSource());
                return;
            }
        }

        // Now delete the source node from the JTree, then add the destination node to the tree.

        // TODO: check for existence of destination in JTree
        var sourceParentPath = preferencesNodeTree.getSelectionPath().getParentPath();
        var sourcePath = preferencesNodeTree.getSelectionPath();
        var sourceNode = (DefaultMutableTreeNode) sourcePath.getLastPathComponent();

        var model = (DefaultTreeModel) preferencesNodeTree.getModel();
        DefaultMutableTreeNode destinationRootNode =
                findRoot((DefaultMutableTreeNode) model.getRoot(), destinationRoot);
        DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(destination);
        try {
            UIUtilities.eagerlyAddChildren(newChild, destination);
        } catch (BackingStoreException ex) {
            handleUIError(ex, errTitle);
            return;
        }
        model.insertNodeInto(newChild, destinationRootNode, destinationRootNode.getChildCount());
        model.removeNodeFromParent(sourceNode);
        preferencesNodeTree.scrollPathToVisible(sourceParentPath);
        preferencesNodeTree.setSelectionPath(sourceParentPath);
    }

    /**
     *
     * @param root            MutableTreeNode that is the root of the tree.
     * @param destinationRoot root Preferences node.
     * @return root MutableTreeNode containing the destinationRoot as a user object.
     */
    private DefaultMutableTreeNode findRoot(DefaultMutableTreeNode root,
            Preferences destinationRoot) {
        LOGGER.log(Level.DEBUG, "destinationRoot={0}", destinationRoot);
        DefaultMutableTreeNode rootNode = null;
        if (Objects.isNull(root)) {
            return null;
        }
        Enumeration<TreeNode> e = root.breadthFirstEnumeration();
        while (e.hasMoreElements() && Objects.isNull(rootNode)) {
            var node = (DefaultMutableTreeNode) e.nextElement();
            LOGGER.log(TRACE, "node={0}", node);
            Object userObj = node.getUserObject();
            if (userObj instanceof String) {
                var prefname = (String) userObj;
                if (prefname.equals("User") || prefname.equals("System")) {
                    if (destinationRoot.parent() == null && prefname.equals("User")
                            && destinationRoot.isUserNode()) {
                        rootNode = node;
                    } else if (destinationRoot.parent() == null && prefname.equals("System")
                            && !destinationRoot.isUserNode()) {
                        rootNode = node;
                    }
                } else if (prefname.equals(destinationRoot.name())) {
                    rootNode = node;
                }
            } else if (userObj instanceof Preferences) {
                var pref = (Preferences) node.getUserObject();
                if (pref.equals(destinationRoot)) {
                    rootNode = node;
                }
            }
        }
        return rootNode;
    }
}
