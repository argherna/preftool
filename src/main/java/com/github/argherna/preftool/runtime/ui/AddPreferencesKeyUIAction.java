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
class AddPreferencesKeyUIAction extends AbstractPreferenceUIAction {

    private final JTable preferencesValuesTable;

    public AddPreferencesKeyUIAction(JTable preferencesValuesTable) {
        this.preferencesValuesTable = preferencesValuesTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var addKeyUI = new AddKeyUI();
        addKeyUI.showUI();

        if (addKeyUI.isInputCanceled()) {
            return;
        }

        var preferences = getPreferencesFromNodeAddress();
        if (Objects.isNull(preferences)) {
            return;
        }

        var key = addKeyUI.getPreferencesKey();
        var valueType = addKeyUI.getPreferencesType();
        var value = addKeyUI.getPreferencesValue();
        var keyAdder = new AddPreferencesKey(preferences, key, valueType, value);
        try {
            keyAdder.call();
        } catch (Exception ex) {
            handleUIError(ex, "Add Key error", (Component) e.getSource());
            return;
        }

        var tableModel = (DefaultTableModel) preferencesValuesTable.getModel();
        tableModel.insertRow(tableModel.getRowCount(), new Object[] {key, valueType.getName(),
                PreferencesUtilities.getTypedValueFrom(preferences, key, valueType)});
    }
}
