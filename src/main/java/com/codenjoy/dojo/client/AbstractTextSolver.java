package com.codenjoy.dojo.client;

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


import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.client.Command.SKIP_THIS_LEVEL;
import static com.codenjoy.dojo.client.Command.START_NEXT_LEVEL;

public abstract class AbstractTextSolver<T> implements Solver<AbstractTextBoard> {

    private AbstractTextBoard board;
    protected JSONObject data;
    protected QuestionsJsonParser parser = new QuestionsJsonParser() {
        @Override
        public int level(JSONObject data) {
            return data.getInt("level");
        }

        @Override
        public List<String> questions(JSONObject data) {
            return Utils.getStrings(data.getJSONArray("questions"));
        }
    };

    public abstract Strings getAnswers(int level, Strings questions);

    @Override
    public String get(AbstractTextBoard board) {
        this.board = board;
        if (board.isGameOver()) return "";

        data = new JSONObject(board.getData());
        int level = parser.level(data);
        List<String> questions = parser.questions(data);

        Strings answers = getAnswers(level, new Strings(questions));
        String answersString = answers.toString();

        if (answers.size() == 1) { // TODO подумать над этим
            String command = answers.iterator().next();
            if (Arrays.asList(START_NEXT_LEVEL, SKIP_THIS_LEVEL).contains(command)) {
                answersString = command;
            }
        }

        return String.format("message('%s')", answersString);
    }
}