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

import com.codenjoy.dojo.games.sample.Element;
import com.codenjoy.dojo.services.generator.language.Go;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.services.properties.GameProperties;
import com.codenjoy.dojo.utils.PrintUtils;
import com.codenjoy.dojo.utils.SmokeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.function.Function;

import static com.codenjoy.dojo.services.properties.GameProperties.getGame;
import static com.codenjoy.dojo.utils.FilePathUtils.normalize;
import static com.codenjoy.dojo.utils.PrintUtils.Color.ERROR;
import static com.codenjoy.dojo.utils.PrintUtils.Color.TEXT;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class ElementGenerator {

    public static final int COMMENT_MAX_LENGTH = 60;
    public static final String PROJECT_BASE_FOLDER = "CodingDojo";

    // используется для тестирования, этим флагом отключаем реальное сохранение файлов
    public static boolean READONLY = false;

    public static final List<String> DIFFERENT_NAME_GAMES = Arrays.asList();
    public static final GameProperties gameProperties = new GameProperties();

    private final String game;
    private final String canonicalGame;

    private final String language;
    private final Template template;
    private final Locale locale;
    private final List<Locale> locales;
    private String base;

    public ElementGenerator(String game, String language, Locale locale, List<Locale> locales, String inputBase) {
        this.canonicalGame = game;
        this.game = getGame(game);
        base = getBase(inputBase);
        this.locale = locale;
        this.locales = locales.stream()
                .map(it -> hasProperties(game, it) ? it : null)
                .filter(Objects::nonNull)
                .collect(toList());

        this.language = language;
        this.template = template();
    }

    private boolean hasProperties(String game, Locale locale) {
        return GameProperties.has(base, locale, game);
    }

    /**
     * Разные способы запуска (из IDE, с bash) дают нам разный контекст.
     * Но алгоритм хочет видеть тут одну и ту же папку.
     * Потому этот метод необходим для унифицирования base папки.
     * @param inputBase любая папка (в том числе внутри CodingDojo).
     * @return Если в пути содержится CodingDojo, то путь будет сокращен до этой папки,
     * иначе вернется без изменения.
     */
    public static String getBase(String inputBase) {
        if (!inputBase.contains(PROJECT_BASE_FOLDER)) {
            return inputBase;
        }
        File absolute = new File(inputBase).getAbsoluteFile();
        while (absolute != null
                && !absolute.getName().equals(PROJECT_BASE_FOLDER))
        {
            absolute = absolute.getParentFile();
        }
        return normalize(absolute.getAbsolutePath());
    }

    public static String getBase() {
        return getBase(new File(".").getAbsolutePath());
    }

    public static String getCanonicalGame(String game) {
        for (String canonicalName : DIFFERENT_NAME_GAMES) {
            if (game.equals(getGame(canonicalName))) {
                return canonicalName;
            }
        }
        return game;
    }

    public String generate() {
        try {
            return build(elements());
        } catch (Exception exception) {
            PrintUtils.printf(
                    "Error during generate: [game=%s, language=%s, locale=%s]\n" +
                    "With exception:        [%s]\n" +
                    "Skipped!",
                    ERROR,
                    game, language, locale, exception.getMessage());
            return null;
        }
    }

    public void generateToFile() {
        File dest = new File(base + "/clients/" + replace(template.file()));
        PrintUtils.printftab(() -> generate(dest),
                "Store '%s:%s:%s' in file: '%s'", TEXT,
                game, language, locale, normalize(dest.getAbsolutePath()));
    }

    private void generate(File dest) {
        // TODO пока не закончу переносить полезные методы с icancode/elemtnt.js
        //      не удалять эту строчку
        if (game.equals("icancode") && language.equals("js")) {
            return;
        }

        String data = generate();

        if (!READONLY && data != null) {
            SmokeUtils.saveToFile(dest, data);
        }
    }

    private CharElement[] elements() {
        try {
            String className = Element.class.getCanonicalName().replace("sample", game);

            return (CharElement[]) getClass().getClassLoader().loadClass(className)
                    .getEnumConstants();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Template template() {
        try {
            String className = Go.class.getPackageName() + "."
                    + capitalize(language);

            Class<?> clazz = getClass().getClassLoader().loadClass(className);
            return (Template) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String build(CharElement[] elements) {
        Template template = template();

        String header = replace(template.header(locales));

        Map<CharElement, String> infos = loadInfo(elements);

        List<String> lines = Arrays.stream(elements)
                .map(el -> replace(template.line(), el, infos))
                .collect(toList());

        List<String> infosList = new LinkedList<>(infos.values());
        StringBuilder body = new StringBuilder();
        for (int index = 0; index < lines.size(); index++) {
            if (template.printComment()) {
                List<String> comments = splitLength(infosList.get(index), COMMENT_MAX_LENGTH);
                if (!comments.isEmpty()) {
                    comments.forEach(comment ->
                            body.append('\n')
                                    .append(template.comment())
                                    .append(comment));
                    if (template.printNewLine()) {
                        body.append('\n');
                    }
                }
            }

            if (template.printLines()) {
                if (template.printNewLine()) {
                    body.append('\n');
                }
                String line = lines.get(index);
                if (template.lastDelimiter() != null && index == lines.size() - 1) {
                    int count = (line.charAt(line.length() - 1) == '\n') ? 2 : 1;
                    line = line.substring(0, line.length() - count)
                            + template.lastDelimiter();
                }
                body.append(line);
            }
        }

        String footer = replace(template.footer());

        return header
                + body
                + footer;
    }

    private Map<CharElement, String> loadInfo(CharElement[] elements) {
        if (!gameProperties.load(base, locale, canonicalGame)) {
            throw new RuntimeException(String.format(
                    "Cant load properties file: [game=%s, language=%s]",
                    game, language));
        }

        return loadInfo(elements, element ->
                gameProperties.get(element.name()));
    }

    private Map<CharElement, String> loadInfo(CharElement[] elements, Function<CharElement, String> getInfo) {
        return Arrays.stream(elements)
                .map(element -> new AbstractMap.SimpleEntry<>(element, getInfo.apply(element)))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (value1, value2) -> value2,
                        LinkedHashMap::new));
    }

    private String replace(String template, CharElement element, Map<CharElement, String> infos) {
        return replace(template)
                .replace("${element-lower}", element.name().toLowerCase())
                .replace("${element}", element.name())
                .replace("${char}", String.valueOf(element.ch()))
                .replace("${info}", infos.get(element));
    }

    private String replace(String template) {
        return GameProperties
                .replace(template, canonicalGame)
                .replace("${tag}", "#" + "%L") // because of warning in the mvn compile in phase lecense header generation
                .replace("${language}", language)
                .replace("${locale}", locale.getLanguage());
    }

    private List<String> splitLength(String text, int length) {
        return new LinkedList<>(){{
            if (StringUtils.isNotEmpty(text)) {
                List<String> words = new LinkedList<>(Arrays.asList(text.split(" ")));

                String line = "";
                while (!words.isEmpty()) {
                    if ((line + " " + words.get(0)).length() <= length) {
                        if (StringUtils.isNotEmpty(line)) {
                            line += " ";
                        }
                        line += words.remove(0);
                    } else {
                        add(line);
                        line = "";
                    }
                }
                add(line);
            }
        }};
    }
}
