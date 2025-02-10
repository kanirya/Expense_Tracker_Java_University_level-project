package model;
import java.sql.*;

public class DbConnection {
    private static Connection connection;
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=OOPsProject;encrypt=true;trustServerCertificate=true";
    private static final String USER = "danish";
    private static final String PASSWORD = "danishkaneria";

    // ✅ Correct Singleton Constructor
    private DbConnection() {}

    // ✅ Singleton Connection (Persistent)
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database:");
            e.printStackTrace();
        }
        return connection;
    }

    // ✅ Add a Method to Close Connection Manually (if needed)
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing the connection:");
            e.printStackTrace();
        }
    }
}
