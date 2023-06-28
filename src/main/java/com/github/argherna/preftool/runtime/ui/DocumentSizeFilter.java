package com.github.argherna.preftool.runtime.ui;

import java.awt.Toolkit;
import java.util.Objects;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * DocumentFilter that limits the size of text that can be input.
 */
public class DocumentSizeFilter extends DocumentFilter {

    private final int maxLength;

    /**
     * Constructs a DocumentSizeFilter with a maximum length.
     * 
     * @param maxLength the maximum length of a String allowed to be entered into
     *                  the underlying Document.
     */
    public DocumentSizeFilter(int maxLength) {
        if (maxLength < 0) {
            throw new IllegalArgumentException(String.format("DocumentSizeFilter must have max length > 0, %d given"));
        }
        this.maxLength = maxLength;
    }

    /**
     * {@inheritDoc}
     * 
     * <P>
     * This implementation checks the current length of the document and if it is
     * greater than this instance's max length, beep.
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {

        if (insertionTooLong(fb.getDocument(), string)) {
            Toolkit.getDefaultToolkit().beep();
        } else {
            super.insertString(fb, offset, string, attr);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * <P>
     * This implementation checks the current length of the document and if it is
     * greater than this instance's max length, beep.
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {

        if (replacementTooLong(fb.getDocument(), text, length)) {
            Toolkit.getDefaultToolkit().beep();
        } else {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    /**
     * Return {@code true} if the given text length plus the current Document
     * length exceeds this instance's max length.
     * 
     * @param document the Document
     * @param text     the String to append to the document.
     * @return {@code true} if the document length plus string length exceeds max
     *         length.
     */
    private boolean insertionTooLong(Document document, String text) {
        return document.getLength() + text.length() > maxLength;
    }

    /**
     * Return {@code true} if the length of the replacement text exceeds max
     * length.
     * 
     * @param document the Document
     * @param text     text to insert/replace
     * @param length   length of text deleted by the replacement
     * @return {@code true} if the length of the replacement text exceeds max
     *         length.
     */
    private boolean replacementTooLong(Document document, String text, int length) {
        if (Objects.isNull(text)) {
            text = "";
        }
        return document.getLength() + text.length() - length > maxLength;
    }

}
