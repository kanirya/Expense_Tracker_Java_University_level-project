package View;

import model.DbConnection;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class AddCategoryPanel extends JPanel {
    private JTextField categoryField;
    private JComboBox<String> categoryTypeCombo;
    private JButton addButton;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    public AddCategoryPanel() {
        setLayout(new BorderLayout(10, 10));

        // Panel for Adding Category
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Category"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Add New Category");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addPanel.add(titleLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        addPanel.add(new JLabel("Category Name:"), gbc);

        gbc.gridx = 1;
        categoryField = new JTextField(15);
        addPanel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        addPanel.add(new JLabel("Category Type:"), gbc);

        gbc.gridx = 1;
        categoryTypeCombo = new JComboBox<>(new String[]{"Income", "Expense"});
        addPanel.add(categoryTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        addButton = new JButton("Add Category");
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.addActionListener(e -> addCategory());
        addPanel.add(addButton, gbc);

        // Panel for Listing Categories
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Existing Categories"));

        tableModel = new DefaultTableModel(new String[]{"Name", "Type", "Action"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only the "Action" column is editable
            }
        };

        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(30);

        // Set custom renderer and editor
        categoryTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        categoryTable.getColumn("Action").setCellEditor(new ButtonEditor(this));

        loadCategories();

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Add Sections to Main Panel
        add(addPanel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.CENTER);
    }

    private void addCategory() {
        String category = categoryField.getText().trim();
        String categoryType = categoryTypeCombo.getSelectedItem().toString();

        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO ExpenseCategories (Name, Type) VALUES (?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category);
            stmt.setString(2, categoryType);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Category added successfully!");
            categoryField.setText("");
            categoryTypeCombo.setSelectedIndex(0);
            loadCategories();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCategories() {
        tableModel.setRowCount(0); // Clear previous data
        String sql = "SELECT * FROM ExpenseCategories";

        try (Connection connection = DbConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("Name"));
                row.add(rs.getString("Type"));
                row.add("Delete"); // Placeholder for button
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteCategory(String categoryName) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM ExpenseCategories WHERE Name = ?";

            try (Connection connection = DbConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, categoryName);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category deleted successfully!");
                loadCategories();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting category: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

// ======================== BUTTON RENDERER ==========================
class ButtonRenderer extends JPanel implements TableCellRenderer {
    public ButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JButton button = new JButton(value.toString());
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
}

// ======================== BUTTON EDITOR ==========================
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String categoryName;
    private AddCategoryPanel panel;
    private JTable table;

    public ButtonEditor(AddCategoryPanel panel) {
        super(new JTextField());
        this.panel = panel;
        this.button = new JButton("Delete");
        button.setBackground(Color.RED);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(e -> deleteCategory());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        categoryName = table.getValueAt(row, 0).toString();
        return button;
    }

    private void deleteCategory() {
        panel.deleteCategory(categoryName);
        fireEditingStopped(); // Stop cell editing
    }
}
