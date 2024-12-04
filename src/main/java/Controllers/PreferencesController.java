package Controllers;


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
import services.CategoryManager;


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

    private List<Category> selectedCategories;


    public void loadCategoriesIntoCheckboxes() {
        try {
            // Fetch categories from the database
            String query = "SELECT * FROM categories";
            ResultSet resultSet = DatabaseManager.search(query);

            categoryContainer.getChildren().clear();

            // List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");  // Get category id
                String categoryName = resultSet.getString("name"); // Get category name

                // Create a checkbox for each category
                CheckBox categoryCheckBox = new CheckBox(categoryName);
                categoryCheckBox.getStyleClass().add("checkbox");
                categoryCheckBox.setId("category_" + categoryId); // Set the ID for later identification
                categoryCheckBox.setOnAction(e -> {
                    // Set the selected state of the category (if needed)
                });

                // Add the checkbox to the category container (VBox)
                categoryContainer.getChildren().add(categoryCheckBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void savePreferencesOnAction(ActionEvent actionEvent) {

        try {
            // Clear previous preferences
            System.out.println(loggedInUser.getUsername());
            System.out.println(loggedInUser.getId());

            String deleteQuery = "DELETE FROM preferences WHERE id = " + loggedInUser.getId();
            System.out.println("deleteQuery ");
            // Using Statement for execution
            DatabaseManager.iud(deleteQuery); // Calling iud method to execute delete query

            // Save new preferences
            for (Category category : selectedCategories) {
                // Check if the category exists in the database before inserting
                boolean categoryExists = CategoryManager.categoryExists(category.getName());

                if (categoryExists) {
                    // Construct query for inserting preferences
                    String insertQuery = "INSERT INTO preferences (id, category_id) VALUES (" +
                            loggedInUser.getId() + ", " + category.getId() + ")";

                    // Using Statement for execution
                    DatabaseManager.iud(insertQuery); // Calling iud method to execute insert query
                } else {
                    // Handle case where the category doesn't exist
                    System.out.println("Category does not exist: " + category.getName());
                }
            }
            System.out.println("Preferences saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method should be called when the checkboxes are populated
    public void setSelectedCategories() {
        selectedCategories = new ArrayList<>();
        // Iterate through the checkboxes in the VBox
        for (Node node : categoryContainer.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                int categoryId = Integer.parseInt(checkBox.getId().replace("category_", ""));
                // Find the corresponding category object for this checkbox
                Category category = getCategoryById(categoryId);
                if (category != null && checkBox.isSelected()) {
                    category.setSelected(true);
                    selectedCategories.add(category);
                }
            }
        }

    }


    private Category getCategoryById(int categoryId) {
        for (Category category : selectedCategories) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }




//---------------------
}
