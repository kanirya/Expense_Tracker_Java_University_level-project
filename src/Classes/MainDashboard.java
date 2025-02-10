package Classes;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private JPanel mainPanel;

    public MainDashboard() {
        setTitle("Dashboard - Expense Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu Panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 4));

        JButton dashboardButton = new JButton("Dashboard");
        JButton transactionButton = new JButton("Transaction");
        JButton categoryButton = new JButton("Category");
        JButton usersButton = new JButton("View Users");

        menuPanel.add(dashboardButton);
        menuPanel.add(transactionButton);
        menuPanel.add(categoryButton);
        menuPanel.add(usersButton);

        // Main Panel (Default: TransactionHistoryPanel)
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        switchPanel(new TransactionHistoryPanel()); // Show TransactionHistoryPanel by default

        add(menuPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Button Actions
        dashboardButton.addActionListener(e -> switchPanel(new TransactionHistoryPanel()));  // Show by default
        transactionButton.addActionListener(e -> switchPanel(new AddTransactionPanel()));
        categoryButton.addActionListener(e -> switchPanel(new AddCategoryPanel()));
        usersButton.addActionListener(e -> switchPanel(new UserListPanel()));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void switchPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainDashboard::new);
    }
}
