package com.github.argherna.preftool.runtime.ui;

import static com.github.argherna.preftool.runtime.ui.AbstractPreferenceUIAction.DISABLE_ACTIONS;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;

/**
 * PropertyChangeListener that listens for changes to text on a JLabel.
 */
class AddressLabelTextChangeListener implements PropertyChangeListener {

    private final List<AbstractPreferenceUIAction> actions;

    // While under development, moveActions will be enabled/disabled.
    private final List<AbstractPreferenceUIAction> moveActions;

    /**
     * Construct a new AddressLabelTextChangeListener instance.
     *
     * @param actions     AbstractPreferenceUIAction objects to enable or disable.
     * @param moveActions AbstractPreferenceUIAction objects that control moving
     *                    nodes and keys.
     */
    public AddressLabelTextChangeListener(List<AbstractPreferenceUIAction> actions,
            List<AbstractPreferenceUIAction> moveActions) {
        this.actions = actions;
        this.moveActions = moveActions;
    }

    /**
     * {@inheritDoc}
     *
     * <P>
     * This implementation will set the Preference Node Address value on all the
     * Actions. The
     * Actions will be enabled if the new value is a non-empty String and disabled
     * otherwise.
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
            // TODO Issue 1: Someday when the problem of consistently deleting a preference
            // properly from an event thread is figured out, the moveActions list can be
            // done away with completely and the ".disableActions" system property can be
            // removed.
            for (AbstractPreferenceUIAction moveAction : moveActions) {
                moveAction.setPreferencesNodeAddress(evt.getNewValue().toString());
                moveAction.setEnabled(!DISABLE_ACTIONS);
            }
        } else if (Objects.nonNull(evt.getNewValue()) && evt.getNewValue().toString().isEmpty()) {

            actions.stream().forEach(a -> a.setEnabled(false));
            moveActions.stream().forEach(ma -> ma.setEnabled(false));
        }
    }

}
