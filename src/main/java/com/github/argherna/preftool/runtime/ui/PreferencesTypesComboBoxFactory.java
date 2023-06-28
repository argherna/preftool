package com.github.argherna.preftool.runtime.ui;

import static com.github.argherna.preftool.runtime.ui.UIConstants.PREF_TYPES;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * Creates an instance of a JComboBox populated with the types of Preferences.
 * 
 * @see JComboBox
 * @see DefaultComboBoxModel
 * @see PairListCellRenderer
 * @see Pair
 */
public class PreferencesTypesComboBoxFactory {

    /**
     * @return JComboBox populated with the types of Preferences that can be saved.
     */
    public JComboBox<Pair<String, Class<?>>> create() {
        var preferencesTypeComboBox = new JComboBox<Pair<String, Class<?>>>();
        preferencesTypeComboBox.setModel(createPreferencesTypeModel());
        preferencesTypeComboBox.setSelectedIndex(0);
        preferencesTypeComboBox.setRenderer(new PairListCellRenderer<Class<?>>());
        return preferencesTypeComboBox;
    }

    /**
     * 
     * @return ComboBoxModel populated with the different Preferences types.
     * @see UIConstants#PREF_TYPES
     */
    private ComboBoxModel<Pair<String, Class<?>>> createPreferencesTypeModel() {
        var cboxModel = new DefaultComboBoxModel<Pair<String, Class<?>>>();
        cboxModel.addAll(PREF_TYPES);
        return cboxModel;
    }
}
