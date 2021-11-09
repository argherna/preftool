package com.github.argherna.preftool.runtime.ui;

import java.awt.Component;
import java.io.File;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * Base class for many of the Action implementations.
 */
@SuppressWarnings("serial")
abstract class AbstractPreferenceUIAction extends AbstractAction {

    static Boolean DISABLE_ACTIONS = Boolean
            .getBoolean(AbstractPreferenceUIAction.class.getPackageName() + ".disableActions");

    static final System.Logger LOGGER =
            System.getLogger(AbstractPreferenceUIAction.class.getPackageName() + ".UIAction");

    static final File DEV_NULL = new File("/dev/null");

    private String preferencesNodeAddress = "";

    // Log system property if set.
    static {
        if (DISABLE_ACTIONS) {
            LOGGER.log(Level.INFO,
                    "disableActions set; certain changes to preferences will not happen.");
        }
    }

    /**
     * @return Preferences node address in {@code root:/path} format.
     */
    String getPreferencesNodeAddress() {
        return preferencesNodeAddress;
    }

    /**
     * Sets the Preferences node address.
     *
     * @param preferencesNodeAddress node address in {@code root:/path} format.
     */
    void setPreferencesNodeAddress(String preferencesNodeAddress) {
        this.preferencesNodeAddress = preferencesNodeAddress;
    }

    /**
     *
     * @return Preferences node or {@code null} if the Preferences node address String is formatted
     *         incorrectly.
     */
    Preferences getPreferencesFromNodeAddress() {
        if (Objects.isNull(preferencesNodeAddress) || preferencesNodeAddress.isEmpty()
                || preferencesNodeAddress.indexOf(":") == -1) {
            return null;
        }
        var prefNodeName = getPreferencesNodeAddress().split(":");
        return prefNodeName[0].equals("User") ? Preferences.userRoot().node(prefNodeName[1])
                : Preferences.systemRoot().node(prefNodeName[1]);
    }

    /**
     * Log and display error messages.
     *
     * <P>
     * This has the same effect as calling
     * {@link AbstractPreferenceUIAction#handleUIError(Exception, String, Component)
     * handlUIError(Exception, String, null)}.
     */
    void handleUIError(Exception ex, String errTitle) {
        handleUIError(ex, errTitle, null);
    }

    /**
     * Log and display error messages.
     *
     * @param ex              cause of the error.
     * @param errTitle        title for an error dialog.
     * @param parentComponent the parent Component.
     */
    void handleUIError(Exception ex, String errTitle, Component parentComponent) {
        var message = String.format("An error occurred: %s", ex.getMessage());
        LOGGER.log(Level.ERROR, message, ex);
        JOptionPane.showMessageDialog(parentComponent, message, errTitle,
                JOptionPane.ERROR_MESSAGE);
    }
}
