package services;

import database.DatabaseManager;
import javafx.scene.control.CheckBox;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CategoryManager {

    // Save categories with category_id and category_name
    public static boolean saveCategoriesWithIDs(Connection conn, List<String> categories) {
        boolean allSaved = true;

        for (int i = 0; i < categories.size(); i++) {
            String categoryName = categories.get(i);
            int categoryId = i + 1; // Generate category_id (simple increment)

            // Check if category already exists
            if (!categoryExists(conn, categoryId)) {
                String query = "INSERT INTO categories (category_id, name) VALUES (" + categoryId + ", '" + categoryName + "')";
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(query);
                } catch (SQLException e) {
                    e.printStackTrace();
                    allSaved = false;
                }
            }
        }
        return allSaved;
    }

    // Updated categoryExists method to accept Connection
    private static boolean categoryExists(Connection conn, int categoryId) {
        String query = "SELECT * FROM categories WHERE category_id = " + categoryId;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking category existence: " + e.getMessage());
            return false;
        }
    }


    // Fetch all categories (category_id and category_name) from the database
    public static ResultSet getCategories() throws Exception {
        String query = "SELECT * FROM categories";
        return DatabaseManager.search(query);
    }
}

