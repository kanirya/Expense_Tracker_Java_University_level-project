package model;
import java.sql.*;
public class DbConnection {
    private static Connection connection;
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=OOPsProject;encrypt=true;trustServerCertificate=true";
    private static final String USER = "danish";
    private static final String PASSWORD = "danishkaneria";

    // Private constructor to prevent instantiation
    private void DbConnection() {}

    // Public method to get the single connection instance
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (SQLException e) {
                System.out.println("Error connecting to the database: " + e.getMessage());
            }
        }
        return connection;
    }
}