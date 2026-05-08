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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.awt.Window;

import org.testng.annotations.Test;

/**
 * Characterization tests for {@link WindowActivity#isFrameOrOwnedActive}.
 *
 * Pin the predicate the historical {@code MainFrame.isAncestorOfActiveWindow()}
 * relied on: the window itself counts, any owned descendant counts, neither
 * means false. Test against mock windows so we don't depend on a real display.
 */
public class WindowActivityTest {

    @Test
    public void frameItselfActive_returnsTrue_andSkipsOwnedLookup() {
        Window frame = mock(Window.class);
        when(frame.isActive()).thenReturn(true);

        assertTrue(WindowActivity.isFrameOrOwnedActive(frame));
        // Owned-windows enumeration must not happen if the frame itself is active.
        verify(frame, never()).getOwnedWindows();
    }

    @Test
    public void frameInactive_noOwnedWindows_returnsFalse() {
        Window frame = mock(Window.class);
        when(frame.isActive()).thenReturn(false);
        when(frame.getOwnedWindows()).thenReturn(new Window[0]);

        assertFalse(WindowActivity.isFrameOrOwnedActive(frame));
    }

    @Test
    public void frameInactive_oneOwnedActive_returnsTrue() {
        Window frame = mock(Window.class);
        Window child = mock(Window.class);
        when(frame.isActive()).thenReturn(false);
        when(child.isActive()).thenReturn(true);
        when(frame.getOwnedWindows()).thenReturn(new Window[] { child });

        assertTrue(WindowActivity.isFrameOrOwnedActive(frame));
    }

    @Test
    public void frameInactive_severalOwned_oneActiveInTheMiddle_returnsTrue() {
        Window frame = mock(Window.class);
        Window c1 = mock(Window.class);
        Window c2 = mock(Window.class);
        Window c3 = mock(Window.class);
        when(frame.isActive()).thenReturn(false);
        when(c1.isActive()).thenReturn(false);
        when(c2.isActive()).thenReturn(true);
        // c3 should never be queried (short-circuit on c2's hit) — leave it lenient.
        lenient().when(c3.isActive()).thenReturn(false);
        when(frame.getOwnedWindows()).thenReturn(new Window[] { c1, c2, c3 });

        assertTrue(WindowActivity.isFrameOrOwnedActive(frame));
        verify(c2, times(1)).isActive();
    }

    @Test
    public void frameInactive_allOwnedInactive_returnsFalse() {
        Window frame = mock(Window.class);
        Window c1 = mock(Window.class);
        Window c2 = mock(Window.class);
        when(frame.isActive()).thenReturn(false);
        when(c1.isActive()).thenReturn(false);
        when(c2.isActive()).thenReturn(false);
        when(frame.getOwnedWindows()).thenReturn(new Window[] { c1, c2 });

        assertFalse(WindowActivity.isFrameOrOwnedActive(frame));
    }
}
