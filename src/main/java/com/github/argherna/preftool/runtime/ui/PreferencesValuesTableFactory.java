package com.github.argherna.preftool.runtime.ui;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Creates instances of {@link JTable} which will display Preferences
 * keys, value types, and values.
 */
public class PreferencesValuesTableFactory {

    /**
     * Returns a JTable instance for displaying Prefereces keys, values and value
     * types.
     * 
     * <P>
     * The {@link TableModel} for the JTable is as-created by
     * {@link PreferencesValuesTableModelFactory}. The {@link TableColumnModel} is
     * as-created by {@link PreferencesValueTableColumnModelConfiguration}.
     * 
     * @return JTable instance
     * @see PreferencesValuesTableModelFactory
     * @see PreferencesValuesTableColumnModelFactory
     */
    public JTable create() {
        var tableModelFactory = new PreferencesValuesTableModelFactory();
        var preferencesValuesTableModel = tableModelFactory.create();

        var preferencesValuesTable = new JTable();
        preferencesValuesTable.setModel(preferencesValuesTableModel);
        preferencesValuesTable.setName("preferencesValuesTable");
        preferencesValuesTable.setFillsViewportHeight(true);
        preferencesValuesTable.setShowHorizontalLines(false);
        preferencesValuesTable.setShowVerticalLines(false);

        return preferencesValuesTable;
    }
}
