package Utils;

import models.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class APIClient {

    private static final String API_KEY = "37aa9c63ea4c47d48ec3302481f1b760";
    private static final String API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;

    public static JSONArray fetchArticles() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");

            return articles;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
