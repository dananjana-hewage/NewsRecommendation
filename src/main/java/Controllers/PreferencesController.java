package Controllers;


import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Category;
import models.User;
import services.CategoryManager;


import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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


    public void loadCategoriesIntoCheckboxes() {

        categoryContainer.getChildren().clear(); // Clear any existing checkboxes

        try {
            System.out.println(CategoryManager.getCategories());
            ResultSet rs = CategoryManager.getCategories();
            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String categoryName = rs.getString("name");

                CheckBox checkBox = new CheckBox(categoryName);
                checkBox.setUserData(categoryId); // Store category_id in CheckBox's userData
                categoryContainer.getChildren().add(checkBox);
                checkBox.getStyleClass().add("checkbox");
            }
        } catch (Exception e) {
            System.out.println("Error loading categories: " + e.getMessage());
        }
    }

    @FXML
    public void savePreferencesOnAction(ActionEvent actionEvent) {
        try {
            // Step 1: Retrieve existing preferences for the user
            String fetchQuery = "SELECT category_id FROM preferences WHERE id = " + loggedInUser.getId();
            ResultSet rs = DatabaseManager.search(fetchQuery);

            // Store existing preferences in a Set for quick lookup
            Set<Integer> existingPreferences = new HashSet<>();
            while (rs.next()) {
                existingPreferences.add(rs.getInt("category_id"));
            }

            // Step 2: Process checkbox selections
            Set<Integer> selectedCategories = new HashSet<>();
            for (var node : categoryContainer.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) node;
                    int categoryId = (int) checkBox.getUserData();
                    if (checkBox.isSelected()) {
                        selectedCategories.add(categoryId);
                    }
                }
            }

            // Step 3: Handle case where no categories are selected
            if (selectedCategories.isEmpty()) {
                // Assume all categories are selected
                for (var node : categoryContainer.getChildren()) {
                    if (node instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) node;
                        selectedCategories.add((int) checkBox.getUserData());
                    }
                }
            }

            // Step 4: Update database preferences
            // Insert new preferences
            for (int categoryId : selectedCategories) {
                if (!existingPreferences.contains(categoryId)) {
                    String insertQuery = "INSERT INTO preferences (id, category_id) VALUES (" +
                            loggedInUser.getId() + ", " + categoryId + ")";
                    if (!DatabaseManager.iud(insertQuery)) {
                        System.out.println("Failed to save preference for category ID: " + categoryId);
                    }
                }
            }

            // Remove deselected preferences
            for (int categoryId : existingPreferences) {
                if (!selectedCategories.contains(categoryId)) {
                    String deleteQuery = "DELETE FROM preferences WHERE id = " + loggedInUser.getId() +
                            " AND category_id = " + categoryId;
                    if (!DatabaseManager.iud(deleteQuery)) {
                        System.out.println("Failed to remove preference for category ID: " + categoryId);
                    }
                }
            }

            System.out.println("Preferences successfully updated!");
            loadDashboardController();
        } catch (Exception e) {
            System.out.println("Error saving preferences: " + e.getMessage());
        }
    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadDashboardController() {
        try {
            // Step 1: Load the dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            // Step 2: Get the controller of the dashboard
            DashboardController dashboardController = loader.getController();

            // Step 3: Pass the logged-in user details to the dashboard controller (if needed)
            dashboardController.setUser(loggedInUser);

            // Step 4: Set the scene and display the dashboard
            Stage stage = (Stage) categoryContainer.getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading dashboard: " + e.getMessage());
        }
    }



//---------------------
}
