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

import java.util.function.Function;

public class NumbersDice implements Dice {

    private Function<Integer, Integer> now;
    private Function<Integer, Integer> then;

    private boolean repeat = false;
    private int current;

    public NumbersDice() {
        this.now = new Numbers();
        repeat = true;
    }

    public NumbersDice(int defaultValue) {
        this.now = new Numbers();
        this.then = n -> defaultValue;
    }

    public NumbersDice(Function<Integer, Integer> then) {
        this.now = new Numbers();
        this.then = then;
    }

    public void will(Integer... next) {
        this.now = new Numbers(next);
    }

    public void will(Function<Integer, Integer> next) {
        this.now = next;
    }

    @Override
    public int next(int max) {
        Integer value = now.apply(max);
        if (value == null) {
            if (repeat) {
                then = n -> current;
            }
            return current = then.apply(max);
        }
        return current = value;
    }
}