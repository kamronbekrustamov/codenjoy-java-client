package com.codenjoy.dojo.services.generator.language;

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

import com.codenjoy.dojo.services.generator.Template;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class Md_header implements Template {

    @Override
    public String header(List<Locale> locales) {
        return "<!--\n" +
                "  ${tag}\n" +
                "  Codenjoy - it's a dojo-like platform from developers to developers.\n" +
                "  %%\n" +
                "  Copyright (C) 2012 - 2022 Codenjoy\n" +
                "  %%\n" +
                "  This program is free software: you can redistribute it and/or modify\n" +
                "  it under the terms of the GNU General Public License as\n" +
                "  published by the Free Software Foundation, either version 3 of the\n" +
                "  License, or (at your option) any later version.\n" +
                "  \n" +
                "  This program is distributed in the hope that it will be useful,\n" +
                "  but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                "  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                "  GNU General Public License for more details.\n" +
                "  \n" +
                "  You should have received a copy of the GNU General Public\n" +
                "  License along with this program.  If not, see\n" +
                "  <http://www.gnu.org/licenses/gpl-3.0.html>.\n" +
                "  #L%\n" +
                "  -->\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "        <title>${game-capitalize} codenjoy — как играть? | Игры на работе</title>\n" +
                "        <base href=\"\" target=\"_self\">\n" +
                "        <link href=\"../../../resources/css/all.min.css\" media=\"all\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "        <link href=\"../../../resources/css/custom.css\" rel=\"stylesheet\">\n" +
                "\n" +
                "        <script src=\"../../../resources/js/all.min.js\"></script>\n" +
                "    </head>\n" +
                "    <body style=\"background-color: white;\"\n" +
                "          class=\"single single-post postid-170 single-format-standard logged-in admin-bar singular one-column content customize-support\">\n" +
                "       <div id=\"settings\" page=\"rules\"></div>\n" +
                "       <a href=\"https://github.com/codenjoyme/codenjoy\"><img style=\"position: absolute; top: 0; right: 0; border: 0;z-index: 100;\" src=\"../../../resources/img/fork-me.png\" alt=\"Fork me on GitHub\"></a>\n" +
                "       <div id=\"page\" class=\"hfeed\">\n" +
                "           <div id=\"main\">\n" +
                "               <div id=\"primary\">\n" +
                "                   <div id=\"content\" role=\"main\">\n" +
                "                       <header class=\"entry-header\">\n" +
                "                           <h1 class=\"entry-title\">${game-capitalize} codenjoy — как играть?&nbsp;\n" +
            process(locales,
                "                               <a href=\"/codenjoy-contest/manual/${game}/${lng}\">[${lng-upper}]</a>\n"
            ) +
                "                           </h1>\n" +
                "                       </header>\n" +
                "                       <div class=\"entry-content\">\n" +
                "                           <div class=\"page-restrict-output\">\n";
    }

    private String process(List<Locale> locales, String template) {
        if (locales.size() == 1) {
            return StringUtils.EMPTY;
        }
            
        return locales.stream()
                .map(Locale::getLanguage)
                .map(language -> template
                        .replace("${lng}", language)
                        .replace("${lng-upper}", language.toUpperCase()))
                .collect(joining(""));
    }

    @Override
    public boolean printComment() {
        return false;
    }

    @Override
    public boolean printNewLine() {
        return false;
    }

    @Override
    public boolean printLines() {
        return false;
    }

    @Override
    public String file() {
        return "../games/${game-canonical}/src/main/webapp/resources/${game}/help/header.html";
    }
}
