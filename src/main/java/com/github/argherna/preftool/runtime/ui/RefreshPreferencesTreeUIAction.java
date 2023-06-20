package com.github.argherna.preftool.runtime.ui;

import java.awt.event.ActionEvent;

import javax.swing.JTree;

/**
 * Refresh the JTree model with modified Preferences nodes and keys.
 */
class RefreshPreferencesTreeUIAction extends AbstractPreferenceUIAction {

    private final JTree preferencesNodeTree;

    /**
     * @param preferencesNodeTree the JTree of Preferences nodes.
     */
    RefreshPreferencesTreeUIAction(JTree preferencesNodeTree) {
        this.preferencesNodeTree = preferencesNodeTree;
    }

    /**
     * {@inheritDoc}
     * 
     * <P>
     * This implementation will recreate the TreeModel by reading the Preferences
     * and building the model over again.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            var modelFactory = new PreferencesTreeModelFactory();
            var refreshedModel = modelFactory.create();
            preferencesNodeTree.setModel(refreshedModel);
        } catch (Exception ex) {
            handleUIError(ex, "Refresh Error");
        }
    }
}
