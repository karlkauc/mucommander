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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import javax.swing.table.JTableHeader;

import org.testng.annotations.Test;

/**
 * Pure-function tests for {@link TableHeaderProperties#apply}.
 *
 * Pin both the property values and the inactive-state clearing behaviour.
 * Critically also pin the misspelt {@code "decending"} string — that's the
 * magic value Aqua looks for; correcting the typo would break the sort
 * arrow rendering on macOS.
 */
public class TableHeaderPropertiesTest {

    @Test
    public void nullHeader_isNoop() {
        // Must not throw — matches the historical null-guard at the top of
        // FileTable.setTableHeaderRenderingProperties().
        TableHeaderProperties.apply(null, true, 0, true);
    }

    @Test
    public void inactiveTable_clearsBothProperties_byPuttingNull() {
        JTableHeader header = mock(JTableHeader.class);

        TableHeaderProperties.apply(header, false, 5, true);

        verify(header).putClientProperty(TableHeaderProperties.SELECTED_COLUMN_KEY, null);
        verify(header).putClientProperty(TableHeaderProperties.SORT_DIRECTION_KEY, null);
    }

    @Test
    public void activeTable_ascending_setsAscendingDirection_andColumnIndex() {
        JTableHeader header = mock(JTableHeader.class);

        TableHeaderProperties.apply(header, true, 3, true);

        verify(header).putClientProperty(TableHeaderProperties.SELECTED_COLUMN_KEY, 3);
        verify(header).putClientProperty(TableHeaderProperties.SORT_DIRECTION_KEY, "ascending");
    }

    @Test
    public void activeTable_descending_setsDecendingTypo_andColumnIndex() {
        JTableHeader header = mock(JTableHeader.class);

        TableHeaderProperties.apply(header, true, 7, false);

        verify(header).putClientProperty(TableHeaderProperties.SELECTED_COLUMN_KEY, 7);
        // Pin the misspelling: Aqua's L&F really does look for "decending".
        verify(header).putClientProperty(TableHeaderProperties.SORT_DIRECTION_KEY, "decending");
    }

    @Test
    public void descendingConstantIsIntentionallyMisspelled() {
        // Tripwire: if anyone "fixes" the typo to "descending", the sort arrow
        // rendering on macOS Aqua silently breaks. Catch it at compile/test time.
        assertEquals(TableHeaderProperties.DESCENDING, "decending");
        assertEquals(TableHeaderProperties.ASCENDING, "ascending");
    }

    @Test
    public void inactiveTable_doesNotEvaluateAscendingArgument() {
        // Sanity: even if isActive is false, the function still runs without
        // depending on the ascending flag in any way that affects output.
        JTableHeader header = mock(JTableHeader.class);

        TableHeaderProperties.apply(header, false, 0, true);
        TableHeaderProperties.apply(header, false, 0, false);

        // Both calls produce the same null+null pair — no other property writes.
        verify(header, never()).putClientProperty(TableHeaderProperties.SORT_DIRECTION_KEY, "ascending");
        verify(header, never()).putClientProperty(TableHeaderProperties.SORT_DIRECTION_KEY, "decending");
    }
}
