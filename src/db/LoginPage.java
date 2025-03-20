package db;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(8);

        // Username Label & Input
        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter username");
        GridPane.setConstraints(usernameInput, 1, 0);

        // Password Label & Input
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter password");
        GridPane.setConstraints(passwordInput, 1, 1);

        // Login Button
        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);

        // Sign Up Button
        Button signupButton = new Button("Sign Up");
        GridPane.setConstraints(signupButton, 1, 3);

        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            login(username, password);
        });

        signupButton.setOnAction(e -> {
            CustomerSignup customerSignup = new CustomerSignup();
            Stage signupStage = new Stage();
            customerSignup.start(signupStage);
        });

        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton, signupButton);

        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void login(String username, String password) {
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            String sql = "SELECT id, role FROM Users WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                GlobalVar.id = resultSet.getInt("id"); // Set GlobalVar.id
                String role = resultSet.getString("role");
                
                if ("Admin".equalsIgnoreCase(role)) {
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Admin!");
                    openAdminDashboard();
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + username + "!");
                    openCustomerDashboard();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error checking user credentials: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void openAdminDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard();
        Stage stage = new Stage();
        adminDashboard.start(stage);
    }

    private void openCustomerDashboard() {
        CustomerDashboard customerDashboard = new CustomerDashboard();
        Stage stage = new Stage();
        customerDashboard.start(stage);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}