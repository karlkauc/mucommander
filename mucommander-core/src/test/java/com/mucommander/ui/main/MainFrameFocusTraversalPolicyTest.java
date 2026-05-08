/*
 * This file is part of muCommander, http://www.mucommander.com
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mucommander.ui.main;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertSame;

import com.mucommander.ui.main.table.FileTable;
import com.mucommander.ui.main.tree.FoldersTreePanel;

import javax.swing.JComponent;
import javax.swing.JTree;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Characterization tests for {@link MainFrameFocusTraversalPolicy}.
 *
 * Pins the Tab-key cycle through the dual-pane UI: location-text-field /
 * tree-panel / file-table on the left side hop to the left file table,
 * the left file table hops to the right file table, and so on. These
 * tests are the safety net for further Phase 5.2 decomposition steps;
 * if anyone moves a getter or rewires the policy, this fails first.
 */
public class MainFrameFocusTraversalPolicyTest {

    private MainFrame mainFrame;
    private FolderPanel leftPanel;
    private FolderPanel rightPanel;
    private FileTable leftTable;
    private FileTable rightTable;
    private JTree leftTree;
    private JTree rightTree;
    private JComponent leftLocation;
    private JComponent rightLocation;

    private MainFrameFocusTraversalPolicy policy;

    @BeforeMethod
    public void setUp() {
        mainFrame = mock(MainFrame.class);
        leftPanel = mock(FolderPanel.class);
        rightPanel = mock(FolderPanel.class);
        leftTable = mock(FileTable.class);
        rightTable = mock(FileTable.class);
        leftTree = mock(JTree.class);
        rightTree = mock(JTree.class);
        leftLocation = mock(JComponent.class);
        rightLocation = mock(JComponent.class);

        FoldersTreePanel leftTreePanel = mock(FoldersTreePanel.class);
        FoldersTreePanel rightTreePanel = mock(FoldersTreePanel.class);
        lenient().when(leftTreePanel.getTree()).thenReturn(leftTree);
        lenient().when(rightTreePanel.getTree()).thenReturn(rightTree);

        when(mainFrame.getLeftPanel()).thenReturn(leftPanel);
        when(mainFrame.getRightPanel()).thenReturn(rightPanel);
        lenient().when(leftPanel.getFileTable()).thenReturn(leftTable);
        lenient().when(rightPanel.getFileTable()).thenReturn(rightTable);
        lenient().when(leftPanel.getFoldersTreePanel()).thenReturn(leftTreePanel);
        lenient().when(rightPanel.getFoldersTreePanel()).thenReturn(rightTreePanel);
        lenient().when(leftPanel.getLocationTextField()).thenReturn(
                mock(com.mucommander.ui.main.LocationTextField.class));
        lenient().when(rightPanel.getLocationTextField()).thenReturn(
                mock(com.mucommander.ui.main.LocationTextField.class));

        // Use the shared mocked LocationTextField references the policy will
        // see when it queries the panels.
        leftLocation = leftPanel.getLocationTextField();
        rightLocation = rightPanel.getLocationTextField();

        policy = new MainFrameFocusTraversalPolicy(mainFrame);
    }

    @Test
    public void getComponentAfter_fromLeftTree_returnsLeftTable() {
        assertSame(policy.getComponentAfter(null, leftTree), leftTable);
    }

    @Test
    public void getComponentAfter_fromRightTree_returnsRightTable() {
        assertSame(policy.getComponentAfter(null, rightTree), rightTable);
    }

    @Test
    public void getComponentAfter_fromLeftLocation_returnsLeftTable() {
        assertSame(policy.getComponentAfter(null, leftLocation), leftTable);
    }

    @Test
    public void getComponentAfter_fromLeftTable_returnsRightTable() {
        assertSame(policy.getComponentAfter(null, leftTable), rightTable);
    }

    @Test
    public void getComponentAfter_fromRightLocation_returnsRightTable() {
        assertSame(policy.getComponentAfter(null, rightLocation), rightTable);
    }

    @Test
    public void getComponentAfter_fromRightTable_wrapsToLeftTable() {
        // Final fallthrough: any component that isn't matched above (in
        // practice the right table itself) routes back to the left table.
        assertSame(policy.getComponentAfter(null, rightTable), leftTable);
    }

    @Test
    public void getComponentBefore_isSymmetricWithGetComponentAfter() {
        assertSame(policy.getComponentBefore(null, leftTable),
                policy.getComponentAfter(null, leftTable));
        assertSame(policy.getComponentBefore(null, rightTable),
                policy.getComponentAfter(null, rightTable));
    }

    @Test
    public void getFirstComponent_returnsLeftTable() {
        assertSame(policy.getFirstComponent(null), leftTable);
    }

    @Test
    public void getLastComponent_returnsRightTable() {
        assertSame(policy.getLastComponent(null), rightTable);
    }

    @Test
    public void getDefaultComponent_returnsActiveTable() {
        when(mainFrame.getActiveTable()).thenReturn(rightTable);
        assertSame(policy.getDefaultComponent(null), rightTable);

        when(mainFrame.getActiveTable()).thenReturn(leftTable);
        assertSame(policy.getDefaultComponent(null), leftTable);
    }
}
