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

import com.codenjoy.dojo.utils.core.MockitoJunitTesting;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.codenjoy.dojo.services.generator.ElementGenerator.getBase;

public class SmokeUtils {

    public static final String SEP = "------------------------------------------";

    public static final String SOURCE_FOLDER = "src/test/resources/";
    public static final String TARGET_FOLDER = "target/";

    // если потребуется дополнительна проверка финального результата, используй это чудо
    public static Consumer<String> recheck;

    private static void assertEquals(Object o1, Object o2) {
        MockitoJunitTesting.testing().assertEquals(o1, o2);
    }

    /**
     * проверяем порционно, потому что в 'mvn test'
     * не видно на больших данных, где именно отличие и это проблема в отладке
     * @param allFirst true - если проверяем все сразу, false - если сперва порционно тик за тиком
     * @param expectedAll что должно быть
     * @param actualAll что реально пришло
     */
    public static void assertSmoke(boolean allFirst, String expectedAll, String actualAll) {
        String[] expected = expectedAll.split(SEP);
        String[] actual = actualAll.split(SEP);

        if (allFirst) {
            assertEquals(expectedAll, actualAll);
        }

        for (int i = 0; i < Math.min(expected.length, actual.length); i++) {
            assertEquals(expected[i], actual[i]);
        }
        assertEquals(expected.length, actual.length);

        if (!allFirst) {
            assertEquals(expectedAll, actualAll);
        }
    }

    public static void assertSmokeFile(String fileName, List<String> messages) {
        String actual = String.join("\n", messages);
        String expected;
        String filePath = fileName.startsWith("src") ? fileName : SOURCE_FOLDER + fileName;
        File expectedFile = new File(filePath);
        File actualFile = new File(TARGET_FOLDER + fileName);
        System.out.println("Expected data is here: " + expectedFile.getAbsolutePath());
        System.out.println("Actual data is here:   " + actualFile.getAbsolutePath());
        if (expectedFile.exists()) {
            expected = load(expectedFile);
            saveToFile(actualFile, actual);
        } else {
            expected = StringUtils.EMPTY;
            saveToFile(expectedFile, actual);
        }

        assertSmoke(true, expected, actual);
        if (recheck != null) {
            recheck.accept(actual);
        }
    }

    public static void assertSmokeEquals(String actual, Class owner, String methodName) {
        SmokeUtils.assertSmokeFile(owner.getSimpleName()
                        + "/" + methodName +  ".data",
                Arrays.asList(actual
                        .replace("\r\n", "\n")
                        .replace(getBase(), "<BASE>")
                        .split("\n")));
    }

    public static void saveToFile(File actualFile, String data) {
        File folder = actualFile.getParentFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (FileWriter writer = new FileWriter(actualFile.getAbsolutePath(), StandardCharsets.UTF_8)) {
            data = fixOSEndLine(data);
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String load(File file) {
        try {
            String data = Files.readString(file.toPath());
            data = fixOSEndLine(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    private static String fixOSEndLine(String data) {
        return data.replace(System.lineSeparator(), "\n");
    }
}
