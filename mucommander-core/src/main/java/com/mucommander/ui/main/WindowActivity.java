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

import java.awt.Window;
import java.util.Arrays;

/**
 * Pure helpers for inspecting the activity state of a {@link Window} and the
 * windows it owns (modal dialogs, child frames, etc.).
 *
 * <p>Extracted from {@code MainFrame.isAncestorOfActiveWindow()} as part of
 * Phase 5.2. The original used a C-style index loop over {@code getOwnedWindows()};
 * this is the modernised, single-purpose form so the predicate can be exercised
 * directly in tests against mock windows.
 */
final class WindowActivity {

    private WindowActivity() {
        // utility
    }

    /**
     * Returns {@code true} if {@code window} is itself active, or if any of
     * its currently-owned descendants (dialogs, child frames) is active.
     *
     * @param window the window to inspect; must not be null
     */
    static boolean isFrameOrOwnedActive(Window window) {
        if (window.isActive()) {
            return true;
        }
        return Arrays.stream(window.getOwnedWindows()).anyMatch(Window::isActive);
    }
}
