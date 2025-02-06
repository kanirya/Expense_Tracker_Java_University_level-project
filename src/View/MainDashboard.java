package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class MainDashboard extends JFrame {
    private JPanel mainPanel;

    public MainDashboard() {
        setTitle("Dashboard - Expense Tracker");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 4));

        JButton addCategoryButton = new JButton("Add Category");
        JButton addTransactionButton = new JButton("Add Transaction");
        JButton viewTransactionsButton = new JButton("View Transactions");
        JButton viewUsersButton = new JButton("View Users");

        menuPanel.add(addCategoryButton);
        menuPanel.add(addTransactionButton);
        menuPanel.add(viewTransactionsButton);
        menuPanel.add(viewUsersButton);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        add(menuPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

//        addCategoryButton.addActionListener(e -> switchPanel(new AddCategoryPanel()));
//        addTransactionButton.addActionListener(e -> switchPanel(new AddTransactionPanel()));
//        viewTransactionsButton.addActionListener(e -> switchPanel(new TransactionHistoryPanel()));
//        viewUsersButton.addActionListener(e -> switchPanel(new UserListPanel()));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void switchPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
