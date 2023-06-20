package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.WARNING;

import java.awt.Component;
import java.lang.System.Logger;
import java.util.StringJoiner;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

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
        var nodeAddress = renderPreferencesNodeAddress(treePath);
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
            LOGGER.log(WARNING, message, ex);

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

    /**
     * Converts the given TreePath to a String of the form {@code /q1/q2/...}.
     *
     * <P>
     * If the TreePath contains a root node like User or System, a simple {@code /}
     * is returned.
     *
     * @param tp the TreePath with preference node names in hierarchical order.
     * @return String representation of the Preferences node name.
     */
    private String renderPreferencesNodeAddress(final TreePath tp) {
        if (tp.getPathCount() <= 1) {
            return "";
        }
    
        var location = new StringJoiner(":").add(tp.getPathComponent(1).toString());
        if (tp.getPathCount() == 2) {
            return location.add("/").toString();
        }
    
        var ndnm = new StringJoiner("/").add("");
    
        /*
         * For each path component in the TreePath: map it to a DefaultMutableTreeNode,
         * get the user
         * object from the node, cast it to a Preferences object, get its name, and add
         * it to the
         * StringJoiner.
         */
        try {
            for (int i = 2; i < tp.getPathCount(); i++) {
                var dmtn = (DefaultMutableTreeNode) tp.getPathComponent(i);
                var nodename = dmtn.getUserObject().toString();
                ndnm.add(nodename);
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
        // IntStream.range(2, tp.getPathCount())
        // .mapToObj(i -> (DefaultMutableTreeNode) tp.getPathComponent(i))
        // .map(n -> (String) n.getUserObject()).forEach(s -> ndnm.add(s));
        return location.add(ndnm.toString()).toString();
    }
}
