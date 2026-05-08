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
 * Type-safe identifiers for the colors that {@link ThemeData} manages.
 *
 * <p>The {@link #id()} value matches the historical {@code public static final int}
 * constants on {@code ThemeData} (e.g. {@code ThemeColor.FILE_TABLE_BORDER.id()
 * == ThemeData.FILE_TABLE_BORDER_COLOR}). The integer constants stay around
 * for now so external callers don't break, but new code should use these enum
 * values.
 */
public enum ThemeColor {
    FILE_TABLE_BORDER(0),
    FILE_TABLE_INACTIVE_BORDER(56),
    FILE_TABLE_BACKGROUND(1),
    FILE_TABLE_ALTERNATE_BACKGROUND(2),
    FILE_TABLE_INACTIVE_BACKGROUND(3),
    FILE_TABLE_INACTIVE_ALTERNATE_BACKGROUND(4),
    FILE_TABLE_UNMATCHED_BACKGROUND(5),
    FILE_TABLE_UNMATCHED_FOREGROUND(6),
    FILE_TABLE_SELECTED_BACKGROUND(7),
    FILE_TABLE_SELECTED_SECONDARY_BACKGROUND(59),
    FILE_TABLE_INACTIVE_SELECTED_SECONDARY_BACKGROUND(60),
    FILE_TABLE_INACTIVE_SELECTED_BACKGROUND(8),
    HIDDEN_FILE_FOREGROUND(9),
    HIDDEN_FILE_INACTIVE_FOREGROUND(10),
    HIDDEN_FILE_SELECTED_FOREGROUND(11),
    HIDDEN_FILE_INACTIVE_SELECTED_FOREGROUND(12),
    FOLDER_FOREGROUND(13),
    FOLDER_INACTIVE_FOREGROUND(14),
    FOLDER_SELECTED_FOREGROUND(15),
    FOLDER_INACTIVE_SELECTED_FOREGROUND(16),
    ARCHIVE_FOREGROUND(17),
    ARCHIVE_INACTIVE_FOREGROUND(18),
    ARCHIVE_SELECTED_FOREGROUND(19),
    ARCHIVE_INACTIVE_SELECTED_FOREGROUND(20),
    SYMLINK_FOREGROUND(21),
    SYMLINK_INACTIVE_FOREGROUND(22),
    SYMLINK_SELECTED_FOREGROUND(23),
    SYMLINK_INACTIVE_SELECTED_FOREGROUND(24),
    MARKED_FOREGROUND(25),
    MARKED_INACTIVE_FOREGROUND(26),
    MARKED_SELECTED_FOREGROUND(27),
    MARKED_INACTIVE_SELECTED_FOREGROUND(28),
    FILE_FOREGROUND(29),
    FILE_INACTIVE_FOREGROUND(30),
    FILE_SELECTED_FOREGROUND(31),
    FILE_INACTIVE_SELECTED_FOREGROUND(32),
    TERMINAL_FOREGROUND(33),
    TERMINAL_BACKGROUND(34),
    TERMINAL_SELECTED_FOREGROUND(35),
    TERMINAL_SELECTED_BACKGROUND(36),
    EDITOR_FOREGROUND(41),
    EDITOR_BACKGROUND(42),
    EDITOR_SELECTED_FOREGROUND(43),
    EDITOR_SELECTED_BACKGROUND(44),
    LOCATION_BAR_FOREGROUND(45),
    LOCATION_BAR_BACKGROUND(46),
    LOCATION_BAR_SELECTED_FOREGROUND(47),
    LOCATION_BAR_SELECTED_BACKGROUND(48),
    LOCATION_BAR_PROGRESS(49),
    STATUS_BAR_FOREGROUND(50),
    STATUS_BAR_BACKGROUND(51),
    STATUS_BAR_BORDER(52),
    STATUS_BAR_OK(53),
    STATUS_BAR_WARNING(54),
    STATUS_BAR_CRITICAL(55),
    FILE_TABLE_SELECTED_OUTLINE(57),
    FILE_TABLE_INACTIVE_SELECTED_OUTLINE(58),
    QUICK_LIST_HEADER_BACKGROUND(61),
    QUICK_LIST_HEADER_SECONDARY_BACKGROUND(62),
    QUICK_LIST_HEADER_FOREGROUND(63),
    QUICK_LIST_ITEM_BACKGROUND(64),
    QUICK_LIST_ITEM_FOREGROUND(65),
    QUICK_LIST_SELECTED_ITEM_BACKGROUND(66),
    QUICK_LIST_SELECTED_ITEM_FOREGROUND(67),
    READ_ONLY_FOREGROUND(68),
    READ_ONLY_INACTIVE_FOREGROUND(69),
    READ_ONLY_SELECTED_FOREGROUND(70),
    READ_ONLY_INACTIVE_SELECTED_FOREGROUND(71),
    /** Slots 37-40 were reserved historically; kept for completeness. */
    RESERVED_37(37),
    RESERVED_38(38),
    RESERVED_39(39),
    RESERVED_40(40);

    private final int id;

    ThemeColor(int id) {
        this.id = id;
    }

    /** Returns the integer id matching the legacy {@code ThemeData} constants. */
    public int id() {
        return id;
    }

    /** Resolves an integer id back to its {@code ThemeColor}, or null if unknown. */
    public static ThemeColor fromId(int id) {
        for (ThemeColor c : values()) {
            if (c.id == id) {
                return c;
            }
        }
        return null;
    }
}
