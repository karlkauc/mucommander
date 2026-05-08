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

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Frame;

import org.mockito.InOrder;
import org.testng.annotations.Test;

/**
 * Characterization tests for {@link WindowFront#bringToFront}.
 *
 * Pin the deiconify-then-toFront sequence: state is only flipped when
 * the iconified bit is set, other state bits (maximised, etc.) are
 * preserved, and the toFront call happens last.
 */
public class WindowFrontTest {

    @Test
    public void normalFrame_callsToFront_andDoesNotChangeState() {
        Frame frame = mock(Frame.class);
        when(frame.getExtendedState()).thenReturn(Frame.NORMAL);

        WindowFront.bringToFront(frame);

        verify(frame, never()).setExtendedState(org.mockito.ArgumentMatchers.anyInt());
        verify(frame).toFront();
    }

    @Test
    public void iconifiedFrame_isFirstSetToNormal_thenBroughtToFront() {
        Frame frame = mock(Frame.class);
        when(frame.getExtendedState()).thenReturn(Frame.ICONIFIED);

        WindowFront.bringToFront(frame);

        InOrder order = inOrder(frame);
        order.verify(frame).setExtendedState(Frame.NORMAL);
        order.verify(frame).toFront();
    }

    @Test
    public void maximizedFrame_keepsState_andCallsToFront() {
        // Maximised but not iconified — bringToFront must not strip the maximised bits.
        Frame frame = mock(Frame.class);
        when(frame.getExtendedState()).thenReturn(Frame.MAXIMIZED_BOTH);

        WindowFront.bringToFront(frame);

        verify(frame, never()).setExtendedState(org.mockito.ArgumentMatchers.anyInt());
        verify(frame).toFront();
    }

    @Test
    public void iconifiedAndMaximised_clearsToNormal_andCallsToFront() {
        // The historical inline code unconditionally sets NORMAL when ICONIFIED is
        // present, dropping any other state bits. Pin that behaviour so callers
        // see the same window restoration as before.
        Frame frame = mock(Frame.class);
        when(frame.getExtendedState()).thenReturn(Frame.ICONIFIED | Frame.MAXIMIZED_BOTH);

        WindowFront.bringToFront(frame);

        verify(frame).setExtendedState(Frame.NORMAL);
        verify(frame).toFront();
    }
}
