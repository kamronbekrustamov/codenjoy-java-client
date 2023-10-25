package com.codenjoy.dojo.services.properties;

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