package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;

import java.awt.Component;
import java.io.File;
import java.util.Objects;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

/**
 * Base class for many of the Action implementations.
 */
public abstract class AbstractPreferenceUIAction extends AbstractAction {

    /**
     * {@systemProperty com.github.argherna.preftool.runtime.ui.disableActions}
     * disables destructive actions in the UI (delete and move).
     */
    protected static Boolean DISABLE_ACTIONS = Boolean
            .getBoolean(AbstractPreferenceUIAction.class.getPackageName() + ".disableActions");

    /** System Logger for subclasses. */
    protected static final System.Logger LOGGER = System
            .getLogger(AbstractPreferenceUIAction.class.getPackageName() + ".UIAction");

    /** Placeholder File object to indicate no file was selected. */
    protected static final File DEV_NULL = new File("/dev/null");

    private String preferencesNodeAddress = "";

    // Log system property if set.
    static {
        if (DISABLE_ACTIONS) {
            LOGGER.log(INFO,
                    "disableActions system property set; certain changes to preferences will not happen.");
        }
    }

    /**
     * Return the Preferences node address in {@code [root]:/[path/to/node]} format.
     * 
     * @return Preferences node address
     */
    protected String getPreferencesNodeAddress() {
        return preferencesNodeAddress;
    }

    /**
     * Set the Preferences node address. The format must be
     * {@code [root]:/[path/to/node]}.
     *
     * @param preferencesNodeAddress node address in {@code root:/path} format.
     */
    protected void setPreferencesNodeAddress(String preferencesNodeAddress) {
        this.preferencesNodeAddress = preferencesNodeAddress;
    }

    /**
     * Return the Preferences node associated with the node address or {@code null}
     * if the Preferences node address String is formatted incorrectly.
     * 
     * @return Preferences node
     */
    protected Preferences getPreferencesFromNodeAddress() {
        if (Objects.isNull(preferencesNodeAddress) || preferencesNodeAddress.isEmpty()
                || preferencesNodeAddress.indexOf(":") == -1) {
            return null;
        }
        var prefNodeName = getPreferencesNodeAddress().split(":");
        return prefNodeName[0].equals("User") ? Preferences.userRoot().node(prefNodeName[1])
                : Preferences.systemRoot().node(prefNodeName[1]);
    }

    /**
     * Log and display error messages. This has the same effect as calling
     * {@link AbstractPreferenceUIAction#handleUIError(Exception, String, Component)
     * handlUIError(Exception, String, null)}.
     * 
     * <P>This should be used in subclasses as follows:
     * <PRE>
     * {@code 
     * try {
     *     methodThatCouldThrowException();
     * } catch (Exception ex) {
     *     handleUIError(ex, "Error occurred");
     * }
     * }</PRE>
     * 
     * @param ex       Exception that caused this method to be called.
     * @param errTitle Title for the error dialog to display.
     */
    protected void handleUIError(Exception ex, String errTitle) {
        handleUIError(ex, errTitle, null);
    }

    /**
     * Log and display error messages.
     *
     * @param ex              Exception that caused this method to be called.
     * @param errTitle        Title for the error dialog to display.
     * @param parentComponent the parent Component (can be {@code null}).
     */
    protected void handleUIError(Exception ex, String errTitle, Component parentComponent) {
        var message = String.format("An error occurred: %s", ex.getMessage());
        LOGGER.log(ERROR, message, ex);
        JOptionPane.showMessageDialog(parentComponent, message, errTitle,
                JOptionPane.ERROR_MESSAGE);
    }
}
