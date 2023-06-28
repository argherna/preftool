package com.github.argherna.preftool.runtime.ui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Toggles the state of main UI action for editing preferences values in a
 * JTable.
 * 
 * <P>
 * When a row is selected in the table, the key, value type, and value of the
 * Preferences object is given to the action and the action is enabled. When
 * that row is delselected, the Preferences attributes are cleared and the
 * action is disabled.
 */
public class PreferencesValuesListSelectionListener implements ListSelectionListener {

    private static final int DESELECTED = -1;

    private final EditPreferencesValueUIAction editPreferencesValueUIAction;

    /**
     * @param editPreferencesValueUIAction Action that will be toggled when a row is
     *                                     selected.
     */
    public PreferencesValuesListSelectionListener(EditPreferencesValueUIAction editPreferencesValueUIAction) {
        this.editPreferencesValueUIAction = editPreferencesValueUIAction;
    }

    /**
     * {@inheritDoc}
     * 
     * <P>
     * This implementation will toggle the enabled state of the action and set the
     * key, value type and value in the action depending on if the row was selected
     * or deselected.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        var table = editPreferencesValueUIAction.getPreferencesValuesTable();
        if (table.getSelectedRow() == DESELECTED) {
            editPreferencesValueUIAction.setEnabled(false);
            editPreferencesValueUIAction.setSelectedRow(DESELECTED);
            editPreferencesValueUIAction.setKey("");
            editPreferencesValueUIAction.setValueType("");
            editPreferencesValueUIAction.setValue("");
        } else {
            editPreferencesValueUIAction.setEnabled(true);
            editPreferencesValueUIAction.setSelectedRow(table.getSelectedRow());
            editPreferencesValueUIAction.setKey((String) table.getValueAt(table.getSelectedRow(), 0));
            editPreferencesValueUIAction.setValueType(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
            editPreferencesValueUIAction.setValue(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
        }
    }

}
