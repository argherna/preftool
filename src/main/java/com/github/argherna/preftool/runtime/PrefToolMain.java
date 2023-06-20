package com.github.argherna.preftool.runtime;

import javax.swing.UIManager;

import com.github.argherna.preftool.runtime.ui.PrefToolUI;

/**
 * PrefTool entry point class.
 */
public final class PrefToolMain {

    static final Boolean DRY_RUN = Boolean.getBoolean(PrefToolMain.class.getPackageName() + ".dryrun");

    static final Boolean SHOWTRACES = Boolean.getBoolean(PrefToolMain.class.getPackageName() + ".showtraces");

    private PrefToolMain() {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't use system look and feel.");
        }

        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    var ui = new PrefToolUI();
                    ui.setVisible(true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
