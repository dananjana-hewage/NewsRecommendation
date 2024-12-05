//package services;
//
//
//import database.DatabaseManager;
//import models.Article;
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ArticleLoader {
//
//    // Load articles from the database based on the selected category
//    public List<Article> loadArticlesByCategory(String userPreferenceCategory) {
//        List<Article> articles = new ArrayList<>();
//        int categoryId = getCategoryId(userPreferenceCategory);
//
//        // If categoryId is valid, load articles from that category
//        if (categoryId != -1) {
//            try {
//                String query = "SELECT article_id, title, description FROM articles WHERE category_id = " + categoryId;
//                ResultSet resultSet = DatabaseManager.search(query);
//                while (resultSet.next()) {
//                    Article article = new Article(
//                            resultSet.getInt("article_id"),
//                            resultSet.getString("title"),
//                            resultSet.getString("description"));
//                    article.setCategoryId(categoryId);
//                    articles.add(article);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return articles;
//    }
//
//    // Get category_id from the categories table for a given category name
//    private int getCategoryId(String category) {
//        int categoryId = -1;
//
//        try {
//            String query = "SELECT category_id FROM categories WHERE category_name = '" + category + "'";
//            ResultSet resultSet = DatabaseManager.search(query);
//            if (resultSet.next()) {
//                categoryId = resultSet.getInt("category_id");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return categoryId;
//    }
//}
