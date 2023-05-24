package com.codenjoy.dojo.client.runner;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2023 Codenjoy
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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ReflectLoaderTest {

    private static final String GAME1 = "test";
    private static final String GAME2 = "testanother";

    public static final String PACKAGE = "com.codenjoy.dojo.games.";

    @Parameterized.Parameters(name = "{index}: {0} {1}")
    public static Object[][] data() {
        return new Object[][]{
                {GAME1, "java"},
                {GAME2, "java"},

                {GAME1, "kotlin"},
                {GAME2, "kotlin"},

                {GAME1, "scala"},
                {GAME2, "scala"},
        };
    }

    @Parameterized.Parameter(0)
    public String game;

    @Parameterized.Parameter(1)
    public String language;

    @Test
    public void testBoard() {
        assertBoard(game, language);
    }

    @Test
    public void testSolver() {
        assertSolver(game, language);
    }

    private String classSuffix(String language) {
        if (language.equals("java")) {
            return "";
        }
        return StringUtils.capitalize(language);
    }

    private void assertBoard(String game, String language) {
        assertObject(game, language, "Board",
                ReflectLoader.loadBoard(game, language));
    }

    private void assertSolver(String game, String language) {
        assertObject(game, language, "YourSolver",
                ReflectLoader.loadSolver(game, language));
    }

    private void assertObject(String game, String language, String name, Object object) {
        String expected = PACKAGE + game + "." + name + classSuffix(language);
        assertEquals(expected, object.getClass().getName());
    }
}
