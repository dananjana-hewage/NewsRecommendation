package services;

import database.DatabaseManager;
import models.Article;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ArticleManager {

    public static List<Article> getArticlesByCategories(List<Integer> categoryIds) {
        List<Article> articles = new ArrayList<>();
        try {
            if (!categoryIds.isEmpty()) {
                String categories = categoryIds.toString().replace("[", "").replace("]", "");
                String query = "SELECT * FROM articles WHERE category_id IN (" + categories + ")";
                ResultSet resultSet = DatabaseManager.search(query);

                while (resultSet.next()) {
                    Article article = new Article();
                    article.setId(resultSet.getInt("id"));
                    article.setTitle(resultSet.getString("title"));
                    article.setImageUrl(resultSet.getString("image_url"));
                    articles.add(article);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articles;
    }
}
