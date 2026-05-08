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

import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import javax.swing.table.TableColumnModel;

import com.mucommander.ui.main.table.FileTable;
import com.mucommander.ui.main.table.SortInfo;

import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * Characterization tests for {@link MainFrameSwap}. Pin the per-component
 * state-swap semantics so that a future refactor of {@link MainFrame#swapFolders()}
 * can't silently regress the tree-state or table-state half.
 */
public class MainFrameSwapTest {

    @Test
    public void swapTreeState_swapsTreeWidthAndVisibility() {
        FolderPanel a = mock(FolderPanel.class);
        FolderPanel b = mock(FolderPanel.class);
        when(a.getTreeWidth()).thenReturn(100);
        when(b.getTreeWidth()).thenReturn(250);
        when(a.isTreeVisible()).thenReturn(true);
        when(b.isTreeVisible()).thenReturn(false);

        MainFrameSwap.swapTreeState(a, b);

        verify(a).setTreeWidth(250);
        verify(b).setTreeWidth(100);
        verify(a).setTreeVisible(false);
        verify(b).setTreeVisible(true);
    }

    @Test
    public void swapTreeState_handlesIdenticalState() {
        // Both panels have the same tree state — swap should still call both setters
        // (the helper doesn't optimise away no-op swaps).
        FolderPanel a = mock(FolderPanel.class);
        FolderPanel b = mock(FolderPanel.class);
        when(a.getTreeWidth()).thenReturn(150);
        when(b.getTreeWidth()).thenReturn(150);
        when(a.isTreeVisible()).thenReturn(true);
        when(b.isTreeVisible()).thenReturn(true);

        MainFrameSwap.swapTreeState(a, b);

        verify(a).setTreeWidth(150);
        verify(b).setTreeWidth(150);
        verify(a).setTreeVisible(true);
        verify(b).setTreeVisible(true);
    }

    @Test
    public void swapTableState_swapsColumnModelInBothDirections() {
        FileTable a = mock(FileTable.class);
        FileTable b = mock(FileTable.class);
        TableColumnModel modelA = mock(TableColumnModel.class);
        TableColumnModel modelB = mock(TableColumnModel.class);
        SortInfo sortA = mock(SortInfo.class);
        SortInfo sortB = mock(SortInfo.class);
        SortInfo sortAClone = mock(SortInfo.class);
        when(a.getColumnModel()).thenReturn(modelA);
        when(b.getColumnModel()).thenReturn(modelB);
        lenient().when(a.getSortInfo()).thenReturn(sortA);
        lenient().when(b.getSortInfo()).thenReturn(sortB);
        when(sortA.clone()).thenReturn(sortAClone);

        MainFrameSwap.swapTableState(a, b);

        verify(a).setColumnModel(modelB);
        verify(b).setColumnModel(modelA);
    }

    @Test
    public void swapTableState_clonesAOldSort_andCrossesSortCriteria() {
        FileTable a = mock(FileTable.class);
        FileTable b = mock(FileTable.class);
        SortInfo sortA = mock(SortInfo.class);
        SortInfo sortB = mock(SortInfo.class);
        SortInfo sortAClone = mock(SortInfo.class);
        lenient().when(a.getColumnModel()).thenReturn(mock(TableColumnModel.class));
        lenient().when(b.getColumnModel()).thenReturn(mock(TableColumnModel.class));
        when(a.getSortInfo()).thenReturn(sortA);
        when(b.getSortInfo()).thenReturn(sortB);
        when(sortA.clone()).thenReturn(sortAClone);

        MainFrameSwap.swapTableState(a, b);

        // a sorts by what was b's sort info; b sorts by the cloned snapshot of a's old sort.
        verify(a).sortBy(sortB);
        verify(b).sortBy(sortAClone);
    }

    @Test
    public void swapTableState_refreshesColumnsVisibilityOnBoth() {
        FileTable a = mock(FileTable.class);
        FileTable b = mock(FileTable.class);
        SortInfo sortA = mock(SortInfo.class);
        lenient().when(a.getColumnModel()).thenReturn(mock(TableColumnModel.class));
        lenient().when(b.getColumnModel()).thenReturn(mock(TableColumnModel.class));
        when(a.getSortInfo()).thenReturn(sortA);
        lenient().when(b.getSortInfo()).thenReturn(mock(SortInfo.class));
        when(sortA.clone()).thenReturn(mock(SortInfo.class));

        MainFrameSwap.swapTableState(a, b);

        verify(a).updateColumnsVisibility();
        verify(b).updateColumnsVisibility();
    }

    @Test
    public void swapTableState_appliesSortBeforeColumnsVisibilityRefresh() {
        // Order matters: sortBy() then updateColumnsVisibility(), per side.
        FileTable a = mock(FileTable.class);
        FileTable b = mock(FileTable.class);
        SortInfo sortA = mock(SortInfo.class);
        SortInfo sortB = mock(SortInfo.class);
        SortInfo sortAClone = mock(SortInfo.class);
        lenient().when(a.getColumnModel()).thenReturn(mock(TableColumnModel.class));
        lenient().when(b.getColumnModel()).thenReturn(mock(TableColumnModel.class));
        when(a.getSortInfo()).thenReturn(sortA);
        when(b.getSortInfo()).thenReturn(sortB);
        when(sortA.clone()).thenReturn(sortAClone);

        MainFrameSwap.swapTableState(a, b);

        InOrder inA = inOrder(a);
        inA.verify(a).sortBy(same(sortB));
        inA.verify(a).updateColumnsVisibility();

        InOrder inB = inOrder(b);
        inB.verify(b).sortBy(same(sortAClone));
        inB.verify(b).updateColumnsVisibility();
    }

    @Test
    public void swapTreeState_isSelfInverse() {
        // Two swaps in a row should land back at the original state.
        // Easiest way to verify: capture the call sequence and check final values
        // by routing setters back into the mock's getter.
        int[] aWidth = { 100 };
        int[] bWidth = { 250 };
        boolean[] aVisible = { true };
        boolean[] bVisible = { false };

        FolderPanel a = mock(FolderPanel.class);
        FolderPanel b = mock(FolderPanel.class);
        when(a.getTreeWidth()).thenAnswer(inv -> aWidth[0]);
        when(b.getTreeWidth()).thenAnswer(inv -> bWidth[0]);
        when(a.isTreeVisible()).thenAnswer(inv -> aVisible[0]);
        when(b.isTreeVisible()).thenAnswer(inv -> bVisible[0]);
        org.mockito.Mockito.doAnswer(inv -> { aWidth[0] = inv.getArgument(0); return null; })
                .when(a).setTreeWidth(org.mockito.ArgumentMatchers.anyInt());
        org.mockito.Mockito.doAnswer(inv -> { bWidth[0] = inv.getArgument(0); return null; })
                .when(b).setTreeWidth(org.mockito.ArgumentMatchers.anyInt());
        org.mockito.Mockito.doAnswer(inv -> { aVisible[0] = inv.getArgument(0); return null; })
                .when(a).setTreeVisible(org.mockito.ArgumentMatchers.anyBoolean());
        org.mockito.Mockito.doAnswer(inv -> { bVisible[0] = inv.getArgument(0); return null; })
                .when(b).setTreeVisible(org.mockito.ArgumentMatchers.anyBoolean());

        MainFrameSwap.swapTreeState(a, b);
        MainFrameSwap.swapTreeState(a, b);

        assertEquals(aWidth[0], 100);
        assertEquals(bWidth[0], 250);
        assertEquals(aVisible[0], true);
        assertEquals(bVisible[0], false);
    }
}
