package com.codenjoy.dojo.client.runner;

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