package com.github.argherna.preftool.runtime.ui;

import static com.github.argherna.preftool.runtime.ui.AbstractPreferenceUIAction.DISABLE_ACTIONS;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Objects;

/**
 * PropertyChangeListener that listens for changes to text on a JLabel.
 * 
 * <P>
 * Sets the Preferences node address for all Actions in each category.
 */
public class AddressLabelTextChangeListener implements PropertyChangeListener {

    private final List<AbstractPreferenceUIAction> actions;

    private final List<AbstractPreferenceUIAction> changeActions;

    private final List<AbstractPreferenceUIAction> editActions;

    /**
     * Construct a new AddressLabelTextChangeListener instance.
     *
     * @param actions       AbstractPreferenceUIAction objects to enable or disable.
     * @param changeActions AbstractPreferenceUIAction objects that control changing
     *                      nodes and keys.
     */
    public AddressLabelTextChangeListener(List<AbstractPreferenceUIAction> actions,
            List<AbstractPreferenceUIAction> changeActions,
            List<AbstractPreferenceUIAction> editActions) {
        this.actions = actions;
        this.changeActions = changeActions;
        this.editActions = editActions;
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
            for (AbstractPreferenceUIAction changeAction : changeActions) {
                changeAction.setPreferencesNodeAddress(evt.getNewValue().toString());
                changeAction.setEnabled(!DISABLE_ACTIONS);
            }
            // edit actions are enabled elsewhere, but still should be informed when the
            // node address changes.
            for (AbstractPreferenceUIAction editAction : editActions) {
                editAction.setPreferencesNodeAddress(evt.getNewValue().toString());
            }
        } else if (Objects.nonNull(evt.getNewValue()) && evt.getNewValue().toString().isEmpty()) {
            actions.stream().forEach(a -> a.setEnabled(false));
            changeActions.stream().forEach(ma -> ma.setEnabled(false));
        }
    }

}
