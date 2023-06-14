package com.codenjoy.dojo.utils;

public class FilePathUtils {

    public static String normalize(String path) {
        return path == null ? null : path.replace("\\", "/");
    }
}
