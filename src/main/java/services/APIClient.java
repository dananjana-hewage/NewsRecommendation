package services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIClient {

    private final String apiKey;
    private static final String API_URL = "https://api.bing.microsoft.com/v7.0/news/search";

    public APIClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<JSONObject> fetchArticlesByCategory(String category) throws Exception {
        String queryUrl = API_URL + "?q=" + category + "&count=50&mkt=en-US";
        URL url = new URL(queryUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Ocp-Apim-Subscription-Key", apiKey);
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray articles = jsonResponse.getJSONArray("value");

        List<JSONObject> result = new ArrayList<>();
        for (int i = 0; i < articles.length(); i++) {
            result.add(articles.getJSONObject(i));
        }
        return result;
    }
}
