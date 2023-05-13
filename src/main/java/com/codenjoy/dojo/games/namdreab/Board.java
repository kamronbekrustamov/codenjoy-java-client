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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.games.namdreab.Element.*;

/**
 * Класс, обрабатывающий строковое представление доски.
 * Содержит ряд унаследованных методов {@link AbstractBoard},
 * но ты можешь добавить сюда любые свои методы на их основе.
 */
public class Board extends AbstractBoard<Element> {

    @Override
    public Element[] elements() {
        return Element.values();
    }

    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y,
                ROCK,
                START_SPOT,
                ENEMY_HERO_SLEEP,
                ENEMY_HERO_TAIL_INACTIVE,
                HERO_TAIL_INACTIVE);
    }

    public boolean isHeroHeadAt(Point pt) {
        return isAt(pt, ElementUtils.heroHead);
    }

    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    public Point getHeroHead() {
        return getFirst(ElementUtils.heroHead);
    }

    public boolean isGameOver() {
        return getHeroHead() == null;
    }

    public boolean isAcornAt(int x, int y) {
        return isAt(x, y, ACORN);
    }
}
