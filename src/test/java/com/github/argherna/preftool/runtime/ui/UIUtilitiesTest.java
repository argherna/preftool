package com.github.argherna.preftool.runtime.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UIUtilitiesTest {

    @Test
    @DisplayName("Render Preferences Node Address from TreePath")
    void testRenderPreferencesNodeAddressFromTreePath() {
        var tp = new TreePath(new DefaultMutableTreeNode[] {
                new DefaultMutableTreeNode("Preferences"), new DefaultMutableTreeNode("User"),
                new DefaultMutableTreeNode("parent"), new DefaultMutableTreeNode("source")});
        var actual = UIUtilities.renderPreferencesNodeAddress(tp);
        assertEquals("User:/parent/source", actual);
    }

    @Test
    @DisplayName("Render Preferences Node Address from TreeNode array")
    void testRenderPreferencesNodeAddressFromArray() {
        var treeNodes = new DefaultMutableTreeNode[] {new DefaultMutableTreeNode("Preferences"),
                new DefaultMutableTreeNode("User"), new DefaultMutableTreeNode("parent"),
                new DefaultMutableTreeNode("source")};
        var actual = UIUtilities.renderPreferencesNodeAddress(treeNodes);
        assertEquals("User:/parent/source", actual);
    }
}
