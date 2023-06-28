package com.github.argherna.preftool.runtime.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Renders Pair instances based on the contents of a ComboBoxModel.
 */
@SuppressWarnings("unchecked")
class PairListCellRenderer<V> extends DefaultListCellRenderer {

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
