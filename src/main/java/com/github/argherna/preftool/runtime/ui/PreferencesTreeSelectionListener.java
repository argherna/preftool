package com.github.argherna.preftool.runtime.ui;

import java.awt.Component;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.github.argherna.preftool.PreferencesUtilities;

/**
 * TreeSelectionListener that updates a JTable with preferences key names, types, and values.
 */
class PreferencesTreeSelectionListener implements TreeSelectionListener {

    private static final Logger LOGGER =
            System.getLogger(PreferencesTreeSelectionListener.class.getName());

    private final JTable preferencesValuesTable;

    private final JLabel nodeAddressLabel;

    PreferencesTreeSelectionListener(JTable preferencesValuesTable, JLabel nodeAddressLabel) {
        this.preferencesValuesTable = preferencesValuesTable;
        this.nodeAddressLabel = nodeAddressLabel;
    }

    /**
     * {@inheritDoc}
     *
     * <P>
     * This implementation will check the selected Preferences node for any key-value pairs and if
     * found will render the data in a table to the right of the tree view. The name of the node
     * will be rendered in the address panel above the tree and the table whether or not there's
     * key-value data.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        var treePath = e.getPath();
        if (treePath.getPath().length < 2) {
            nodeAddressLabel.setText("");
            return;
        }

        // var node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        var tableModel = UIUtilities.createPreferencesDataTableModel();
        var nodeAddress = UIUtilities.renderPreferencesNodeAddress(treePath);
        try {
            var pref = toPreferences(nodeAddress);
            if (pref.keys().length > 0) {
                var keys = pref.keys();
                for (int i = 0; i < keys.length; i++) {
                    var valueType = PreferencesUtilities.guessType(pref, keys[i]);
                    var valueTypeName =
                            valueType.getName().startsWith("[B") ? "byte []" : valueType.getName();
                    tableModel.insertRow(i, new Object[] {keys[i], valueTypeName,
                            PreferencesUtilities.getTypedValueFrom(pref, keys[i], valueType)});
                }
            }
        } catch (BackingStoreException ex) {
            var message = String.format("Preferences problem while building table view: %s",
                    ex.getMessage());
            LOGGER.log(Level.WARNING, message, ex);

            JOptionPane.showMessageDialog((Component) e.getSource(), message, "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException ex) {
            var treeSelectionModel = ((JTree) e.getSource()).getSelectionModel();
            treeSelectionModel.setSelectionPath(treePath.getParentPath());
        }
        preferencesValuesTable.setModel(tableModel);
        nodeAddressLabel.setText(nodeAddress);
    }

    private Preferences toPreferences(String nodeAddress) {
        var nameComponents = nodeAddress.split(":");
        var rootNode = nameComponents[0].equalsIgnoreCase("User") ? Preferences.userRoot()
                : Preferences.systemRoot();
        return rootNode.node(nameComponents[1]);
    }
}
