package com.codenjoy.dojo.utils;

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

import com.codenjoy.dojo.services.generator.ElementGenerator;
import com.codenjoy.dojo.services.printer.CharElement;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class GamesUtils {

    public static List<String> games() {
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
