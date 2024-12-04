package Controllers;


import database.DatabaseManager;
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




    private User loggedInUser;
    // Method to set the logged-in user (called from LoginController)
    public void setUser(User user) {
        this.loggedInUser = user;
    }

public void initialize() {
    //ArticleLoader.saveArticlesToDatabase();
    loadArticles();
}

    @FXML
    private VBox articlesVBox;


    // Method to load articles into the VBox
    public void loadArticles() {
        try {
            // Query to fetch articles from the database
            ResultSet resultSet = DatabaseManager.search("SELECT * FROM articles");

            // Clear the VBox before adding new articles
            articlesVBox.getChildren().clear();

            // Iterate through the result set and add each article to the VBox
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String imageUrl = resultSet.getString("image_url");
                String category = resultSet.getString("category");

                // Create an HBox for each article
                HBox articleBox = new HBox();
                articleBox.setSpacing(10);

                // Create an ImageView for the article image
                ImageView imageView = new ImageView();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Image image = new Image(imageUrl, 100, 100, true, true); // Set width & height
                    imageView.setImage(image);
                }
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);

                // Create a Label for the article title
                Label titleLabel = new Label(title);
                titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
                titleLabel.setWrapText(true); // Wrap text if too long

                // Create a Label for the category
                Label categoryLabel = new Label("Category: " + category);
                categoryLabel.setStyle("-fx-font-size: 12; -fx-text-fill: gray;");

                // Add the image and labels to the HBox
                articleBox.getChildren().addAll(imageView, new VBox(titleLabel, categoryLabel));

                // Add the HBox to the VBox
                articlesVBox.getChildren().add(articleBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setPreferencesOnAction(ActionEvent actionEvent) {

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
            //preferencesController.loadCategories();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

