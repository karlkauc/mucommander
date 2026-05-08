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
package com.mucommander.ui.main.table;

import java.awt.Rectangle;

/**
 * Pure helpers for computing the {@link Rectangle} that bounds a single
 * row or a contiguous range of rows of a fixed-row-height table.
 *
 * <p>Extracted from {@code FileTable.repaintRow()} and
 * {@code FileTable.repaintRange()} as a Phase 5.3 step. Splitting the math
 * out lets the bounds calculation be unit-tested without a JTable on screen.
 */
final class RowRepaintBounds {

    private RowRepaintBounds() {
        // utility
    }

    /**
     * Returns the rectangle covering a single row at index {@code row}.
     *
     * @param row       row index (0-based)
     * @param rowHeight pixel height of every row
     * @param width     width of the table viewport in pixels
     */
    static Rectangle forRow(int row, int rowHeight, int width) {
        return new Rectangle(0, row * rowHeight, width, rowHeight);
    }

    /**
     * Returns the rectangle covering the inclusive range
     * {@code [min(startRow, endRow), max(startRow, endRow)]}. {@code endRow}
     * may be less than, greater than, or equal to {@code startRow}; a
     * single-row range is the same as {@link #forRow}.
     *
     * @param startRow  first row of the range (inclusive)
     * @param endRow    last row of the range (inclusive)
     * @param rowHeight pixel height of every row
     * @param width     width of the table viewport in pixels
     */
    static Rectangle forRange(int startRow, int endRow, int rowHeight, int width) {
        int y = Math.min(startRow, endRow) * rowHeight;
        int height = (Math.abs(startRow - endRow) + 1) * rowHeight;
        return new Rectangle(0, y, width, height);
    }
}
