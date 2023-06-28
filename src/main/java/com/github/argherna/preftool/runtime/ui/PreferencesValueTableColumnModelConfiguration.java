package com.github.argherna.preftool.runtime.ui;

import javax.swing.DefaultCellEditor;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * Creates a TableColumnModel for a JTable to use for displaying Preferences
 * keys, values, and value types.
 * 
 * <P>
 * The TableColumnModel assumes there's a 3-column table. It will set the
 * following:
 * 
 * <OL>
 * <LI>Column {@value #DEFAULT_VALUE_TYPE_COLUMN_INDEX} will use a JComboBox
 * with the different value type names as selections.
 * </OL>
 * 
 * @see PreferencesTypesComboBoxFactory
 */
public class PreferencesValueTableColumnModelConfiguration {

    /** Column for the Preferences key. */
    public static final int DEFAULT_KEY_COLUMN_INDEX = 0;

    /** Column for the Preferences value type. */
    public static final int DEFAULT_VALUE_TYPE_COLUMN_INDEX = 1;

    /** Column for the Preferences value. */
    public static final int DEFAULT_VALUE_COLUMN_INDEX = 2;

    public void configure(DefaultTableColumnModel tableColumnModel) {
        configureKeyTypeColumnModel(tableColumnModel.getColumn(DEFAULT_VALUE_TYPE_COLUMN_INDEX));
    }

    public DefaultTableColumnModel create() {
        var prefKeyTableColumnModel = new DefaultTableColumnModel();
        configureKeyTypeColumnModel(prefKeyTableColumnModel.getColumn(1));
        return prefKeyTableColumnModel;
    }

    private void configureKeyTypeColumnModel(TableColumn keyColumn) {
        var typeComboboxFactory = new PreferencesTypesComboBoxFactory();
        var typeSelection = typeComboboxFactory.create();
        keyColumn.setCellEditor(new DefaultCellEditor(typeSelection));
    }
}
