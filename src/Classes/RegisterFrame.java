package Classes;

import model.DbConnection;
import model.Global;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton, backButton;

    public RegisterFrame() {
        setTitle("Register - Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel titleLabel = new JLabel("Register", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setPreferredSize(new Dimension(150, 25));
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(150, 25));
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        registerButton = new JButton("Register");
        backButton = new JButton("Back");
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        add(buttonPanel, gbc);

        // Button Listeners
        registerButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DbConnection.getConnection()) {
            if (connection == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check if the username already exists
            String checkUserQuery = "SELECT COUNT(*) FROM Users WHERE username = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUserQuery)) {
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Username already exists. Choose a different one.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Insert new user
            String query = "INSERT INTO Users (username, password) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "User Registered Successfully!");
                dispose();
                new LoginFrame();
                Global.Username=username;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
