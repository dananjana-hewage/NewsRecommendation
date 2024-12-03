//package controllers;
//
//import database.DatabaseManager;
//import models.Article;
//import models.Preferences;
//
//import java.sql.ResultSet;
//import java.util.ArrayList;
//import java.util.List;
//
//public class RecommendationController {
//    private DatabaseManager dbManager;
//
//    public RecommendationController() {
//        dbManager = new DatabaseManager();
//    }
//
//    // Recommend articles based on user preferences
//    public List<Article> recommendArticlesByPreferences(int userId) {
//        Preferences preferences = dbManager.getPreferencesByUserId(userId);
//        List<Article> recommendedArticles = new ArrayList<>();
//
//        if (preferences != null) {
//            for (String category : preferences.getCategories()) {
//                ResultSet articles = dbManager.getRecommendedArticles(category);
//                try {
//                    while (articles.next()) {
//                        Article article = new Article(
//                                articles.getInt("id"),
//                                articles.getString("title"),
//                                articles.getString("content"),
//                                articles.getString("image_url"),
//                                articles.getString("category")
//                        );
//                        recommendedArticles.add(article);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return recommendedArticles;
//    }
//
//    // Recommend articles based on watch history or liked articles
////    public List<Article> recommendArticlesByHistoryOrLikes(int userId) {
////        // Fetch articles based on watch history or likes
////        ResultSet articles = dbManager.getUserLikedOrWatchedArticles(userId);
////        List<Article> recommendedArticles = new ArrayList<>();
////
////        try {
////            while (articles.next()) {
////                Article article = new Article(
////                        articles.getInt("id"),
////                        articles.getString("title"),
////                        articles.getString("content"),
////                        articles.getString("image_url"),
////                        articles.getString("category")
////                );
////                recommendedArticles.add(article);
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        return recommendedArticles;
////    }
//}
