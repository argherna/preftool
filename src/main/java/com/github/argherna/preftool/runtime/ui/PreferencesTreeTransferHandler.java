package com.github.argherna.preftool.runtime.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.github.argherna.preftool.AddPreferencesNode;
import com.github.argherna.preftool.PreferencesUtilities;
import com.github.argherna.preftool.RemovePreferencesNode;

class PreferencesTreeTransferHandler extends TransferHandler {

    private final DataFlavor nodeFlavor;

    private final DataFlavor[] flavors = new DataFlavor[1];

    private DefaultMutableTreeNode nodeToRemove;

    /*
     * Instance initializer common to all constructors to set up the DataFlavors.
     */
    {
        try {
            var mimeType = String.format("%s;class=\"%s\"", DataFlavor.javaJVMLocalObjectMimeType,
                    DefaultMutableTreeNode.class.getName());
            nodeFlavor = new DataFlavor(mimeType);
            flavors[0] = nodeFlavor;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if (!support.isDataFlavorSupported(nodeFlavor)) {
            return false;
        }

        var dropLocation = (JTree.DropLocation) support.getDropLocation();
        var tpDest = dropLocation.getPath();

        // Can't import if the destination is above User or System.
        if (isRootOfPreferencesTree(tpDest)) {
            return false;
        }

        // Can't import if the destination contains a node of the same name.
        try {
            var sourceName = getTreeNodeFrom(support).getUserObject().toString();
            var destPreferences =
                    getPreferencesFrom((DefaultMutableTreeNode) tpDest.getLastPathComponent());
            if (destPreferences.nodeExists(sourceName)) {
                return false;
            }
        } catch (Exception e) {
            UIUtilities.handleUIError(e, "Import Error");
            return false;
        }

        return true;
    }

    /**
     *
     * @param destination TreePath of the destination
     * @return {@code true} if the parent TreePath of the destination is {@code null}.
     */
    private boolean isRootOfPreferencesTree(TreePath destination) {
        return Objects.nonNull(destination) ? Objects.isNull(destination.getParentPath()) : true;
    }

    /**
     * Creates a Transferable object to move the TreeNode.
     *
     * @param c the component holding the data to be transferred; provided to enable sharing of
     *          {@code TransferHandler}s
     * @return the representation of the data to be transferred, or {@code null} if the {@code c} is
     *         not a {@link JTree}.
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        Transferable transferable = null;
        var path = ((JTree) c).getSelectionPath();
        if (Objects.nonNull(path)) {
            nodeToRemove = (DefaultMutableTreeNode) path.getLastPathComponent();
            transferable = new NodeTransferable(
                    copy((DefaultMutableTreeNode) path.getLastPathComponent()));
        }
        return transferable;
    }

    /**
     * @param node TreeNode to copy
     * @return defensive copy used in {@linkplain #createTransferable(JComponent)}.
     */
    private DefaultMutableTreeNode copy(TreeNode node) {
        var copy = new DefaultMutableTreeNode(node);
        if (!node.isLeaf()) {
            // recurse to copy the children too.
            for (int i = 0; i < node.getChildCount(); i++) {
                var child = node.getChildAt(i);
                copy.add(copy(child));
            }
        }
        return copy;
    }

    /**
     * Removes the source node of the move and the underlying Preferences object.
     *
     * @param source the component that was the source of the data
     * @param data   The data that was transferred or possibly null if the action is {@code NONE}.
     * @param action the actual action that was performed
     * @see TransferHandler#MOVE
     */
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if ((action & MOVE) == MOVE) {
            var tree = (JTree) source;
            var model = (DefaultTreeModel) tree.getModel();
            model.removeNodeFromParent(nodeToRemove);
        }
        if (!AbstractPreferenceUIAction.DISABLE_ACTIONS) {
            var remover = new RemovePreferencesNode(getPreferencesFrom(nodeToRemove));
            try {
                remover.call();
            } catch (Exception e) {
                UIUtilities.handleUIError(e, "Remove Preferences Error");
            }
        }
    }

    /**
     * Returns the type of transfer actions supported by the source which is {@code COPY_OR_MOVE}
     *
     * @param c the component holding the data to be transferred; provided to enable sharing of
     *          {@code TransferHandler}s
     * @return {@code COPY_OR_MOVE}
     * @see TransferHandler#COPY_OR_MOVE
     */
    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    /**
     *
     * @param support TransferSupport Object.
     * @return {@code true} if the JTree was updated and new Preferences Object was added.
     */
    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        DefaultMutableTreeNode node = null;
        try {
            node = getTreeNodeFrom(support);
        } catch (IOException | UnsupportedFlavorException ex) {
            UIUtilities.handleUIError(ex, "Unexpected error");
            return false;
        }

        var dropLocation = (JTree.DropLocation) support.getDropLocation();
        var destination = dropLocation.getPath();
        var parent = (DefaultMutableTreeNode) destination.getLastPathComponent();

        var tree = (JTree) support.getComponent();
        var model = (DefaultTreeModel) tree.getModel();
        model.insertNodeInto(node, parent, getIndexFrom(dropLocation, parent));

        var source = getPreferencesFrom((DefaultMutableTreeNode) node.getUserObject());
        var destParentPreferences = getPreferencesFrom(parent);
        var adder = new AddPreferencesNode(destParentPreferences, node.getUserObject().toString());
        try {
            var dest = adder.call();
            PreferencesUtilities.copy(source, dest);
        } catch (Exception ex) {
            UIUtilities.handleUIError(ex, "Move Preference Error");
            return false;
        }
        return true;
    }

    private DefaultMutableTreeNode getTreeNodeFrom(TransferSupport support)
            throws UnsupportedFlavorException, IOException {
        var xferable = support.getTransferable();
        return (DefaultMutableTreeNode) xferable.getTransferData(nodeFlavor);
    }

    private Preferences getPreferencesFrom(DefaultMutableTreeNode dmtn) {
        var prefNodeAddress = UIUtilities
                .renderPreferencesNodeAddress(toDefaultMutableTreeNodeArray(dmtn.getPath()));
        var nodeAddressArray = prefNodeAddress.split(":");
        return PreferencesUtilities.getPreferencesRoot(nodeAddressArray[0])
                .node(nodeAddressArray[1]);
    }

    private DefaultMutableTreeNode[] toDefaultMutableTreeNodeArray(TreeNode[] treeNodes) {
        DefaultMutableTreeNode[] dmtnArray = new DefaultMutableTreeNode[treeNodes.length];

        for (int i = 0; i < treeNodes.length; i++) {
            var treeNode = treeNodes[i];
            if (treeNode instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) treeNode;
                dmtnArray[i] = dmtn;
            } else {
                throw new RuntimeException("Not a DefaultMutableTreeNode!!!");
            }
        }
        return dmtnArray;
    }

    /**
     *
     * @param dropLocation the drop location
     * @param parent       TreeNode
     * @return the parent child count if the drop occurred on the path itself, else the child index
     *         of the drop location.
     */
    private int getIndexFrom(JTree.DropLocation dropLocation, TreeNode parent) {
        return dropLocation.getChildIndex() == -1 ? parent.getChildCount()
                : dropLocation.getChildIndex();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    private final class NodeTransferable implements Transferable {

        private final DefaultMutableTreeNode node;

        NodeTransferable(DefaultMutableTreeNode node) {
            this.node = node;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodeFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return node;
        }
    }
}
