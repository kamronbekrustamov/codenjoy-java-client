package com.codenjoy.dojo.client.runner;

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

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.dice.RandomDice;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectLoader {

    private static final String PRIMARY_SOLVER_NAME = "YourSolver";
    private static final String PRIMARY_BOARD_NAME = "Board";

    public static Solver loadJavaSolver(String game) {
        return loadSolver(game, "java");
    }

    public static Solver loadScalaSolver(String game) {
        return loadSolver(game, "scala");
    }

    public static Solver loadKotlinSolver(String game) {
        return loadSolver(game, "kotlin");
    }

    public static Solver loadSolver(String game, String language) {
        try {
            return (Solver) load(Solver.class, game, language, PRIMARY_SOLVER_NAME)
                    .getDeclaredConstructor(Dice.class)
                    .newInstance(new RandomDice());
        } catch (Exception e) {
            String message = "Error loading Solver for: " + game;
            throw new RuntimeException(message, e);
        }
    }

    public static ClientBoard loadJavaBoard(String game) {
        return loadBoard(game, "java");
    }

    public static ClientBoard loadScalaBoard(String game) {
        return loadBoard(game, "scala");
    }

    public static ClientBoard loadKotlinBoard(String game) {
        return loadBoard(game, "kotlin");
    }

    public static ClientBoard loadBoard(String game, String language) {
        try {
            return (ClientBoard) load(ClientBoard.class, game, language, PRIMARY_BOARD_NAME)
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            String message = "Error loading Board for: " + game;
            throw new RuntimeException(message, e);
        }
    }

    private static Class<?> load(Class<?> type, String game, String language, String primarySimpleName) {
        String packageName = String.format("com.codenjoy.dojo.games.%s", game);
        List<Class<?>> candidates = new Reflections(packageName).getSubTypesOf(type).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .filter(clazz -> !Modifier.isInterface(clazz.getModifiers()))
                .filter(clazz -> Modifier.isPublic(clazz.getModifiers()))
                .filter(clazz -> clazz.getCanonicalName().contains(String.format(".%s.", game)))
                .filter(clazz -> filterClazzByJvmLanguage(clazz, language))
                .sorted(Comparator.comparing(Class::getSimpleName))
                .collect(Collectors.toList());
        if (candidates.isEmpty()) {
            throw new NoSuchElementException(type.getSimpleName() + " not found for: " + game);
        }
        return candidates.stream()
                .filter(clazz -> clazz.getSimpleName().equals(primarySimpleName))
                .findFirst().orElseGet(() -> candidates.get(0));
    }

    private static boolean filterClazzByJvmLanguage(Class<?> clazz, String language) {
        Optional<Language> langAnnotation = Arrays.stream(clazz.getDeclaredAnnotations())
                .filter(a -> a.annotationType().equals(Language.class))
                .map(a -> (Language) a)
                .findFirst();
        if ("java".equals(language) && langAnnotation.isEmpty()) {
            return true;
        }
        return langAnnotation.isPresent() && langAnnotation.get().value().equals(language);
    }
}
