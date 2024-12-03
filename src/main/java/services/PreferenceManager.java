package services;

import database.DatabaseManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {
    public static List<Integer> getUserSelectedCategories(int userId) {
        List<Integer> categoryIds = new ArrayList<>();
        try {
            String query = "SELECT category_id FROM preferences WHERE id = " + userId;
            ResultSet resultSet = DatabaseManager.search(query);

            while (resultSet.next()) {
                categoryIds.add(resultSet.getInt("category_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryIds;
    }
}
