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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.swing.JSplitPane;

import org.testng.annotations.Test;

/**
 * Pure-function tests for {@link SplitPaneOrientation}. Pin the
 * muCommander-vs-Swing flip in one place: muVertical=true must map to
 * {@link JSplitPane#HORIZONTAL_SPLIT} (panels side-by-side, divider
 * vertical on screen) and back.
 */
public class SplitPaneOrientationTest {

    @Test
    public void forMuVertical_true_isHorizontalSplit() {
        // muCommander "vertical" = panels side-by-side = JSplitPane.HORIZONTAL_SPLIT.
        assertEquals(SplitPaneOrientation.forMuVertical(true), JSplitPane.HORIZONTAL_SPLIT);
    }

    @Test
    public void forMuVertical_false_isVerticalSplit() {
        assertEquals(SplitPaneOrientation.forMuVertical(false), JSplitPane.VERTICAL_SPLIT);
    }

    @Test
    public void isMuVertical_horizontalSplit_true() {
        assertTrue(SplitPaneOrientation.isMuVertical(JSplitPane.HORIZONTAL_SPLIT));
    }

    @Test
    public void isMuVertical_verticalSplit_false() {
        assertFalse(SplitPaneOrientation.isMuVertical(JSplitPane.VERTICAL_SPLIT));
    }

    @Test
    public void roundTrip_isIdentity_forBothBooleans() {
        assertTrue(SplitPaneOrientation.isMuVertical(SplitPaneOrientation.forMuVertical(true)));
        assertFalse(SplitPaneOrientation.isMuVertical(SplitPaneOrientation.forMuVertical(false)));
    }
}
