package com.codenjoy.dojo.services.generator;

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

import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.utils.PrintUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.codenjoy.dojo.utils.PrintUtils.Color.ERROR;
import static com.codenjoy.dojo.utils.PrintUtils.Color.INFO;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class ElementGeneratorRunner {

    public static final String ALL_GAMES = "all";

    private static String base;
    private static String games;
    private static String clients;
    private static List<String> allGames;
    private static Locale locale;

    public static void main(String[] args) {
        System.out.println("+-----------------------------+");
        System.out.println("| Starting elements generator |");
        System.out.println("+-----------------------------+");

        allGames = games();
        if (args != null && args.length == 4) {
            base = args[0];
            games = args[1];
            clients = args[2];
            locale = Locale.forLanguageTag(args[3]);
            printInfo("Environment");
        } else {
            base = "";
            games = ALL_GAMES;
            clients = "md,md_header,md_footer,java,cpp,go,js,php,python,csharp";
            locale = Locale.ENGLISH;
            printInfo("Runner");
        }
        if (isAllGames()) {
            games = allGames.stream().collect(joining(","));
        }

        if (!new File(base).isAbsolute()) {
            base = new File(base).getAbsoluteFile().getPath();
            PrintUtils.printf("\t   absolute:'%s'",
                    INFO,
                    base);
        }

        if (!gamesSourcesPresent(base)) {
            PrintUtils.printf("Please run this script on a fully cloned project c with submodules (with --recursive option)\n" +
                            "    git clone --recursive https://github.com/codenjoyme/codenjoy.git",
                    ERROR,
                    base);
            return;
        }

        for (String game : games.split(",")) {
            System.out.println();
            if (!allGames.contains(game)) {
                PrintUtils.printf("Game not found: '%s'", ERROR, game);
                continue;
            }
            for (String language : clients.split(",")) {
                new ElementGenerator(game, language, locale, base).generateToFile();
            }
        }
    }

    private static boolean gamesSourcesPresent(String base) {
        File file = new File(base);
        while (!file.getName().equals("CodingDojo")) {
            file = file.getParentFile();
            if (file == null) {
                return false;
            }

        }
        return file.exists();
    }

    private static boolean isAllGames() {
        return ALL_GAMES.equalsIgnoreCase(games);
    }

    private static void printInfo(String source) {
        PrintUtils.printf(
                "Got from %s:\n" +
                "\t 'GAMES':   '%s'\n" +
                "\t 'CLIENTS': '%s'\n" +
                "\t 'BASE':    '%s'",
                INFO,
                source,
                isAllGames() ? "all=" + allGames : games,
                clients, base);
    }

    private static List<String> games() {
        String packageName = "com.codenjoy.dojo.games";
        return new Reflections(packageName).getSubTypesOf(CharElement.class).stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .filter(clazz -> !Modifier.isInterface(clazz.getModifiers()))
                .filter(clazz -> Modifier.isPublic(clazz.getModifiers()))
                .filter(clazz -> !clazz.toString().contains("test"))
                .map(Class::getCanonicalName)
                .map(name -> StringUtils.substringBetween(name,
                        "com.codenjoy.dojo.games.", ".Element"))
                .filter(Objects::nonNull)
                .map(ElementGenerator::getCanonicalGame)
                .sorted()
                .collect(toList());
    }
}
