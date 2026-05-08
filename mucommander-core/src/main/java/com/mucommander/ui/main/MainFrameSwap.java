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

import javax.swing.table.TableColumnModel;

import com.mucommander.ui.main.table.FileTable;
import com.mucommander.ui.main.table.SortInfo;

/**
 * Pure helpers used by {@link MainFrame#swapFolders()} to swap per-component
 * UI state between the left and right halves of a dual-pane main frame.
 *
 * <p>{@code swapFolders()} itself still owns the structural moves
 * (rebinding the two {@code leftFolderPanel}/{@code rightFolderPanel}
 * fields, swapping out the {@code foldersSplitPane}'s left/right
 * components, requesting focus). What lives here is the in-place state
 * exchange between two existing {@link FolderPanel} or {@link FileTable}
 * instances — operations that read and write through public getters and
 * setters and don't touch any MainFrame field directly.
 *
 * <p>Extracted from {@code MainFrame.swapFolders()} as part of the
 * Phase 5.2 decomposition so this state-shuffling logic can be exercised
 * in isolation against mocked panels and tables.
 */
final class MainFrameSwap {

    private MainFrameSwap() {
        // utility
    }

    /**
     * Swaps the folders-tree state ({@code treeWidth} and
     * {@code treeVisible}) between the two panels in place — after the
     * call, {@code a}'s tree settings are what {@code b}'s used to be and
     * vice versa.
     */
    static void swapTreeState(FolderPanel a, FolderPanel b) {
        int tempWidth = a.getTreeWidth();
        a.setTreeWidth(b.getTreeWidth());
        b.setTreeWidth(tempWidth);
        boolean tempVisible = a.isTreeVisible();
        a.setTreeVisible(b.isTreeVisible());
        b.setTreeVisible(tempVisible);
    }

    /**
     * Swaps the file-table state ({@link TableColumnModel} and
     * {@link SortInfo}) between the two tables in place — after the call,
     * each table sorts and shows columns the way the other one did, and
     * each one's column visibility is refreshed.
     *
     * <p>The {@code SortInfo} swap goes via {@code clone()} so {@code a}
     * holds onto a snapshot of its old sort criteria while {@code b}'s
     * gets installed.
     */
    static void swapTableState(FileTable a, FileTable b) {
        TableColumnModel modelA = a.getColumnModel();
        a.setColumnModel(b.getColumnModel());
        b.setColumnModel(modelA);

        SortInfo sortInfoA = (SortInfo) a.getSortInfo().clone();
        a.sortBy(b.getSortInfo());
        a.updateColumnsVisibility();

        b.sortBy(sortInfoA);
        b.updateColumnsVisibility();
    }
}
