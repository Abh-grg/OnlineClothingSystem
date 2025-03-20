package db;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Admin Dashboard");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Left panel with buttons
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        Button addProductButton = new Button("Add Product");
        Button viewUsersButton = new Button("View Users");
        Button viewOrderHistoryButton = new Button("View Order History");
        leftPanel.getChildren().addAll(addProductButton, viewUsersButton, viewOrderHistoryButton);

        // Center panel for content
        StackPane centerPane = new StackPane();

        // Button actions
        addProductButton.setOnAction(e -> centerPane.getChildren().setAll(createAddProductPane()));
        viewUsersButton.setOnAction(e -> centerPane.getChildren().setAll(createUsersPane()));
        viewOrderHistoryButton.setOnAction(e -> centerPane.getChildren().setAll(createOrderHistoryPane()));

        // Initial view
        centerPane.getChildren().add(new Label("Select an option from the left panel"));

        root.setLeft(leftPanel);
        root.setCenter(centerPane);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createAddProductPane() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));

        Label title = new Label("Add New Product");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);

        Label descLabel = new Label("Description:");
        TextArea descField = new TextArea();
        descField.setPrefRowCount(3);
        form.add(descLabel, 0, 1);
        form.add(descField, 1, 1);

        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();
        form.add(priceLabel, 0, 2);
        form.add(priceField, 1, 2);

        Label stockLabel = new Label("Stock:");
        TextField stockField = new TextField();
        form.add(stockLabel, 0, 3);
        form.add(stockField, 1, 3);

        Label imageLabel = new Label("Image URL:");
        TextField imageField = new TextField();
        form.add(imageLabel, 0, 4);
        form.add(imageField, 1, 4);

        Button submitButton = new Button("Submit");
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

    private ScrollPane createUsersPane() {
        TableView<User> table = new TableView<>();
        table.setPrefWidth(800);

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(idCol, nameCol, contactCol, emailCol, roleCol);
        table.getItems().addAll(fetchUsersFromDatabase());

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox createOrderHistoryPane() {
        // TableView for order history
        TableView<OrderItemHistory> table = new TableView<>();
        table.setPrefWidth(800);

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

        table.getColumns().addAll(orderIdCol, customerNameCol, productNameCol, quantityCol, priceCol, totalPriceCol, paymentMethodCol, orderDateCol, statusCol);
        table.getItems().addAll(fetchOrderHistoryFromDatabase());

        // ComboBox for status selection
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Pending", "Shipped", "Delivered", "Cancelled");
        statusComboBox.setPromptText("Change Status");

        // Button to apply status change
        Button updateStatusButton = new Button("Update Status");
        updateStatusButton.setDisable(true); // Disabled until an order is selected

        // Enable button and set ComboBox value when an order is selected
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                updateStatusButton.setDisable(false);
                statusComboBox.setValue(newSelection.getStatus());
            } else {
                updateStatusButton.setDisable(true);
                statusComboBox.setValue(null);
            }
        });

        // Action to update status in database and TableView
        updateStatusButton.setOnAction(e -> {
            OrderItemHistory selectedOrder = table.getSelectionModel().getSelectedItem();
            String newStatus = statusComboBox.getValue();
            if (selectedOrder != null && newStatus != null && !newStatus.equals(selectedOrder.getStatus())) {
                try {
                    updateOrderStatus(selectedOrder.getOrderId(), newStatus);
                    // Update the TableView by refreshing the data
                    selectedOrder.setStatus(newStatus); // Update the object (requires setter)
                    table.refresh();
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Order status updated to " + newStatus);
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update status: " + ex.getMessage());
                }
            } else if (newStatus == null) {
                showAlert(Alert.AlertType.WARNING, "No Status Selected", "Please select a status to update.");
            }
        });

        // Layout for table and status controls
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToWidth(true);
        HBox statusControls = new HBox(10, new Label("Status:"), statusComboBox, updateStatusButton);
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

    // Static nested class for User
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

    // Static nested class for OrderItemHistory with setter for status
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
        public void setStatus(String status) { this.status = status; } // Added setter for status
    }
}