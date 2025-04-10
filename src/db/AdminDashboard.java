package db;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends Application {
    private Stage primaryStage;  // Store stage reference

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Admin Dashboard - Online Clothing");

        // Main container with gradient background
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #6B48FF, #00DDEB);");
        root.setPadding(new Insets(20));

        // Left panel with buttons
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");
        leftPanel.setAlignment(Pos.CENTER);

        // Buttons with LoginPage style
        Button addProductButton = new Button("Add Product");
        addProductButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 12 40;" +
                "-fx-cursor: hand;");

        Button viewProductsButton = new Button("View Products");
        viewProductsButton.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: #6B48FF;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 38;" +
                "-fx-cursor: hand;");

        Button viewUsersButton = new Button("View Users");
        viewUsersButton.setStyle("-fx-background-color: transparent;" +
                "-fx-text-fill: #6B48FF;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-width: 2;" +
                "-fx-padding: 10 38;" +
                "-fx-cursor: hand;");

        Button viewOrderHistoryButton = new Button("View Order History");
        viewOrderHistoryButton.setStyle("-fx-background-color: transparent;" +
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

        leftPanel.getChildren().addAll(addProductButton, viewProductsButton, viewUsersButton, 
                                    viewOrderHistoryButton, logoutButton);

        // Center panel for content
        StackPane centerPane = new StackPane();
        centerPane.setPadding(new Insets(20));
        centerPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95);" +
                "-fx-border-radius: 15;" +
                "-fx-background-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5);");

        // Initial view
        Label initialLabel = new Label("Select an option from the left panel");
        initialLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        initialLabel.setStyle("-fx-text-fill: #666666;");
        centerPane.getChildren().add(initialLabel);

        // Button actions
        addProductButton.setOnAction(e -> centerPane.getChildren().setAll(createAddProductPane()));
        viewProductsButton.setOnAction(e -> centerPane.getChildren().setAll(createProductsPane()));
        viewUsersButton.setOnAction(e -> centerPane.getChildren().setAll(createUsersPane()));
        viewOrderHistoryButton.setOnAction(e -> centerPane.getChildren().setAll(createOrderHistoryPane()));

        // Logout button action
        logoutButton.setOnAction(e -> {
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

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createAddProductPane() {
        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));

        // Title with gradient text
        Label title = new Label("Add New Product");
        title.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 28));
        title.setStyle("-fx-text-fill: linear-gradient(to right, #6B48FF, #00DDEB);");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        Label nameLabel = new Label("Name:");
        nameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        nameLabel.setStyle("-fx-text-fill: #333333;");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");
        nameField.setStyle("-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-padding: 12;" +
                "-fx-font-size: 14px;" +
                "-fx-background-color: #ffffff;");
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);

        Label descLabel = new Label("Description:");
        descLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        descLabel.setStyle("-fx-text-fill: #333333;");
        TextArea descField = new TextArea();
        descField.setPromptText("Enter product description");
        descField.setPrefRowCount(3);
        descField.setStyle("-fx-background-radius: 15;" +
                "-fx-border-radius: 15;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-padding: 10;" +
                "-fx-font-size: 14px;" +
                "-fx-background-color: #ffffff;");
        form.add(descLabel, 0, 1);
        form.add(descField, 1, 1);

        Label priceLabel = new Label("Price:");
        priceLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        priceLabel.setStyle("-fx-text-fill: #333333;");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter price");
        priceField.setStyle("-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-padding: 12;" +
                "-fx-font-size: 14px;" +
                "-fx-background-color: #ffffff;");
        form.add(priceLabel, 0, 2);
        form.add(priceField, 1, 2);

        Label stockLabel = new Label("Stock:");
        stockLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        stockLabel.setStyle("-fx-text-fill: #333333;");
        TextField stockField = new TextField();
        stockField.setPromptText("Enter stock quantity");
        stockField.setStyle("-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-padding: 12;" +
                "-fx-font-size: 14px;" +
                "-fx-background-color: #ffffff;");
        form.add(stockLabel, 0, 3);
        form.add(stockField, 1, 3);

        Label imageLabel = new Label("Image URL:");
        imageLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        imageLabel.setStyle("-fx-text-fill: #333333;");
        TextField imageField = new TextField();
        imageField.setPromptText("Enter image URL");
        imageField.setStyle("-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-padding: 12;" +
                "-fx-font-size: 14px;" +
                "-fx-background-color: #ffffff;");
        form.add(imageLabel, 0, 4);
        form.add(imageField, 1, 4);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 12 40;" +
                "-fx-cursor: hand;");
        submitButton.setOnAction(e -> {
            try {
                addProduct(nameField.getText(), descField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(stockField.getText()),
                        imageField.getText());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully!");
                nameField.clear();
                descField.clear();
                priceField.clear();
                stockField.clear();
                imageField.clear();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Price and Stock must be numeric!");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add product: " + ex.getMessage());
            }
        });

        pane.getChildren().addAll(title, form, submitButton);
        return pane;
    }

    private ScrollPane createProductsPane() {
        TableView<Product> table = new TableView<>();
        table.setPrefWidth(800);
        table.setStyle("-fx-background-color: #ffffff;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;");

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        idCol.setPrefWidth(80);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(100);

        TableColumn<Product, String> imageUrlCol = new TableColumn<>("Image URL");
        imageUrlCol.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        imageUrlCol.setPrefWidth(200);

        table.getColumns().addAll(idCol, nameCol, descCol, priceCol, stockCol, imageUrlCol);
        table.getItems().addAll(fetchProductsFromDatabase());

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private ScrollPane createUsersPane() {
        TableView<User> table = new TableView<>();
        table.setPrefWidth(800);
        table.setStyle("-fx-background-color: #ffffff;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;");

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<User, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactCol.setPrefWidth(150);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, nameCol, contactCol, emailCol, roleCol);
        table.getItems().addAll(fetchUsersFromDatabase());

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox createOrderHistoryPane() {
        TableView<OrderItemHistory> table = new TableView<>();
        table.setPrefWidth(800);
        table.setStyle("-fx-background-color: #ffffff;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;");

        TableColumn<OrderItemHistory, Integer> orderIdCol = new TableColumn<>("Order ID");
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderIdCol.setPrefWidth(80);

        TableColumn<OrderItemHistory, String> customerNameCol = new TableColumn<>("Customer Name");
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerNameCol.setPrefWidth(120);

        TableColumn<OrderItemHistory, String> productNameCol = new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productNameCol.setPrefWidth(150);

        TableColumn<OrderItemHistory, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(80);

        TableColumn<OrderItemHistory, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(80);

        TableColumn<OrderItemHistory, Double> totalPriceCol = new TableColumn<>("Total Price");
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        totalPriceCol.setPrefWidth(100);

        TableColumn<OrderItemHistory, String> paymentMethodCol = new TableColumn<>("Payment Method");
        paymentMethodCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        paymentMethodCol.setPrefWidth(120);

        TableColumn<OrderItemHistory, Timestamp> orderDateCol = new TableColumn<>("Order Date");
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderDateCol.setPrefWidth(150);

        TableColumn<OrderItemHistory, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(orderIdCol, customerNameCol, productNameCol, quantityCol, priceCol, 
                                totalPriceCol, paymentMethodCol, orderDateCol, statusCol);
        table.getItems().addAll(fetchOrderHistoryFromDatabase());

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Pending", "Shipped", "Delivered", "Cancelled");
        statusComboBox.setPromptText("Change Status");
        statusComboBox.setStyle("-fx-background-radius: 25;" +
                "-fx-border-radius: 25;" +
                "-fx-border-color: #6B48FF;" +
                "-fx-padding: 5;" +
                "-fx-font-size: 14px;" +
                "-fx-background-color: #ffffff;");

        Label statusLabel = new Label("Status:");
        statusLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        statusLabel.setStyle("-fx-text-fill: #333333;");

        Button updateStatusButton = new Button("Update Status");
        updateStatusButton.setStyle("-fx-background-color: linear-gradient(to right, #6B48FF, #00DDEB);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 25;" +
                "-fx-padding: 10 30;" +
                "-fx-cursor: hand;");
        updateStatusButton.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateStatusButton.setDisable(false);
                statusComboBox.setValue(newSelection.getStatus());
            } else {
                updateStatusButton.setDisable(true);
                statusComboBox.setValue(null);
            }
        });

        updateStatusButton.setOnAction(e -> {
            OrderItemHistory selectedOrder = table.getSelectionModel().getSelectedItem();
            String newStatus = statusComboBox.getValue();
            if (selectedOrder != null && newStatus != null && !newStatus.equals(selectedOrder.getStatus())) {
                try {
                    updateOrderStatus(selectedOrder.getOrderId(), newStatus);
                    selectedOrder.setStatus(newStatus);
                    table.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Order status updated to " + newStatus);
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update status: " + ex.getMessage());
                }
            } else if (newStatus == null) {
                showAlert(Alert.AlertType.WARNING, "No Status Selected", "Please select a status to update.");
            }
        });

        VBox pane = new VBox(15);
        pane.setPadding(new Insets(20));
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        HBox statusControls = new HBox(15, statusLabel, statusComboBox, updateStatusButton);
        statusControls.setAlignment(Pos.CENTER_LEFT);
        pane.getChildren().addAll(scrollPane, statusControls);

        return pane;
    }

    private void addProduct(String name, String description, double price, int stock, String imageUrl) throws SQLException {
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            String sql = "INSERT INTO Products (name, description, price, stock, image_url) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setInt(4, stock);
            stmt.setString(5, imageUrl);
            stmt.executeUpdate();
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

    private List<User> fetchUsersFromDatabase() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            String sql = "SELECT id, name, contact, email, role FROM Users WHERE role != 'Admin'";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact"),
                        rs.getString("email"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not fetch users: " + ex.getMessage());
        }
        return users;
    }

    private List<OrderItemHistory> fetchOrderHistoryFromDatabase() {
        List<OrderItemHistory> history = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            String sql = "SELECT o.order_id, u.name AS customer_name, p.name AS product_name, oi.quantity, " +
                        "oi.price, o.total_price, o.payment_method, o.order_date, o.status " +
                        "FROM Orders o " +
                        "JOIN Users u ON o.customer_id = u.id " +
                        "JOIN Order_Items oi ON o.order_id = oi.order_id " +
                        "JOIN Products p ON oi.product_id = p.product_id";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                history.add(new OrderItemHistory(
                        rs.getInt("order_id"),
                        rs.getString("customer_name"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("total_price"),
                        rs.getString("payment_method"),
                        rs.getTimestamp("order_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not fetch order history: " + ex.getMessage());
        }
        return history;
    }

    private void updateOrderStatus(int orderId, String newStatus) throws SQLException {
        try (Connection connection = DatabaseConfig.getInstance().getConnection()) {
            String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No order found with ID " + orderId);
            }
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

    public static class Product {
        private int productId;
        private String name;
        private String description;
        private double price;
        private int stock;
        private String imageUrl;

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

    public static class User {
        private int id;
        private String name;
        private String contact;
        private String email;
        private String role;

        public User(int id, String name, String contact, String email, String role) {
            this.id = id;
            this.name = name;
            this.contact = contact;
            this.email = email;
            this.role = role;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getContact() { return contact; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }

    public static class OrderItemHistory {
        private int orderId;
        private String customerName;
        private String productName;
        private int quantity;
        private double price;
        private double totalPrice;
        private String paymentMethod;
        private Timestamp orderDate;
        private String status;

        public OrderItemHistory(int orderId, String customerName, String productName, int quantity, double price,
                                double totalPrice, String paymentMethod, Timestamp orderDate, String status) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
            this.totalPrice = totalPrice;
            this.paymentMethod = paymentMethod;
            this.orderDate = orderDate;
            this.status = status;
        }

        public int getOrderId() { return orderId; }
        public String getCustomerName() { return customerName; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getTotalPrice() { return totalPrice; }
        public String getPaymentMethod() { return paymentMethod; }
        public Timestamp getOrderDate() { return orderDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
