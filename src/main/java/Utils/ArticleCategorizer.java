//package Utils;
//
//import database.DatabaseManager;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//
//import java.util.*;
//
//public class ArticleCategorizer {
//
//    private StanfordCoreNLP pipeline;
//    private ArticleProcessor articleProcessor;
//
//    // Constructor to initialize the NLP pipeline and the ArticleProcessor
//    public ArticleCategorizer() {
//        articleProcessor = new ArticleProcessor(); // Initialize the ArticleProcessor for categorization
//    }
//
//    // Method to categorize an article (uses ArticleProcessor for categorization)
//    public String categorizeArticle(String articleText) {
//        return articleProcessor.categorizeArticle(articleText); // Categorize article using ArticleProcessor
//    }
//
//    // Method to get the category_id for a category name
//    private int getCategoryId(String categoryName) {
//        // Query to find the category_id by name
//        String query = "SELECT category_id FROM categories WHERE category_name = '" + categoryName + "'";
//        int categoryId = DatabaseManager.getId(query); // Assuming DatabaseManager has a getId method to execute queries and return IDs
//
//        // If the category doesn't exist, insert it and then get the new ID
//        if (categoryId == -1) {
//            // Insert the new category into the categories table
//            query = "INSERT INTO categories (category_name) VALUES ('" + categoryName + "')";
//            DatabaseManager.iud(query); // Assuming DatabaseManager has an iud method for INSERT/UPDATE/DELETE
//            categoryId = DatabaseManager.getId("SELECT category_id FROM categories WHERE category_name = '" + categoryName + "'"); // Get the new category ID
//        }
//
//        return categoryId;
//    }
//
//    // Method to save the article to the database
//    public void saveArticleToDatabase(String articleText, String title, String description, String category) {
//        int categoryId = getCategoryId(category); // Get the category ID for the article's category
//
//        // Insert the article into the articles table with the category_id
//        String query = "INSERT INTO articles (title, description, category_id) VALUES ('" + title + "', '" + description + "', " + categoryId + ")";
//        DatabaseManager.iud(query); // Insert the article
//    }
//
//    public static void main(String[] args) {
//        // Example usage of ArticleCategorizer
//        String articleText = "AI is transforming the world of technology and healthcare. Machine learning is being used in medical research.";
//        String title = "AI and Healthcare Transformation";
//        String description = "An article about AI's impact on technology and healthcare.";
//
//        ArticleCategorizer categorizer = new ArticleCategorizer();
//
//        // Categorize the article
//        String category = categorizer.categorizeArticle(articleText);
//        System.out.println("The article is categorized under: " + category);
//
//        // Save the article to the database
//        categorizer.saveArticleToDatabase(articleText, title, description, category);
//    }
//}
