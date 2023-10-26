package com.codenjoy.dojo.services.properties;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2023 Codenjoy
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

import java.util.*;

/**
 * Нужен только для сохранения порядка properties в файле.
 * Возможно не все методы реализованы.
 */
public class LinkedProperties extends Properties {

    private final Map<Object, Object> map = new LinkedHashMap<>();

    @Override
    public synchronized Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public synchronized boolean contains(Object value) {
        return map.containsValue(value);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public synchronized Enumeration<Object> elements() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized Set<Map.Entry<Object, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public synchronized Object get(Object key) {
        return map.get(key);
    }

    @Override
    public synchronized Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public synchronized int size() {
        return map.size();
    }

    @Override
    public synchronized Object clone() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
        map.putAll(t);
    }

    @Override
    public synchronized Set<Object> keySet() {
        return map.keySet();
    }

    @Override
    public synchronized Collection<Object> values() {
        return map.values();
    }

    @Override
    public synchronized String toString() {
        return map.toString();
    }

    @Override
    public synchronized boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public String getProperty(String key) {
        return (String) map.get(key);
    }

    public Map<String, String> asMap() {
        return new LinkedHashMap<>() {{
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                put((String) entry.getKey(), (String) entry.getValue());
            }
        }};
    }
}
