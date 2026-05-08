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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.expectThrows;

import com.mucommander.ui.event.TableSelectionListener;

import org.testng.annotations.Test;

/**
 * Characterization tests for {@link TableSelectionListeners}.
 *
 * Pin the registry semantics that {@link FileTable} relied on inline:
 * idempotent add, both fire-methods reach every listener, remove stops
 * notifications, exceptions bubble out of fire().
 */
public class TableSelectionListenersTest {

    @Test
    public void fireSelected_withNoListeners_isNoop() {
        new TableSelectionListeners().fireSelectedFileChanged(mock(FileTable.class));
    }

    @Test
    public void fireMarked_withNoListeners_isNoop() {
        new TableSelectionListeners().fireMarkedFilesChanged(mock(FileTable.class));
    }

    @Test
    public void add_thenFireSelected_invokesSelectedCallbackOnce() {
        TableSelectionListeners reg = new TableSelectionListeners();
        TableSelectionListener listener = mock(TableSelectionListener.class);
        FileTable source = mock(FileTable.class);

        reg.add(listener);
        reg.fireSelectedFileChanged(source);

        verify(listener, times(1)).selectedFileChanged(source);
        verify(listener, never()).markedFilesChanged(source);
    }

    @Test
    public void add_thenFireMarked_invokesMarkedCallbackOnce() {
        TableSelectionListeners reg = new TableSelectionListeners();
        TableSelectionListener listener = mock(TableSelectionListener.class);
        FileTable source = mock(FileTable.class);

        reg.add(listener);
        reg.fireMarkedFilesChanged(source);

        verify(listener, times(1)).markedFilesChanged(source);
        verify(listener, never()).selectedFileChanged(source);
    }

    @Test
    public void add_sameListenerTwice_stillFiresOnce() {
        TableSelectionListeners reg = new TableSelectionListeners();
        TableSelectionListener listener = mock(TableSelectionListener.class);
        FileTable source = mock(FileTable.class);

        reg.add(listener);
        reg.add(listener);
        reg.fireSelectedFileChanged(source);

        verify(listener, times(1)).selectedFileChanged(source);
    }

    @Test
    public void remove_stopsBothNotificationKinds() {
        TableSelectionListeners reg = new TableSelectionListeners();
        TableSelectionListener listener = mock(TableSelectionListener.class);
        FileTable source = mock(FileTable.class);

        reg.add(listener);
        reg.remove(listener);
        reg.fireSelectedFileChanged(source);
        reg.fireMarkedFilesChanged(source);

        verify(listener, never()).selectedFileChanged(source);
        verify(listener, never()).markedFilesChanged(source);
    }

    @Test
    public void fire_invokesAllListeners() {
        TableSelectionListeners reg = new TableSelectionListeners();
        TableSelectionListener a = mock(TableSelectionListener.class);
        TableSelectionListener b = mock(TableSelectionListener.class);
        TableSelectionListener c = mock(TableSelectionListener.class);
        FileTable source = mock(FileTable.class);

        reg.add(a);
        reg.add(b);
        reg.add(c);
        reg.fireSelectedFileChanged(source);

        verify(a).selectedFileChanged(source);
        verify(b).selectedFileChanged(source);
        verify(c).selectedFileChanged(source);
    }

    @Test
    public void fireSelected_propagatesListenerException() {
        TableSelectionListeners reg = new TableSelectionListeners();
        TableSelectionListener throwing = mock(TableSelectionListener.class);
        FileTable source = mock(FileTable.class);
        doThrow(new RuntimeException("listener boom"))
                .when(throwing).selectedFileChanged(source);

        reg.add(throwing);

        expectThrows(RuntimeException.class, () -> reg.fireSelectedFileChanged(source));
    }
}
