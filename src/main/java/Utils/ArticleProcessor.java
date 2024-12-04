package Utils;

import database.DatabaseManager;
import org.json.JSONArray;
import org.json.JSONObject;
import services.StanfordNlpCategorizer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArticleProcessor {

    private static final String API_URL = "https://api.bing.microsoft.com/v7.0/news/search";
    private static final String API_KEY = "4bab0bdf2abf424abedd86eeb1a607f9";

    public static void fetchAndProcessArticles() {
        try {
            StanfordNlpCategorizer categorizer = new StanfordNlpCategorizer();

            // Fetch articles from Bing API
            String queryUrl = API_URL + "?count=50&mkt=en-US"; // Fetch all articles
            URL url = new URL(queryUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the API response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("value");

            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);

                String title = article.optString("name", "No Title");
                String description = article.optString("description", "");
                String imageUrl = article.optJSONObject("image").optString("thumbnailUrl", "");

                // Combine title and description for categorization
                String combinedText = title + " " + description;

                // Categorize article using Stanford NLP
                String category = categorizer.categorize(combinedText);

                if (category != null) {
                    // Save article to database if categorized
                    DatabaseManager.iud("INSERT INTO articles (title, image_url, category) VALUES ('" +
                            title.replace("'", "''") + "', '" +
                            imageUrl + "', '" +
                            category + "')");

                    System.out.println("Article saved: " + title + " | Category: " + category);
                } else {
                    System.out.println("Uncategorized article skipped: " + title);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

