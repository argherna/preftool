
package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.INFO;

import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;

import javax.swing.tree.TreeModel;

class TreeModelNodeChangeListener implements NodeChangeListener {

    private final TreeModel treeModel;

    private static final System.Logger LOGGER = System
            .getLogger(TreeModelNodeChangeListener.class.getName());

    TreeModelNodeChangeListener(TreeModel treeModel) {
        this.treeModel = treeModel;
    }

    @Override
    public void childAdded(NodeChangeEvent evt) {
    }

    @Override
    public void childRemoved(NodeChangeEvent evt) {
        var node = evt.getChild();
        LOGGER.log(INFO, "child node removed: {0}", node);
    }
}