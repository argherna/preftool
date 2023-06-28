package com.github.argherna.preftool.runtime.ui;

import javax.swing.table.DefaultTableModel;

/**
 * Creates instances of {@link DefaultTableModel} which will display Preferences
 * keys and values.
 */
public class PreferencesValuesTableModelFactory {

    /**
     * Creates the DefaultTableModel used for displaying Preferences keys and
     * values.
     * 
     * <P>
     * This TableModel enforces no edits on any of the cells.
     * 
     * @return DefaultTableModel.
     */
    public DefaultTableModel create() {
        var tableModel = new DefaultTableModel() {

            /**
             * This implementation always returns {@code false}.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.addColumn("Name");
        tableModel.addColumn("Type");
        tableModel.addColumn("Value");
        return tableModel;
    }
}
