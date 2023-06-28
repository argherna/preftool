package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.WARNING;

import java.awt.Component;
import java.util.prefs.BackingStoreException;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;

import com.github.argherna.preftool.PreferencesUtilities;

/**
 * TreeSelectionListener that updates a JTable with preferences key names,
 * types, and values.
 */
public class PreferencesValuesTableUpdateListener extends AbstractTreeSelectionListener {

    private final JTable preferencesValuesTable;

    /**
     * Constructs a PreferencesValueTableUpdateListener with the JTable.
     * 
     * @param preferencesValuesTable JTable being listened to.
     */
    public PreferencesValuesTableUpdateListener(JTable preferencesValuesTable) {
        this.preferencesValuesTable = preferencesValuesTable;
    }

    /**
     * {@inheritDoc}
     *
     * <P>
     * This implementation will check the selected Preferences node for any
     * key-value pairs and if found will render the data in a table to the right of
     * the tree view. The name of the node will be rendered in the address panel
     * above the tree and the table whether or not there's key-value data.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        var treePath = e.getPath();
        if (isTrivialPath(treePath)) {
            return;
        }

        var tableModelFactory = new PreferencesValuesTableModelFactory();
        var tableModel = tableModelFactory.create();
        var nodeAddress = renderPreferencesNodeAddress(treePath);

        try {
            var pref = toPreferences(nodeAddress);
            if (pref.keys().length > 0) {
                var keys = pref.keys();
                for (int i = 0; i < keys.length; i++) {
                    var valueType = PreferencesUtilities.guessType(pref, keys[i]);
                    var valueTypeName = valueType.getName().startsWith("[B") ? "byte []" : valueType.getName();

                    tableModel.insertRow(i, new Object[] { keys[i], valueTypeName,
                            PreferencesUtilities.getTypedValueFrom(pref, keys[i], valueType) });
                }
            }
        } catch (BackingStoreException ex) {
            var message = String.format("Preferences problem while building table view: %s",
                    ex.getMessage());
            AbstractTreeSelectionListener.LOGGER.log(WARNING, message, ex);

            JOptionPane.showMessageDialog((Component) e.getSource(), message, "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            var treeSelectionModel = ((JTree) e.getSource()).getSelectionModel();
            treeSelectionModel.setSelectionPath(treePath.getParentPath());
        }

        preferencesValuesTable.setModel(tableModel);
    }
}
