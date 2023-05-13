package com.codenjoy.dojo.games.namdreab;

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

import com.codenjoy.dojo.services.Direction;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.util.Arrays;

import static com.codenjoy.dojo.games.namdreab.Element.*;
import static com.codenjoy.dojo.services.Direction.*;

public class ElementUtils {

    public static Multimap<Direction, Element> parts = LinkedHashMultimap.create();

    private static void part(Element element, Direction... directions) {
        Arrays.stream(directions)
                .forEach(direction -> {
                    parts.put(direction, element);
                    parts.put(direction, ElementUtils.enemyHero(element));
                });
    }

    static {
        part(HERO_DOWN,             DOWN);
        part(HERO_LEFT,             LEFT);
        part(HERO_RIGHT,            RIGHT);
        part(HERO_UP,               UP);
        part(HERO_DEAD,             UP, DOWN, LEFT, RIGHT);
        part(HERO_EVIL,             UP, DOWN, LEFT, RIGHT);
        part(HERO_FLY,              UP, DOWN, LEFT, RIGHT);
        part(HERO_SLEEP,            UP, DOWN, LEFT, RIGHT);

        part(HERO_BEARD_HORIZONTAL, LEFT, RIGHT);
        part(HERO_BEARD_VERTICAL,   UP, DOWN);
        part(HERO_BEARD_LEFT_DOWN,  RIGHT, UP);
        part(HERO_BEARD_LEFT_UP,    RIGHT, DOWN);
        part(HERO_BEARD_RIGHT_DOWN, LEFT, UP);
        part(HERO_BEARD_RIGHT_UP,   LEFT, DOWN);

        part(HERO_TAIL_DOWN,        DOWN);
        part(HERO_TAIL_LEFT,        LEFT);
        part(HERO_TAIL_UP,          UP);
        part(HERO_TAIL_RIGHT,       RIGHT);
        part(HERO_TAIL_INACTIVE,    UP, DOWN, LEFT, RIGHT);
    }

    public static final Element[] heroHead = new Element[]{
            HERO_DOWN,
            HERO_LEFT,
            HERO_RIGHT,
            HERO_UP,
            HERO_SLEEP,
            HERO_EVIL,
            HERO_FLY
    };

    public static boolean isFly(Element element) {
        return element == HERO_FLY
                || element == ENEMY_HERO_FLY;
    }

    public static boolean isEvil(Element element) {
        return element == HERO_EVIL
                || element == ENEMY_HERO_EVIL;
    }

    public static Element tail(TailDirection direction) {
        switch (direction) {
            case VERTICAL_DOWN:    return HERO_TAIL_DOWN;
            case VERTICAL_UP:      return HERO_TAIL_UP;
            case HORIZONTAL_LEFT:  return HERO_TAIL_LEFT;
            case HORIZONTAL_RIGHT: return HERO_TAIL_RIGHT;
        }
        throw new IllegalArgumentException("Bad direction: " + direction);
    }
    public static Element head(Direction direction) {
        switch (direction) {
            case DOWN:  return HERO_DOWN;
            case UP:    return HERO_UP;
            case LEFT:  return HERO_LEFT;
            case RIGHT: return HERO_RIGHT;
        }
        throw new IllegalArgumentException("Bad direction: " + direction);
    }

    public static Element beard(BodyDirection direction) {
        switch (direction) {
            case HORIZONTAL:        return HERO_BEARD_HORIZONTAL;
            case VERTICAL:          return HERO_BEARD_VERTICAL;
            case TURNED_LEFT_DOWN:  return HERO_BEARD_LEFT_DOWN;
            case TURNED_LEFT_UP:    return HERO_BEARD_LEFT_UP;
            case TURNED_RIGHT_DOWN: return HERO_BEARD_RIGHT_DOWN;
            case TURNED_RIGHT_UP:   return HERO_BEARD_RIGHT_UP;
        }
        throw new IllegalArgumentException("Bad direction: " + direction);
    }

    public static Element enemyHero(Element element) {
        switch (element) {
            case HERO_DOWN:             return ENEMY_HERO_DOWN;
            case HERO_LEFT:             return ENEMY_HERO_LEFT;
            case HERO_RIGHT:            return ENEMY_HERO_RIGHT;
            case HERO_UP:               return ENEMY_HERO_UP;
            case HERO_DEAD:             return ENEMY_HERO_DEAD;
            case HERO_EVIL:             return ENEMY_HERO_EVIL;
            case HERO_FLY:              return ENEMY_HERO_FLY;
            case HERO_SLEEP:            return ENEMY_HERO_SLEEP;
            case HERO_BEARD_HORIZONTAL: return ENEMY_HERO_BEARD_HORIZONTAL;
            case HERO_BEARD_VERTICAL:   return ENEMY_HERO_BEARD_VERTICAL;
            case HERO_BEARD_LEFT_DOWN:  return ENEMY_HERO_BEARD_LEFT_DOWN;
            case HERO_BEARD_LEFT_UP:    return ENEMY_HERO_BEARD_LEFT_UP;
            case HERO_BEARD_RIGHT_DOWN: return ENEMY_HERO_BEARD_RIGHT_DOWN;
            case HERO_BEARD_RIGHT_UP:   return ENEMY_HERO_BEARD_RIGHT_UP;
            case HERO_TAIL_DOWN:        return ENEMY_HERO_TAIL_DOWN;
            case HERO_TAIL_LEFT:        return ENEMY_HERO_TAIL_LEFT;
            case HERO_TAIL_UP:          return ENEMY_HERO_TAIL_UP;
            case HERO_TAIL_RIGHT:       return ENEMY_HERO_TAIL_RIGHT;
            case HERO_TAIL_INACTIVE:    return ENEMY_HERO_TAIL_INACTIVE;
        }
        throw new IllegalArgumentException("Bad hero state: " + element);
    }
}