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

import javax.swing.table.JTableHeader;

/**
 * Sets the {@code JTableHeader.selectedColumn} and {@code JTableHeader.sortDirection}
 * client properties that macOS Aqua / native L&amp;F react to when drawing
 * sort indicators on column headers.
 *
 * <p>Extracted from {@code FileTable.setTableHeaderRenderingProperties()} as
 * a Phase 5.3 step. Concentrating the rule in one place makes the
 * historical {@code "decending"} typo (yes, really) explicit and testable.
 *
 * <p>The "decending" misspelling is preserved on purpose: it's the magic
 * string macOS Aqua looks for, and changing it would silently break the
 * sort arrow rendering on systems that depend on it.
 */
final class TableHeaderProperties {

    /** Client-property key for the column with the active sort indicator. */
    static final String SELECTED_COLUMN_KEY = "JTableHeader.selectedColumn";

    /** Client-property key for the direction of the active sort indicator. */
    static final String SORT_DIRECTION_KEY = "JTableHeader.sortDirection";

    /** Property value flagging an ascending sort. */
    static final String ASCENDING = "ascending";

    /**
     * Property value flagging a descending sort. <strong>Note:</strong> the
     * Aqua look-and-feel expects the misspelling "decending" — fixing the
     * typo would break the sort arrow rendering. Do not "correct" it.
     */
    static final String DESCENDING = "decending";

    private TableHeaderProperties() {
        // utility
    }

    /**
     * Applies the sort-indicator client properties to {@code header}.
     *
     * @param header               the {@link JTableHeader} to update; if null,
     *                             the call is a no-op (matches the historical
     *                             null-guard at the top of
     *                             {@code setTableHeaderRenderingProperties})
     * @param isActive             true if the host table is the foreground
     *                             active one; when false, both properties are
     *                             cleared so no arrow is drawn
     * @param sortColumnViewIndex  the view-coordinate column index of the
     *                             current sort criterion
     * @param ascending            true for an ascending arrow, false for descending
     */
    static void apply(JTableHeader header, boolean isActive, int sortColumnViewIndex, boolean ascending) {
        if (header == null) {
            return;
        }
        header.putClientProperty(SELECTED_COLUMN_KEY, isActive ? sortColumnViewIndex : null);
        header.putClientProperty(SORT_DIRECTION_KEY, isActive ? (ascending ? ASCENDING : DESCENDING) : null);
    }
}
