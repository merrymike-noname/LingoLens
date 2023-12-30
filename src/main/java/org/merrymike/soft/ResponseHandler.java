package org.merrymike.soft;


import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;

public class ResponseHandler {
    private static ResponseHandler responseHandler;
    private final Properties properties = PropertiesManager.getProperties();
    private final Random random = new Random();

    public static synchronized ResponseHandler getResponseHandler() {
        if (responseHandler == null) {
            responseHandler = new ResponseHandler();
        }
        return responseHandler;
    }

    private ResponseHandler(){

    }

    private int getRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public JSONObject getResponse() {

        StringBuilder urlRequest = new StringBuilder();

        int wordsAmount = getRandom(Integer.parseInt(properties.getProperty("min")), Integer.parseInt(properties.getProperty("max")));
        int maxPages = checkAvailableAmount(wordsAmount);
        int page = getRandom(1, maxPages);
        urlRequest.append("https://dev.tatoeba.org/uk/api_v0/search?from=").append(properties.getProperty("from"))
                .append("&native=&orphans=no&query=&sort_reverse=&tags=&to=").append(properties.getProperty("to")).append("&trans_filter=limit")
                .append("&trans_link=&trans_orphan=")
                .append("&trans_user=&unapproved=no&user=&word_count_max=").append(properties.getProperty("max"))
                .append("&word_count_min=").append(wordsAmount).append("&page=").append(page).append("&sort=random");

        JSONObject jsonObject = null;
        try {
            URL url = new URL(urlRequest.toString());
            String json = IOUtils.toString(url, StandardCharsets.UTF_8);
            jsonObject = new JSONObject(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private int checkAvailableAmount(int wordsAmount) {
        StringBuilder urlRequest = new StringBuilder();
        urlRequest.append("https://dev.tatoeba.org/uk/api_v0/search?from=").append(properties.getProperty("from"))
                .append("&native=&orphans=no&query=&sort_reverse=&tags=&to=").append(properties.getProperty("to")).append("&trans_filter=limit")
                .append("&trans_link=&trans_orphan=")
                .append("&trans_user=&unapproved=no&user=&word_count_max=").append(properties.getProperty("max")).append("&word_count_min=")
                .append(wordsAmount).append("&page=").append(1).append("&sort=random");

        int maxPages = 30;
        try {
            URL url = new URL(urlRequest.toString());
            String json = IOUtils.toString(url, StandardCharsets.UTF_8);
            maxPages = new JSONObject(json).getJSONObject("paging").getJSONObject("Sentences").getInt("pageCount");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return maxPages;
    }
}
