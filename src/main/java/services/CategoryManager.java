package services;

import database.DatabaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoryManager {

    // Check if a category exists in the database
    public static boolean categoryExists(String categoryName) {

        try {
            // Check if the category already exists by its name
            String query = "SELECT * FROM categories WHERE name = '" + categoryName + "'";
            ResultSet resultSet = DatabaseManager.search(query);

            return resultSet.next(); // Return true if the category exists
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Handle any other exceptions here
        }
    }

    // You can add other category-related methods here (e.g., adding a category, deleting a category, etc.)
}
