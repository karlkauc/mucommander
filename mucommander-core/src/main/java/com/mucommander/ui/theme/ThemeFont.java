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
package com.mucommander.ui.theme;

/**
 * Type-safe identifiers for the fonts that {@link ThemeData} manages.
 *
 * <p>The {@link #id()} value matches the historical {@code public static final int}
 * constants on {@code ThemeData} (e.g. {@code ThemeFont.FILE_TABLE.id() == ThemeData.FILE_TABLE_FONT}).
 * The integer constants stay around for now so external callers don't break, but new
 * code should use these enum values.
 */
public enum ThemeFont {
    /** Font used in the folder panels. */
    FILE_TABLE(0),
    /** Font used to display terminal output. */
    TERMINAL(1),
    /** Font used in the file editor and viewer. */
    EDITOR(2),
    /** Font used in the location bar. */
    LOCATION_BAR(3),
    /** Font used in the status bar. (Slot 4 was historically skipped.) */
    STATUS_BAR(5),
    /** Font used in the quick list header. */
    QUICK_LIST_HEADER(6),
    /** Font used in the quick list item. */
    QUICK_LIST_ITEM(7);

    private final int id;

    ThemeFont(int id) {
        this.id = id;
    }

    /** Returns the integer id matching the legacy {@code ThemeData} constants. */
    public int id() {
        return id;
    }

    /** Resolves an integer id back to its {@code ThemeFont}, or null if unknown. */
    public static ThemeFont fromId(int id) {
        for (ThemeFont f : values()) {
            if (f.id == id) {
                return f;
            }
        }
        return null;
    }
}
