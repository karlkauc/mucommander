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
package com.mucommander;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Pure-function tests for {@link ApplicationErrorMessage#format}.
 *
 * Pin the four-flag formatting truth table so a future caller can rely on
 * the exact string the historical inline code produced.
 */
public class ApplicationErrorMessageTest {

    @Test
    public void quit_noException_addsWarningPrefix() {
        assertEquals(
                ApplicationErrorMessage.format("config not found", null, true, false),
                "Warning: config not found");
    }

    @Test
    public void noQuit_noException_isJustTheMessage() {
        assertEquals(
                ApplicationErrorMessage.format("config not found", null, false, false),
                "config not found");
    }

    @Test
    public void noQuit_withException_appendsExceptionMessage() {
        Exception ex = new RuntimeException("permission denied");
        assertEquals(
                ApplicationErrorMessage.format("config not found", ex, false, false),
                "config not found: permission denied");
    }

    @Test
    public void quit_withException_warningPrefixAndExceptionTail() {
        Exception ex = new RuntimeException("permission denied");
        assertEquals(
                ApplicationErrorMessage.format("config not found", ex, true, false),
                "Warning: config not found: permission denied");
    }

    @Test
    public void silent_suppressesExceptionDetails() {
        // Same as previous case but with silent=true — exception tail goes away.
        Exception ex = new RuntimeException("permission denied");
        assertEquals(
                ApplicationErrorMessage.format("config not found", ex, true, true),
                "Warning: config not found");
    }

    @Test
    public void silent_withNullException_isUnaffected() {
        // The silent flag is irrelevant when there's no exception to suppress.
        assertEquals(
                ApplicationErrorMessage.format("msg", null, false, true),
                "msg");
        assertEquals(
                ApplicationErrorMessage.format("msg", null, true, true),
                "Warning: msg");
    }

    @Test
    public void emptyMessage_isPreserved() {
        // The function doesn't second-guess its inputs.
        assertEquals(ApplicationErrorMessage.format("", null, false, false), "");
        assertEquals(ApplicationErrorMessage.format("", null, true, false), "Warning: ");
    }
}
