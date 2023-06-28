package com.github.argherna.preftool.runtime.ui;

import static com.github.argherna.preftool.runtime.ui.UIConstants.PREF_TEXT_COLUMNS;
import static com.github.argherna.preftool.runtime.ui.UIConstants.PREF_TEXT_ROWS;

import java.util.prefs.Preferences;

import javax.swing.JTextArea;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Creates JTextAreas for entering Preferences values.
 */
public class PreferencesValueTextAreaFactory {

    /**
     * Return a JTextArea. The JTextArea will be
     * {@link UIConstants#PREF_TEXT_COLUMNS} wide by
     * {@link UIConstants#PREF_TEXT_ROWS} high and have a {@link Document} that will
     * only accept {@link Preferences#MAX_VALUE_LENGTH} characters.
     * 
     * @return JTextArea
     */
    public JTextArea create() {
        var preferencesValueTextArea = new JTextArea(createPreferencesValueDocument(), null, PREF_TEXT_ROWS,
                PREF_TEXT_COLUMNS);
        return preferencesValueTextArea;
    }

    /**
     * Return a Document for the JTextArea. The document will have a
     * {@link DocumentSizeFilter} with the max length set to
     * {@link Preferences#MAX_VALUE_LENGTH}.
     *
     * @return a Document.
     */
    private Document createPreferencesValueDocument() {
        var valueDocument = new PlainDocument();
        valueDocument.setDocumentFilter(new DocumentSizeFilter(Preferences.MAX_VALUE_LENGTH));
        return valueDocument;
    }
}
