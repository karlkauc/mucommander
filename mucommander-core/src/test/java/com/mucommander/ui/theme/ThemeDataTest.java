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
package com.mucommander.ui.theme;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.awt.Color;
import java.awt.Font;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Characterization tests for {@link ThemeData}.
 *
 * These tests pin down today's behaviour as the safety net for the
 * Phase 5.1 refactor (array/index hack -> enum/map). They are intentionally
 * narrow: they assert what {@code ThemeData} promises in its public API
 * (set/get round-trips, clone independence, identity comparison) and do not
 * claim anything about default-value resolution, which depends on the
 * current Look &amp; Feel.
 */
public class ThemeDataTest {

    @BeforeClass
    public void enableHeadless() {
        // ThemeData transitively touches Swing component defaults via its
        // DefaultColor / DefaultFont resolvers; force headless mode so the
        // tests do not depend on a display being available.
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    public void newThemeData_hasNoExplicitColorsSet() {
        ThemeData data = new ThemeData();
        for (int id = 0; id < ThemeData.COLOR_COUNT; id++) {
            assertFalse(data.isColorSet(id),
                    "Color id " + id + " must not be marked as set on a fresh ThemeData");
        }
    }

    @Test
    public void newThemeData_hasNoExplicitFontsSet() {
        ThemeData data = new ThemeData();
        for (int id = 0; id < ThemeData.FONT_COUNT; id++) {
            assertFalse(data.isFontSet(id),
                    "Font id " + id + " must not be marked as set on a fresh ThemeData");
        }
    }

    @Test
    public void setColor_thenGetColor_returnsSameInstance() {
        ThemeData data = new ThemeData();
        Color expected = new Color(0x12, 0x34, 0x56);

        boolean changed = data.setColor(ThemeData.FILE_FOREGROUND_COLOR, expected);

        assertTrue(changed, "setColor must report a change when the previous value differs");
        assertTrue(data.isColorSet(ThemeData.FILE_FOREGROUND_COLOR));
        assertSame(data.getColor(ThemeData.FILE_FOREGROUND_COLOR), expected);
    }

    @Test
    public void setFont_thenGetFont_returnsSameInstance() {
        ThemeData data = new ThemeData();
        Font expected = new Font(Font.MONOSPACED, Font.PLAIN, 13);

        boolean changed = data.setFont(ThemeData.EDITOR_FONT, expected);

        assertTrue(changed, "setFont must report a change when the previous value differs");
        assertTrue(data.isFontSet(ThemeData.EDITOR_FONT));
        assertSame(data.getFont(ThemeData.EDITOR_FONT), expected);
    }

    @Test
    public void setColor_withIdenticalValue_reportsNoChange() {
        ThemeData data = new ThemeData();
        Color color = new Color(10, 20, 30);

        data.setColor(ThemeData.FILE_FOREGROUND_COLOR, color);
        boolean changedAgain = data.setColor(ThemeData.FILE_FOREGROUND_COLOR, color);

        assertFalse(changedAgain,
                "setColor with the same value must report no change on the second call");
    }

    @Test
    public void setColor_thenSetNull_unsetsTheColor() {
        ThemeData data = new ThemeData();
        data.setColor(ThemeData.FILE_FOREGROUND_COLOR, new Color(1, 2, 3));
        assertTrue(data.isColorSet(ThemeData.FILE_FOREGROUND_COLOR));

        data.setColor(ThemeData.FILE_FOREGROUND_COLOR, null);

        assertFalse(data.isColorSet(ThemeData.FILE_FOREGROUND_COLOR),
                "setColor(null) must clear the explicit color override");
    }

    @Test
    public void cloneData_producesIndependentCopy() {
        ThemeData original = new ThemeData();
        Color originalColor = new Color(100, 100, 100);
        original.setColor(ThemeData.FILE_FOREGROUND_COLOR, originalColor);

        ThemeData clone = original.cloneData();
        assertNotSame(clone, original);
        assertSame(clone.getColor(ThemeData.FILE_FOREGROUND_COLOR), originalColor);

        clone.setColor(ThemeData.FILE_FOREGROUND_COLOR, new Color(200, 0, 0));

        assertSame(original.getColor(ThemeData.FILE_FOREGROUND_COLOR), originalColor,
                "Mutating the clone must not affect the original");
    }

    @Test
    public void isIdentical_emptyData_returnsTrue() {
        ThemeData a = new ThemeData();
        ThemeData b = new ThemeData();

        assertTrue(a.isIdentical(b, true),
                "Two empty ThemeData instances are identical when defaults are ignored");
    }

    @Test
    public void isIdentical_returnsFalse_whenOverrideDiffers() {
        ThemeData a = new ThemeData();
        ThemeData b = new ThemeData();
        a.setColor(ThemeData.FILE_FOREGROUND_COLOR, new Color(1, 2, 3));
        b.setColor(ThemeData.FILE_FOREGROUND_COLOR, new Color(9, 9, 9));

        assertFalse(a.isIdentical(b, true),
                "ThemeData instances with different explicit overrides must not be identical");
    }

    @Test
    public void importData_copiesExplicitOverrides() {
        ThemeData source = new ThemeData();
        Color expectedColor = new Color(0xAB, 0xCD, 0xEF);
        Font expectedFont = new Font(Font.SANS_SERIF, Font.BOLD, 11);
        source.setColor(ThemeData.FILE_FOREGROUND_COLOR, expectedColor);
        source.setFont(ThemeData.EDITOR_FONT, expectedFont);

        ThemeData target = new ThemeData();
        target.importData(source);

        assertTrue(target.isColorSet(ThemeData.FILE_FOREGROUND_COLOR));
        assertTrue(target.isFontSet(ThemeData.EDITOR_FONT));
        assertSame(target.getColor(ThemeData.FILE_FOREGROUND_COLOR), expectedColor);
        assertSame(target.getFont(ThemeData.EDITOR_FONT), expectedFont);
    }

    @Test
    public void isColorDifferent_reportsTrueOnDifferentRgb_falseOnSame() {
        ThemeData data = new ThemeData();
        Color stored = new Color(50, 60, 70);
        data.setColor(ThemeData.FILE_FOREGROUND_COLOR, stored);

        assertFalse(data.isColorDifferent(ThemeData.FILE_FOREGROUND_COLOR, stored, true));
        assertTrue(data.isColorDifferent(ThemeData.FILE_FOREGROUND_COLOR,
                new Color(99, 99, 99), true));
    }

    @Test
    public void fontAndColorCounts_matchPublishedConstants() {
        // Tripwire: if someone adds a font/color id without bumping the count
        // constants, the array-index hack silently breaks. Pin the contract
        // so the Phase 5.1 refactor can rely on these numbers.
        assertEquals(ThemeData.FONT_COUNT, 8);
        assertEquals(ThemeData.COLOR_COUNT, 72);

        ThemeData data = new ThemeData();
        for (int id = 0; id < ThemeData.FONT_COUNT; id++) {
            assertNotNull(data.getFont(id),
                    "getFont(" + id + ") must never return null per ThemeData contract");
        }
        for (int id = 0; id < ThemeData.COLOR_COUNT; id++) {
            assertNotNull(data.getColor(id),
                    "getColor(" + id + ") must never return null per ThemeData contract");
        }
    }
}
