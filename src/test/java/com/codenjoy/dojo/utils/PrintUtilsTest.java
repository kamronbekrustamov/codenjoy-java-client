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


import org.junit.Test;

import static com.codenjoy.dojo.utils.PrintUtils.Color.*;
import static org.junit.Assert.assertEquals;

public class PrintUtilsTest {

    private RedirectOutput output = new RedirectOutput();

    public void assertPrint(Runnable runnable, String expected) {
        output.redirect();
        try {
            runnable.run();
            assertEquals(expected, output.toString());
        } finally {
            output.rollback();
        }
    }

    @Test
    public void shouldPrint_whenOneLine_withoutParameters() {
        assertPrint(() -> PrintUtils.printf("Line", INFO),
                "\u001B[44;97mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_whenTwoLines_withoutParameters() {
        assertPrint(() -> PrintUtils.printf("Line1\nLine2", INFO),
                "\u001B[44;97mLine1\u001B[0m\n" +
                "\u001B[44;97mLine2\u001B[0m\n");
    }

    @Test
    public void shouldPrint_colorError() {
        assertPrint(() -> PrintUtils.printf("Line", ERROR),
                "\u001B[41;97mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_colorWarning() {
        assertPrint(() -> PrintUtils.printf("Line", WARNING),
                "\u001B[43;97mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_colorWarning2() {
        assertPrint(() -> PrintUtils.printf("Line", WARNING2),
                "\u001B[45;97mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_colorSummary() {
        assertPrint(() -> PrintUtils.printf("Line", SUMMARY),
                "\u001B[42;97mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_colorInfo() {
        assertPrint(() -> PrintUtils.printf("Line", INFO),
                "\u001B[44;97mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_colorInfo2() {
        assertPrint(() -> PrintUtils.printf("Line", INFO2),
                "\u001B[46;97mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_colorInfo3() {
        assertPrint(() -> PrintUtils.printf("Line", INFO3),
                "\u001B[47;97mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_colorText() {
        assertPrint(() -> PrintUtils.printf("Line", TEXT),
                "\u001B[mLine\u001B[0m\n");
    }

    @Test
    public void shouldPrint_whenOneLine_withParameters_parameterInTheEndOfLine() {
        assertPrint(() -> PrintUtils.printf("Line %s", INFO, "param1"),
                "\u001B[44;97mLine param1\u001B[0m\n");
    }

    @Test
    public void shouldPrint_whenOneLine_withParameters_parameterInTheStartOfLine() {
        assertPrint(() -> PrintUtils.printf("%s Line", INFO, "param1"),
                "\u001B[44;97mparam1 Line\u001B[0m\n");
    }

    @Test
    public void shouldPrint_whenThreeLines_withParameters_allParametersInFirstLine() {
        assertPrint(() -> PrintUtils.printf("Line1 [%s-%s-%s]\nLine2\nLine3", INFO,
                                            "param1", "param2", "param3"),
                "\u001B[44;97mLine1 [param1-param2-param3]\u001B[0m\n" +
                "\u001B[44;97mLine2\u001B[0m\n" +
                "\u001B[44;97mLine3\u001B[0m\n");
    }

    @Test
    public void shouldPrint_whenThreeLines_withParameters_allParametersInLastLine() {
        assertPrint(() -> PrintUtils.printf("Line1\nLine2\nLine3 [%s-%s-%s]", INFO,
                                            "param1", "param2", "param3"),
                "\u001B[44;97mLine1\u001B[0m\n" +
                "\u001B[44;97mLine2\u001B[0m\n" +
                "\u001B[44;97mLine3 [param1-param2-param3]\u001B[0m\n");
    }

    @Test
    public void shouldPrint_whenThreeLines_withParameters_eachLineHasParameters() {
        assertPrint(() -> PrintUtils.printf("Line1 [%s]\nLine2 [%s-%s]\nLine3 [%s-%s-%s]", INFO,
                                            "param1", "param2", "param3", "param4", "param5", "param6"),
                "\u001B[44;97mLine1 [param1]\u001B[0m\n" +
                "\u001B[44;97mLine2 [param2-param3]\u001B[0m\n" +
                "\u001B[44;97mLine3 [param4-param5-param6]\u001B[0m\n");
    }

    @Test
    public void shouldPrint_withTabs() {
        assertPrint(() -> {
            PrintUtils.printf("Line1", INFO);
            PrintUtils.tab();
            try {
                PrintUtils.printf("Line2 [%s]", TEXT, "param1");
                PrintUtils.tab();
                try {
                    PrintUtils.printf("Line3 [%s]\nLine4 [%s]", ERROR,
                                        "param2", "param3");
                } finally {
                    PrintUtils.untab();
                }
            } finally {
                PrintUtils.untab();
            }
        },
                "\u001B[44;97mLine1\u001B[0m\n" +
                "\t\u001B[mLine2 [param1]\u001B[0m\n" +
                "\t\t\u001B[41;97mLine3 [param2]\u001B[0m\n" +
                "\t\t\u001B[41;97mLine4 [param3]\u001B[0m\n");
    }

    @Test
    public void shouldPrint_withTabs_caseSugarMethod() {
        assertPrint(() -> PrintUtils.printftab(this::doIt, "Line1", INFO),
                "\u001B[44;97mLine1\u001B[0m\n" +
                        "\t\u001B[mLine2 [param1]\u001B[0m\n" +
                        "\t\t\u001B[41;97mLine3 [param2]\u001B[0m\n" +
                        "\t\t\u001B[41;97mLine4 [param3]\u001B[0m\n");
    }

    private void doIt() {
        PrintUtils.printftab(this::doIt2,
                "Line2 [%s]", TEXT, "param1");
    }

    private void doIt2() {
        PrintUtils.printf("Line3 [%s]\nLine4 [%s]", ERROR,
                "param2", "param3");
    }
}