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

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import com.mucommander.ui.event.ActivePanelListener;

/**
 * Registry of {@link ActivePanelListener}s for a {@link MainFrame}, extracted
 * from MainFrame as a Phase 5.2 decomposition step.
 *
 * <p>Backed by a synchronized {@link WeakHashMap} so listeners that go out of
 * scope without being explicitly removed get garbage-collected with their
 * owners — this matches the historical MainFrame behaviour and is what the
 * field's javadoc ("stored as weak references") promised.
 *
 * <p>{@link #fire} iterates over the current key set; an exception thrown by
 * one listener will propagate, which mirrors the original behaviour.
 */
class ActivePanelListeners {

    private final Map<ActivePanelListener, ?> listeners =
            Collections.synchronizedMap(new WeakHashMap<>());

    /** Registers a listener; same listener registered twice has no extra effect. */
    void add(ActivePanelListener listener) {
        listeners.put(listener, null);
    }

    /** Removes a previously-registered listener. No-op if the listener wasn't registered. */
    void remove(ActivePanelListener listener) {
        listeners.remove(listener);
    }

    /** Notifies every registered listener that the given panel just became active. */
    void fire(FolderPanel folderPanel) {
        listeners.keySet().forEach(listener -> listener.activePanelChanged(folderPanel));
    }
}
