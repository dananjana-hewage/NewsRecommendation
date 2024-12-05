package Utils;

import database.DatabaseManager;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import models.Article;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.*;

public class ArticleProcessor {

    private StanfordCoreNLP pipeline;

    public ArticleProcessor() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        pipeline = new StanfordCoreNLP(props);
    }

    public List<String> extractKeywords(String content) {
        List<String> keywords = new ArrayList<>();
        Annotation document = new Annotation(content);
        pipeline.annotate(document);

        // Extract keywords (nouns, proper nouns, adjectives)
        for (CoreLabel token : document.get(CoreAnnotations.TokensAnnotation.class)) {
            String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (pos.equals("NN") || pos.equals("NNP") || pos.equals("JJ")) {
                keywords.add(token.word().toLowerCase());
            }
        }
        return keywords;
    }

    public String categorizeArticle(String content) {
        List<String> extractedKeywords = extractKeywords(content);

        // Predefined categories and keywords
        List<List<String>> categoryKeywordsList = Arrays.asList(
                Arrays.asList("ai", "technology", "machine", "software", "robot"), // Technology keywords
                Arrays.asList("medicine", "health", "wellness", "doctor", "hospital"), // Health keywords
                Arrays.asList("game", "team", "player", "sports", "match"), // Sports keywords
                Arrays.asList("finance", "money", "economy", "business", "stock") // Finance keywords
        );

        List<String> categoryNames = Arrays.asList("Technology", "Health", "Sports", "Finance");

        for (int i = 0; i < categoryKeywordsList.size(); i++) {
            for (String keyword : categoryKeywordsList.get(i)) {
                if (extractedKeywords.contains(keyword)) {
                    return categoryNames.get(i);
                }
            }
        }
        return "Uncategorized";
    }

    // Fetch category_id from the categories table using the category name
    private int getCategoryId(String category) {
        int categoryId = -1;

        try {
            String query = "SELECT category_id FROM categories WHERE category_name = '" + category + "'";
            ResultSet resultSet = DatabaseManager.search(query);
            if (resultSet.next()) {
                categoryId = resultSet.getInt("category_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryId;
    }

    public void processAndStoreArticles() {
        JSONArray articles = utils.APIClient.fetchArticles();

        if (articles != null) {
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                String title = article.getString("name");
               // String description = article.getString("description");

                // Categorize the article
                String category = categorizeArticle(title);

                // Get the category ID from the database
                int categoryId = getCategoryId(category);

                // Store in the database

                // Create an Article object
                Article articleObject = new Article(categoryId, title);

                // Store in the database
                saveArticleToDatabase(articleObject);

            }
        }
    }

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

//    private void saveArticleToDatabase(String title, String description, String category) {
//        String query = "INSERT INTO articles (title, category_id) VALUES ('"
//                + article.getTitle() + "', '"
//                + article.getCategoryId() + ")";
//        DatabaseManager.iud(query);
//    }

    public static void main(String[] args) {
        ArticleProcessor processor = new ArticleProcessor();
        String articleContent = "Artificial intelligence and machine learning are transforming the technology industry.";

        // Categorize the article
        String category = processor.categorizeArticle(articleContent);
        System.out.println("Category: " + category);
    }


}
