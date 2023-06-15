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

import com.codenjoy.dojo.utils.FilePathUtils;
import com.codenjoy.dojo.utils.GamesUtils;
import com.codenjoy.dojo.utils.PrintUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.codenjoy.dojo.services.generator.ElementGenerator.PROJECT_BASE_FOLDER;
import static com.codenjoy.dojo.utils.PrintUtils.Color.*;
import static java.util.stream.Collectors.toList;

public class ElementGeneratorRunner {

    public static final String ALL = "all";
    private static List<String> ALL_GAMES = GamesUtils.games();
    private static List<String> ALL_LOCALES = Arrays.asList("en", "ru");
    private static List<String> ALL_CLIENTS = Arrays.asList("md", "md_header", "md_footer", "java", "cpp", "go", "js", "php", "python", "csharp");

    private static String base;
    private static String games;
    private static String clients;
    private static String locales;

    public static void main(String[] args) {
        System.out.println("+-----------------------------+");
        System.out.println("| Starting elements generator |");
        System.out.println("+-----------------------------+");

        if (args != null && args.length == 4) {
            base = args[0];
            games = args[1];
            clients = args[2];
            locales = args[3];
            printInfo("Environment", INFO);
        } else {
            base = "";
            games = ALL;
            clients = ALL;
            locales = ALL;
            printInfo("Runner", INFO);
        }
        games = decodeAll(games, ALL_GAMES);
        locales = decodeAll(locales, ALL_LOCALES);
        clients = decodeAll(clients, ALL_CLIENTS);
        base = makeAbsolute(base);
        printInfo("Processed", TEXT);

        if (!gamesSourcesPresent(base)) {
            pleaseRunInAllProject();
            return;
        }

        for (String game : games.split(",")) {
            System.out.println();
            PrintUtils.printftab(() -> generate(game),
                    "Generating elements for game '%s'", INFO, game);
        }
    }

    private static void generate(String game) {
        if (!ALL_GAMES.contains(game)) {
            PrintUtils.printf("Game not found: '%s'", ERROR, game);
            return;
        }

        for (String language : clients.split(",")) {
            for (Locale locale : filter(locales, language)) {
                new ElementGenerator(game, language, locale, localesFor(locales), base).generateToFile();
            }
        }
    }

    public static String makeAbsolute(String input) {
        File path = new File(input);
        if (path.isAbsolute()) {
            return input;
        }
        return FilePathUtils.normalize(path.getAbsoluteFile().getPath());
    }

    public static String decodeAll(String list, List<String> all) {
        return ALL.equalsIgnoreCase(list)
                ? String.join(",", all)
                : list;
    }

    public static void pleaseRunInAllProject() {
        PrintUtils.printf("Please run this script on a fully cloned project c with submodules (with --recursive option)\n" +
                        "    git clone --recursive https://github.com/codenjoyme/codenjoy.git",
                ERROR,
                base);
    }

    // для всех языков по умолчанию берется только первая локаль в списке,
    // а для elements.md мы генерим для всех выбранных локалей
    // TODO как-то это костыльно, подумать на досуге
    private static List<Locale> filter(String localesString, String language) {
        List<Locale> locales = localesFor(localesString);
        if (language.equals("md")) {
            return locales;
        }

        return Arrays.asList(locales.get(0));
    }

    public static List<Locale> localesFor(String codes) {
        return Arrays.stream(codes.split(","))
                .map(Locale::forLanguageTag)
                .collect(toList());
    }

    public static boolean gamesSourcesPresent(String base) {
        File file = new File(base);
        while (!file.getName().equals(PROJECT_BASE_FOLDER)) {
            file = file.getParentFile();
            if (file == null) {
                return false;
            }

        }
        return file.exists();
    }

    private static void printInfo(String source, PrintUtils.Color color) {
        PrintUtils.printf(
                "Got from %s:\n" +
                "\t 'GAMES':   '%s'\n" +
                "\t 'CLIENTS': '%s'\n" +
                "\t 'LOCALES': '%s'\n" +
                "\t 'BASE':    '%s'",
                color,
                source,
                games,
                clients,
                locales,
                base);
    }
}
