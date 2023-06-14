package com.github.argherna.preftool.runtime.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeSelectionModel;

/**
 * Main panel for the PrefTool user interface.
 */
public class PrefToolUI extends JFrame {

    /**
     * Constructs a new instance of the PrefToolUI.
     *
     * <P>
     * This instance will assemble the main frame for the user interface. Clients
     * will need to call
     * {@linkplain #setVisible(boolean) setVisible(true)} to show the window.
     */
    public PrefToolUI() {
        super("PrefTool");

        var preferencesValuesTable = createPreferencesValuesTable();
        var preferencesNodeTree = createPreferencesNodeTree();
        var importUIAction = new ImportUIAction(preferencesNodeTree);

        var exportUIAction = new ExportUIAction();
        var nodeAddressLabel = createNodeAddressLabel();
        var newNodeUIAction = new NewNodeUIAction(preferencesNodeTree);
        var addPreferencesKeyUIAction = new AddPreferencesKeyUIAction(preferencesValuesTable);
        var moveNodeUIAction = new MoveNodeUIAction(preferencesNodeTree);

        nodeAddressLabel.addPropertyChangeListener("text",
                new AddressLabelTextChangeListener(List.of(exportUIAction,
                        newNodeUIAction, addPreferencesKeyUIAction),
                        List.of(moveNodeUIAction)));

        preferencesNodeTree.addTreeSelectionListener(
                new PreferencesTreeSelectionListener(preferencesValuesTable, nodeAddressLabel));

        add(createMainUIPanel(preferencesNodeTree, preferencesValuesTable, nodeAddressLabel));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(createMenuBar(importUIAction, exportUIAction, new ExitUIAction(),
                newNodeUIAction, addPreferencesKeyUIAction, moveNodeUIAction));
        addWindowListener(new PrefToolWindowListener());
        pack();
    }

    /**
     *
     * @param preferencesNodeTree    JTree with Preferences nodes.
     * @param preferencesValuesTable JTable for Preferences keys, types and values.
     * @param nodeAddressLabel       JLabel that displays the Preferences node
     *                               "address".
     * @return JPanel containing the user interface layout.
     */
    private JPanel createMainUIPanel(JTree preferencesNodeTree, JTable preferencesValuesTable,
            JLabel nodeAddressLabel) {
        var prefToolPanel = new JPanel(new BorderLayout());
        prefToolPanel.setName("PrefToolPanel");

        var minimumSize = createMinimumScrollPaneSize();
        var treeView = createPreferencesTreeScrollPane(preferencesNodeTree, minimumSize);
        var tableView = createPreferencesDataTableScrollPane(preferencesValuesTable, minimumSize);
        var splitPane = createPreferencesSplitPane(treeView, tableView);
        prefToolPanel.add(splitPane, BorderLayout.CENTER);

        var nodeAddressPanel = createNodeAddressPanel();
        nodeAddressPanel.add(nodeAddressLabel);
        prefToolPanel.add(nodeAddressPanel, BorderLayout.PAGE_START);

        return prefToolPanel;
    }

    /**
     *
     * @return minimum Dimension for the JScrollPanes.
     */
    private Dimension createMinimumScrollPaneSize() {
        return new Dimension(100, 50);
    }

    /**
     *
     * @return JTree with Preferences node data.
     */
    private JTree createPreferencesNodeTree() {
        var preferencesNodeTree = new JTree(UIUtilities.createPreferencesTreeModel());
        preferencesNodeTree.getSelectionModel()
                .setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        preferencesNodeTree.setCellRenderer(new PreferencesTreeCellRenderer());
        preferencesNodeTree.setName("preferencesNodeTree");
        preferencesNodeTree.setRootVisible(true);
        preferencesNodeTree.setDragEnabled(true);
        preferencesNodeTree.setDropMode(DropMode.ON);
        preferencesNodeTree.setTransferHandler(new PreferencesTreeTransferHandler());
        return preferencesNodeTree;
    }

    /**
     *
     * @param tree        JTree with Preferences nodes.
     * @param minimumSize Dimension for the JScrollPane.
     * @return the JScrollPane for the JTree.
     */
    private JScrollPane createPreferencesTreeScrollPane(JTree tree, Dimension minimumSize) {
        var preferencesTreeScrollPane = new JScrollPane(tree);
        preferencesTreeScrollPane.setMinimumSize(minimumSize);
        preferencesTreeScrollPane.setName("preferencesTreeScrollPane");
        return preferencesTreeScrollPane;
    }

    /**
     *
     * @return JTable containing Preferences keys, types, and values.
     */
    private JTable createPreferencesValuesTable() {
        var preferencesValuesTable = new JTable();
        preferencesValuesTable.setModel(UIUtilities.createPreferencesDataTableModel());
        preferencesValuesTable.setName("preferencesValuesTable");
        return preferencesValuesTable;
    }

    /**
     *
     * @param minimumSize minimum Dimension for this JScrollPane.
     * @return JScrollPane that will contain the Preferences data table.
     */
    private JScrollPane createPreferencesDataTableScrollPane(JTable table, Dimension minimumSize) {
        var dataTableScrollPane = new JScrollPane(table);
        dataTableScrollPane.setMinimumSize(minimumSize);
        dataTableScrollPane.setName("preferencesDataTableScrollPane");
        return dataTableScrollPane;
    }

    /**
     *
     * @param treeView  JScrollPane containing the JTree.
     * @param tableView JScrollPane containing the JTable.
     * @return JSplitPane with the tree on the left and the table on the right.
     */
    private JSplitPane createPreferencesSplitPane(JScrollPane treeView, JScrollPane tableView) {
        var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(tableView);

        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(1000, 600));
        splitPane.setName("preferencesSplitPane");
        return splitPane;
    }

    /**
     *
     * @return initialized JLabel to display node address.
     */
    private JLabel createNodeAddressLabel() {
        var nodeAddressLabel = new JLabel();
        nodeAddressLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nodeAddressLabel.setName("nodeAddressLabel");
        return nodeAddressLabel;
    }

    /**
     *
     * @return JPanel to contain the JLabel that displays the node address.
     */
    private JPanel createNodeAddressPanel() {
        var nodeAddressPanel = new JPanel();
        nodeAddressPanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        nodeAddressPanel.setLayout(new BoxLayout(nodeAddressPanel, BoxLayout.X_AXIS));
        nodeAddressPanel.setName("nodeAddressPanel");
        nodeAddressPanel.setPreferredSize(new Dimension(getWidth(), 16));
        return nodeAddressPanel;
    }

    /**
     *
     * @param importUIAction            Action for the import JMenuItem.
     * @param exportUIAction            Action for the export JMenuItem.
     * @param exitUIAction              Action for the exit JMenuItem.
     * @param newNodeUIAction           Action for the new node JMenuItem.
     * @param addPreferencesKeyUIAction Action for the add key JMenuItem.
     * @param moveNodeUIAction          Action for the move node JMenuItem.
     * @return the menu for the user interface.
     */
    private JMenuBar createMenuBar(Action importUIAction, Action exportUIAction,
            Action exitUIAction, Action newNodeUIAction, Action addPreferencesKeyUIAction,
            Action moveNodeUIAction) {
        var menuBar = new JMenuBar();
        menuBar.setName("menuBar");
        menuBar.add(createFileMenu(importUIAction, exportUIAction, exitUIAction));
        menuBar.add(createEditMenu(newNodeUIAction, addPreferencesKeyUIAction, moveNodeUIAction));
        return menuBar;
    }

    /**
     *
     * @param importAction Action to execute for imports.
     * @param exportAction Action to execute for exports.
     * @param exitAction   Action to execute for exits.
     * @return the File JMenu.
     */
    private JMenu createFileMenu(Action importAction, Action exportAction, Action exitAction) {
        var fileMenu = new JMenu("File");
        fileMenu.setName("fileMenu");
        fileMenu.add(createImportMenuItem(importAction));
        fileMenu.add(createExportSubmenu(exportAction));
        fileMenu.addSeparator();
        fileMenu.add(createExitMenuItem(exitAction));
        return fileMenu;
    }

    /**
     *
     * @param importAction Action to execute for imports.
     * @return the Import JMenuItem.
     */
    private JMenuItem createImportMenuItem(Action importAction) {
        var importMenuItem = new JMenuItem(importAction);
        importMenuItem.setName("importMenuItem");
        importMenuItem.setText("Import...");
        return importMenuItem;
    }

    /**
     *
     * @param exportAction Action to execute for exports.
     * @return the Export (sub) JMenu.
     */
    private JMenu createExportSubmenu(Action exportAction) {
        exportAction.setEnabled(false);
        var exportSubmenu = new JMenu("Export");
        exportSubmenu.setName("exportSubmenu");
        exportSubmenu.add(createExportMenuItem(exportAction, "exportSubtreeMenuItem", "Subtree"));
        exportSubmenu.add(createExportMenuItem(exportAction, "exportNodeMenuItem", "Node"));
        return exportSubmenu;
    }

    /**
     * Utility to create a JMenuItem for executing the export Action.
     *
     * @param exportAction Action to execute for exports.
     * @param name         the name of the JMenuItem.
     * @param text         the text for the JMenuItem.
     * @return the export JMenuItem.
     */
    private JMenuItem createExportMenuItem(Action exportAction, String name, String text) {
        var exportMenuItem = new JMenuItem(exportAction);
        exportMenuItem.setName(name);
        exportMenuItem.setText(text);
        return exportMenuItem;
    }

    /**
     *
     * @param exitAction Action to execute for exits.
     * @return the Exit JMenuItem.
     */
    private JMenuItem createExitMenuItem(Action exitAction) {
        var exitMenuItem = new JMenuItem(exitAction);
        exitMenuItem.setName("exitMenuItem");
        return exitMenuItem;
    }

    /**
     *
     * @param newNodeAction Action for the new node JMenuItem.
     * @param addKeyAction  Action to add key value pair to a Preferences node.
     * @param moveAction    Action to execute for moving a Preferences node.
     * @return the Edit JMenu.
     */
    private JMenu createEditMenu(Action newNodeAction, Action addKeyAction,
            Action moveAction) {
        newNodeAction.setEnabled(false);
        addKeyAction.setEnabled(false);
        moveAction.setEnabled(false);
        var editMenu = new JMenu("Edit");
        editMenu.setName("editMenu");
        editMenu.add(createEditNewSubmenu(newNodeAction, addKeyAction));
        editMenu.addSeparator();
        editMenu.add(createEditMoveMenuItem(moveAction));
        editMenu.add(createEditRenameMenuItem(moveAction));
        return editMenu;
    }

    /**
     *
     * @param newNodeAction Action for the new node JMenuItem.
     * @param addKeyAction  Action for the new key JMenuItem.
     * @return the New (sub) JMenu.
     */
    private JMenu createEditNewSubmenu(Action newNodeAction, Action addKeyAction) {
        var editNewSubmenu = new JMenu("New");
        editNewSubmenu.setName("newEditSubmenu");
        editNewSubmenu.add(createEditNewNodeMenuItem(newNodeAction));
        editNewSubmenu.add(createEditNewKeyMenuItem(addKeyAction));
        return editNewSubmenu;
    }

    /**
     *
     * @param newNodeAction Action for the JMenuItem.
     * @return the new Node JMenuItem.
     */
    private JMenuItem createEditNewNodeMenuItem(Action newNodeAction) {
        var editNewNodeMenuItem = new JMenuItem(newNodeAction);
        editNewNodeMenuItem.setName("editNewNodeMenuItem");
        editNewNodeMenuItem.setText("Node");
        return editNewNodeMenuItem;
    }

    /**
     *
     * @param addKeyAction Action for the JMenuItem.
     * @return the new Key JMenuItem.
     */
    private JMenuItem createEditNewKeyMenuItem(Action addKeyAction) {
        var editNewKeyMenuItem = new JMenuItem(addKeyAction);
        editNewKeyMenuItem.setName("editNewNodeMenuItem");
        editNewKeyMenuItem.setText("Key");
        return editNewKeyMenuItem;
    }

    /**
     *
     * @param moveAction Action to execute for moving a Preferences node.
     * @return Move JMenuItem.
     */
    private JMenuItem createEditMoveMenuItem(Action moveAction) {
        var editMoveMenuItem = new JMenuItem(moveAction);
        editMoveMenuItem.setName("editMoveMenuItem");
        editMoveMenuItem.setText("Move");
        return editMoveMenuItem;
    }

    /**
     *
     * @param moveAction Action to execute for moving a Preferences node.
     * @return Rename JMenuItem.
     */
    private JMenuItem createEditRenameMenuItem(Action moveAction) {
        var editRenameMenuItem = new JMenuItem(moveAction);
        editRenameMenuItem.setName("editRenameMenuItem");
        editRenameMenuItem.setText("Rename");
        return editRenameMenuItem;
    }
}
