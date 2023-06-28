package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.ERROR;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Base class for PrefTool's dialog boxes.
 */
public abstract class AbstractDialogUI {

    /** System Logger for all subclasses. */
    protected static final System.Logger LOGGER = System
            .getLogger(AbstractDialogUI.class.getPackageName() + ".DialogUI");

    private boolean inputCanceled = false;

    /**
     * Shows the dialog box.
     * 
     * <P>
     * Classes that override this method will format all the components for the
     * dialog.
     */
    public abstract void showUI();

    /**
     * Factory for associating a JLabel to a JComponent.
     * 
     * @param component Component being labeled.
     * @param text      label text
     * @return JLabel with reference to labeled component.
     */
    protected JLabel createLabelFor(JComponent component, String text) {
        var label = new JLabel(text);
        label.setLabelFor(component);
        return label;
    }

    /**
     * Returns all text in the given {@link Document}.
     * 
     * @param doc the Document to get text from.
     * @return the text in doc.
     * @throws BadLocationException if a BadLocationException occurs.
     */
    protected String getTextFrom(Document doc) throws BadLocationException {
        return doc.getText(0, doc.getLength());
    }

    /**
     * Returns all text in the given {@link Document}.
     * 
     * <P>
     * If an error occurs, an error dialog will be shown and the exception will be
     * logged.
     * 
     * @param doc             Document of JTextField.
     * @param errorTitle      title to put into the error dialog.
     * @param errorLogMessage message to log in case of error.
     * @return value in the Document or an empty String if an error occurs.
     */
    protected String getTextFrom(Document doc, String errorTitle, String errorLogMessage) {
        try {
            return getTextFrom(doc);
        } catch (BadLocationException ex) {
            LOGGER.log(ERROR, errorLogMessage, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), errorTitle,
                    JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    /**
     * Returns {@code true} if input from the dialog was canceled.
     * 
     * @return true if the user selects Cancel on the dialog box.
     */
    public boolean isInputCanceled() {
        return inputCanceled;
    }

    /**
     * Set whether input was canceled from the dialog box.
     * 
     * @param inputCanceled true if input from the dialog box was Canceled.
     */
    public void setInputCanceled(boolean inputCanceled) {
        this.inputCanceled = inputCanceled;
    }

    /**
     * Returns the text used to set the Preferences value.
     */
    public abstract String getPreferencesValueText();
}
