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

import com.codenjoy.dojo.utils.GamesUtils;
import com.codenjoy.dojo.utils.PrintUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    private static List<Locale> locales;

    public static void main(String[] args) {
        System.out.println("+-----------------------------+");
        System.out.println("| Starting elements generator |");
        System.out.println("+-----------------------------+");

        allGames = GamesUtils.games();
        if (args != null && args.length == 4) {
            base = args[0];
            games = args[1];
            clients = args[2];
            locales = localesFor(args[3].split(","));
            printInfo("Environment");
        } else {
            base = "";
            games = ALL_GAMES;
            clients = "md,md_header,md_footer,java,cpp,go,js,php,python,csharp";
            locales = localesFor("en", "ru");
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
                for (Locale locale : filter(locales, language)) {
                    new ElementGenerator(game, language, locale, locales, base).generateToFile();
                }
            }
        }
    }

    // для всех языков по умолчанию берется только первая локаль в списке,
    // а для elements.md мы генерим для всех выбранных локалей
    // TODO как-то это костыльно, подумать на досуге
    private static List<Locale> filter(List<Locale> locales, String language) {
        if (language.equals("md")) {
            return locales;
        }

        return Arrays.asList(locales.get(0));
    }

    public static List<Locale> localesFor(String... codes) {
        return Arrays.stream(codes)
                .map(Locale::forLanguageTag)
                .collect(toList());
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
                "\t 'LOCALES': '%s'\n" +
                "\t 'BASE':    '%s'",
                INFO,
                source,
                isAllGames() ? "all=" + allGames : games,
                clients, locales, base);
    }
}
