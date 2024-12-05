package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APIClient {

    private static final String BING_API_KEY = "4bab0bdf2abf424abedd86eeb1a607f9";
    private static final String BING_API_URL = "https://api.bing.microsoft.com/v7.0/news/search?q=technology&count=10";

    public static JSONArray fetchArticles() {
        try {
            URL url = new URL(BING_API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", BING_API_KEY);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            StringBuilder jsonResponse = new StringBuilder();
            while (scanner.hasNext()) {
                jsonResponse.append(scanner.nextLine());
            }

            JSONObject responseObject = new JSONObject(jsonResponse.toString());
            return responseObject.getJSONArray("value"); // Get articles
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
