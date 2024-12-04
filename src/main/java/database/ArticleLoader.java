//package database;
//
//import Utils.APIClient;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.sql.ResultSet;
//
//public class ArticleLoader {
//
//    // Method to fetch and store articles
//    public void loadArticles(String query, int count) {
//        // Fetch the data from the API using APIClient
//        JSONObject response = APIClient.fetchArticlesFromAPI(query, count);
//        if (response != null) {
//            // Parse the articles array from the API response
//            JSONArray articlesArray = response.getJSONArray("value");
//
//            // Loop through each article and store it in the database
//            for (int i = 0; i < articlesArray.length(); i++) {
//                JSONObject article = articlesArray.getJSONObject(i);
//                String title = article.getString("name");
//                String imageUrl = article.getJSONObject("image").getJSONObject("thumbnail").getString("contentUrl");
//                String category = article.optString("category", "Other");  // Default to "Other" if no category is found
//
//                // Save the category to the database and get the category_id
//                int categoryId = getCategoryId(category);
//
//                // Insert the article into the database
//                String queryInsert = "INSERT INTO articles (title, image_url, category_id) VALUES (?, ?, ?)";
//                DatabaseManager.iud(queryInsert, title, imageUrl, categoryId);
//            }
//        }
//    }
//
//    // Method to get the category ID from the database (or insert a new category if not found)
//    private int getCategoryId(String category) {
//        try {
//            // Check if the category exists in the database
//            ResultSet rs = DatabaseManager.search("SELECT category_id FROM categories WHERE category_name = '" + category + "'");
//            if (rs.next()) {
//                return rs.getInt("category_id");
//            } else {
//                // Insert the new category if it doesn't exist
//                String insertCategoryQuery = "INSERT INTO categories (category_name) VALUES (?)";
//                DatabaseManager.iud(insertCategoryQuery, category);
//
//                // Get the newly inserted category_id
//                ResultSet newCategoryRs = DatabaseManager.search("SELECT category_id FROM categories WHERE category_name = '" + category + "'");
//                if (newCategoryRs.next()) {
//                    return newCategoryRs.getInt("category_id");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;  // Return 0 if insertion fails
//    }
//
//
//}
