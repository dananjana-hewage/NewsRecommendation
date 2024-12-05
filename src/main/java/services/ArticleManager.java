package services;

import Utils.ArticleProcessor;
import database.DatabaseManager;
import models.Article;


import java.sql.ResultSet;
import java.util.List;

public class ArticleManager {

    private ArticleProcessor articleProcessor;

    public ArticleManager() {
        articleProcessor = new ArticleProcessor(); // Initializes the ArticleProcessor
    }

    // Categorize and save articles to the database
//    public void categorizeAndSaveArticles(List<Article> articles) {
//        for (Article article : articles) {
//            // Process each article using ArticleProcessor for categorization
//            String category = articleProcessor.categorizeArticle(article.getTitle() + " " + article.getDescription());
//
//            // Get category_id from categories table
//            int categoryId = getCategoryId(category);
//
//            // Set categoryId in the article object
//            article.setCategoryId(categoryId);
//
//            // Save the article to the database
//            saveArticleToDatabase(article);
//        }
//    }

//    // Fetch category_id from the categories table using the category name
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

    // Save the article to the database
    private void saveArticleToDatabase(Article article) {
        try {
            String query = "INSERT INTO articles (title, category_id) VALUES ('"
                    + article.getTitle() + "', '"
                    + article.getCategoryId() + ")";
            DatabaseManager.iud(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
