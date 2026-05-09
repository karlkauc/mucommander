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


package com.mucommander.commons.file.util;

import java.util.Set;

public class WindowsFilenameSanitizer {

    private static final Set<Character> ILLEGAL_CHARS = Set.of(
            '<', '>', ':', '"', '/', '\\', '|', '?', '*', '`'
    );

    public static String sanitizeFileName(String input) {
        StringBuilder sanitized = new StringBuilder(input.length());

        for (int i = 0, len = input.length(); i < len; i++) {
            char c = input.charAt(i);
            if (ILLEGAL_CHARS.contains(c)) {
                sanitized.append('_');
            } else {
                sanitized.append(c);
            }
        }

        return sanitized.toString();
    }

}
