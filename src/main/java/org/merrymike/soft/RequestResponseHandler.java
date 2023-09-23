package org.merrymike.soft;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RequestResponseHandler {
    private static RequestResponseHandler requestResponseHandler;
    private final Gson gson;
    private final Random random;
    private final int bufferSize = 8192;

    public static synchronized RequestResponseHandler getRequestResponseHandler() {
        if (requestResponseHandler == null) {
            requestResponseHandler = new RequestResponseHandler();
        }
        return requestResponseHandler;
    }

    private RequestResponseHandler() {
        gson = new Gson();
        random = new Random();
    }

    public Map<String, Object> jsonToMap(String input) {
        Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
        return gson.fromJson(input, type);
    }

    private Map<String, Object> getFullResponseMap() {
        String urlRequest = "https://dev.tatoeba.org/uk/api_v0/search?from=eng&has_audio=" +
                "&native=&orphans=no&query=&sort_reverse=&tags=&to=rus&trans_filter=limit" +
                "&trans_has_audio=&trans_link=&trans_orphan=&trans_to=rus&trans_unapproved=" +
                "&trans_user=&unapproved=no&user=&word_count_max=&word_count_min="
                + getRandom(10) + "&page=" + getRandom(100) + "&sort=relevance";
        StringBuilder response = getResponse(urlRequest);
        return jsonToMap(response.toString());
    }

    private StringBuilder getResponse(String urlRequest) {
        StringBuilder response = new StringBuilder(bufferSize);
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlRequest);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Accept-Encoding", "gzip");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), bufferSize);
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }

    public List<String> getSentences() {
        List<Map<String, Object>> results = (List<Map<String, Object>>) getFullResponseMap().get("results");
        int randomSentenceIndex = Integer.parseInt(getRandom(10)) - 1;
        List<String> sentences = new ArrayList<>(results.size());
        sentences.add(results.get(randomSentenceIndex).get("text").toString());
        List<List<Map<String, Object>>> allTranslations =
                (List<List<Map<String, Object>>>) results.get(randomSentenceIndex).get("translations");
        results = null;
        for (List<Map<String, Object>> allTranslation : allTranslations) {
            if (!allTranslation.isEmpty()) {
                sentences.add(allTranslation.get(0).get("text").toString());
            }
        }
        allTranslations = null;
        System.gc();
        return sentences;
    }

    private String getRandom(int cap) {
        return String.valueOf(random.nextInt(cap) + 1);
    }
}
