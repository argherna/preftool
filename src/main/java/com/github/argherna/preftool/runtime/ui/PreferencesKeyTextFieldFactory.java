package com.github.argherna.preftool.runtime.ui;

import static com.github.argherna.preftool.runtime.ui.UIConstants.PREF_TEXT_COLUMNS;

import java.util.prefs.Preferences;

import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Creates JTextFields for entering Preferences key names.
 */
public class PreferencesKeyTextFieldFactory {

    /**
     * Return a JTextField. The JTextField will have a {@link Document} that is
     * {@link UIConstants#PREF_TEXT_COLUMNS} wide and will only accept
     * {@link Preferences#MAX_KEY_LENGTH} characters.
     * 
     * @return JTextField
     */
    public JTextField create() {
        var preferencesKeyTextField = new JTextField(createPreferencesKeyDocument(), null, PREF_TEXT_COLUMNS);
        return preferencesKeyTextField;
    }

    /**
     * Return a Document for the JTextField. The document will have a
     * {@link DocumentSizeFilter} with the max length set to
     * {@link Preferences#MAX_KEY_LENGTH}.
     * 
     * @return a Document
     */
    private Document createPreferencesKeyDocument() {
        var preferencesKeyDocument = new PlainDocument();
        preferencesKeyDocument.setDocumentFilter(new DocumentSizeFilter(Preferences.MAX_KEY_LENGTH));
        return preferencesKeyDocument;
    }
}
