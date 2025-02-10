package View;

import model.DbConnection;
import model.Global;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AddTransactionPanel extends JPanel {
    private JTextField amountField;
    private JComboBox<String> categoryDropdown;
    private JButton addButton;
    private HashMap<String, Integer> categoryMap;  // Stores category names with IDs
    private HashMap<String, String> categoryTypeMap; // Stores category types (Income/Expense)

    public AddTransactionPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Add Transaction");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        amountField = new JTextField(10);
        add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Category:"), gbc);

        gbc.gridx = 1;
        categoryDropdown = new JComboBox<>();
        categoryMap = new HashMap<>();
        categoryTypeMap = new HashMap<>();
        loadCategories();
        add(categoryDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        addButton = new JButton("Add Transaction");
        add(addButton, gbc);

        addButton.addActionListener(e -> addTransaction());
    }

    private void loadCategories() {
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT Id, Name, Type FROM ExpenseCategories");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int categoryId = rs.getInt("Id");
                String categoryName = rs.getString("Name");
                String categoryType = rs.getString("Type"); // Fetch type

                categoryMap.put(categoryName, categoryId); // Store ID
                categoryTypeMap.put(categoryName, categoryType); // Store type
                categoryDropdown.addItem(categoryName); // Show category name
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTransaction() {
        String amountText = amountField.getText().trim();
        String selectedCategory = (String) categoryDropdown.getSelectedItem();
        String username = Global.Username; // Get current logged-in user

        if (amountText.isEmpty() || selectedCategory == null || username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int categoryId = categoryMap.get(selectedCategory); // Get category ID
        String categoryType = categoryTypeMap.get(selectedCategory); // Get category type
        double amount = Double.parseDouble(amountText);

        // If the category type is "Expense", make the amount negative
        if ("Expense".equalsIgnoreCase(categoryType)) {
            amount *= -1;
        }

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "INSERT INTO Transactions (Username, CategoryId, Amount, TransactionDate) VALUES (?, ?, ?, GETDATE())")) {

            stmt.setString(1, username); // Store the username
            stmt.setInt(2, categoryId);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Transaction added successfully!");
            amountField.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
