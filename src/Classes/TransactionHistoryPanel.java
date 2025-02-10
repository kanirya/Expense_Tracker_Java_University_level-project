package Classes;

import model.DbConnection;
import model.Global;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class TransactionHistoryPanel extends JPanel {
    private JLabel totalIncomeLabel, totalExpenseLabel, netAmountLabel;
    private DefaultTableModel tableModel;

    public TransactionHistoryPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240)); // Light gray background

        // Dashboard Panel
        JPanel dashboardPanel = new JPanel(new GridLayout(1, 3, 20, 10));
        dashboardPanel.setBackground(new Color(240, 240, 240));
        dashboardPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        totalIncomeLabel = createDashboardLabel("Total Income", new Color(34, 139, 34)); // Green
        totalExpenseLabel = createDashboardLabel("Total Expense", new Color(178, 34, 34)); // Red
        netAmountLabel = createDashboardLabel("Net Amount", new Color(70, 130, 180)); // Blue

        dashboardPanel.add(totalIncomeLabel);
        dashboardPanel.add(totalExpenseLabel);
        dashboardPanel.add(netAmountLabel);

        add(dashboardPanel, BorderLayout.NORTH);

        // Transaction Table
        String[] columnNames = {"ID", "Category", "Amount", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(70, 130, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(new Color(200, 200, 200));

        // Center-align "Amount" column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Load Data
        loadTransactionHistory();
        updateDashboard();
    }

    private JLabel createDashboardLabel(String title, Color color) {
        JLabel label = new JLabel(title + ": Rs 0.00", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(color);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return label;
    }

    private void loadTransactionHistory() {
        tableModel.setRowCount(0); // Clear existing data
        String username = Global.Username; // Get the logged-in user's username

        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Username not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT t.Id, c.Name, t.Amount, t.TransactionDate FROM Transactions t " +
                             "JOIN ExpenseCategories c ON t.CategoryId = c.Id WHERE t.Username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] rowData = {
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        String.format("Rs%.2f", rs.getDouble("Amount")),
                        rs.getTimestamp("TransactionDate")
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDashboard() {
        double totalIncome = 0;
        double totalExpense = 0;
        String username = Global.Username; // Get the logged-in user

        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Username not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT SUM(Amount) AS Total FROM Transactions WHERE Username = ? AND Amount > 0")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalIncome = rs.getDouble("Total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT SUM(Amount) AS Total FROM Transactions WHERE Username = ? AND Amount < 0")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                totalExpense = rs.getDouble("Total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double netAmount = totalIncome + totalExpense;

        totalIncomeLabel.setText("Total Income: Rs " + String.format("%.2f", totalIncome));
        totalExpenseLabel.setText("Total Expense: Rs " + String.format("%.2f", totalExpense));
        netAmountLabel.setText("Net Amount: Rs " + String.format("%.2f", netAmount));
    }
}
