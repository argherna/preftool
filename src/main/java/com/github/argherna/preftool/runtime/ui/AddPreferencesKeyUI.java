package com.github.argherna.preftool.runtime.ui;

import java.util.Objects;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * User interface for adding a Preferences Key, Value and Type.
 */
public class AddPreferencesKeyUI extends AbstractDialogUI {

    private final JTextField preferencesKeyTextField;

    private final JComboBox<Pair<String, Class<?>>> preferencesTypesComboBox;

    private final JTextArea preferencesValueTextArea;

    /**
     * Construct a new AddKeyUI instance.
     */
    public AddPreferencesKeyUI() {
        var preferenceKeyTextFieldFactory = new PreferencesKeyTextFieldFactory();
        preferencesKeyTextField = preferenceKeyTextFieldFactory.create();

        var preferencesTypesComboBoxFactory = new PreferencesTypesComboBoxFactory();
        preferencesTypesComboBox = preferencesTypesComboBoxFactory.create();

        var preferencesValueTextAreaFactory = new PreferencesValueTextAreaFactory();
        preferencesValueTextArea = preferencesValueTextAreaFactory.create();
    }

    /**
     *
     * @return the Preferences key.
     */
    protected String getPreferencesKeyText() {
        return getTextFrom(preferencesKeyTextField.getDocument(), "Add Preferences Key Error",
                "Problem with preferences key");
    }

    /**
     *
     * @return the Preferences value.
     */
    @Override
    public String getPreferencesValueText() {
        return getTextFrom(preferencesValueTextArea.getDocument(), "Add Preferences Value Error",
                "Problem with preferences value");
    }

    /**
     *
     * @return the Preferences type.
     */
    @SuppressWarnings("unchecked")
    protected Class<?> getPreferencesType() {
        var selection = (Pair<String, Class<?>>) preferencesTypesComboBox.getModel().getSelectedItem();
        return (Objects.nonNull(selection)) ? selection.getV() : null;
    }

    /**
     * Shows the user interface.
     */
    public void showUI() {
        var preferencesKeyLabelComponent = createLabelFor(preferencesKeyTextField, "Key");
        var preferencesTypeLabelComponent = createLabelFor(preferencesTypesComboBox, "Type");
        var preferencesValueLabelComponent = createLabelFor(preferencesValueTextArea, "Value");

        var title = "Enter Key, Type, Value";
        final JComponent[] inputs = new JComponent[] { preferencesKeyLabelComponent, preferencesKeyTextField,
                preferencesTypeLabelComponent, preferencesTypesComboBox, preferencesValueLabelComponent,
                preferencesValueTextArea };

        int result = JOptionPane.showConfirmDialog(null, inputs, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        setInputCanceled(result != JOptionPane.OK_OPTION);
    }
}
