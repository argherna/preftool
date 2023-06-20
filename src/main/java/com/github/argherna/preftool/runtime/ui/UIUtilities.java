package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.ERROR;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * User interface utilities.
 */
class UIUtilities {

    private static final System.Logger LOGGER = System.getLogger(UIUtilities.class.getName());

    private UIUtilities() {
    }

    /**
     * @return a DefaultTableModel to hold the Preferences key, value and type data.
     */
    static DefaultTableModel createPreferencesDataTableModel() {
        var tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Type");
        tableModel.addColumn("Value");
        return tableModel;
    }

    /**
     * Log and display error messages.
     *
     * <P>
     * This has the same effect as calling
     * {@link AbstractPreferenceUIAction#handleUIError(Exception, String, Component)
     * handlUIError(Exception, String, null)}.
     */
    static void handleUIError(Exception ex, String errTitle) {
        handleUIError(ex, errTitle, null);
    }

    /**
     * Log and display error messages.
     *
     * @param ex              cause of the error.
     * @param errTitle        title for an error dialog.
     * @param parentComponent the parent Component.
     */
    static void handleUIError(Exception ex, String errTitle, Component parentComponent) {
        var message = String.format("An error occurred: %s", ex.getMessage());
        LOGGER.log(ERROR, message, ex);
        JOptionPane.showMessageDialog(parentComponent, message, errTitle,
                JOptionPane.ERROR_MESSAGE);
    }
}
