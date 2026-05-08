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

/**
 * Pure helper for {@link Application#createErrorMessage}.
 *
 * <p>Concentrates the four-flag formatting rule (warning prefix on quit,
 * exception details suppressed in silent mode) so it can be unit-tested
 * without touching the activator singleton or system streams.
 */
final class ApplicationErrorMessage {

    private ApplicationErrorMessage() {
        // utility
    }

    /**
     * Formats an error message string from its inputs.
     *
     * <p>Format rules (matching the historical inline behaviour):
     * <ul>
     *   <li>If {@code quit} is true, the message starts with {@code "Warning: "}.</li>
     *   <li>If {@code silent} is false and {@code exception} is non-null, the
     *       exception's message is appended after a colon and a space.</li>
     *   <li>{@code silent=true} suppresses the exception details — used by
     *       headless/CI flags so terminal output stays terse.</li>
     * </ul>
     *
     * @param msg       the human-readable error message; treated as opaque
     * @param exception optional exception to include in the output; may be null
     * @param quit      whether the application is about to quit (drives the prefix)
     * @param silent    whether to suppress the exception details
     */
    static String format(String msg, Exception exception, boolean quit, boolean silent) {
        StringBuilder error = new StringBuilder();
        if (quit) {
            error.append("Warning: ");
        }
        error.append(msg);
        if (!silent && exception != null) {
            error.append(": ").append(exception.getMessage());
        }
        return error.toString();
    }
}
