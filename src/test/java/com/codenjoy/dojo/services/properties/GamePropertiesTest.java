package com.codenjoy.dojo.services.properties;

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

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class GamePropertiesTest {

    @Test
    public void shouldGetKey_whenExists() {
        assertEquals("[Score] Win score",
                GameProperties.get("", Locale.ENGLISH, "test", "WIN_SCORE"));

        assertEquals("[Score] Lose penalty",
                GameProperties.get("", Locale.ENGLISH, "test", "LOSE_PENALTY"));
    }

    @Test
    public void shouldGetAllKeys_withSortingOrder() {
        // given
        GameProperties properties = new GameProperties();

        // when
        properties.load("", Locale.ENGLISH, "test");

        // then
        assertEquals("{game.test.element.NONE=Short comment.,\n" +
                        "game.test.element.WALL=Long long long long long long long long long long long looooooooong long long long long long long long long long long comment.,\n" +
                        "game.test.element.HERO=Another short comment.,\n" +
                        "game.test.element.OTHER_HERO=One more time.,\n" +
                        "game.test.element.HERO_DEAD=,\n" +
                        "game.test.element.OTHER_HERO_DEAD_LONG_LONG_LONG_LONG_LONG=Long name.,\n" +
                        "game.test.element.G=Short name.,\n" +
                        "game.test.settings.WIN_SCORE=[Score] Win score,\n" +
                        "game.test.settings.LOSE_PENALTY=[Score] Lose penalty,\n" +
                        "game.test.settings.SIZE=[Level] Size}",
                properties.properties().toString().replace(", ", ",\n"));
    }

    @Test
    public void shouldHasGameProperties_whenExists() {
        assertEquals(true, GameProperties.has("", Locale.ENGLISH, "test"));
    }

    @Test
    public void shouldHasGameProperties_whenNonExists() {
        assertEquals(false, GameProperties.has("", Locale.ENGLISH, "non-exists-game"));
    }

    @Test
    public void shouldCantLoadGameProperty_whenGameNotExists() {
        try {
            GameProperties.get("", Locale.ENGLISH, "non-exists-game", "WIN_SCORE");
            fail();
        } catch (Exception e) {
            assertEquals("Cant load properties file for game: non-exists-game",
                    e.getMessage());
        }
    }

    @Test
    public void shouldCantLoadGameProperty_whenGamePropertyNotExists() {
        try {
            GameProperties.get("", Locale.ENGLISH, "test", "NON_EXISTS_PROPERTY");
            fail();
        } catch (Exception e) {
            assertEquals("Key not found for either element or setting: " +
                            "[name=NON_EXISTS_PROPERTY, game=test]. " +
                            "Please check that you have defined it in the " +
                            "info.properties file in the resources folder of the game.",
                    e.getMessage());
        }
    }
}