package com.github.argherna.preftool.runtime.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.github.argherna.preftool.AddPreferencesKey;
import com.github.argherna.preftool.PreferencesUtilities;

/**
 * Action that adds a key and value pair to a Preferences node.
 */
public class AddPreferencesKeyUIAction extends AbstractPreferenceUIAction {

    private final JTable preferencesValuesTable;

    /**
     * Constructs a new AddPreferencesKeyUIAction with a JTable for holding
     * Preferences data.
     * 
     * @param preferencesValuesTable JTable with Preferences keys, value types, and
     *                               values.
     */
    public AddPreferencesKeyUIAction(JTable preferencesValuesTable) {
        this.preferencesValuesTable = preferencesValuesTable;
    }

    /**
     * {@inheritDoc}
     * 
     * <P>
     * This implementation will gather Preferences data from a dialog box, then
     * apply that to this instance's JTable.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        var addPreferencesKeyUI = new AddPreferencesKeyUI();
        addPreferencesKeyUI.showUI();

        if (addPreferencesKeyUI.isInputCanceled()) {
            return;
        }

        var preferences = getPreferencesFromNodeAddress();
        if (Objects.isNull(preferences)) {
            return;
        }

        var key = addPreferencesKeyUI.getPreferencesKeyText();
        var valueType = addPreferencesKeyUI.getPreferencesType();
        var value = addPreferencesKeyUI.getPreferencesValueText();
        var keyAdder = new AddPreferencesKey(preferences, key, valueType, value);
        try {
            keyAdder.call();
        } catch (Exception ex) {
            handleUIError(ex, "Add Key error", (Component) e.getSource());
            return;
        }

        var tableModel = (DefaultTableModel) preferencesValuesTable.getModel();
        tableModel.insertRow(tableModel.getRowCount(), new Object[] { key, valueType.getName(),
                PreferencesUtilities.getTypedValueFrom(preferences, key, valueType) });
    }
}
