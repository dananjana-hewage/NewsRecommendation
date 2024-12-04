package Controllers;


import Utils.ArticleProcessor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import database.DatabaseManager;
import javafx.scene.layout.VBox;
import models.User;
import Controllers.PreferencesController;
import javafx.fxml.FXMLLoader;



import java.awt.*;
import java.sql.ResultSet;


public class loginController {

    @FXML
    private TextField emailSu;

    @FXML
    private Button onSignInPaneBtn;

    @FXML
    private Button onSignUpPaneBtn;

    @FXML
    private PasswordField passwordSi;

    @FXML
    private PasswordField passwordSu;

    @FXML
    private Button signInBtn;

    @FXML
    private AnchorPane signInPane;

    @FXML
    private Button signUpBtn;

    @FXML
    private AnchorPane signUpPage;

    @FXML
    private TextField usernameSi;

    @FXML
    private TextField usernameSu;


    @FXML
    void onSignInPane(ActionEvent event) {
        // Logic for switching to Sign-In Pane
        signInPane.setVisible(true);
        signUpPage.setVisible(false);
    }

    @FXML
    void onSignUpPane(ActionEvent event) {
        // Logic for switching to Sign-Up Pane
        signInPane.setVisible(false);
        signUpPage.setVisible(true);
    }

    @FXML
    void signInOnAction(ActionEvent event) throws Exception {
        // Logic for signing in
        String username = usernameSi.getText();
        String password = passwordSi.getText();

        System.out.println("Sign-In attempted with username: " + username + " and password: " + password);

        // Validate inputs
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("All fields are required!");
            showAlert("Error", "All fields are required!");
            return;
        }


  //Check for the user
ResultSet resultSet= DatabaseManager.search("SELECT * FROM users WHERE `username`='"+username+"'");
        User user=new User();
        if(resultSet.next()){

    user.setId(resultSet.getInt("id"));
    user.setUsername(resultSet.getString("username"));
    user.setPassword(resultSet.getString("password"));
    user.setEmail(resultSet.getString("email"));

}

        // Handle user authentication
        if (user != null) {
            if (user.getPassword().equals(password)) {
                System.out.println("Sign-In successful for user: " + user.getUsername());
                showAlert("Success", "Welcome back, " + user.getUsername() + "!");
                System.out.println(user.getId());

                // Logic for switching to Sign-In Pane to dashboard
                loadDashboard(user);

                //loadArticlesFromNewsAPI();

                // Add logic here to navigate to the next screen or dashboard
            } else {
                showAlert("Error", "Incorrect password. Please try again.");
            }
        } else {
            showAlert("Error", "No account found with this username.");
        }
    }


    @FXML
    void signUpOnAction(ActionEvent event) {
        // Logic for signing up

        String username = usernameSu.getText();
        String email = emailSu.getText();
        String password = passwordSu.getText();
        System.out.println("Sign-Up attempted with username: " + username + ", email: " + email + ", and password: " + password);

        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format. Please enter a valid email.");
            return;
        }
boolean isInserted=DatabaseManager.iud("INSERT INTO users VALUES ('"+username+"','"+email+"','"+password+"')");


        // Provide feedback based on the result
        if (isInserted) {
            System.out.println("User registered successfully!");
            showAlert("Success", "User registered successfully!");

            // Logic for switching to Sign-Up Pane to dashboard
            signUpPage.setVisible(false);
            signInPane.setVisible(true);



        } else {
            System.out.println("Failed to register user.");
            showAlert("Error", "Failed to register user. Please try again.");
        }
    }

    private void loadDashboard(User user) {
        try {
            // Load the dashboard FXML file
            System.out.println("Loading Dashboard...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/recommendationsystem/dashboard.fxml"));
            AnchorPane dashboardPane = loader.load();

            // Load articles to the dashboard for the logged-in user
            DashboardController dashboardController = loader.getController();
            dashboardController.loadArticles();
            dashboardController.setUser(user);

            ArticleProcessor.fetchAndProcessArticles();

            // Get the current scene's root and replace it with the dashboard
            AnchorPane rootPane = (AnchorPane) signInPane.getScene().getRoot();
            rootPane.getChildren().clear(); // Clear existing content
            rootPane.getChildren().add(dashboardPane);// Add the dashboard content

            System.out.println("dashboard loaded successfully!");


        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the dashboard.");
        }
    }


//    private void loadArticlesFromNewsAPI() {
//        try {
//            ArticleLoader.saveArticlesToDatabase();
//            showAlert("Success", "Articles loaded successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            showAlert("Error", "Failed to load articles.");
//        }
//    }
    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        // Regex pattern for validating email addresses
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    // Helper method to show alerts (optional GUI improvement)
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}