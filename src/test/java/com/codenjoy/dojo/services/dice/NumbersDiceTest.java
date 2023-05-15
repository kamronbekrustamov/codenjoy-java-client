package com.codenjoy.dojo.services.dice;

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

import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.List;

import static com.codenjoy.dojo.services.dice.MockDiceTest.list;
import static com.codenjoy.dojo.services.dice.MockDiceTest.next;
import static org.junit.Assert.assertEquals;

public class NumbersDiceTest {

    @Test
    public void shouldReturnAllNumbers_thenDefaultValue() {
        // given
        NumbersDice dice = new NumbersDice(-1);
        dice.will(1, 2, 3, 4, 5);

        // when then
        assertEquals(1, dice.next(100));
        assertEquals(2, dice.next(100));
        assertEquals(3, dice.next(100));
        assertEquals(4, dice.next(100));
        assertEquals(5, dice.next(100));

        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
    }

    @Test
    public void shouldReturnAllNumbers_thenLastValue() {
        // given
        NumbersDice dice = new NumbersDice();
        dice.will(1, 2, 3, 4, 5);

        // when then
        assertEquals(1, dice.next(100));
        assertEquals(2, dice.next(100));
        assertEquals(3, dice.next(100));
        assertEquals(4, dice.next(100));
        assertEquals(5, dice.next(100));

        assertEquals(5, dice.next(100));
        assertEquals(5, dice.next(100));
        assertEquals(5, dice.next(100));
        assertEquals(5, dice.next(100));

        // when
        dice.will(6, 7, 8);

        // then
        assertEquals(6, dice.next(100));
        assertEquals(7, dice.next(100));
        assertEquals(8, dice.next(100));

        assertEquals(8, dice.next(100));
        assertEquals(8, dice.next(100));
        assertEquals(8, dice.next(100));
        assertEquals(8, dice.next(100));

        // when
        dice.will(9, 10);

        // then
        assertEquals(9, dice.next(100));
        assertEquals(10, dice.next(100));

        assertEquals(10, dice.next(100));
        assertEquals(10, dice.next(100));
        assertEquals(10, dice.next(100));
        assertEquals(10, dice.next(100));
    }

    @Test
    public void shouldReturnAllNumbers_thenDefaultValue_fromGenerator() {
        // given
        NumbersDice diceDefault = new NumbersDice(-1);
        diceDefault.will(6, 7, 8);

        NumbersDice dice = new NumbersDice(diceDefault::next);
        dice.will(1, 2, 3, 4, 5);

        // when then
        assertEquals(1, dice.next(100));
        assertEquals(2, dice.next(100));
        assertEquals(3, dice.next(100));
        assertEquals(4, dice.next(100));
        assertEquals(5, dice.next(100));

        assertEquals(6, dice.next(100));
        assertEquals(7, dice.next(100));
        assertEquals(8, dice.next(100));

        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
    }

    @Test
    public void shouldReturnAllNumbers_regenerateWhenCallWill() {
        // given
        NumbersDice dice = new NumbersDice(-1);
        dice.will(1, 2, 3, 4, 5);

        // then
        assertEquals(1, dice.next(100));
        assertEquals(2, dice.next(100));
        assertEquals(3, dice.next(100));
        assertEquals(4, dice.next(100));
        assertEquals(5, dice.next(100));

        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));

        // when
        dice.will(6, 7, 8);

        // then
        assertEquals(6, dice.next(100));
        assertEquals(7, dice.next(100));
        assertEquals(8, dice.next(100));

        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
    }

    @Test
    public void shouldReturnAllNumbers_thenDefaultValue_caseSupplier() {
        // given
        NumbersDice dice = new NumbersDice(-1);
        List<Integer> list = list(1, 2, 3, 4, 5);
        
        // when 
        dice.will(max -> {
            assertEquals(Integer.valueOf(100), max);
            return next(list);
        });

        // then
        assertEquals(1, dice.next(100));
        assertEquals(2, dice.next(100));
        assertEquals(3, dice.next(100));
        assertEquals(4, dice.next(100));
        assertEquals(5, dice.next(100));

        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
        assertEquals(-1, dice.next(100));
    }

    @Test
    public void shouldReturnDefaultValue_whenEmpty() {
        // given
        Dice dice = new NumbersDice(-100);

        // when then
        assertEquals(-100, dice.next(100));
        assertEquals(-100, dice.next(100));
        assertEquals(-100, dice.next(100));
        assertEquals(-100, dice.next(100));
    }
}