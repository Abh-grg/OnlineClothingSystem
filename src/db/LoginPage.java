package db;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Online Clothing - Login");

        // Main container with gradient background
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #6B48FF, #00DDEB);");

        // Center panel with transparency
        VBox centerPanel = new VBox(25);
        centerPanel.setMaxWidth(450);
        centerPanel.setPadding(new Insets(40));
        centerPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" + 
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        // Logo
        ImageView logo = new ImageView(new Image("https://img.icons8.com/color/48/000000/clothes.png"));
        logo.setFitHeight(60);
        logo.setFitWidth(60);
        logo.setBlendMode(BlendMode.SRC_ATOP);

        // Title
        Label titleLabel = new Label("Online Clothing");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: linear-gradient(to right, #6B48FF, #00DDEB);");

        // Subtitle
        Label subtitleLabel = new Label("Sign in to your account");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitleLabel.setStyle("-fx-text-fill: #666666;");

        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setVgap(20);
        formGrid.setHgap(10);

        // Username
        Label usernameLabel = new Label("Username");
        usernameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        usernameLabel.setStyle("-fx-text-fill: #333333;");
        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Enter username");
        usernameInput.setStyle("-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-padding: 12;" +
                "-fx-font-size: 14px;" +
                "-fx-background-color: #ffffff;");
        GridPane.setConstraints(usernameLabel, 0, 0);
        GridPane.setConstraints(usernameInput, 0, 1);

        // Password
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        passwordLabel.setStyle("-fx-text-fill: #333333;");
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Enter password");
        passwordInput.setStyle("-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-padding: 12;" +
                "-fx-font-size: 14px;" +
                "-fx-background-color: #ffffff;");
        GridPane.setConstraints(passwordLabel, 0, 2);
        GridPane.setConstraints(passwordInput, 0, 3);

        formGrid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 12 40;" +
                "-fx-cursor: hand;");

        Button signupButton = new Button("Sign Up");
        signupButton.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: #6B48FF;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 38;" +
                "-fx-cursor: hand;");

        // Button actions
        loginButton.setOnAction(e -> login(usernameInput.getText(), passwordInput.getText()));
        signupButton.setOnAction(e -> {
            CustomerSignup customerSignup = new CustomerSignup();
            Stage signupStage = new Stage();
            customerSignup.start(signupStage);
        });

        buttonBox.getChildren().addAll(loginButton, signupButton);

        // Assemble panel
        centerPanel.getChildren().addAll(logo, titleLabel, subtitleLabel, formGrid, buttonBox);
        centerPanel.setAlignment(Pos.CENTER);

        root.setCenter(centerPanel);
        BorderPane.setAlignment(centerPanel, Pos.CENTER);
        BorderPane.setMargin(centerPanel, new Insets(30));

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Encapsulation: Private login method (hides database details)
    private void login(String username, String password) {
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            String sql = "SELECT id, role FROM Users WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                GlobalVar.id = resultSet.getInt("id");
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

    // Abstraction: Alert handling method
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    // Polymorphism: Different dashboards open based on role
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

    public static void main(String[] args) {
        launch(args);
    }
}
