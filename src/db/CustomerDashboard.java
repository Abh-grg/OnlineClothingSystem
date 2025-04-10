package db;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDashboard extends Application {

    private List<CartItem> cartItems = new ArrayList<>();
    private VBox cartPane;
    private Label totalPriceLabel;

    public CustomerDashboard() {
        // No need for customerId parameter since we’re using GlobalVar.id
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Customer Dashboard - Online Clothing");

        // Main container with gradient background
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #6B48FF, #00DDEB);");
        root.setPadding(new Insets(20));

        // Left panel for buttons with transparency
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        leftPanel.setAlignment(Pos.CENTER);

        // Buttons with LoginPage style
        Button collectionsButton = new Button("Collections");
        collectionsButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 12 40;" +
                "-fx-cursor: hand;");

        Button cartButton = new Button("View Cart");
        cartButton.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: #6B48FF;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 38;" +
                "-fx-cursor: hand;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: #6B48FF;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 38;" +
                "-fx-cursor: hand;");

        leftPanel.getChildren().addAll(collectionsButton, cartButton, logoutButton);

        // Center panel with transparency
        StackPane centerPane = new StackPane();
        centerPane.setPadding(new Insets(20));
        centerPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        ScrollPane scrollPane = new ScrollPane(createCollectionsPane());
        scrollPane.setFitToWidth(true);
        cartPane = createCartPane();

        centerPane.getChildren().add(scrollPane);

        collectionsButton.setOnAction(e -> {
            centerPane.getChildren().clear();
            centerPane.getChildren().add(new ScrollPane(createCollectionsPane()));
        });

        cartButton.setOnAction(e -> {
            centerPane.getChildren().clear();
            updateCartPane();
            centerPane.getChildren().add(cartPane);
        });

        // Logout button action (unchanged)
        logoutButton.setOnAction(e -> {
            cartItems.clear();
            GlobalVar.id = 0;
            showAlert(Alert.AlertType.INFORMATION, "Logout", "Logout successful!");
            primaryStage.close();
            try {
                new LoginPage().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Logout Error", 
                    "Failed to open login page: " + ex.getMessage());
            }
        });

        root.setLeft(leftPanel);
        root.setCenter(centerPane);
        BorderPane.setMargin(leftPanel, new Insets(20));
        BorderPane.setMargin(centerPane, new Insets(20));

        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createCollectionsPane() {
        VBox pane = new VBox(20);
        pane.setPadding(new Insets(20));
        pane.setAlignment(Pos.TOP_CENTER);

        // Title with gradient text
        Label title = new Label("Product Collections");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        title.setStyle("-fx-text-fill: linear-gradient(to right, #6B48FF, #00DDEB);");

        List<Product> products = fetchProductsFromDatabase();
        if (products.isEmpty()) {
            Label noProductsLabel = new Label("No products available. Showing sample items.");
            noProductsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            noProductsLabel.setStyle("-fx-text-fill: #666666;");
            products = getDefaultProducts();
            pane.getChildren().add(noProductsLabel);
        }

        // Calculate total price of displayed products
        double totalPrice = products.stream().mapToDouble(Product::getPrice).sum();
        Label totalPriceLabel = new Label("Total Price of Products: $" + String.format("%.2f", totalPrice));
        totalPriceLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        totalPriceLabel.setStyle("-fx-text-fill: #333333;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        int col = 0, row = 0;
        for (Product product : products) {
            VBox itemFrame = new VBox(15);
            itemFrame.setPadding(new Insets(15));
            itemFrame.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" +
                    "-fx-border-radius: 10;" +
                    "-fx-background-radius: 10;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 3);");
            itemFrame.setAlignment(Pos.CENTER);
            itemFrame.setPrefWidth(250);

            ImageView imageView;
            try {
                imageView = new ImageView(new Image(product.getImageUrl() != null && !product.getImageUrl().isEmpty() 
                    ? product.getImageUrl() 
                    : "file:/path/to/default.png"));
            } catch (IllegalArgumentException e) {
                imageView = new ImageView(new Image("file:/path/to/default.png"));
            }
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            Label nameLabel = new Label(product.getName());
            nameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
            nameLabel.setStyle("-fx-text-fill: #333333;");

            Label priceLabel = new Label("$" + String.format("%.2f", product.getPrice()));
            priceLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            priceLabel.setStyle("-fx-text-fill: #666666;");

            Label descLabel = new Label(product.getDescription() != null ? product.getDescription() : "No description");
            descLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
            descLabel.setStyle("-fx-text-fill: #666666;");
            descLabel.setWrapText(true);
            descLabel.setMaxWidth(220);

            Button addButton = new Button("Add to Cart");
            addButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 25;" +
                    "-fx-padding: 10 30;" +
                    "-fx-cursor: hand;");
            addButton.setOnAction(e -> {
                cartItems.add(new CartItem(product, 1));
                showAlert(Alert.AlertType.INFORMATION, "Added", product.getName() + " added to cart!");
            });

            itemFrame.getChildren().addAll(imageView, nameLabel, priceLabel, descLabel, addButton);
            grid.add(itemFrame, col, row);
            col++;
            if (col > 2) {
                col = 0;
                row++;
            }
        }

        pane.getChildren().addAll(title, totalPriceLabel, grid);
        return pane;
    }

    private VBox createCartPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));

        // Title with gradient text
        Label title = new Label("Your Cart");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        title.setStyle("-fx-text-fill: linear-gradient(to right, #6B48FF, #00DDEB);");

        totalPriceLabel = new Label("Total: $0.00");
        totalPriceLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        totalPriceLabel.setStyle("-fx-text-fill: #333333;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button buyButton = new Button("Buy");
        buyButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 12 40;" +
                "-fx-cursor: hand;");

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: #6B48FF;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 38;" +
                "-fx-cursor: hand;");

        buyButton.setOnAction(e -> handleBuyAction());
        cancelButton.setOnAction(e -> {
            cartItems.clear();
            updateCartPane();
        });

        buttonBox.getChildren().addAll(buyButton, cancelButton);
        pane.getChildren().addAll(title, totalPriceLabel, buttonBox);
        return pane;
    }

    private void updateCartPane() {
        cartPane.getChildren().clear();
        Label title = new Label("Your Cart");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        title.setStyle("-fx-text-fill: linear-gradient(to right, #6B48FF, #00DDEB);");
        cartPane.getChildren().add(title);

        if (cartItems.isEmpty()) {
            Label emptyLabel = new Label("Cart is empty.");
            emptyLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            emptyLabel.setStyle("-fx-text-fill: #666666;");
            cartPane.getChildren().add(emptyLabel);
        } else {
            double totalPrice = 0.0;
            for (CartItem item : cartItems) {
                HBox itemBox = new HBox(15);
                itemBox.setAlignment(Pos.CENTER_LEFT);
                Label itemLabel = new Label(item.getProduct().getName() + " - $" + String.format("%.2f", item.getProduct().getPrice()));
                itemLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                itemLabel.setStyle("-fx-text-fill: #333333;");
                Spinner<Integer> quantitySpinner = new Spinner<>(1, item.getProduct().getStock(), item.getQuantity());
                quantitySpinner.setStyle("-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-border-color: #6B48FF;" +
                        "-fx-padding: 5;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-color: #ffffff;");
                quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
                    item.setQuantity(newVal);
                    updateTotalPrice();
                });
                itemBox.getChildren().addAll(itemLabel, quantitySpinner);
                cartPane.getChildren().add(itemBox);
                totalPrice += item.getProduct().getPrice() * item.getQuantity();
            }
            totalPriceLabel.setText("Total: $" + String.format("%.2f", totalPrice));
        }

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button buyButton = new Button("Buy");
        buyButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 12 40;" +
                "-fx-cursor: hand;");

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: #6B48FF;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 38;" +
                "-fx-cursor: hand;");

        buyButton.setOnAction(e -> handleBuyAction());
        cancelButton.setOnAction(e -> {
            cartItems.clear();
            updateCartPane();
        });

        buttonBox.getChildren().addAll(buyButton, cancelButton);
        cartPane.getChildren().addAll(totalPriceLabel, buttonBox);
    }

    private void updateTotalPrice() {
        double totalPrice = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
        totalPriceLabel.setText("Total: $" + String.format("%.2f", totalPrice));
    }

    private void handleBuyAction() {
        if (cartItems.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Cart", "Your cart is empty!");
            return;
        }

        // Show payment method dialog
        Stage paymentStage = new Stage();
        paymentStage.initModality(Modality.APPLICATION_MODAL);
        paymentStage.setTitle("Select Payment Method");

        VBox paymentPane = new VBox(15);
        paymentPane.setPadding(new Insets(20));
        paymentPane.setAlignment(Pos.CENTER);
        paymentPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        Label paymentLabel = new Label("Choose a Payment Method:");
        paymentLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        paymentLabel.setStyle("-fx-text-fill: #333333;");

        ToggleGroup paymentGroup = new ToggleGroup();
        RadioButton creditCard = new RadioButton("Credit Card");
        RadioButton paypal = new RadioButton("PayPal");
        RadioButton cod = new RadioButton("Cash on Delivery");
        creditCard.setToggleGroup(paymentGroup);
        paypal.setToggleGroup(paymentGroup);
        cod.setToggleGroup(paymentGroup);
        creditCard.setSelected(true);
        creditCard.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        paypal.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");
        cod.setStyle("-fx-font-size: 14px; -fx-text-fill: #333333;");

        Button confirmButton = new Button("Confirm Payment");
        confirmButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 12 40;" +
                "-fx-cursor: hand;");
        confirmButton.setOnAction(e -> {
            RadioButton selectedMethod = (RadioButton) paymentGroup.getSelectedToggle();
            if (selectedMethod != null) {
                String paymentMethod = selectedMethod.getText();
                processOrder(paymentMethod);
                paymentStage.close();
            } else {
                showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a payment method!");
            }
        });

        paymentPane.getChildren().addAll(paymentLabel, creditCard, paypal, cod, confirmButton);
        Scene paymentScene = new Scene(paymentPane, 300, 250);
        paymentScene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        paymentStage.setScene(paymentScene);
        paymentStage.showAndWait();
    }

    private void processOrder(String paymentMethod) {
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            connection.setAutoCommit(false);

            String orderSql = "INSERT INTO Orders (customer_id, total_price, payment_method) VALUES (?, ?, ?)";
            PreparedStatement orderStmt = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            double totalPrice = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
            orderStmt.setInt(1, GlobalVar.id);
            orderStmt.setDouble(2, totalPrice);
            orderStmt.setString(3, paymentMethod);
            orderStmt.executeUpdate();

            ResultSet rs = orderStmt.getGeneratedKeys();
            int orderId = rs.next() ? rs.getInt(1) : -1;
            if (orderId == -1) throw new SQLException("Failed to retrieve order ID");

            String itemSql = "INSERT INTO Order_Items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement itemStmt = connection.prepareStatement(itemSql);
            for (CartItem item : cartItems) {
                itemStmt.setInt(1, orderId);
                itemStmt.setInt(2, item.getProduct().getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getProduct().getPrice());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            String stockSql = "UPDATE Products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";
            PreparedStatement stockStmt = connection.prepareStatement(stockSql);
            for (CartItem item : cartItems) {
                stockStmt.setInt(1, item.getQuantity());
                stockStmt.setInt(2, item.getProduct().getProductId());
                stockStmt.setInt(3, item.getQuantity());
                stockStmt.addBatch();
            }
            int[] stockUpdates = stockStmt.executeBatch();
            for (int update : stockUpdates) {
                if (update == 0) throw new SQLException("Insufficient stock for one or more items");
            }

            connection.commit();

            StringBuilder orderDetails = new StringBuilder("New Order Received (Order ID: " + orderId + "):\n");
            orderDetails.append("Customer ID: ").append(GlobalVar.id).append("\n");
            orderDetails.append("Payment Method: ").append(paymentMethod).append("\n");
            for (CartItem item : cartItems) {
                orderDetails.append(item.getProduct().getName()).append(" - Quantity: ").append(item.getQuantity())
                        .append(" - Price: $").append(item.getProduct().getPrice() * item.getQuantity()).append("\n");
            }
            orderDetails.append("Total: $").append(String.format("%.2f", totalPrice));
            System.out.println(orderDetails.toString());

            showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Order placed successfully with " + paymentMethod + "!");
            cartItems.clear();
            updateCartPane();
        } catch (SQLException ex) {
            try (Connection conn = DatabaseConfig.getInstance().getConnection()) {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Order Error", "Failed to place order: " + ex.getMessage());
        }
    }

    private List<Product> fetchProductsFromDatabase() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            String sql = "SELECT product_id, name, description, price, stock, image_url FROM Products";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("image_url")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not fetch products: " + ex.getMessage());
        }
        return products;
    }

    private List<Product> getDefaultProducts() {
        List<Product> defaultProducts = new ArrayList<>();
        String defaultImage = "file:/path/to/default.png";
        for (int i = 1; i <= 20; i++) {
            defaultProducts.add(new Product(
                    -i,
                    "Sample Item " + i,
                    "Sample description for Item " + i,
                    19.99 + (i * 5.0),
                    50,
                    defaultImage
            ));
        }
        return defaultProducts;
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

class Product {
    private final int productId;
    private final String name;
    private final String description;
    private final double price;
    private final int stock;
    private final String imageUrl;

    public Product(int productId, String name, String description, double price, int stock, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
}

class CartItem {
    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
