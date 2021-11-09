package com.github.argherna.preftool.runtime.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;

/**
 * PropertyChangeListener that listens for changes to text on a JLabel.
 */
class AddressLabelTextChangeListener implements PropertyChangeListener {

    private final List<AbstractPreferenceUIAction> actions;

    /**
     * Construct a new AddressLabelTextChangeListener instance.
     *
     * @param actions AbstractPreferenceUIAction objects to enable or disable.
     */
    public AddressLabelTextChangeListener(List<AbstractPreferenceUIAction> actions) {
        this.actions = actions;
    }

    /**
     * {@inheritDoc}
     *
     * <P>
     * This implementation will set the Preference Node Address value on all the Actions. The
     * Actions will be enabled if the new value is a non-empty String and disabled otherwise.
     *
     * @param evt the PropertyChangeEvent.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.nonNull(evt.getNewValue()) && !evt.getNewValue().toString().isEmpty()) {
            for (AbstractPreferenceUIAction action : actions) {
                action.setPreferencesNodeAddress(evt.getNewValue().toString());
                action.setEnabled(true);
            }
        } else if (Objects.nonNull(evt.getNewValue()) && evt.getNewValue().toString().isEmpty()) {
            actions.stream().forEach(a -> a.setEnabled(false));
        }
    }

}
