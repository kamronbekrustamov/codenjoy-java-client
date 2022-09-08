package com.codenjoy.dojo.utils;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

public class PrintUtils {

    public enum Color {
        TEXT(""),         // black background
        INFO("44;93"),    // blue --
        SUMMARY("42;93"), // green --
        WARNING("45;93"), // pink --
        ERROR("41;93");   // red --

        private final String color;

        Color(String color) {
            this.color = color;
        }

        public String color() {
            return color;
        }
    }

    public static void printf(String format, Color color, Object ... args) {
        format = String.format("\u001B[%sm%s\u001B[0m\n", color.color, format);
        System.out.printf(format, args);
    }
}
