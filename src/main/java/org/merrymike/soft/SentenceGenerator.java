package org.merrymike.soft;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class SentenceGenerator {
    private final List<String> sentences = new ArrayList<>();
    private final ResponseHandler responseHandler = new ResponseHandler();

    public List<String> getSentences() {
        JSONObject jsonObject = responseHandler.getResponse();

        String sentence = jsonObject.getJSONArray("results").getJSONObject(0).getString("text");
        JSONArray translationsArray = jsonObject.getJSONArray("results").getJSONObject(0).optJSONArray("translations");

        JSONObject translationObject = null;
        int arrayIndex = 0;
        while (translationObject == null) {
            translationObject = translationsArray.getJSONArray(arrayIndex).optJSONObject(0);
            arrayIndex++;
        }
        String translation = translationObject.getString("text");

        sentences.clear();
        sentences.add(sentence);
        sentences.add(translation);
        return sentences;
    }

}
