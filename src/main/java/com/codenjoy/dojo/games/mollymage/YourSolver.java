package com.codenjoy.dojo.games.mollymage;

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

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: your name
 * <p>
 * This is your AI algorithm for the game.
 * Implement it at your own discretion.
 * Pay attention to {@link YourSolverTest} - there is
 * a test framework for you.
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    private static final Random random = new Random();

    private static int count = -1;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }


    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";
        Point myHero = board.getHero();
        List<Direction> directions = getDirectionsToMove(myHero);
        count++;
        if (directions.isEmpty()) {
            return Command.NONE;
        }
        if (count % 4 == 0) {
            count = 0;
            return Command.MOVE_THEN_DROP_POTION.apply(getRandom(directions));
        } else {
            return Command.MOVE.apply(getRandom(directions));
        }
    }


    public List<Direction> getDirectionsToMove(Point myHero) {
        Map<Point, Direction> directionMap = adjacentPoints(myHero);
        return directionMap.keySet().stream()
                .filter(point -> !board.isBarrierAt(point))
                .filter(point -> !board.isFutureBlastAt(point))
                .filter(point -> !board.isBlastAt(point))
                .filter(point -> !isThereLessThanTwoSecondBlastExpected(point))
                .filter(point -> !canGhostAppear(point))
                .map(directionMap::get).collect(Collectors.toList());
    }

    public Map<Point, Direction> adjacentPoints(Point myHero) {
        return Map.of(
                new PointImpl(myHero.getX() + 1, myHero.getY()), Direction.RIGHT,
                new PointImpl(myHero.getX() - 1, myHero.getY()), Direction.LEFT,
                new PointImpl(myHero.getX(), myHero.getY() + 1), Direction.UP,
                new PointImpl(myHero.getX(), myHero.getY() - 1), Direction.DOWN
        );
    }

    public Direction getRandom(List<Direction> directions) {
        return directions.get(random.nextInt(directions.size()));
    }

    public boolean canGhostAppear(Point point) {
        return adjacentPoints(point).keySet().stream()
                .anyMatch(board::isGhostAt);
    }

    public boolean isThereLessThanTwoSecondBlastExpected(Point point) {
        return Stream.of(point,
                        new PointImpl(point.getX() + 1, point.getY()),
                        new PointImpl(point.getX() + 2, point.getY()),
                        new PointImpl(point.getX() + 3, point.getY()),
                        new PointImpl(point.getX() - 1, point.getY()),
                        new PointImpl(point.getX() - 2, point.getY()),
                        new PointImpl(point.getX() - 3, point.getY()),
                        new PointImpl(point.getX(), point.getY() + 1),
                        new PointImpl(point.getX(), point.getY() + 2),
                        new PointImpl(point.getX(), point.getY() + 3),
                        new PointImpl(point.getX(), point.getY() - 1),
                        new PointImpl(point.getX(), point.getY() - 2),
                        new PointImpl(point.getX(), point.getY() - 3)
                )
                .anyMatch(point1 -> board.isAt(point1, Element.POTION_TIMER_2, Element.POTION_TIMER_1));
    }
}