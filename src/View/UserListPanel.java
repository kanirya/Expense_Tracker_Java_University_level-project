package View;

import model.DbConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserListPanel extends JPanel {

    public UserListPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240)); // Light background

        JLabel titleLabel = new JLabel("Registered Users", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(30, 30, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table Model
        String[] columnNames = {"Username"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(new Color(200, 200, 200));

        // Fetch Data
        fetchUsers(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Delete Button
        JButton deleteButton = new JButton("Delete User");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(255, 99, 71)); // Red color
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteUser(table, tableModel));
        add(deleteButton, BorderLayout.SOUTH);
    }

    // Method to fetch and display users in the table
    private void fetchUsers(DefaultTableModel tableModel) {
        String sql = "SELECT username FROM Users";

        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] rowData = {
                        rs.getString("username") // Only Username
                };
                tableModel.addRow(rowData);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete the selected user
    private void deleteUser(JTable table, DefaultTableModel tableModel) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) table.getValueAt(selectedRow, 0); // Get selected username
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the user: " + username + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Proceed to delete the user from the database
            String sql = "DELETE FROM Users WHERE username = ?";

            try (Connection connection = DbConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setString(1, username);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Remove the row from the table if deletion was successful
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "User deletion failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
