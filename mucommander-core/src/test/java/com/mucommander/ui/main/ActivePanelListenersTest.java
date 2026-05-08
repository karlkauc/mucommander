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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.expectThrows;

import com.mucommander.ui.event.ActivePanelListener;

import org.testng.annotations.Test;

/**
 * Characterization tests for {@link ActivePanelListeners}.
 *
 * Pins the registry semantics that MainFrame relied on when
 * addActivePanelListener / removeActivePanelListener / fireActivePanelChanged
 * lived inline: idempotent add, fire reaches every listener once,
 * remove stops further notifications, and a thrown listener exception
 * still propagates (callers expect to see broken listeners).
 */
public class ActivePanelListenersTest {

    @Test
    public void fire_withNoListeners_isNoop() {
        ActivePanelListeners registry = new ActivePanelListeners();
        // Just must not throw.
        registry.fire(mock(FolderPanel.class));
    }

    @Test
    public void add_thenFire_invokesListenerOnce() {
        ActivePanelListeners registry = new ActivePanelListeners();
        ActivePanelListener listener = mock(ActivePanelListener.class);
        FolderPanel folderPanel = mock(FolderPanel.class);

        registry.add(listener);
        registry.fire(folderPanel);

        verify(listener, times(1)).activePanelChanged(folderPanel);
    }

    @Test
    public void add_sameListenerTwice_stillInvokesOnce() {
        ActivePanelListeners registry = new ActivePanelListeners();
        ActivePanelListener listener = mock(ActivePanelListener.class);
        FolderPanel folderPanel = mock(FolderPanel.class);

        registry.add(listener);
        registry.add(listener);
        registry.fire(folderPanel);

        verify(listener, times(1)).activePanelChanged(folderPanel);
    }

    @Test
    public void remove_stopsFurtherNotifications() {
        ActivePanelListeners registry = new ActivePanelListeners();
        ActivePanelListener listener = mock(ActivePanelListener.class);
        FolderPanel folderPanel = mock(FolderPanel.class);

        registry.add(listener);
        registry.remove(listener);
        registry.fire(folderPanel);

        verify(listener, never()).activePanelChanged(folderPanel);
    }

    @Test
    public void remove_unregisteredListener_isNoop() {
        ActivePanelListeners registry = new ActivePanelListeners();
        ActivePanelListener listener = mock(ActivePanelListener.class);

        // Removing something that was never added must not throw.
        registry.remove(listener);

        registry.fire(mock(FolderPanel.class));
        verify(listener, never()).activePanelChanged(any(FolderPanel.class));
    }

    @Test
    public void fire_invokesAllListeners() {
        ActivePanelListeners registry = new ActivePanelListeners();
        ActivePanelListener a = mock(ActivePanelListener.class);
        ActivePanelListener b = mock(ActivePanelListener.class);
        ActivePanelListener c = mock(ActivePanelListener.class);
        FolderPanel folderPanel = mock(FolderPanel.class);

        registry.add(a);
        registry.add(b);
        registry.add(c);
        registry.fire(folderPanel);

        verify(a).activePanelChanged(folderPanel);
        verify(b).activePanelChanged(folderPanel);
        verify(c).activePanelChanged(folderPanel);
    }

    @Test
    public void fire_propagatesListenerException() {
        ActivePanelListeners registry = new ActivePanelListeners();
        ActivePanelListener throwing = mock(ActivePanelListener.class);
        FolderPanel folderPanel = mock(FolderPanel.class);
        doThrow(new RuntimeException("listener boom"))
                .when(throwing).activePanelChanged(folderPanel);

        registry.add(throwing);

        // Original behaviour: exceptions in a listener bubble out of fire().
        // Callers in MainFrame have always been responsible for not registering
        // listeners that misbehave.
        expectThrows(RuntimeException.class, () -> registry.fire(folderPanel));
    }
}
