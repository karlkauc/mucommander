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

import java.awt.Frame;

/**
 * Helper for raising a {@link Frame} to the foreground, restoring it from
 * the iconified (minimised) state first if necessary.
 *
 * <p>Extracted from {@code MainFrame.toFront()} as part of Phase 5.2.
 * Plain {@link Frame#toFront()} doesn't deiconify the window, so the
 * iconified bit has to be cleared first or the user wouldn't see anything
 * happen. The bit fiddling lives here so it's testable against a mock
 * Frame instead of needing a real display.
 */
final class WindowFront {

    private WindowFront() {
        // utility
    }

    /**
     * Raises {@code frame} to the foreground. If the frame is currently
     * iconified, its extended state is first switched to {@link Frame#NORMAL};
     * other state bits (e.g. maximised) are preserved untouched.
     */
    static void bringToFront(Frame frame) {
        if ((frame.getExtendedState() & Frame.ICONIFIED) != 0) {
            frame.setExtendedState(Frame.NORMAL);
        }
        frame.toFront();
    }
}
