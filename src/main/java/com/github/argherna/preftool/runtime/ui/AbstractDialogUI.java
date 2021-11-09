package com.github.argherna.preftool.runtime.ui;

import java.awt.Component;
import java.lang.System.Logger.Level;
import java.util.Objects;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

abstract class AbstractDialogUI {

    static final System.Logger LOGGER =
            System.getLogger(AbstractDialogUI.class.getPackageName() + ".DialogUI");

    private String title;

    private boolean inputCanceled = false;

    abstract void showUI();

    /**
     *
     * @param component Component being labeled.
     * @param text      label text
     * @return JLabel with reference to labeled component.
     */
    JLabel createLabelFor(JComponent component, String text) {
        var label = new JLabel(text);
        label.setLabelFor(component);
        return label;
    }

    /**
     *
     * @param doc the Document to get text from.
     * @return the text in doc.
     * @throws BadLocationException if a BadLocationException occurs.
     */
    String getTextFrom(Document doc) throws BadLocationException {
        return doc.getText(0, doc.getLength());
    }

    /**
     *
     * @param doc             Document of JTextField.
     * @param errorTitle      title to put into the error dialog.
     * @param errorLogMessage message to log in case of error.
     * @return value in the Document or an empty String if an error occurs.
     */
    String getTextFrom(Document doc, String errorTitle, String errorLogMessage) {
        try {
            return getTextFrom(doc);
        } catch (BadLocationException ex) {
            LOGGER.log(Level.ERROR, errorLogMessage, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage(), errorTitle,
                    JOptionPane.ERROR_MESSAGE);
            return "";
        }
    }

    /**
     * Holder class.
     *
     */
    static final class Pair<K, V> {

        private final K k;

        private final V v;

        /**
         * Construct a new Pair.
         *
         * @param k the key for this Pair.
         * @param v the value for this Pair.
         */
        Pair(K k, V v) {
            this.k = k;
            this.v = v;
        }

        /**
         *
         * @return the key of this Pair.
         */
        K getK() {
            return k;
        }

        /**
         *
         * @return the value of this Pair.
         */
        V getV() {
            return v;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Pair [k=").append(k).append(", v=").append(v).append("]");
            return builder.toString();
        }

        @Override
        public int hashCode() {
            return Objects.hash(k, v);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            @SuppressWarnings("unchecked")
            Pair<K, V> other = (Pair<K, V>) obj;
            return Objects.equals(k, other.k) && Objects.equals(v, other.v);
        }
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    boolean isInputCanceled() {
        return inputCanceled;
    }

    void setInputCanceled(boolean inputCanceled) {
        this.inputCanceled = inputCanceled;
    }

    /**
     * Renders Pair instances based on the contents of the ComboBoxModel.
     */
    @SuppressWarnings({"serial", "unchecked"})
    static class PairListCellRenderer<V> extends DefaultListCellRenderer {

        /**
         * {@inheritDoc}
         */
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Pair<String, V> p = (Pair<String, V>) value;
            setText(p.getK());
            return this;
        }
    }
}
