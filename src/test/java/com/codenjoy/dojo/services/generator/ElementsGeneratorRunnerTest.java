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

import com.codenjoy.dojo.utils.RedirectOutput;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static com.codenjoy.dojo.services.generator.ElementGeneratorTest.base;
import static com.codenjoy.dojo.services.generator.ElementGeneratorTest.skipTestWarning;
import static com.codenjoy.dojo.utils.SmokeUtils.assertSmokeEquals;

public class ElementsGeneratorRunnerTest {

    @Rule
    public TestName test = new TestName();

    private RedirectOutput output = new RedirectOutput();

    @Test
    public void shouldGenerate_allGames_andLanguages() {
        // given
        output.redirect();
        ElementGenerator.READONLY = true;

        try {
            // when
            ElementGeneratorRunner.main(new String[0]);

            // then
            String actual = output.toString();
            output.rollback();
            if (base("") == null) {
                skipTestWarning();
                return;
            }
            assertEquals(actual);
        } finally {
            // when maven run tests it uses same test class instance for each test
            ElementGenerator.READONLY = false;
        }
    }

    private void assertEquals(String actual) {
        assertSmokeEquals(actual, getClass(), test.getMethodName());
    }
}
