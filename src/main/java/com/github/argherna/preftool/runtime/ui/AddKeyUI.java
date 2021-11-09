package com.github.argherna.preftool.runtime.ui;

import java.util.List;
import java.util.Objects;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * User interface for adding a Preferences Key, Value and Type.
 */
class AddKeyUI extends AbstractDialogUI {

    private static final List<Pair<String, Class<?>>> PREF_TYPES =
            List.of(new Pair<String, Class<?>>(String.class.getName(), String.class),
                    new Pair<String, Class<?>>("byte[]", byte[].class),
                    new Pair<String, Class<?>>(boolean.class.getName(), boolean.class),
                    new Pair<String, Class<?>>(double.class.getName(), double.class),
                    new Pair<String, Class<?>>(float.class.getName(), float.class),
                    new Pair<String, Class<?>>(int.class.getName(), int.class),
                    new Pair<String, Class<?>>(long.class.getName(), long.class));

    private Document preferencesKey;

    private ComboBoxModel<Pair<String, Class<?>>> preferencesType;

    private Document preferencesValue;

    /**
     * Construct a new AddKeyUI instance.
     */
    AddKeyUI() {
        preferencesKey = createPreferencesKeyModel();
        preferencesType = createPreferencesTypeModel();
        preferencesValue = createPreferencesValueModel();
    }

    /**
     *
     * @return Document for the Preferences key.
     */
    private Document createPreferencesKeyModel() {
        return new PlainDocument();
    }

    /**
     *
     * @return Document for the Preferences value.
     */
    private Document createPreferencesValueModel() {
        return new PlainDocument();
    }

    /**
     *
     * @return ComboBoxModel model for the Preferences type.
     */
    private ComboBoxModel<Pair<String, Class<?>>> createPreferencesTypeModel() {
        var cboxModel = new DefaultComboBoxModel<Pair<String, Class<?>>>();
        cboxModel.addAll(PREF_TYPES);
        return cboxModel;
    }

    /**
     *
     * @return the Preferences key.
     */
    String getPreferencesKey() {
        return getTextFrom(preferencesKey, "Add Preferences Key Error",
                "Problem with preferences key");
    }

    /**
     *
     * @return the Preferences value.
     */
    String getPreferencesValue() {
        return getTextFrom(preferencesValue, "Add Preferences Value Error",
                "Problem with preferences value");
    }

    /**
     *
     * @return the Preferences type.
     */
    @SuppressWarnings("unchecked")
    Class<?> getPreferencesType() {
        var selection = (Pair<String, Class<?>>) preferencesType.getSelectedItem();
        return (Objects.nonNull(selection)) ? selection.getV() : null;
    }

    /**
     * Shows the user interface.
     */
    void showUI() {
        var preferencesKeyTextField = createPreferencesKeyTextField();
        var preferencesKeyLabel = createLabelFor(preferencesKeyTextField, "Key");

        var preferencesTypeComboBox = createPreferencesTypeComboBox();
        var preferencesTypeLabel = createLabelFor(preferencesTypeComboBox, "Type");

        var preferencesValueTextField = createPreferencesValueTextField();
        var preferencesValueLabel = createLabelFor(preferencesValueTextField, "Value");

        final JComponent[] inputs = new JComponent[] {preferencesKeyLabel, preferencesKeyTextField,
                preferencesTypeLabel, preferencesTypeComboBox, preferencesValueLabel,
                preferencesValueTextField};
        int result = JOptionPane.showConfirmDialog(null, inputs, "Enter Key, Type, Value",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        setInputCanceled(result != JOptionPane.OK_OPTION);
    }

    /**
     *
     * @return JTextField for the preferences key name.
     */
    private JTextField createPreferencesKeyTextField() {
        var preferencesKeyTextField = new JTextField();
        preferencesKeyTextField.setDocument(preferencesKey);
        return preferencesKeyTextField;
    }

    /**
     *
     * @return JComboBox with the selectable preferences value types.
     */
    private JComboBox<Pair<String, Class<?>>> createPreferencesTypeComboBox() {
        var preferencesTypeComboBox = new JComboBox<Pair<String, Class<?>>>();
        preferencesTypeComboBox.setModel(preferencesType);
        preferencesTypeComboBox.setSelectedIndex(0);
        preferencesTypeComboBox.setRenderer(new PairListCellRenderer<Class<?>>());
        return preferencesTypeComboBox;
    }

    /**
     *
     * @return JTextField for the preferences value.
     */
    private JTextField createPreferencesValueTextField() {
        var preferencesValueTextField = new JTextField();
        preferencesValueTextField.setDocument(preferencesValue);
        return preferencesValueTextField;
    }

}
