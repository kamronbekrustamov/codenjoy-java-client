package com.codenjoy.dojo.client;

import org.json.JSONObject;

import java.util.List;

public interface QuestionsJsonParser {

    int level(JSONObject data);

    List<String> questions(JSONObject data);
}
