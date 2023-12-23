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
    private static long lastSeenTime = System.nanoTime();

    public YourSolver(Dice dice) {
        this.dice = dice;
    }


    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";

        System.out.println("Time: " + (System.nanoTime() - lastSeenTime)/ 1_000_000);
        lastSeenTime = System.nanoTime();

        Point myHero = board.getHero();

        Map<Point, Direction> pointsMap = adjacentPoints(myHero);

        boolean isStuck = isPointSurroundedByBarriers(myHero, myHero);
        if (isStuck) {
            return Command.DROP_POTION;
        }

        List<Direction> safestDirections = getSafestDirections(myHero);
        if (!safestDirections.isEmpty()) {
            List<Direction> list = choosePoints(pointsMap, safestDirections);
            if (list.isEmpty()) {
                return Command.MOVE.apply(getRandom(safestDirections));
            } else {
                return Command.DROP_POTION_THEN_MOVE.apply(getRandom(list));
            }
        }
        List<Direction> secondSafestDirections = getSecondSafestDirections(myHero);
        if (!secondSafestDirections.isEmpty()) {
            List<Direction> list = choosePoints(pointsMap, secondSafestDirections);
            if (list.isEmpty()) {
                return Command.MOVE.apply(getRandom(secondSafestDirections));
            } else {
                return Command.DROP_POTION_THEN_MOVE.apply(getRandom(list));
            }
        }

        List<Direction> safestDirectionsWithoutGhostChecking = getSafestDirectionsWithoutGhostChecking(myHero);
        if (!safestDirectionsWithoutGhostChecking.isEmpty()) {
            List<Direction> list = choosePoints(pointsMap, safestDirectionsWithoutGhostChecking);
            if (list.isEmpty()) {
                return Command.MOVE.apply(getRandom(safestDirectionsWithoutGhostChecking));
            } else {
                return Command.DROP_POTION_THEN_MOVE.apply(getRandom(list));
            }
        }

        List<Direction> secondSafestDirectionsWithoutGhostChecking = getSecondSafestDirectionsWithoutGhostChecking(myHero);
        if (!secondSafestDirectionsWithoutGhostChecking.isEmpty()) {
            List<Direction> list = choosePoints(pointsMap, secondSafestDirectionsWithoutGhostChecking);
            if (list.isEmpty()) {
                return Command.MOVE.apply(getRandom(secondSafestDirectionsWithoutGhostChecking));
            } else {
                return Command.DROP_POTION_THEN_MOVE.apply(getRandom(list));
            }
        }

        List<Direction> thirdSafestDirections = getThirdSafestDirections(myHero);
        if (!thirdSafestDirections.isEmpty()) {
            List<Direction> list = choosePoints(pointsMap, thirdSafestDirections);
            if (list.isEmpty()) {
                return Command.MOVE.apply(getRandom(thirdSafestDirections));
            } else {
                return Command.DROP_POTION_THEN_MOVE.apply(getRandom(list));
            }
        }

        List<Direction> thirdSafestDirectionsWithoutGhostChecking = getThirdSafestDirectionsWithoutGhostChecking(myHero);
        if (!thirdSafestDirectionsWithoutGhostChecking.isEmpty()) {
            List<Direction> list = choosePoints(pointsMap, thirdSafestDirectionsWithoutGhostChecking);
            if (list.isEmpty()) {
                return Command.MOVE.apply(getRandom(thirdSafestDirectionsWithoutGhostChecking));
            } else {
                return Command.DROP_POTION_THEN_MOVE.apply(getRandom(list));
            }
        }

        return Command.NONE;
    }


    public List<Direction> getSafestDirections(Point myHero) {
        Map<Point, Direction> directionMap = adjacentPoints(myHero);
        return directionMap.keySet().stream()
                .filter(point -> !board.isBarrierAt(point))
                .filter(point -> !board.isFutureBlastAt(point))
                .filter(point -> !board.isBlastAt(point))
                .filter(point -> !lessThanThreeSecondBlastExpected(point))
                .filter(point -> !canGhostAppear(point))
                .map(directionMap::get).collect(Collectors.toList());
    }

    public List<Direction> getSafestDirectionsWithoutGhostChecking(Point myHero) {
        Map<Point, Direction> directionMap = adjacentPoints(myHero);
        return directionMap.keySet().stream()
                .filter(point -> !board.isBarrierAt(point))
                .filter(point -> !board.isFutureBlastAt(point))
                .filter(point -> !board.isBlastAt(point))
                .filter(point -> !lessThanThreeSecondBlastExpected(point))
                .map(directionMap::get).collect(Collectors.toList());
    }

    public List<Direction> getSecondSafestDirections(Point myHero) {
        Map<Point, Direction> directionMap = adjacentPoints(myHero);
        return directionMap.keySet().stream()
                .filter(point -> !board.isBarrierAt(point))
                .filter(point -> !board.isFutureBlastAt(point))
                .filter(point -> !board.isBlastAt(point))
                .filter(point -> !lessThanTwoSecondBlastExpected(point))
                .filter(point -> !canGhostAppear(point))
                .map(directionMap::get).collect(Collectors.toList());
    }

    public List<Direction> getSecondSafestDirectionsWithoutGhostChecking(Point myHero) {
        Map<Point, Direction> directionMap = adjacentPoints(myHero);
        return directionMap.keySet().stream()
                .filter(point -> !board.isBarrierAt(point))
                .filter(point -> !board.isFutureBlastAt(point))
                .filter(point -> !board.isBlastAt(point))
                .filter(point -> !lessThanTwoSecondBlastExpected(point))
                .map(directionMap::get).collect(Collectors.toList());
    }

    public List<Direction> getThirdSafestDirections(Point myHero) {
        Map<Point, Direction> directionMap = adjacentPoints(myHero);
        return directionMap.keySet().stream()
                .filter(point -> !board.isBarrierAt(point))
                .filter(point -> !board.isFutureBlastAt(point))
                .filter(point -> !board.isBlastAt(point))
                .filter(point -> !lessThanTwoSecondBlastExpected(point))
                .filter(point -> !canGhostAppear(point))
                .map(directionMap::get).collect(Collectors.toList());
    }

    public List<Direction> getThirdSafestDirectionsWithoutGhostChecking(Point myHero) {
        Map<Point, Direction> directionMap = adjacentPoints(myHero);
        return directionMap.keySet().stream()
                .filter(point -> !board.isBarrierAt(point))
                .filter(point -> !board.isFutureBlastAt(point))
                .filter(point -> !board.isBlastAt(point))
                .filter(point -> !lessThanOneSecondBlastExpected(point))
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
//        int x = 11, y = 11;
//        int heroX = board.getHero().getX(), heroY = board.getHero().getY();
//        Map<Point, Direction> pointDirectionMap = adjacentPoints(board.getHero());
//        for (Point p : pointDirectionMap.keySet()) {
//
//        }
        return directions.get(random.nextInt(directions.size()));
    }

    public boolean canGhostAppear(Point point) {
        return adjacentPoints(point).keySet().stream()
                .anyMatch(board::isGhostAt);
    }

    public boolean lessThanOneSecondBlastExpected(Point point) {
        return get3PointsInAllDirections(point).stream()
                .anyMatch(point1 -> board.isAt(point1, Element.POTION_TIMER_1));
    }

    public boolean lessThanTwoSecondBlastExpected(Point point) {
        return get3PointsInAllDirections(point).stream()
                .anyMatch(point1 -> board.isAt(point1, Element.POTION_TIMER_1, Element.POTION_TIMER_2));
    }

    public boolean lessThanThreeSecondBlastExpected(Point point) {
        return get3PointsInAllDirections(point).stream()
                .anyMatch(point1 -> board.isAt(point1, Element.POTION_TIMER_1, Element.POTION_TIMER_2, Element.POTION_TIMER_3));
    }

    public List<Point> get3PointsInAllDirections(Point point) {
        return List.of(point,
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
        );
    }

    private boolean isPointSurroundedByBarriers(Point point, Point myHero) {
        return adjacentPoints(point).keySet().stream()
                .filter(p -> !p.equals(myHero))
                .allMatch(board::isBarrierAt);
    }
    private List<Direction> choosePoints(Map<Point, Direction> pointDirectionMap, List<Direction> directions) {
        return pointDirectionMap.keySet().stream()
                .filter(p -> directions.contains(pointDirectionMap.get(p)))
                .filter(p -> !isPointSurroundedByBarriers(p, board.getHero()))
                .map(pointDirectionMap::get)
                .collect(Collectors.toList());
    }
}