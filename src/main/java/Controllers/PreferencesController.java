package Controllers;

import Utils.APIClient;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Category;
import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferencesController {

    @FXML
    public Button savePreferencesBtn;

    @FXML
    public AnchorPane preferencePane;
    @FXML
    private VBox categoryContainer;

    private User loggedInUser;

    public void setUser(User user) {
        this.loggedInUser = user;
    }


    public void loadCategories() {
        try {
            // Fetch categories from the database
            ResultSet rs = DatabaseManager.search("SELECT name FROM categories");

            // Clear existing children in case of reload
            categoryContainer.getChildren().clear();

            // Add each category as a CheckBox
            while (rs.next()) {
                String categoryName = rs.getString("name");
                CheckBox checkBox = new CheckBox(categoryName);
                checkBox.getStyleClass().add("checkbox");
                categoryContainer.getChildren().add(checkBox);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePreferencesOnAction(ActionEvent actionEvent) {

        try {
            // Fetch selected categories
            List<String> selectedCategories = new ArrayList<>();
            for (Node node : categoryContainer.getChildren()) {
                if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                    selectedCategories.add(checkBox.getText());
                }
            }

            // Get category IDs for the selected categories
            for (String category : selectedCategories) {
                // Query to get category_id by category_name
                ResultSet rs = DatabaseManager.search(
                        "SELECT category_id FROM categories WHERE name = '" + category + "'");

                if (rs.next()) {
                    int categoryId = rs.getInt("category_id");

                    // Insert user_id and category_id into preferences table
                    String query = "INSERT INTO preferences (id, category_id) VALUES (" +
                            loggedInUser.getId() + ", " + categoryId + ")";
                    DatabaseManager.iud(query);
                }
            }

            // Show success message
            showAlert("Preferences Saved", "Your preferences have been saved successfully.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while saving preferences.", Alert.AlertType.ERROR);
        }

    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


//---------------------
}
