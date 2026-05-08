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

import org.testng.annotations.Test;

/**
 * Pure-function tests for {@link MainFrameTitle#buildTitle}. Pins the formatting
 * contract that lives behind {@code MainFrame.updateWindowTitle()}: app-name
 * suffix everywhere except macOS, frame-index disambiguator only when more
 * than one main frame is open, otherwise the title is just the active folder
 * path.
 */
public class MainFrameTitleTest {

    @Test
    public void singleFrame_nonMac_appendsAppName() {
        assertEquals(MainFrameTitle.buildTitle("/home/karl", false, 0, 1),
                "/home/karl - muCommander");
    }

    @Test
    public void singleFrame_mac_omitsAppName() {
        assertEquals(MainFrameTitle.buildTitle("/Users/karl", true, 0, 1),
                "/Users/karl");
    }

    @Test
    public void multiFrame_firstOf3_nonMac_includesIndex() {
        assertEquals(MainFrameTitle.buildTitle("/tmp", false, 0, 3),
                "/tmp - muCommander [1]");
    }

    @Test
    public void multiFrame_thirdOf3_nonMac_includesIndex() {
        assertEquals(MainFrameTitle.buildTitle("/tmp", false, 2, 3),
                "/tmp - muCommander [3]");
    }

    @Test
    public void multiFrame_mac_omitsAppNameButIncludesIndex() {
        assertEquals(MainFrameTitle.buildTitle("/Users/karl", true, 1, 2),
                "/Users/karl [2]");
    }

    @Test
    public void singleFrame_zeroTotal_treatsAsSingle() {
        // totalFrames <= 1 means "no disambiguator", including the edge case
        // of an empty list (this shouldn't happen at runtime but the function
        // shouldn't crash either).
        assertEquals(MainFrameTitle.buildTitle("/", false, 0, 0),
                "/ - muCommander");
    }

    @Test
    public void preservesAbsolutePathContents() {
        // The path is treated as opaque — spaces, special chars, trailing
        // slashes pass through unchanged.
        assertEquals(MainFrameTitle.buildTitle("/path with spaces/and-dashes/", false, 0, 1),
                "/path with spaces/and-dashes/ - muCommander");
    }
}
