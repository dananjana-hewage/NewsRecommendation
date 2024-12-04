package Utils;

import database.DatabaseManager;
import org.json.JSONObject;
import services.APIClient;
import services.CategoryManager;
import services.StanfordNlpCategorizer;

import java.util.List;

public class ArticleProcessor {

    private static final String API_KEY = "4bab0bdf2abf424abedd86eeb1a607f9";

    public static void fetchAndProcessArticles() {
        try {
            APIClient apiClient = new APIClient(API_KEY);
            StanfordNlpCategorizer categorizer = new StanfordNlpCategorizer();

            String[] categories = {"Technology", "Health", "Sports", "AI"};

            // Insert categories into the categories table if they don't exist
            for (String category : categories) {
                if (!CategoryManager.categoryExists(category)) {
                    String insertCategoryQuery = "INSERT INTO categories (name) VALUES ('" + category + "')";
                    DatabaseManager.iud(insertCategoryQuery);
                    System.out.println("Category " + category + " added to the database.");
                }
            }

            for (String category : categories) {
                // Store the category in the categories table if it doesn't already exist
                //String checkCategoryQuery = "SELECT * FROM categories WHERE name = '" + category + "'";
                List<JSONObject> articles = apiClient.fetchArticlesByCategory(category);

                // Insert the category into the database if it doesn't exist
                for (JSONObject article : articles) {
                    String title = article.optString("name", "No Title");
                    String description = article.optString("description", "");
                    String imageUrl = article.optJSONObject("image").optString("thumbnailUrl", "");

                    String combinedText = title + " " + description;
                    String assignedCategory = categorizer.categorize(combinedText);

                    if (assignedCategory != null) {
                        String insertArticleQuery = "INSERT INTO articles (title, image_url, category) VALUES ('" +
                                title.replace("'", "''") + "', '" +
                                imageUrl + "', '" +
                                assignedCategory + "')";
                        DatabaseManager.iud(insertArticleQuery);
                        System.out.println("Saved: " + title + " | Category: " + assignedCategory);
                    } else {
                        System.out.println("Uncategorized article skipped: " + title);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
