package com.github.argherna.preftool.runtime.ui;

import javax.swing.JLabel;
import javax.swing.event.TreeSelectionEvent;

/**
 * Updates the node address label in the user interface when a Tree node is
 * selected.
 */
public class NodeAddressLabelUpdateListener extends AbstractTreeSelectionListener {

    private final JLabel nodeAddressLabel;

    /**
     * 
     * @param nodeAddressLabel label whose text will be updated when a Tree node is
     *                         selected.
     */
    public NodeAddressLabelUpdateListener(JLabel nodeAddressLabel) {
        this.nodeAddressLabel = nodeAddressLabel;
    }

    /**
     * {@inheritDoc}
     * 
     * <P>
     * If the node selected is below one of the roots (system or user), update the
     * label with the node's address in the form {@code [root]:/[path/to/node]}.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        var treePath = e.getPath();
        if (isTrivialPath(treePath)) {
            nodeAddressLabel.setText("");
            return;
        }
        var nodeAddress = renderPreferencesNodeAddress(treePath);
        nodeAddressLabel.setText(nodeAddress);
    }

}
