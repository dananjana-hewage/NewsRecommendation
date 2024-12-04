package services;

import Utils.ArticleProcessor;
import database.DatabaseManager;
import models.Article;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArticleManager {

        private final ArticleProcessor articleProcessor = new ArticleProcessor();

        public void fetchAndSaveArticles() {
            try {
                // Fetch articles from Bing Search API
                BingSearchAPIClient apiClient = new BingSearchAPIClient("YOUR_API_KEY");
                List<Article> articles = apiClient.fetchArticlesByCategory("Technology");
                articles.addAll(apiClient.fetchArticlesByCategory("Health"));
                articles.addAll(apiClient.fetchArticlesByCategory("Sports"));
                articles.addAll(apiClient.fetchArticlesByCategory("AI"));

                // Process articles
                List<Article> categorizedArticles = articleProcessor.categorizeArticles(articles);

                // Save categorized articles to the database
                for (Article article : categorizedArticles) {
                    DatabaseManager.iud("INSERT INTO articles (title, image_url, category) VALUES ('"
                            + article.getTitle() + "', '"
                            + article.getImageUrl() + "', '"
                            + article.getCategory() + "')");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
