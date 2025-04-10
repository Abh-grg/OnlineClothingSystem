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
import java.sql.SQLException;

public class CustomerSignup extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Online Clothing - Sign Up");

        // Main container with gradient background
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #6B48FF, #00DDEB);");

        // Center panel with subtle transparency
        VBox centerPanel = new VBox(25);
        centerPanel.setMaxWidth(450);
        centerPanel.setMaxHeight(600); // Set a max height to prevent stretching
        centerPanel.setPadding(new Insets(40));
        centerPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" +
                           "-fx-border-radius: 15;" +
                           "-fx-background-radius: 15;" +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        // Logo
        Image logoImage = new Image("file:/Users/saishtiwari/Documents/logo.png");
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitHeight(60);
        logoImageView.setFitWidth(60);
        logoImageView.setBlendMode(BlendMode.SRC_ATOP);

        // Title
        Label titleLabel = new Label("Create Account");
        titleLabel.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: linear-gradient(to right, #6B48FF, #00DDEB);");

        // Subtitle
        Label subtitleLabel = new Label("Join Online Clothing");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitleLabel.setStyle("-fx-text-fill: #666666;");

        // Form grid
        GridPane formGrid = new GridPane();
        formGrid.setVgap(20);
        formGrid.setHgap(10);

        // Name
        Label nameLabel = new Label("Name:");
        nameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #333333;");
        TextField nameInput = new TextField();
        nameInput.setPromptText("Full Name");
        nameInput.setStyle("-fx-background-radius: 25;" +
                         "-fx-border-radius: 25;" +
                         "-fx-border-color: #6B48FF;" +
                         "-fx-padding: 12;" +
                         "-fx-font-size: 14px;" +
                         "-fx-background-color: #ffffff;");
        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setConstraints(nameInput, 0, 1);

        // Contact
        Label contactLabel = new Label("Contact Number:");
        contactLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        contactLabel.setStyle("-fx-text-fill: #333333;");
        TextField contactInput = new TextField();
        contactInput.setPromptText("Contact Number");
        contactInput.setStyle("-fx-background-radius: 25;" +
                            "-fx-border-radius: 25;" +
                            "-fx-border-color: #6B48FF;" +
                            "-fx-padding: 12;" +
                            "-fx-font-size: 14px;" +
                            "-fx-background-color: #ffffff;");
        GridPane.setConstraints(contactLabel, 0, 2);
        GridPane.setConstraints(contactInput, 0, 3);

        // Email
        Label emailLabel = new Label("Email:");
        emailLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        emailLabel.setStyle("-fx-text-fill: #333333;");
        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");
        emailInput.setStyle("-fx-background-radius: 25;" +
                          "-fx-border-radius: 25;" +
                          "-fx-border-color: #6B48FF;" +
                          "-fx-padding: 12;" +
                          "-fx-font-size: 14px;" +
                          "-fx-background-color: #ffffff;");
        GridPane.setConstraints(emailLabel, 0, 4);
        GridPane.setConstraints(emailInput, 0, 5);

        // Password
        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        passwordLabel.setStyle("-fx-text-fill: #333333;");
        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.setStyle("-fx-background-radius: 25;" +
                             "-fx-border-radius: 25;" +
                             "-fx-border-color: #6B48FF;" +
                             "-fx-padding: 12;" +
                             "-fx-font-size: 14px;" +
                             "-fx-background-color: #ffffff;");
        GridPane.setConstraints(passwordLabel, 0, 6);
        GridPane.setConstraints(passwordInput, 0, 7);

        // Confirm Password
        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        confirmPasswordLabel.setStyle("-fx-text-fill: #333333;");
        PasswordField confirmPasswordInput = new PasswordField();
        confirmPasswordInput.setPromptText("Confirm Password");
        confirmPasswordInput.setStyle("-fx-background-radius: 25;" +
                                   "-fx-border-radius: 25;" +
                                   "-fx-border-color: #6B48FF;" +
                                   "-fx-padding: 12;" +
                                   "-fx-font-size: 14px;" +
                                   "-fx-background-color: #ffffff;");
        GridPane.setConstraints(confirmPasswordLabel, 0, 8);
        GridPane.setConstraints(confirmPasswordInput, 0, 9);

        formGrid.getChildren().addAll(nameLabel, nameInput, contactLabel, contactInput, 
                                    emailLabel, emailInput, passwordLabel, passwordInput, 
                                    confirmPasswordLabel, confirmPasswordInput);

        // Buttons HBox
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                              "-fx-text-fill: white;" +
                              "-fx-font-size: 16px;" +
                              "-fx-font-weight: bold;" +
                              "-fx-background-radius: 25;" +
                              "-fx-padding: 12 40;" +
                              "-fx-cursor: hand;");

        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.setStyle("-fx-background-color: transparent;" +
                                 "-fx-text-fill: #6B48FF;" +
                                 "-fx-font-size: 16px;" +
                                 "-fx-font-weight: bold;" +
                                 "-fx-background-radius: 25;" +
                                 "-fx-border-color: #6B48FF;" +
                                 "-fx-border-width: 2;" +
                                 "-fx-padding: 10 38;" +
                                 "-fx-cursor: hand;");

        // Hover effects
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: linear-gradient(to right, #5A3CE6, #00C4D3);" +
                                                                    "-fx-text-fill: white;" +
                                                                    "-fx-font-size: 16px;" +
                                                                    "-fx-font-weight: bold;" +
                                                                    "-fx-background-radius: 25;" +
                                                                    "-fx-padding: 12 40;" +
                                                                    "-fx-cursor: hand;" +
                                                                    "-fx-scale-y: 1.05;" +
                                                                    "-fx-scale-x: 1.05;"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                                                                   "-fx-text-fill: white;" +
                                                                   "-fx-font-size: 16px;" +
                                                                   "-fx-font-weight: bold;" +
                                                                   "-fx-background-radius: 25;" +
                                                                   "-fx-padding: 12 40;" +
                                                                   "-fx-cursor: hand;"));

        backToLoginButton.setOnMouseEntered(e -> backToLoginButton.setStyle("-fx-background-color: #6B48FF;" +
                                                                          "-fx-text-fill: white;" +
                                                                          "-fx-font-size: 16px;" +
                                                                          "-fx-font-weight: bold;" +
                                                                          "-fx-background-radius: 25;" +
                                                                          "-fx-border-color: #6B48FF;" +
                                                                          "-fx-border-width: 2;" +
                                                                          "-fx-padding: 10 38;" +
                                                                          "-fx-cursor: hand;" +
                                                                          "-fx-scale-y: 1.05;" +
                                                                          "-fx-scale-x: 1.05;"));
        backToLoginButton.setOnMouseExited(e -> backToLoginButton.setStyle("-fx-background-color: transparent;" +
                                                                         "-fx-text-fill: #6B48FF;" +
                                                                         "-fx-font-size: 16px;" +
                                                                         "-fx-font-weight: bold;" +
                                                                         "-fx-background-radius: 25;" +
                                                                         "-fx-border-color: #6B48FF;" +
                                                                         "-fx-border-width: 2;" +
                                                                         "-fx-padding: 10 38;" +
                                                                         "-fx-cursor: hand;"));

        // Button actions (unchanged)
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

        buttonBox.getChildren().addAll(registerButton, backToLoginButton);

        // Assemble center panel
        centerPanel.getChildren().addAll(logoImageView, titleLabel, subtitleLabel, formGrid, buttonBox);
        centerPanel.setAlignment(Pos.CENTER);

        // Wrap centerPanel in a StackPane to center it within the ScrollPane
        StackPane centerWrapper = new StackPane(centerPanel);
        centerWrapper.setAlignment(Pos.CENTER);

        // ScrollPane for scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setContent(centerWrapper);

        // Add ScrollPane to root
        root.setCenter(scrollPane);
        BorderPane.setAlignment(scrollPane, Pos.CENTER);
        BorderPane.setMargin(scrollPane, new Insets(30));

        // Create the scene
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);

        // Maximize the window to test full-screen centering
        primaryStage.setMaximized(true);

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
