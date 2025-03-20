package db;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomerSignup extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Signup");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(8);

        // Logo Image
        Image logoImage = new Image("file:/Users/saishtiwari/Documents/logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(150);
        logoImageView.setPreserveRatio(true);
        GridPane.setConstraints(logoImageView, 0, 0, 2, 1);

        // Name Input
        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 1);
        TextField nameInput = new TextField();
        nameInput.setPromptText("Full Name");
        GridPane.setConstraints(nameInput, 1, 1);

        // Contact Input
        Label contactLabel = new Label("Contact Number:");
        GridPane.setConstraints(contactLabel, 0, 2);
        TextField contactInput = new TextField();
        contactInput.setPromptText("Contact Number");
        GridPane.setConstraints(contactInput, 1, 2);

        // Email Input
        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 3);
        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");
        GridPane.setConstraints(emailInput, 1, 3);

        // Password Input
        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 4);
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        GridPane.setConstraints(passwordInput, 1, 4);

        // Confirm Password Input
        Label confirmPasswordLabel = new Label("Confirm Password:");
        GridPane.setConstraints(confirmPasswordLabel, 0, 5);
        PasswordField confirmPasswordInput = new PasswordField();
        confirmPasswordInput.setPromptText("Confirm Password");
        GridPane.setConstraints(confirmPasswordInput, 1, 5);

        // Register Button
        Button registerButton = new Button("Register");
        GridPane.setConstraints(registerButton, 1, 6);

        // Back to Login Button
        Button backToLoginButton = new Button("Back to Login");
        GridPane.setConstraints(backToLoginButton, 0, 6);

        registerButton.setOnAction(e -> {
            String name = nameInput.getText();
            String contact = contactInput.getText();
            String email = emailInput.getText();
            String password = passwordInput.getText();
            String confirmPassword = confirmPasswordInput.getText();

            if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Error", "All fields are required!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
                return;
            }

            registerUser(name, contact, email, password, primaryStage);
        });

        backToLoginButton.setOnAction(e -> {
            primaryStage.close();
            LoginPage loginPage = new LoginPage();
            Stage loginStage = new Stage();
            loginPage.start(loginStage);
        });

        grid.getChildren().addAll(logoImageView, nameLabel, nameInput, contactLabel, contactInput, emailLabel, emailInput,
                passwordLabel, passwordInput, confirmPasswordLabel, confirmPasswordInput, registerButton, backToLoginButton);

        Scene scene = new Scene(grid, 450, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void registerUser(String name, String contact, String email, String password, Stage primaryStage) {
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            String sql = "INSERT INTO Users (name, contact, email, password, role) VALUES (?, ?, ?, ?, 'Customer')";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, contact);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Successful!");
                primaryStage.close();
                LoginPage loginPage = new LoginPage();
                Stage loginStage = new Stage();
                loginPage.start(loginStage);
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "User already exists or invalid details.");
            ex.printStackTrace();
        }
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