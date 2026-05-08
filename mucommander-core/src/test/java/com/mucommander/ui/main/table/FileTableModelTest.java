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

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import com.mucommander.commons.file.AbstractFile;
import com.mucommander.commons.file.FileURL;
import com.mucommander.commons.file.util.FileSet;

import org.testng.annotations.Test;

/**
 * Characterization tests for {@link FileTableModel}.
 *
 * Lives in the same package as {@code FileTableModel} so the tests can call
 * the package-private {@code setCurrentFolder(folder, children)}.
 *
 * Phase 5.0 made these viable: {@code Translator.get(...)} now returns the
 * key as a fallback when the dictionary bundle is uninitialised, so
 * {@code Column.<clinit>} doesn't NPE in test contexts. {@code MuPreferences}
 * is eagerly constructed with safe defaults, so {@code FileTableModel.<clinit>}
 * works without an OSGi bootstrap.
 *
 * These tests pin down the marking, file-count, and parent-folder semantics
 * so the planned Phase 5.3 MVC decomposition (extracting selection/sort/
 * quick-search out of FileTable) can rely on a stable contract for the model.
 */
public class FileTableModelTest {

    private static AbstractFile mockFile(String name, boolean directory, long size) {
        AbstractFile file = mock(AbstractFile.class);
        // FileTableModel.fillCellCache() calls getURL().getScheme() on every
        // child to pick a name-comparator; stub a minimal local-file URL.
        FileURL url = mock(FileURL.class);
        lenient().when(url.getScheme()).thenReturn("file");
        lenient().when(file.getURL()).thenReturn(url);
        // Lenient stubs because not every test path consumes every property
        // (e.g. tests that never call setRowMarked won't read isDirectory/getSize).
        lenient().when(file.getName()).thenReturn(name);
        lenient().when(file.isDirectory()).thenReturn(directory);
        lenient().when(file.getSize()).thenReturn(size);
        lenient().when(file.isBrowsable()).thenReturn(directory);
        lenient().when(file.isHidden()).thenReturn(false);
        lenient().when(file.isSymlink()).thenReturn(false);
        return file;
    }

    private static AbstractFile mockFolderWithoutParent() {
        AbstractFile folder = mockFile("folder", true, 0L);
        lenient().when(folder.getParent()).thenReturn(null);
        return folder;
    }

    private static FileTableModel modelWith(AbstractFile folder, AbstractFile... children) {
        FileTableModel model = new FileTableModel();
        model.setSortInfo(new SortInfo());
        model.setCurrentFolder(folder, children);
        return model;
    }

    @Test
    public void freshModel_hasNoCurrentFolder() {
        FileTableModel model = new FileTableModel();
        assertNull(model.getCurrentFolder());
        assertEquals(model.getFileCount(), 0);
        assertFalse(model.hasParentFolder());
    }

    @Test
    public void freshModel_hasZeroRowCount() {
        FileTableModel model = new FileTableModel();
        assertEquals(model.getRowCount(), 0);
    }

    @Test
    public void freshModel_columnCountMatchesEnum() {
        FileTableModel model = new FileTableModel();
        // Tripwire so renaming or removing a Column value will be noticed.
        assertEquals(model.getColumnCount(), Column.values().length);
    }

    @Test
    public void setCurrentFolder_storesFolderAndFileCount() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a.txt", false, 100);
        AbstractFile b = mockFile("b.txt", false, 200);
        AbstractFile c = mockFile("c.txt", false, 300);

        FileTableModel model = modelWith(folder, a, b, c);

        assertNotNull(model.getCurrentFolder(), "currentFolder must not be null after setCurrentFolder");
        assertEquals(model.getFileCount(), 3);
    }

    @Test
    public void setCurrentFolder_withoutParent_rowCountEqualsFileCount() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a", false, 1);
        AbstractFile b = mockFile("b", false, 2);

        FileTableModel model = modelWith(folder, a, b);

        assertFalse(model.hasParentFolder());
        // No '..' row, so rowCount == fileCount.
        assertEquals(model.getRowCount(), 2);
        assertEquals(model.getFirstMarkableRow(), 0);
    }

    @Test
    public void setRowMarked_marksRow_andUpdatesCount() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a", false, 100);
        AbstractFile b = mockFile("b", false, 200);
        FileTableModel model = modelWith(folder, a, b);

        assertEquals(model.getNbMarkedFiles(), 0);
        assertFalse(model.isRowMarked(0));

        model.setRowMarked(0, true);

        assertTrue(model.isRowMarked(0));
        assertEquals(model.getNbMarkedFiles(), 1);
    }

    @Test
    public void setRowMarked_idempotent_doesNotChangeCount() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a", false, 100);
        FileTableModel model = modelWith(folder, a);

        model.setRowMarked(0, true);
        model.setRowMarked(0, true);

        // Marking an already-marked row is a no-op; count must not increment twice.
        assertEquals(model.getNbMarkedFiles(), 1);
    }

    @Test
    public void unmark_decreasesCount_andClearsState() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a", false, 100);
        FileTableModel model = modelWith(folder, a);

        model.setRowMarked(0, true);
        assertEquals(model.getNbMarkedFiles(), 1);

        model.setRowMarked(0, false);

        assertFalse(model.isRowMarked(0));
        assertEquals(model.getNbMarkedFiles(), 0);
    }

    @Test
    public void setRangeMarked_marksAllRowsInRange() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a", false, 10);
        AbstractFile b = mockFile("b", false, 20);
        AbstractFile c = mockFile("c", false, 30);
        AbstractFile d = mockFile("d", false, 40);
        FileTableModel model = modelWith(folder, a, b, c, d);

        model.setRangeMarked(1, 2, true);

        assertFalse(model.isRowMarked(0));
        assertTrue(model.isRowMarked(1));
        assertTrue(model.isRowMarked(2));
        assertFalse(model.isRowMarked(3));
        assertEquals(model.getNbMarkedFiles(), 2);
    }

    @Test
    public void setRangeMarked_descendingRange_alsoWorks() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a", false, 10);
        AbstractFile b = mockFile("b", false, 20);
        AbstractFile c = mockFile("c", false, 30);
        FileTableModel model = modelWith(folder, a, b, c);

        // The contract explicitly supports endRow < startRow.
        model.setRangeMarked(2, 0, true);

        assertEquals(model.getNbMarkedFiles(), 3);
    }

    @Test
    public void getMarkedFiles_returnsOnlyMarkedFiles() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a", false, 10);
        AbstractFile b = mockFile("b", false, 20);
        AbstractFile c = mockFile("c", false, 30);
        FileTableModel model = modelWith(folder, a, b, c);

        model.setRowMarked(0, true);
        model.setRowMarked(2, true);

        FileSet marked = model.getMarkedFiles();
        assertEquals(marked.size(), 2);
    }

    @Test
    public void totalMarkedSize_sumsRegularFileSizes() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile a = mockFile("a", false, 100);
        AbstractFile b = mockFile("b", false, 250);
        FileTableModel model = modelWith(folder, a, b);

        model.setRowMarked(0, true);
        model.setRowMarked(1, true);

        assertEquals(model.getTotalMarkedSize(), 350L);
    }

    @Test
    public void totalMarkedSize_skipsDirectories() {
        AbstractFile folder = mockFolderWithoutParent();
        AbstractFile dir = mockFile("subdir", true, 100);
        AbstractFile file = mockFile("file", false, 200);
        FileTableModel model = modelWith(folder, dir, file);

        model.setRowMarked(0, true);
        model.setRowMarked(1, true);

        // setRowMarked() does not call getSize() on directories — they
        // contribute 0 to the marked total regardless of the mocked size.
        assertEquals(model.getTotalMarkedSize(), 200L);
        assertEquals(model.getNbMarkedFiles(), 2);
    }

    @Test
    public void freshModel_firstMarkableRowIsZero_whenNoParent() {
        FileTableModel model = new FileTableModel();
        assertEquals(model.getFirstMarkableRow(), 0,
                "Without a parent folder, the first markable row is 0");
    }
}
