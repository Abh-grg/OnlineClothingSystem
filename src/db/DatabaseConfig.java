package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private final String DB_URL = "jdbc:mysql://localhost/OnlineShopping";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "9816107284";

    private static final DatabaseConfig instance = new DatabaseConfig();

    public static DatabaseConfig getInstance() {
        return instance;
    }

    private DatabaseConfig() {
        // Optionally load the driver explicitly (not needed for newer versions)
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure the driver is loaded
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}