package com.codenjoy.dojo.services;

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


import static com.codenjoy.dojo.services.PointImpl.pt;

public class LengthToXY {

    private int size;

    public LengthToXY(int size) {
        this.size = size;
    }

    public Point point(int length) {
        if (length == -1) {
            return null;
        }
        return pt(length % size, size - 1 - length / size);
    }

    public int length(int x, int y) {
        return (size - 1 - y) * size + x;
    }

    public static class Map { // TODO test me

        private LengthToXY xy;
        private char[] map;
        private int size;

        public Map(String map) {
            this.map = map.toCharArray();
            size = (int) Math.sqrt(map.length());
            xy = new LengthToXY(size);
        }

        public Map(int size) {
            map = new char[size * size];
            xy = new LengthToXY(size);
        }

        public int getSize() {
            return size;
        }

        public char getAt(int x, int y) {
            int length = xy.length(x, y);
            return map[length];
        }

        public char setAt(int x, int y, char ch) {
            int length = xy.length(x, y);
            char old = map[length];
            map[length] = ch;
            return old;
        }

        public boolean isOutOf(int x, int y) {
            return Point.isOutOf(x, y, size);
        }

        public String map() {
            return new String(map);
        }
    }
}
