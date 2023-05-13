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

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PrintUtils {

    private static int tab = 0;

    public enum Color {
        TEXT(""),          // black background
        INFO("44;97"),     // blue --
        INFO2("46;97"),    // light blue --
        INFO3("47;97"),    // light gray --
        SUMMARY("42;97"),  // green --
        WARNING("43;97"),  // yellow --
        WARNING2("45;97"), // pink --
        ERROR("41;97");    // red --

        private final String color;

        Color(String color) {
            this.color = color;
        }

        public String color() {
            return color;
        }
    }

    public static void printftab(Runnable runnable, String format, Color color, Object ... args) {
        printf(format, color, args);
        PrintUtils.tab();
        try {
            runnable.run();
        } finally {
            PrintUtils.untab();
        }
    }

    public static void printf(String format, Color color, Object ... args) {
        List<Object> list = Arrays.asList(args);
        String tabs = Strings.repeat("\t", tab);
        AtomicInteger index = new AtomicInteger(0);
        Arrays.stream(format.split("\n")).forEach(line -> {
                    int count = StringUtils.countMatches(line, "%s");
                    String colored = String.format("%s\033[%sm%s\033[0m\n",
                            tabs, color.color, line);
                    Object[] args1 = list.subList(index.get(), index.get() + count).toArray();
                    System.out.printf(colored, args1);
                    index.addAndGet(count);
                });
    }

    public static void tab() {
        tab++;
    }

    public static void untab() {
        tab--;
    }
}