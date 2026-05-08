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

import java.io.File;

import com.mucommander.commons.file.AbstractFile;
import com.mucommander.commons.file.archive.AbstractArchiveEntryFile;
import com.mucommander.commons.file.protocol.local.LocalFile;

/**
 * Picks the {@link File} to install on a JFrame's
 * {@code Window.documentFile} root-pane property — the underlying mechanism
 * macOS uses to render the proxy icon in the window title bar.
 *
 * <p>Three branches:
 * <ul>
 *   <li>active folder is a regular local file → its underlying {@link File}.</li>
 *   <li>active folder is inside a local archive → the archive file's
 *       underlying {@link File} (closest representable {@code File}).</li>
 *   <li>anything else (remote / virtual) → the special {@code /Network}
 *       directory, the same fallback the historical inline code used.</li>
 * </ul>
 *
 * <p>Extracted from {@code MainFrame.updateWindowTitle()} as part of the
 * Phase 5.2 decomposition. The Swing side-effect (the actual
 * {@code rootPane.putClientProperty(...)} call) stays in MainFrame; this
 * helper is purely about picking the right {@code File} object so the
 * decision can be tested without an EDT or window.
 */
final class MacDocumentFile {

    /** macOS Network virtual location, used as fallback for non-local folders. */
    private static final File NETWORK_FALLBACK = new File("/Network");

    private MacDocumentFile() {
        // utility
    }

    /**
     * Returns the {@link File} to register as the proxy-icon target for the
     * given {@code currentFolder}. Never returns {@code null} — non-local
     * folders fall back to {@code /Network}.
     */
    static File resolveFor(AbstractFile currentFolder) {
        if (LocalFile.SCHEMA.equals(currentFolder.getURL().getScheme())) {
            AbstractFile source = currentFolder.hasAncestor(AbstractArchiveEntryFile.class)
                    ? currentFolder.getParentArchive()
                    : currentFolder;
            return (File) source.getUnderlyingFileObject();
        }
        // Remote / virtual folder — fall back to the macOS "Network Neighborhood".
        return NETWORK_FALLBACK;
    }
}
