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

import static org.testng.Assert.assertEquals;

import java.awt.Rectangle;

import org.testng.annotations.Test;

/**
 * Pure-function tests for {@link RowRepaintBounds}.
 *
 * Pin the geometry so a repaint clip never grows or shrinks unexpectedly:
 * the rectangle always covers the full table width, starts at x=0, and the
 * range form copes with descending input ({@code endRow < startRow}).
 */
public class RowRepaintBoundsTest {

    @Test
    public void forRow_zeroIndex_yIsZero() {
        Rectangle r = RowRepaintBounds.forRow(0, 20, 800);
        assertEquals(r, new Rectangle(0, 0, 800, 20));
    }

    @Test
    public void forRow_nthIndex_yIsNTimesRowHeight() {
        Rectangle r = RowRepaintBounds.forRow(7, 20, 800);
        assertEquals(r, new Rectangle(0, 140, 800, 20));
    }

    @Test
    public void forRange_singleRow_isSameAsForRow() {
        Rectangle range = RowRepaintBounds.forRange(3, 3, 18, 600);
        Rectangle row = RowRepaintBounds.forRow(3, 18, 600);
        assertEquals(range, row);
    }

    @Test
    public void forRange_ascending_coversBothEndsInclusive() {
        // rows 2..5 inclusive at 10px each → height = 4 rows × 10px = 40
        Rectangle r = RowRepaintBounds.forRange(2, 5, 10, 500);
        assertEquals(r, new Rectangle(0, 20, 500, 40));
    }

    @Test
    public void forRange_descending_treatedSameAsAscending() {
        // The contract explicitly supports endRow < startRow.
        Rectangle ascending = RowRepaintBounds.forRange(2, 5, 10, 500);
        Rectangle descending = RowRepaintBounds.forRange(5, 2, 10, 500);
        assertEquals(descending, ascending);
    }

    @Test
    public void forRange_largeWidth_passesThrough() {
        Rectangle r = RowRepaintBounds.forRange(0, 99, 16, 4096);
        assertEquals(r.x, 0);
        assertEquals(r.y, 0);
        assertEquals(r.width, 4096);
        assertEquals(r.height, 100 * 16);
    }
}
