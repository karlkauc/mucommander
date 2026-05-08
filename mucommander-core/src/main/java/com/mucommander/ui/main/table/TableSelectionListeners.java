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

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import com.mucommander.ui.event.TableSelectionListener;

/**
 * Registry of {@link TableSelectionListener}s for a {@link FileTable},
 * extracted from FileTable as a Phase 5.3 decomposition step. Mirrors
 * {@code com.mucommander.ui.main.ActivePanelListeners} (same WeakHashMap +
 * add/remove pattern) and broadcasts the two callbacks the listener
 * interface defines: selected-file-changed and marked-files-changed.
 *
 * <p>Backed by a synchronized {@link WeakHashMap} so listeners that go out
 * of scope without explicitly unregistering get garbage-collected, matching
 * the historical FileTable behaviour.
 */
class TableSelectionListeners {

    private final Map<TableSelectionListener, ?> listeners =
            Collections.synchronizedMap(new WeakHashMap<>());

    /** Registers a listener; same listener registered twice has no extra effect. */
    void add(TableSelectionListener listener) {
        listeners.put(listener, null);
    }

    /** Removes a previously-registered listener. No-op if the listener wasn't registered. */
    void remove(TableSelectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies every registered listener that the currently-selected file
     * on {@code source} just changed.
     */
    void fireSelectedFileChanged(FileTable source) {
        listeners.keySet().forEach(listener -> listener.selectedFileChanged(source));
    }

    /**
     * Notifies every registered listener that the marked-files set on
     * {@code source} just changed.
     */
    void fireMarkedFilesChanged(FileTable source) {
        listeners.keySet().forEach(listener -> listener.markedFilesChanged(source));
    }
}
