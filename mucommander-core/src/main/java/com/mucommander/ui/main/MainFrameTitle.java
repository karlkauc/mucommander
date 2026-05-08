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

/**
 * Pure helpers for building a {@link MainFrame}'s window title from its
 * inputs (active folder path, OS, frame ordinal). Extracted from
 * {@code MainFrame.updateWindowTitle()} as part of the Phase 5.2
 * decomposition — keeping the string-building logic separate from the
 * Swing/AWT side-effects makes both halves easier to test and reason about.
 */
final class MainFrameTitle {

    private static final String APP_NAME = "muCommander";

    private MainFrameTitle() {
        // utility
    }

    /**
     * Returns the title that should be shown in the OS window decoration.
     *
     * <p>Format: {@code <absolutePath>[ - muCommander][ [N]]}
     * <ul>
     *   <li>The {@code  - muCommander} suffix is appended on every OS
     *       except macOS, where the application menu already shows the name.</li>
     *   <li>The trailing {@code [N]} disambiguator is appended only when
     *       multiple main frames are open; {@code N} is the 1-based ordinal
     *       of this frame in the window list (so the first frame stays bare).</li>
     * </ul>
     *
     * @param absolutePath the absolute path of the active folder; treated as opaque
     * @param isMac        true on macOS — drives the app-name suffix
     * @param frameIndex   0-based index of this frame in the window list
     * @param totalFrames  total number of currently open main frames; values
     *                     {@code <= 1} suppress the {@code [N]} disambiguator
     * @return the formatted title string
     */
    static String buildTitle(String absolutePath, boolean isMac, int frameIndex, int totalFrames) {
        StringBuilder title = new StringBuilder(absolutePath);
        if (!isMac) {
            title.append(" - ").append(APP_NAME);
        }
        if (totalFrames > 1) {
            title.append(" [").append(frameIndex + 1).append(']');
        }
        return title.toString();
    }
}
