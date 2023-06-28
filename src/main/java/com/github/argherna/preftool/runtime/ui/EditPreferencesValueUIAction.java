package com.github.argherna.preftool.runtime.ui;

import java.awt.event.ActionEvent;
import java.util.Objects;
import java.awt.Component;

import javax.swing.JTable;

import com.github.argherna.preftool.AddPreferencesKey;

/**
 * Action that edits a Preferences value.
 */
public class EditPreferencesValueUIAction extends AbstractPreferenceUIAction {

    private final JTable preferencesValuesTable;

    private String key;

    private String valueType;

    private String value;

    private int selectedRow = -1;

    /**
     * @param preferencesValuesTable
     */
    public EditPreferencesValueUIAction(JTable preferencesValuesTable) {
        this.preferencesValuesTable = preferencesValuesTable;
    }

    /**
     * @param selectedRow the selectedRow to set
     */
    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @param valueType the valueType to set
     */
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * 
     * <P>
     * This implementation will edit a Preferences value if the following are true:
     * <OL>
     * <LI>The {@code key}, {@code valueType}, and {@code value} are set.
     * <LI>Input from the dialog box is not canceled.
     * <LI>The text for the new value is not blank or empty.
     * <LI>The active node address points to an existing Preferences node.
     * </OL>
     * 
     * If the above are all true, then the Preferences value will be saved.
     * 
     * @see #setKey(String)
     * @see #setValueType(String)
     * @see #setValue(String)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Don't do anything if key, value type or value is empty.
        if (key.isEmpty() || valueType.isEmpty() || value.isEmpty()) {
            return;
        }

        var editPreferencesKeyUI = new EditPreferencesValueUI(key, valueType, value);
        editPreferencesKeyUI.showUI();

        if (editPreferencesKeyUI.isInputCanceled()) {
            return;
        }

        var newValue = editPreferencesKeyUI.getPreferencesValueText();
        if (newValue.isEmpty() || newValue.isBlank()) {
            return;
        }

        var preferences = getPreferencesFromNodeAddress();
        if (Objects.isNull(preferences)) {
            return;
        }

        // AddPreferencesKey will be the same as changing a value since this operation
        // uses the put methods anyway.
        var keyAdder = new AddPreferencesKey(preferences, key, getValueTypeClass(), newValue);
        try {
            keyAdder.call();
        } catch (Exception ex) {
            handleUIError(ex, "Edit Key errir", (Component) e.getSource());
            return;
        }

        var tableModel = preferencesValuesTable.getModel();
        tableModel.setValueAt(newValue, selectedRow, 2);
    }

    /**
     * Return the Class object represented by the value type.
     * 
     * @return Class object.
     */
    private Class<?> getValueTypeClass() {
        if (valueType.equals("boolean")) {
            return boolean.class;
        }
        if (valueType.equals("byte[]")) {
            return byte[].class;
        }
        if (valueType.equals("double")) {
            return double.class;
        }
        if (valueType.equals("float")) {
            return float.class;
        }
        if (valueType.equals("int")) {
            return int.class;
        }
        if (valueType.equals("long")) {
            return long.class;
        }
        return String.class;
    }

    /**
     * Return the JTable with preferences values.
     * 
     * @return the preferencesValuesTable
     */
    public JTable getPreferencesValuesTable() {
        return preferencesValuesTable;
    }
}
