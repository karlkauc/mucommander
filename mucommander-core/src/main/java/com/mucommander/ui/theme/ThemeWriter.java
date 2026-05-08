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

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;

import com.mucommander.commons.util.xml.XmlAttributes;
import com.mucommander.commons.util.xml.XmlWriter;

/**
 * Class used to save themes in XML format.
 * @author Nicolas Rinaudo
 */
class ThemeWriter implements ThemeXmlConstants {
    // - Initialisation ------------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Prevents instanciation of the class.
     */
    private ThemeWriter() {}



    // - XML output ----------------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Saves the specified theme to the specified output stream.
     * @param  theme       theme to save.
     * @param  stream      where to write the theme to.
     * @throws IOException thrown if any IO related error occurs.
     */
    public static void write(ThemeData theme, OutputStream stream) throws IOException {
        XmlWriter out;

        out = new XmlWriter(stream);
        out.startElement(ELEMENT_ROOT);
        out.println();

        // - File table description ------------------------------------------------------
        // -------------------------------------------------------------------------------
        out.startElement(ELEMENT_TABLE);
        out.println();

        // Global values.
        if(theme.isColorSet(ThemeColor.FILE_TABLE_BORDER))
            out.writeStandAloneElement(ELEMENT_BORDER, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_BORDER)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_INACTIVE_BORDER))
            out.writeStandAloneElement(ELEMENT_INACTIVE_BORDER, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_INACTIVE_BORDER)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_SELECTED_OUTLINE))
            out.writeStandAloneElement(ELEMENT_OUTLINE, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_SELECTED_OUTLINE)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_INACTIVE_SELECTED_OUTLINE))
            out.writeStandAloneElement(ELEMENT_INACTIVE_OUTLINE, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_INACTIVE_SELECTED_OUTLINE)));
        if(theme.isFontSet(ThemeFont.FILE_TABLE))
            out.writeStandAloneElement(ELEMENT_FONT, getFontAttributes(theme.getFont(ThemeFont.FILE_TABLE)));

        // Normal background colors.
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.FILE_TABLE_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_INACTIVE_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_INACTIVE_BACKGROUND)));
        out.endElement(ELEMENT_NORMAL);

        // Selected background colors.
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.FILE_TABLE_SELECTED_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_SELECTED_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_INACTIVE_SELECTED_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_INACTIVE_SELECTED_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_INACTIVE_SELECTED_SECONDARY_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_SECONDARY_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_INACTIVE_SELECTED_SECONDARY_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_SELECTED_SECONDARY_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_SECONDARY_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_SELECTED_SECONDARY_BACKGROUND)));

        out.endElement(ELEMENT_SELECTED);

        // Alternate background colors.
        out.startElement(ELEMENT_ALTERNATE);
        out.println();
        if(theme.isColorSet(ThemeColor.FILE_TABLE_ALTERNATE_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_ALTERNATE_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_INACTIVE_ALTERNATE_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_INACTIVE_ALTERNATE_BACKGROUND)));
        out.endElement(ELEMENT_ALTERNATE);

        // Unmatched colors.
        out.startElement(ELEMENT_UNMATCHED);
        out.println();
        if(theme.isColorSet(ThemeColor.FILE_TABLE_UNMATCHED_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_UNMATCHED_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.FILE_TABLE_UNMATCHED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_TABLE_UNMATCHED_FOREGROUND)));
        out.endElement(ELEMENT_UNMATCHED);

        // Hidden files.
        out.startElement(ELEMENT_HIDDEN);
        out.println();
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.HIDDEN_FILE_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.HIDDEN_FILE_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.HIDDEN_FILE_INACTIVE_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.HIDDEN_FILE_INACTIVE_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.HIDDEN_FILE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.HIDDEN_FILE_SELECTED_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.HIDDEN_FILE_INACTIVE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.HIDDEN_FILE_INACTIVE_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_HIDDEN);

        // Folders.
        out.startElement(ELEMENT_FOLDER);
        out.println();
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.FOLDER_INACTIVE_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FOLDER_INACTIVE_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.FOLDER_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FOLDER_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.FOLDER_INACTIVE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FOLDER_INACTIVE_SELECTED_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.FOLDER_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FOLDER_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_FOLDER);

        // Archives.
        out.startElement(ELEMENT_ARCHIVE);
        out.println();
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.ARCHIVE_INACTIVE_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.ARCHIVE_INACTIVE_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.ARCHIVE_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.ARCHIVE_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.ARCHIVE_INACTIVE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.ARCHIVE_INACTIVE_SELECTED_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.ARCHIVE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.ARCHIVE_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_ARCHIVE);

        // Symlink.
        out.startElement(ELEMENT_SYMLINK);
        out.println();
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.SYMLINK_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.SYMLINK_INACTIVE_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.SYMLINK_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.SYMLINK_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.SYMLINK_INACTIVE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.SYMLINK_INACTIVE_SELECTED_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.SYMLINK_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.SYMLINK_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_SYMLINK);

        // Read-only files
        out.startElement(ELEMENT_READ_ONLY);
        out.println();
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.READ_ONLY_INACTIVE_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.READ_ONLY_INACTIVE_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.READ_ONLY_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.READ_ONLY_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.READ_ONLY_INACTIVE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.READ_ONLY_INACTIVE_SELECTED_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.READ_ONLY_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.READ_ONLY_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_READ_ONLY);

        // Marked files.
        out.startElement(ELEMENT_MARKED);
        out.println();
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.MARKED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.MARKED_INACTIVE_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.MARKED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.MARKED_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.MARKED_INACTIVE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.MARKED_INACTIVE_SELECTED_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.MARKED_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.MARKED_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_MARKED);

        // Plain files.
        out.startElement(ELEMENT_FILE);
        out.println();
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.FILE_INACTIVE_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_INACTIVE_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.FILE_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.FILE_INACTIVE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_INACTIVE_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_INACTIVE_SELECTED_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.FILE_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.FILE_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_FILE);
        out.endElement(ELEMENT_TABLE);



        // - Terminal/Shell description ----------------------------------------------------------
        // -------------------------------------------------------------------------------
        out.startElement(ELEMENT_TERMINAL);
        out.println();
        if(theme.isFontSet(ThemeFont.TERMINAL))
            out.writeStandAloneElement(ELEMENT_FONT, getFontAttributes(theme.getFont(ThemeFont.TERMINAL)));

        // Normal colors.
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.TERMINAL_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.TERMINAL_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.TERMINAL_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.TERMINAL_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);

        // Selected colors.
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.TERMINAL_SELECTED_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.TERMINAL_SELECTED_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.TERMINAL_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.TERMINAL_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_TERMINAL);

        // - Editor description ----------------------------------------------------------
        // -------------------------------------------------------------------------------
        out.startElement(ELEMENT_EDITOR);
        out.println();
        if(theme.isFontSet(ThemeFont.EDITOR))
            out.writeStandAloneElement(ELEMENT_FONT, getFontAttributes(theme.getFont(ThemeFont.EDITOR)));

        // Normal colors.
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.EDITOR_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.EDITOR_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.EDITOR_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.EDITOR_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);

        // Selected colors.
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.EDITOR_SELECTED_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.EDITOR_SELECTED_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.EDITOR_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.EDITOR_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_EDITOR);


        // - Location bar description ----------------------------------------------------
        // -------------------------------------------------------------------------------
        out.startElement(ELEMENT_LOCATION_BAR);
        out.println();
        if(theme.isFontSet(ThemeFont.LOCATION_BAR))
            out.writeStandAloneElement(ELEMENT_FONT, getFontAttributes(theme.getFont(ThemeFont.LOCATION_BAR)));
        if(theme.isColorSet(ThemeColor.LOCATION_BAR_PROGRESS))
            out.writeStandAloneElement(ELEMENT_PROGRESS, getColorAttributes(theme.getColor(ThemeColor.LOCATION_BAR_PROGRESS)));

        // Normal colors.
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.LOCATION_BAR_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.LOCATION_BAR_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.LOCATION_BAR_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.LOCATION_BAR_FOREGROUND)));
        out.endElement(ELEMENT_NORMAL);

        // Selected colors.
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.LOCATION_BAR_SELECTED_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.LOCATION_BAR_SELECTED_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.LOCATION_BAR_SELECTED_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.LOCATION_BAR_SELECTED_FOREGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_LOCATION_BAR);



        // - Volume label description ----------------------------------------------------
        // -------------------------------------------------------------------------------
        out.startElement(ELEMENT_STATUS_BAR);
        out.println();
        // Font.
        if(theme.isFontSet(ThemeFont.STATUS_BAR))
            out.writeStandAloneElement(ELEMENT_FONT, getFontAttributes(theme.getFont(ThemeFont.STATUS_BAR)));

        // Colors.
        if(theme.isColorSet(ThemeColor.STATUS_BAR_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.STATUS_BAR_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.STATUS_BAR_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.STATUS_BAR_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.STATUS_BAR_BORDER))
            out.writeStandAloneElement(ELEMENT_BORDER, getColorAttributes(theme.getColor(ThemeColor.STATUS_BAR_BORDER)));
        if(theme.isColorSet(ThemeColor.STATUS_BAR_OK))
            out.writeStandAloneElement(ELEMENT_OK, getColorAttributes(theme.getColor(ThemeColor.STATUS_BAR_OK)));
        if(theme.isColorSet(ThemeColor.STATUS_BAR_WARNING))
            out.writeStandAloneElement(ELEMENT_WARNING, getColorAttributes(theme.getColor(ThemeColor.STATUS_BAR_WARNING)));
        if(theme.isColorSet(ThemeColor.STATUS_BAR_CRITICAL))
            out.writeStandAloneElement(ELEMENT_CRITICAL, getColorAttributes(theme.getColor(ThemeColor.STATUS_BAR_CRITICAL)));
        out.endElement(ELEMENT_STATUS_BAR);



        // - Quick list label description ----------------------------------------------------
        // -------------------------------------------------------------------------------
        out.startElement(ELEMENT_QUICK_LIST);
        out.println();

        // Quick list header
        out.startElement(ELEMENT_HEADER);
        out.println();
        // Font.
        if(theme.isFontSet(ThemeFont.QUICK_LIST_HEADER))
            out.writeStandAloneElement(ELEMENT_FONT, getFontAttributes(theme.getFont(ThemeFont.QUICK_LIST_HEADER)));
        // Colors.
        if(theme.isColorSet(ThemeColor.QUICK_LIST_HEADER_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.QUICK_LIST_HEADER_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.QUICK_LIST_HEADER_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.QUICK_LIST_HEADER_BACKGROUND)));
        if(theme.isColorSet(ThemeColor.QUICK_LIST_HEADER_SECONDARY_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_SECONDARY_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.QUICK_LIST_HEADER_SECONDARY_BACKGROUND)));
        out.endElement(ELEMENT_HEADER);

        // Quick list item
        out.startElement(ELEMENT_ITEM);
        out.println();
        // Font.
        if(theme.isFontSet(ThemeFont.QUICK_LIST_ITEM))
            out.writeStandAloneElement(ELEMENT_FONT, getFontAttributes(theme.getFont(ThemeFont.QUICK_LIST_ITEM)));
        // Colors.
        // Normal colors.
        out.startElement(ELEMENT_NORMAL);
        out.println();
        if(theme.isColorSet(ThemeColor.QUICK_LIST_ITEM_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.QUICK_LIST_ITEM_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.QUICK_LIST_ITEM_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.QUICK_LIST_ITEM_BACKGROUND)));
        out.endElement(ELEMENT_NORMAL);
        // Selected colors.
        out.startElement(ELEMENT_SELECTED);
        out.println();
        if(theme.isColorSet(ThemeColor.QUICK_LIST_SELECTED_ITEM_FOREGROUND))
            out.writeStandAloneElement(ELEMENT_FOREGROUND, getColorAttributes(theme.getColor(ThemeColor.QUICK_LIST_SELECTED_ITEM_FOREGROUND)));
        if(theme.isColorSet(ThemeColor.QUICK_LIST_SELECTED_ITEM_BACKGROUND))
            out.writeStandAloneElement(ELEMENT_BACKGROUND, getColorAttributes(theme.getColor(ThemeColor.QUICK_LIST_SELECTED_ITEM_BACKGROUND)));
        out.endElement(ELEMENT_SELECTED);
        out.endElement(ELEMENT_ITEM);
        out.endElement(ELEMENT_QUICK_LIST);

        out.endElement(ELEMENT_ROOT);
    }



    // - Helper methods ------------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Returns the XML attributes describing the specified font.
     * @param  font font to described as XML attributes.
     * @return      the XML attributes describing the specified font.
     */
    private static XmlAttributes getFontAttributes(Font font) {
        XmlAttributes attributes; // Stores the font's description.

        attributes = new XmlAttributes();

        // Font family and size.
        attributes.add(ATTRIBUTE_FAMILY, font.getFamily());
        attributes.add(ATTRIBUTE_SIZE, Integer.toString(font.getSize()));

        // Font style.
        if(font.isBold())
            attributes.add(ATTRIBUTE_BOLD, VALUE_TRUE);
        if(font.isItalic())
            attributes.add(ATTRIBUTE_ITALIC, VALUE_TRUE);

        return attributes;
    }

    /**
     * Returns the XML attributes describing the specified color.
     * @param  color color to described as XML attributes.
     * @return       the XML attributes describing the specified color.
     */
    private static XmlAttributes getColorAttributes(Color color) {
        XmlAttributes attributes; // Stores the color's description.
        StringBuilder buffer;     // Used to build the color's string representation.

        buffer = new StringBuilder();

        // Red component.
        if(color.getRed() < 16)
            buffer.append('0');
        buffer.append(Integer.toString(color.getRed(), 16));

        // Green component.
        if(color.getGreen() < 16)
            buffer.append('0');
        buffer.append(Integer.toString(color.getGreen(), 16));

        // Blue component.
        if(color.getBlue() < 16)
            buffer.append('0');
        buffer.append(Integer.toString(color.getBlue(), 16));

        // Builds the XML attributes.
        attributes = new XmlAttributes();
        attributes.add(ATTRIBUTE_COLOR, buffer.toString());

        if(color.getAlpha() != 255) {
            buffer.setLength(0);
            if(color.getAlpha() < 16)
                buffer.append('0');
            buffer.append(Integer.toString(color.getAlpha(), 16));
            attributes.add(ATTRIBUTE_ALPHA, buffer.toString());
        }

        return attributes;
    }

}
