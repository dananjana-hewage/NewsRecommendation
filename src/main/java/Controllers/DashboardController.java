package Controllers;

import Utils.APIClient;
import database.ArticleLoader;
import database.DatabaseManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Article;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import models.User;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ArticleManager;
import services.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class  DashboardController {

    @FXML
    public Button setPreferencesBtn;
    @FXML
    private ListView<String> articlesListView; // List view to display article titles
    @FXML
    private ImageView articleImageView; // Image view to display the article image
    @FXML

    private Label articleTitleLabel; // Label to display article title
    @FXML
    private Label articleDescriptionLabel; // Label to display article description

    private APIClient apiClient = new APIClient();


    private User loggedInUser;
    // Method to set the logged-in user (called from LoginController)
    public void setUser(User user) {
        this.loggedInUser = user;
    }

public void initialize() {
    ArticleLoader.saveArticlesToDatabase();
    //loadArticlesToDashboard(loggedInUser.getId());
}

    @FXML
    private VBox articlesVBox;

    public void loadArticlesToDashboard(int userId) {
        try {
            // Get user preferences
            List<Integer> categoryIds = PreferenceManager.getUserSelectedCategories(userId);

            // Get articles based on preferences
            List<Article> articles = ArticleManager.getArticlesByCategories(categoryIds);




            // Clear VBox before loading new content
            articlesVBox.getChildren().clear();

            // Populate VBox with articles
            for (Article article : articles) {
                Label titleLabel = new Label(article.getTitle());
                ImageView imageView = new ImageView(new Image(article.getImageUrl()));

                // Style the elements
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                // Add to VBox
                HBox articleBox = new HBox(10, imageView, titleLabel);
                articlesVBox.getChildren().add(articleBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void setPreferencesOnAction() {

        try {
            // Load Preference.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/recommendationsystem/preferences.fxml"));
            Parent preferenceRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) setPreferencesBtn.getScene().getWindow();
            stage.setScene(new Scene(preferenceRoot));

            // Retrieve the controller for Preference.fxml
            PreferencesController preferencesController = loader.getController();
            preferencesController.setUser(loggedInUser);
            preferencesController.loadCategories();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
//














    public void loadArticlesBasedOnPreferences(User user) {
        try {
            // Fetch the user's selected categories from the database
            List<String> selectedCategories = getUserPreferences(user);

            // Iterate through categories and fetch articles for each one
            for (String category : selectedCategories) {
                fetchArticlesFromAPI(category, 1); // You can pass the page number dynamically for pagination
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private List<String> getUserPreferences(User user) {
        List<String> preferences = new ArrayList<>();
        try {
            ResultSet resultSet = DatabaseManager.search("SELECT category FROM user_preferences WHERE user_id = " + user.getId());
            while (resultSet.next()) {
                preferences.add(resultSet.getString("category"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preferences;
    }

    private void fetchArticlesFromAPI(String category, int page) {
        try {
            // Construct the API URL for fetching articles
            String apiUrl = "https://newsapi.org/v2/top-headlines?category=" + category + "&page=" + page + "&apiKey=" + "37aa9c63ea4c47d48ec3302481f1b760";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray articles = jsonResponse.getJSONArray("articles");

            // Load articles into your database
            for (int i = 0; i < articles.length(); i++) {
                JSONObject article = articles.getJSONObject(i);
                String articleId = article.getString("url");
                String title = article.getString("title");
                String description = article.getString("description");
                String imageUrl = article.getString("urlToImage");
                String articleUrl = article.getString("url");

                // Insert article into the database (ensure that you only store necessary fields)
                DatabaseManager.iud("INSERT INTO articles (article_id, title, description, image_url, category, url) VALUES ('"
                        + articleId + "', '"
                        + title + "', '"
                        + description + "', '"
                        + imageUrl + "', '"
                        + category + "', '"
                        + articleUrl + "')");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void initialize() {
//        // This can be used to load articles or categories when the dashboard starts
//        loadArticles("business");
//    }

    // Set preferences method triggered by a button click


}
