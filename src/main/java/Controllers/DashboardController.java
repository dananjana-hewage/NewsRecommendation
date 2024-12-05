package Controllers;


import Utils.APIClient;
import Utils.ArticleProcessor;
import database.DatabaseManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import models.Category;
import models.User;
import org.json.JSONArray;
import org.json.JSONObject;


import services.CategoryManager;
import services.PreferenceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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




    private User loggedInUser;
    // Method to set the logged-in user (called from LoginController)
    public void setUser(User user) {
        this.loggedInUser = user;
    }

public void initialize() {
        saveCategoriesAction();
//        ArticleProcessor articleProcessor = new ArticleProcessor();
//        articleProcessor.processAndStoreArticles();
        loadArticles();

}



    @FXML
    private VBox articlesVBox;

    private ExecutorService executorService = Executors.newFixedThreadPool(2); // For background threads


    public void loadArticles() {
        articlesVBox.getChildren().clear(); // Clear existing articles in the VBox

        // Execute the task asynchronously
        executorService.submit(() -> {
            try {
                // Fetch articles from the API
                JSONArray articlesArray = APIClient.fetchArticles();

                List<Article> articles = new ArrayList<>();
                for (int i = 0; i < articlesArray.length(); i++) {
                    JSONObject jsonObject = articlesArray.getJSONObject(i);
                    int articleId = i + 1; // Generate a dummy ID (or use one from the API)
                    String title = jsonObject.optString("name", "No Title");
                    String description = jsonObject.optString("description", "No Description");

                    // Add the article to the list
                    articles.add(new Article(articleId, title));
                }

                // Update the UI on the JavaFX Application Thread
                Platform.runLater(() -> {
                    for (Article article : articles) {
                        HBox articlePreview = new HBox();
                        articlePreview.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: #f9f9f9;");
                        articlePreview.setSpacing(10);

                        // Title Label
                        Label titleLabel = new Label(article.getTitle());
                        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: blue;");

                        // Add click event to show the full article
                        //titleLabel.setOnMouseClicked(event -> showFullArticle(article));

                        // Add the title label to the HBox
                        articlePreview.getChildren().addAll(titleLabel);

                        // Add the HBox to the VBox
                        articlesVBox.getChildren().add(articlePreview);
                    }
                    System.out.println("Articles loaded");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    // Load articles asynchronously
//    public void loadArticles() {
//        articlesVBox.getChildren().clear();
//
//        executorService.submit(() -> {
//            String query = "SELECT article_id, title, FROM articles";
//
//            try {
//                // Get the Statement object from DatabaseManager
//                Statement statement = DatabaseManager.createConnection();
//                // Get the Connection object from the Statement
//                Connection conn = statement.getConnection();
//
//                try (PreparedStatement stmt = conn.prepareStatement(query);
//                     ResultSet rs = stmt.executeQuery()) {
//
//                    List<Article> articles = new ArrayList<>();
//                    while (rs.next()) {
//                        int articleId = rs.getInt("article_id");
//                        String title = rs.getString("title");
//                        String description = rs.getString("description");
//                        articles.add(new Article(articleId, title));
//                        System.out.println("Fetched: " + title);
//                    }
//
//                    Platform.runLater(() -> {
//                        for (Article article : articles) {
//                            System.out.println("Adding article: " + article.getTitle());
//                            HBox articlePreview = new HBox();
//                            articlePreview.setStyle("-fx-padding: 10;");
//
//                            Label titleLabel = new Label(article.getTitle());
//                            titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
//                            titleLabel.setOnMouseClicked(event -> showFullArticle(article.getArticleId()));
//
////                            Label descriptionLabel = new Label(article.getDescription());
////                            descriptionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
//
//                            articlePreview.getChildren().addAll(titleLabel);
//                            articlesVBox.getChildren().add(articlePreview);
//                        }
//                    });
//                    System.out.println("Articles loaded");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }



    // Show full article when title is clicked
    private void showFullArticle(int articleId) {
        executorService.submit(() -> {
            try {
                // Load the full article scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fullArticle.fxml"));
                Parent root = loader.load();

                // Pass the articleId to the full article controller
                FullArticleController fullArticleController = loader.getController();
                fullArticleController.loadFullArticle(articleId);

                // Create a new scene for the full article
                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Full Article");
                    stage.show();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void saveCategoriesAction() {
        List<String> categories = List.of("Technology", "Health", "Sports", "Entertainment");

        try {
            // Get the Statement object from DatabaseManager
            Statement statement = DatabaseManager.createConnection();
            // Get the Connection object from the Statement
            Connection conn = statement.getConnection();

            boolean success = CategoryManager.saveCategoriesWithIDs(conn, categories);
            if (success) {
                System.out.println("Categories saved successfully!");
            } else {
                System.out.println("Failed to save categories.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    @FXML
    public void setPreferencesOnAction(ActionEvent actionEvent) {

        try {
            System.out.println("Loading preferences.fxml");
            // Load Preference.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/recommendationsystem/preferences.fxml"));
            Parent preferenceRoot = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) setPreferencesBtn.getScene().getWindow();
            stage.setScene(new Scene(preferenceRoot));

            // Retrieve the controller for Preference.fxml
            PreferencesController preferencesController = loader.getController();
            preferencesController.loadCategoriesIntoCheckboxes();
            preferencesController.setUser(loggedInUser);

           // preferencesController.lo
            System.out.println("Preferences Loaded");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

