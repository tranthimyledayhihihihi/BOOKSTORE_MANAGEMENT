
package GUI;

import BLL.CategoryBLL;
import model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class categoryGUI extends JPanel {
    private final CategoryBLL categoryBLL;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    public categoryGUI() {
        categoryBLL = new CategoryBLL();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.EAST);
        loadCategories();
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "Tên", "Số lượng sách", "Mô tả"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.getSelectionModel().addListSelectionListener(e -> populateFields());
        JScrollPane tableScrollPane = new JScrollPane(categoryTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 400));
        return tableScrollPane;
    }

    private JPanel createFormPanel() {
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setPreferredSize(new Dimension(320, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tên danh mục
        gbc.gridx = 0; gbc.gridy = 0;
        controlPanel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(18);
        controlPanel.add(nameField, gbc);

        // Mô tả
        gbc.gridx = 0; gbc.gridy++;
        controlPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 18);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        controlPanel.add(new JScrollPane(descriptionArea), gbc);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Thêm");
        updateButton = new JButton("Cập nhật");
        deleteButton = new JButton("Xóa");
        refreshButton = new JButton("Làm mới");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        controlPanel.add(buttonPanel, gbc);

        // Xử lý sự kiện cho các nút
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        refreshButton.addActionListener(e -> refreshCategories());

        return controlPanel;
    }

    private void loadCategories() {
        tableModel.setRowCount(0);
        List<Category> categories = categoryBLL.getAllCategories();
        for (Category category : categories) {
            tableModel.addRow(new Object[]{
                category.getCategoryId(),
                category.getName(),
                category.getBookCount(),
                category.getDescription()
            });
        }
    }

    private void populateFields() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            descriptionArea.setText((String) tableModel.getValueAt(selectedRow, 3));
        }
    }

    private void addCategory() {
        try {
            Category category = new Category();
            category.setName(nameField.getText().trim());
            category.setDescription(descriptionArea.getText().trim().isEmpty() ? null : descriptionArea.getText().trim());
            if (categoryBLL.addCategory(category)) {
                JOptionPane.showMessageDialog(this, "Thêm danh mục thành công!");
                loadCategories();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm danh mục thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        try {
            int selectedRow = categoryTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Category category = new Category();
            category.setCategoryId((Integer) tableModel.getValueAt(selectedRow, 0));
            category.setName(nameField.getText().trim());
            category.setDescription(descriptionArea.getText().trim().isEmpty() ? null : descriptionArea.getText().trim());
            if (categoryBLL.updateCategory(category)) {
                JOptionPane.showMessageDialog(this, "Cập nhật danh mục thành công!");
                loadCategories();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật danh mục thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        try {
            int selectedRow = categoryTable.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int categoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa danh mục này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (categoryBLL.deleteCategory(categoryId)) {
                    JOptionPane.showMessageDialog(this, "Xóa danh mục thành công!");
                    loadCategories();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa danh mục thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshCategories() {
        loadCategories();
        clearFields();
    }

    private void clearFields() {
        nameField.setText("");
        descriptionArea.setText("");
        categoryTable.clearSelection();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản Lý Danh Mục");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new categoryGUI());
            frame.setVisible(true);
        });
    }
}
