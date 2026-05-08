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

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.File;

import com.mucommander.commons.file.AbstractFile;
import com.mucommander.commons.file.FileURL;
import com.mucommander.commons.file.archive.AbstractArchiveEntryFile;
import com.mucommander.commons.file.archive.AbstractArchiveFile;
import com.mucommander.commons.file.protocol.local.LocalFile;

import org.testng.annotations.Test;

/**
 * Characterization tests for {@link MacDocumentFile#resolveFor}.
 *
 * Pin the three branches the historical inline code in
 * {@code MainFrame.updateWindowTitle()} had: regular local file, local
 * archive entry (use the archive itself, since archive members aren't
 * representable as java.io.File), and the remote/virtual fallback to
 * {@code /Network}.
 */
public class MacDocumentFileTest {

    @Test
    public void localFile_notInArchive_returnsItsUnderlyingFile() {
        File expected = new File("/home/karl/notes.txt");
        AbstractFile localFile = mockLocalFile(expected, false);

        assertSame(MacDocumentFile.resolveFor(localFile), expected);
    }

    @Test
    public void localFileInsideArchive_returnsArchiveUnderlyingFile() {
        File archiveFile = new File("/home/karl/data.zip");
        AbstractFile entry = mockArchiveEntry(archiveFile);

        assertSame(MacDocumentFile.resolveFor(entry), archiveFile);
    }

    @Test
    public void nonLocalFolder_fallsBackToNetwork() {
        AbstractFile remote = mockNonLocalFile("ftp");

        File result = MacDocumentFile.resolveFor(remote);

        // The fallback is the macOS /Network virtual location.
        assertEquals(result.getPath(), "/Network");
    }

    @Test
    public void smbFolder_fallsBackToNetwork() {
        // Any non-local scheme triggers the same fallback.
        AbstractFile smb = mockNonLocalFile("smb");

        File result = MacDocumentFile.resolveFor(smb);

        assertEquals(result.getPath(), "/Network");
    }

    private static AbstractFile mockLocalFile(File underlying, boolean inArchive) {
        AbstractFile file = mock(AbstractFile.class);
        FileURL url = mock(FileURL.class);
        when(url.getScheme()).thenReturn(LocalFile.SCHEMA);
        when(file.getURL()).thenReturn(url);
        when(file.hasAncestor(AbstractArchiveEntryFile.class)).thenReturn(inArchive);
        lenient().when(file.getUnderlyingFileObject()).thenReturn(underlying);
        return file;
    }

    private static AbstractFile mockArchiveEntry(File archiveUnderlying) {
        AbstractFile entry = mockLocalFile(null, true);
        AbstractArchiveFile archive = mock(AbstractArchiveFile.class);
        when(archive.getUnderlyingFileObject()).thenReturn(archiveUnderlying);
        when(entry.getParentArchive()).thenReturn(archive);
        return entry;
    }

    private static AbstractFile mockNonLocalFile(String scheme) {
        AbstractFile file = mock(AbstractFile.class);
        FileURL url = mock(FileURL.class);
        when(url.getScheme()).thenReturn(scheme);
        when(file.getURL()).thenReturn(url);
        return file;
    }
}
