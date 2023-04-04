package com.codenjoy.dojo.services.properties;

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