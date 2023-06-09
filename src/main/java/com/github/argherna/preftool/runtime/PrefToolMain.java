package com.github.argherna.preftool.runtime;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.UIManager;
// import javax.swing.UIManager.LookAndFeelInfo;

import com.github.argherna.preftool.runtime.ui.PrefToolUI;

/**
 * PrefTool entry point class.
 */
public final class PrefToolMain {

    static final Boolean DRY_RUN =
            Boolean.getBoolean(PrefToolMain.class.getPackageName() + ".dryrun");

    static final Boolean SHOWTRACES =
            Boolean.getBoolean(PrefToolMain.class.getPackageName() + ".showtraces");

    private PrefToolMain() {}

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
                var ui = new PrefToolUI();
                ui.setVisible(true);
            }
        });
    }
}
