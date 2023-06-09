package com.github.argherna.preftool.runtime.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;

import com.github.argherna.preftool.ImportPreferences;

/**
 * An Action that imports Preferences from an XML file.
 */
class ImportUIAction extends AbstractPreferenceUIAction {

    private final JTree preferencesNodeTree;

    /**
     * Construct a new ImportUIAction.
     *
     * @param preferencesNodeTree JTree to be updated after a successful import.
     */
    ImportUIAction(JTree preferencesNodeTree) {
        this.preferencesNodeTree = preferencesNodeTree;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var importFile = getImportFile();
        if (importFile == DEV_NULL) {
            return;
        }

        // Import the XML file.
        try (var inputStream = new FileInputStream(importFile);) {
            var importer = new ImportPreferences(inputStream);
            importer.call();
        } catch (Exception ex) {
            handleUIError(ex, "Import error", (Component) e.getSource());
            return;
        }

        // Update the user interface (eagerly)
        preferencesNodeTree.setModel(UIUtilities.createPreferencesTreeModel());
    }

    private File getImportFile() {
        var importFile = DEV_NULL;
        var jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        var option = jfc.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            importFile = jfc.getSelectedFile();
        }
        return importFile;
    }
}
