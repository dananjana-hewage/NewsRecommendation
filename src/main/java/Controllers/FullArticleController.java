package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.sql.*;
import database.DatabaseManager;

public class FullArticleController {

    @FXML
    private Label articleTitleLabel;
    @FXML
    private Label articleContentLabel;

    // Load the full article details
    public void loadFullArticle(int articleId) {
        String query = "SELECT title, content FROM articles WHERE article_id = ?";

        try {
            // Get the Statement object from DatabaseManager
            Statement statement = DatabaseManager.createConnection();
            // Get the Connection object from the Statement
            Connection conn = statement.getConnection();

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, articleId); // Set the article ID
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String title = rs.getString("title");
                    String content = rs.getString("content");

                    // Set the article title and content
                    articleTitleLabel.setText(title);
                    articleContentLabel.setText(content);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
