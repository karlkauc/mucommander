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

package com.mucommander.ui.dialog.pref.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.mucommander.commons.util.ui.border.MutableLineBorder;
import com.mucommander.text.Translator;
import com.mucommander.ui.chooser.PreviewLabel;
import com.mucommander.ui.icon.CustomFileIconProvider;
import com.mucommander.ui.icon.FileIcons;
import com.mucommander.ui.icon.IconManager;
import com.mucommander.ui.main.table.CellLabel;
import com.mucommander.ui.theme.ThemeColor;
import com.mucommander.ui.theme.ThemeData;
import com.mucommander.ui.theme.ThemeFont;

/**
 * @author Nicolas Rinaudo
 */
class FilePreviewPanel extends JScrollPane implements PropertyChangeListener {
    // - Row identifiers ------------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    private static final int FOLDER         = 0;
    private static final int PLAIN_FILE     = 1;
    private static final int ARCHIVE        = 2;
    private static final int HIDDEN_FILE    = 3;
    private static final int SYMLINK        = 4;
    private static final int READ_ONLY_FILE = 5;
    private static final int MARKED_FILE    = 6;



    // - Instance fields -----------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    private ThemeData    data;
    private boolean      isActive;
    private PreviewTable table;
    private ImageIcon    symlinkIcon;



    // - Initialisation ------------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Creates a new preview panel on the specified theme data.
     * @param data     data to preview.
     * @param isActive whether we're previewing the active or inactive state.
     */
    public FilePreviewPanel(ThemeData data, boolean isActive) {
        super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.data     = data;
        this.isActive = isActive;
        symlinkIcon   = IconManager.getCompositeIcon(IconManager.getIcon(IconManager.FILE_ICON_SET, CustomFileIconProvider.FILE_ICON_NAME),
                                                     IconManager.getIcon(IconManager.FILE_ICON_SET, CustomFileIconProvider.SYMLINK_ICON_NAME));

        initUI();
    }

    /**
     * Initialises the previwer's UI.
     */
    private void initUI() {
        table = new PreviewTable();
        setViewportView(table);

        getViewport().setBackground(data.getColor(isActive ? ThemeColor.FILE_TABLE_BACKGROUND : ThemeColor.FILE_TABLE_INACTIVE_BACKGROUND));

        setBorder(new MutableLineBorder(data.getColor(isActive ? ThemeColor.FILE_TABLE_BORDER : ThemeColor.FILE_TABLE_INACTIVE_BORDER)));

        addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName().equals(PreviewLabel.BACKGROUND_COLOR_PROPERTY_NAME))
            getViewport().setBackground(data.getColor(isActive ? ThemeColor.FILE_TABLE_BACKGROUND : ThemeColor.FILE_TABLE_INACTIVE_BACKGROUND));
        else if(event.getPropertyName().equals(PreviewLabel.BORDER_COLOR_PROPERTY_NAME)) {
            // Some (rather evil) look and feels will change borders outside of muCommander's control,
            // this check is necessary to ensure no exception is thrown.
            if(getBorder() instanceof MutableLineBorder)
                ((MutableLineBorder)getBorder()).setLineColor(data.getColor(isActive ? ThemeColor.FILE_TABLE_BORDER : ThemeColor.FILE_TABLE_INACTIVE_BORDER));
        }
        else if(!event.getPropertyName().equals(PreviewLabel.FOREGROUND_COLOR_PROPERTY_NAME))
            return;
        repaint();
    }


    // - Theme listening -----------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Resets the table's row height with the new font.
     */
    @Override
    public void setFont(Font font) {
        if(table != null)
            table.setRowHeight(font);
    }



    // - Preview table -------------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Used to preview the current table theme.
     * @author Nicolas Rinaudo
     */
    private class PreviewTable extends JTable {
        private PreviewCellRenderer cellRenderer;
        private Dimension           preferredSize;

        /**
         * Creates a new preview table.
         */
        public PreviewTable() {
            super(new String[][] {{"", Translator.get("theme_editor.folder")},
                                  {"", Translator.get("theme_editor.plain_file")},
                                  {"", Translator.get("theme_editor.archive_file")},
                                  {"", Translator.get("theme_editor.hidden_file")},
                                  {"", Translator.get("theme_editor.symbolic_link")},
                                  {"", Translator.get("theme_editor.read_only_file")},
                                  {"", Translator.get("theme_editor.marked_file")}},
                new String[] {"", Translator.get("preview")});

            // Initialises table painting.
            cellRenderer = new PreviewCellRenderer();
            setShowGrid(false);

            // Initialises the table selection.
            getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            changeSelection(0, 0, false, false);

            // Initialises row dimensions.
            setRowHeight(data.getFont(ThemeFont.FILE_TABLE));
            setIntercellSpacing(new Dimension(0,0));

            // Initialises the table header.
            getTableHeader().setResizingAllowed(false);
            getTableHeader().setReorderingAllowed(false);
            ((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        }

        /**
         * Resets column widths.
         */
        @Override
        public void doLayout() {
            int width;

            getColumnModel().getColumn(0).setWidth(width = getIconWidth());
            getColumnModel().getColumn(1).setWidth(Math.max(getWidth() - width, getLabelWidth()));
        }

        /**
         * Returns the width of the icon label.
         */
        private int getIconWidth() {return (int)FileIcons.getIconDimension().getWidth() + 2 * CellLabel.CELL_BORDER_WIDTH;}

        /**
         * Returns the width of the text label.
         */
        private int getLabelWidth() {
            FontMetrics fm;
            int         rowCount;
            int         width;

            fm       = getFontMetrics(data.getFont(ThemeFont.FILE_TABLE));
            rowCount = getModel().getRowCount();
            width    = 0;
            for(int i = 0; i < rowCount; i++)
                width = Math.max(width, fm.stringWidth(((String)(getModel().getValueAt(i, 1)))) + 2 * CellLabel.CELL_BORDER_WIDTH);
            return width;
        }

        /**
         * Returns the table's preferred size.
         */
        @Override
        public Dimension getPreferredSize() {return new Dimension(getIconWidth() + 2 * getLabelWidth(), getModel().getRowCount()*getRowHeight());}

        /**
         * Returns the table's preferred size.
         */
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            if(preferredSize == null)
                preferredSize = getPreferredSize();
            return preferredSize;
        }

        /**
         * Initialises the row height depending on the font.
         */
        private void setRowHeight(Font font) {
            setRowHeight(2 * CellLabel.CELL_BORDER_HEIGHT + Math.max(getFontMetrics(font).getHeight(),
                                                                     (int)FileIcons.getIconDimension().getHeight()));
        }

        /**
         * Uses our preview cell renderer rather than the default one.
         */
        @Override
        public TableCellRenderer getCellRenderer(int row, int column) {return cellRenderer;}

        /**
         * Cell are not editable.
         */
        @Override
        public boolean isCellEditable(int row, int column) {return false;}
    }



    // - Preview renderer ----------------------------------------------------------------
    // -----------------------------------------------------------------------------------
    /**
     * Used to render preview cells.
     * @author Nicolas Rinaudo
     */
    private class PreviewCellRenderer implements TableCellRenderer {
        private CellLabel label;
        private CellLabel icon;

        /**
         * Creates a new preview cell renderer.
         */
        public PreviewCellRenderer() {
            label = new CellLabel();
            icon  = new CellLabel();
        }

        /**
         * Returns the foregorund color of the specified cell.
         */
        private Color getForegroundColor(int row, boolean isSelected) {
            switch(row) {
                // Folders.
            case FOLDER:
                if(FilePreviewPanel.this.isActive)
                    return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.FOLDER_SELECTED_FOREGROUND :
                                                               ThemeColor.FOLDER_FOREGROUND);
                return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.FOLDER_INACTIVE_SELECTED_FOREGROUND :
                                                           ThemeColor.FOLDER_INACTIVE_FOREGROUND);

                // Plain files.
            case PLAIN_FILE:
                if(FilePreviewPanel.this.isActive)
                    return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.FILE_SELECTED_FOREGROUND :
                                                               ThemeColor.FILE_FOREGROUND);
                return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.FILE_INACTIVE_SELECTED_FOREGROUND :
                                                           ThemeColor.FILE_INACTIVE_FOREGROUND);

                // Archives.
            case ARCHIVE:
                if(FilePreviewPanel.this.isActive)
                    return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.ARCHIVE_SELECTED_FOREGROUND :
                                                               ThemeColor.ARCHIVE_FOREGROUND);
                return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.ARCHIVE_INACTIVE_SELECTED_FOREGROUND :
                                                           ThemeColor.ARCHIVE_INACTIVE_FOREGROUND);

                // Hidden files.
            case HIDDEN_FILE:
                if(FilePreviewPanel.this.isActive)
                    return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.HIDDEN_FILE_SELECTED_FOREGROUND :
                                                               ThemeColor.HIDDEN_FILE_FOREGROUND);
                return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.HIDDEN_FILE_INACTIVE_SELECTED_FOREGROUND :
                                                           ThemeColor.HIDDEN_FILE_INACTIVE_FOREGROUND);

                // Symlinks.
            case SYMLINK:
                if(FilePreviewPanel.this.isActive)
                    return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.SYMLINK_SELECTED_FOREGROUND :
                                                               ThemeColor.SYMLINK_FOREGROUND);
                return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.SYMLINK_INACTIVE_SELECTED_FOREGROUND :
                                                           ThemeColor.SYMLINK_INACTIVE_FOREGROUND);

                // Read-only
            case READ_ONLY_FILE:
                if (FilePreviewPanel.this.isActive)
                    return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.READ_ONLY_SELECTED_FOREGROUND :
                                                               ThemeColor.READ_ONLY_FOREGROUND);
                return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.READ_ONLY_INACTIVE_SELECTED_FOREGROUND :
                                                           ThemeColor.READ_ONLY_INACTIVE_FOREGROUND);
                // Marked files.
            case MARKED_FILE:
                if(FilePreviewPanel.this.isActive)
                    return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.MARKED_SELECTED_FOREGROUND :
                                                               ThemeColor.MARKED_FOREGROUND);
                return FilePreviewPanel.this.data.getColor(isSelected ? ThemeColor.MARKED_INACTIVE_SELECTED_FOREGROUND :
                                                           ThemeColor.MARKED_INACTIVE_FOREGROUND);
            }

            // Impossible.
            return null;
        }

        /**
         * Returns the object used to render the specified cell.
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            CellLabel currentLabel;

            // Icon label foreground.
            if(column == 0) {
                currentLabel = icon;
                if(row == FOLDER)
                    currentLabel.setIcon(IconManager.getIcon(IconManager.FILE_ICON_SET, CustomFileIconProvider.FOLDER_ICON_NAME));
                else if(row == ARCHIVE)
                    currentLabel.setIcon(IconManager.getIcon(IconManager.FILE_ICON_SET, CustomFileIconProvider.ARCHIVE_ICON_NAME));
                else if(row == SYMLINK)
                    currentLabel.setIcon(symlinkIcon);
                else
                    currentLabel.setIcon(IconManager.getIcon(IconManager.FILE_ICON_SET, CustomFileIconProvider.FILE_ICON_NAME));
            }
            // Text label foreground.
            else {
                currentLabel = label;
                currentLabel.setFont(data.getFont(ThemeFont.FILE_TABLE));
                currentLabel.setText((String)value);
                currentLabel.setForeground(getForegroundColor(row, isSelected));
            }

            // Foreground.
            if(isSelected)
                currentLabel.setOutline(isActive ? data.getColor(ThemeColor.FILE_TABLE_SELECTED_OUTLINE) :
                                        data.getColor(ThemeColor.FILE_TABLE_INACTIVE_SELECTED_OUTLINE));
            else
                currentLabel.setOutline(null);

            // Background.
            if(FilePreviewPanel.this.isActive) {
                if(isSelected)
                    currentLabel.setBackground(FilePreviewPanel.this.data.getColor(ThemeColor.FILE_TABLE_SELECTED_BACKGROUND));
                else
                    currentLabel.setBackground(FilePreviewPanel.this.data.getColor((row % 2 == 0) ? ThemeColor.FILE_TABLE_BACKGROUND :
                                                                                   ThemeColor.FILE_TABLE_ALTERNATE_BACKGROUND));
            }
            else {
                if(isSelected)
                    currentLabel.setBackground(FilePreviewPanel.this.data.getColor(ThemeColor.FILE_TABLE_INACTIVE_SELECTED_BACKGROUND));
                else
                    currentLabel.setBackground(FilePreviewPanel.this.data.getColor((row % 2 == 0) ? ThemeColor.FILE_TABLE_INACTIVE_BACKGROUND :
                                                                                   ThemeColor.FILE_TABLE_INACTIVE_ALTERNATE_BACKGROUND));
            }

            return currentLabel;
        }
    }
}
