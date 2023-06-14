package com.codenjoy.dojo.utils;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FilePathUtilsTest {

    @Test
    public void shouldNormalisePath_whenWindowsPath() {
        assertEquals("path/to/file", FilePathUtils.normalize("path\\to\\file"));
    }

    @Test
    public void shouldNormalisePath_whenUnixPath() {
        assertEquals("path/to/file", FilePathUtils.normalize("path/to/file"));
    }

    @Test
    public void shouldNormalisePath_whenPathIsNull() {
        assertNull(FilePathUtils.normalize(null));
    }
}