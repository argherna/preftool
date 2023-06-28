package com.github.argherna.preftool.runtime.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import com.github.argherna.preftool.ExportPreferences;

/**
 * An Action that exports a Preferences node.
 */
public class ExportUIAction extends AbstractPreferenceUIAction {

    /**
     * Processes the given ActionEvent.
     *
     * <P>
     * Preferences will be exported if the Preferences node can be determined from
     * the ActionEvent
     * and if a file to save the output to is selected. Errors are logged and
     * feedback is given to
     * the user.
     *
     * @param e the ActionEvent.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        var preferences = getPreferencesFromNodeAddress();
        if (Objects.isNull(preferences)) {
            return;
        }

        var saveFile = getSaveFile();
        if (saveFile == DEV_NULL) {
            return;
        }

        try (var outputStream = new FileOutputStream(saveFile);) {
            var exporter = new ExportPreferences(preferences, outputStream,
                    e.getActionCommand().equals("Node"));
            exporter.call();
        } catch (Exception ex) {
            handleUIError(ex, "Export error", (Component) e.getSource());
            return;
        }
    }

    /**
     * Return the File representing where the exported Preferences data will be
     * saved. If the user cancels the save,
     * {@link AbstractPreferenceUIAction#DEV_NULL} is returned.
     * 
     * @return File object
     */
    private File getSaveFile() {
        var saveFile = DEV_NULL;
        var jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        var option = jfc.showSaveDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            saveFile = jfc.getSelectedFile();
        }
        return saveFile;
    }
}
