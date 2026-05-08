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

import javax.swing.JSplitPane;

/**
 * Two-way translation between muCommander's "vertical/horizontal" terminology
 * for the dual-folder split and the {@link JSplitPane} constants.
 *
 * <p>The terms in muCommander are the opposite of Swing's: a "vertical split"
 * in muCommander stacks the panels side-by-side (so the divider is vertical
 * on screen), which {@link JSplitPane} calls a {@code HORIZONTAL_SPLIT}.
 * That used to be sprinkled across {@code MainFrame} as inline ternaries,
 * each carrying a comment about the inversion. Pulled out so the mapping
 * lives in one place and can be tested without an EDT.
 */
final class SplitPaneOrientation {

    private SplitPaneOrientation() {
        // utility
    }

    /**
     * Returns the {@link JSplitPane} orientation constant that matches
     * muCommander's notion of "vertical split".
     *
     * @param muVertical {@code true} if the folder panels should be split
     *                   vertically in muCommander's sense (panels side-by-side)
     * @return {@link JSplitPane#HORIZONTAL_SPLIT} when {@code muVertical} is
     *         true, {@link JSplitPane#VERTICAL_SPLIT} otherwise
     */
    static int forMuVertical(boolean muVertical) {
        return muVertical ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT;
    }

    /**
     * Inverse of {@link #forMuVertical}: given a {@link JSplitPane}
     * orientation constant, returns muCommander's "is vertical?" flag.
     */
    static boolean isMuVertical(int jSplitPaneOrientation) {
        return jSplitPaneOrientation == JSplitPane.HORIZONTAL_SPLIT;
    }
}
