package database;

import Utils.APIClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class ArticleLoader {

    public static void saveArticlesToDatabase() {
        try {
            JSONArray articles = APIClient.fetchArticles();
            if (articles != null) {
                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String title = article.getString("title");
                    String imageUrl = article.optString("urlToImage", "");

                    // Determine category_id (example logic)
                    int categoryId = determineCategoryId(title);

                    // Insert into database
                    String query = "INSERT INTO articles (title, image_url, category_id) VALUES ('" +
                            title.replace("'", "''") + "', '" +
                            imageUrl.replace("'", "''") + "', " +
                            categoryId + ")";
                    DatabaseManager.iud(query);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static int determineCategoryId(String title) {
        if (title.toLowerCase().contains("sports")) {
            return 1; // Sports category ID
        } else if (title.toLowerCase().contains("technology")) {
            return 2; // Technology category ID
        } else if (title.toLowerCase().contains("politics")) {
            return 3; // Politics category ID
        } else {
            return 4; // Default category ID
        }
    }


}
