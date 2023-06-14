package com.github.argherna.preftool.runtime.ui;

import static java.lang.System.Logger.Level.ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

class MoveNodeUI extends AbstractDialogUI {

    private static final String DEFAULT_TITLE = "Move Preferences Node";

    private static final List<Pair<String, Preferences>> ROOTS =
            List.of(new Pair<String, Preferences>("User", Preferences.userRoot()),
                    new Pair<String, Preferences>("System", Preferences.systemRoot()));

    private ComboBoxModel<Pair<String, Preferences>> preferencesRoot;

    private Document destinationNodeName;

    private Preferences sourceParent;

    MoveNodeUI() {
        this(null);
    }

    MoveNodeUI(Preferences sourceParent) {
        this.sourceParent = sourceParent;
        preferencesRoot = createPreferencesRootComboBoxModel();
        destinationNodeName = createDestinationNodeNameDocument();
        setTitle(DEFAULT_TITLE);
    }

    private ComboBoxModel<Pair<String, Preferences>> createPreferencesRootComboBoxModel() {
        var preferencesRoot = new DefaultComboBoxModel<Pair<String, Preferences>>();
        preferencesRoot.addAll(ROOTS);
        return preferencesRoot;
    }

    private Document createDestinationNodeNameDocument() {
        return new PlainDocument();
    }

    @Override
    void showUI() {
        var components = new ArrayList<JComponent>();
        var destinationNodeNameTextField = createDestinationNodeNameTextField();
        var destinationNodeNameTextFieldLabel =
                createLabelFor(destinationNodeNameTextField, "Name");
        components.add(destinationNodeNameTextFieldLabel);
        components.add(destinationNodeNameTextField);

        if (Objects.isNull(sourceParent)) {
            var preferencesRootComboBox = createPreferencesRootComboBox();
            var preferencesRootComboBoxLabel = createLabelFor(preferencesRootComboBox, "Root");
            components.add(preferencesRootComboBoxLabel);
            components.add(preferencesRootComboBox);
        }

        int result = JOptionPane.showConfirmDialog(null, components.toArray(), getTitle(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        setInputCanceled(result != JOptionPane.OK_OPTION);
    }

    private JTextField createDestinationNodeNameTextField() {
        var destinationNodeNameTextField = new JTextField();
        destinationNodeNameTextField.setDocument(destinationNodeName);
        return destinationNodeNameTextField;
    }

    private JComboBox<Pair<String, Preferences>> createPreferencesRootComboBox() {
        var preferencesRootComboBox = new JComboBox<>(preferencesRoot);
        preferencesRootComboBox.setSelectedIndex(0);
        preferencesRootComboBox.setRenderer(new PairListCellRenderer<Preferences>());
        return preferencesRootComboBox;
    }

    String getDestinationNodeName() {
        try {
            return getTextFrom(destinationNodeName);
        } catch (BadLocationException ex) {
            var message = String.format("An error occurred: %s", ex.getMessage());
            LOGGER.log(ERROR, message, ex);
            JOptionPane.showMessageDialog(null, message, "Move Node Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    Preferences getDestinationPreferencesRoot() {
        if (Objects.nonNull(sourceParent)) {
            return sourceParent;
        } else {
            return ((Pair<String, Preferences>) preferencesRoot.getSelectedItem()).getV();
        }
    }
}
