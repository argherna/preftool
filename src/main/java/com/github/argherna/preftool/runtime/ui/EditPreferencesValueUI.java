package com.github.argherna.preftool.runtime.ui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * User interface for editing a Preferences value.
 */
public class EditPreferencesValueUI extends AbstractDialogUI {

    private final JLabel preferencesKeyLabel;

    private final JLabel preferencesValueTypeLabel;

    private final JTextArea preferencesValueTextArea;

    /**
     * Constructs a new EditPreferencesValueUI with a Preferences key, value type,
     * and value.
     * 
     * @param key       Preferences key.
     * @param valueType class name of the value type.
     * @param value     value as String.
     */
    public EditPreferencesValueUI(String key, String valueType, String value) {
        preferencesKeyLabel = new JLabel(key);
        preferencesKeyLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        preferencesValueTypeLabel = new JLabel(valueType);
        preferencesValueTypeLabel.setBorder(BorderFactory.createLoweredBevelBorder());

        var preferencesValueTextAreaFactory = new PreferencesValueTextAreaFactory();
        preferencesValueTextArea = preferencesValueTextAreaFactory.create();
        preferencesValueTextArea.setText(value);
    }

    /**
     * Shows the dialog box for editing a Preferences value. The Preferences value
     * is the only editable field for this dialog box.
     */
    @Override
    public void showUI() {
        var preferencesKeyLabelComponent = createLabelFor(preferencesKeyLabel, "Key");
        var preferencesTypeLabelComponent = createLabelFor(preferencesValueTypeLabel, "Type");
        var preferencesValueLabelComponent = createLabelFor(preferencesValueTextArea, "Value");

        final JComponent[] inputs = new JComponent[] { preferencesKeyLabelComponent, preferencesKeyLabel,
                preferencesTypeLabelComponent, preferencesValueTypeLabel, preferencesValueLabelComponent,
                preferencesValueTextArea };
        var title = "Edit Value";
        int result = JOptionPane.showConfirmDialog(null, inputs, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        setInputCanceled(result != JOptionPane.OK_OPTION);
    }

    /**
     * Return the text in the value text area as a String.
     * 
     * @return Value as a String.
     */
    @Override
    public String getPreferencesValueText() {
        return getTextFrom(preferencesValueTextArea.getDocument(), "Edit Preferences Value Error",
                "Problem with preferences value");
    }
}
