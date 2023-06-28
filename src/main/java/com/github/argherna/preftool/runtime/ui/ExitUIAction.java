package com.github.argherna.preftool.runtime.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Action that exits the UI, closing the application.
 */
public class ExitUIAction extends AbstractAction {

    ExitUIAction() {
        super("Exit");
    }

    /**
     * Exits the app by calling {@linkplain System#exit(int) System.exit}.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
