package GUI;
import GUI.HomeGUI;
import GUI.BookStorePanel;
import GUI.categoryGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainGUI extends JFrame {
    private int customerId;
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color MENU_BG_COLOR = new Color(44, 62, 80);
    private static final Color MENU_HOVER_COLOR = new Color(52, 73, 94);
    private static final Color MENU_ACTIVE_COLOR = new Color(41, 128, 185);
    private static final Color MENU_TEXT_COLOR = new Color(236, 240, 241);

    private JScrollPane mainScrollPane;
    private JPanel mainContentPanel; // Panel chính chứa nội dung
    private String currentMenu = "Trang chủ";
    private java.util.List<JPanel> menuPanels = new ArrayList<>();

    public MainGUI(int customerId) {
        this.customerId = customerId;
        setTitle("Nhà Sách Online");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        add(new HeaderPanel(this), BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(MENU_BG_COLOR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        String[][] menuItems = {
            {"🏠", "Trang chủ"},
            {"📚", "Cửa hàng"},
            {"🛒", "Giỏ hàng"},
            {"📦", "Đơn hàng"},
            {"👤", "Tài khoản"}
        };

        for (String[] item : menuItems) {
            JPanel menuItem = createMenuItem(item[0], item[1]);
            sidebar.add(menuItem);
            sidebar.add(Box.createVerticalStrut(5));
            menuPanels.add(menuItem);
        }
        sidebar.add(Box.createVerticalGlue());
        JLabel copyright = new JLabel("© 2024 Nhà Sách Online");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyright.setForeground(MENU_TEXT_COLOR);
        copyright.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(copyright);

        add(sidebar, BorderLayout.WEST);

        // Main content panel - Đây là panel chính chứa nội dung
        mainContentPanel = new JPanel(new BorderLayout());
        mainScrollPane = new JScrollPane(mainContentPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(mainScrollPane, BorderLayout.CENTER);

        // Show Home by default
        setMainPanel(new HomeGUI(customerId));
        setActiveMenu("Trang chủ");
    }

    private JPanel createMenuItem(String icon, String text) {
        JPanel menuItem = new JPanel();
        menuItem.setLayout(new BoxLayout(menuItem, BoxLayout.X_AXIS));
        menuItem.setBackground(MENU_BG_COLOR);
        menuItem.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        menuItem.setMaximumSize(new Dimension(250, 45));
        menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        iconLabel.setForeground(MENU_TEXT_COLOR);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        textLabel.setForeground(MENU_TEXT_COLOR);

        menuItem.add(iconLabel);
        menuItem.add(Box.createHorizontalStrut(15));
        menuItem.add(textLabel);
        menuItem.add(Box.createHorizontalGlue());

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!currentMenu.equals(text)) menuItem.setBackground(MENU_HOVER_COLOR);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!currentMenu.equals(text)) menuItem.setBackground(MENU_BG_COLOR);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentMenu = text;
                setActiveMenu(text);
                try {
                    switch (text) {
                        case "Trang chủ": 
                            setMainPanel(new HomeGUI(customerId)); 
                            break;
                        case "Cửa hàng": 
                            // Truyền mainContentPanel vào BookStorePanel
                            setMainPanel(new JLabel("Đang tải...", SwingConstants.CENTER));
SwingWorker<JComponent, Void> worker = new SwingWorker<>() {
    @Override
    protected JComponent doInBackground() {
        return new BookStorePanel(customerId, mainContentPanel);
    }
    @Override
    protected void done() {
        try {
            setMainPanel(get());
        } catch (Exception ex) {
            setMainPanel(new JLabel("Lỗi khi tải panel!"));
        }
    }
};
worker.execute();
                            break;
                       case "Giỏ hàng":
    setMainPanel(new JLabel("Đang tải...", SwingConstants.CENTER));
    SwingWorker<JComponent, Void> cartWorker = new SwingWorker<>() {
        @Override
        protected JComponent doInBackground() {
            return new CartPanel(customerId, mainContentPanel, MainGUI.this);
        }
        @Override
        protected void done() {
            try {
                setMainPanel(get());
            } catch (Exception ex) {
                setMainPanel(new JLabel("Lỗi khi tải panel!"));
            }
        }
    };
    cartWorker.execute();
    break;
                        default: 
                            setMainPanel(new JLabel("<html><div style='font-size:20px;color:gray;padding:30px'>Chức năng đang phát triển...</div></html>", SwingConstants.CENTER));
                    }
                } catch (Exception ex) {
                    setMainPanel(new JLabel("<html><div style='font-size:20px;color:red;padding:30px'>Lỗi khi tải panel: " + ex.getMessage() + "</div></html>", SwingConstants.CENTER));
                    ex.printStackTrace();
                }
            }
        });

        return menuItem;
    }

    // Đổi màu active cho menu
    private void setActiveMenu(String text) {
        for (JPanel panel : menuPanels) {
            JLabel label = (JLabel) panel.getComponent(2); // textLabel
            if (label.getText().equals(text)) {
                panel.setBackground(MENU_ACTIVE_COLOR);
            } else {
                panel.setBackground(MENU_BG_COLOR);
            }
        }
    }

    // Thay đổi nội dung panel chính
    private void setMainPanel(JComponent component) {
        mainContentPanel.removeAll();
        mainContentPanel.add(component, BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        SwingUtilities.invokeLater(() -> mainScrollPane.getVerticalScrollBar().setValue(0));
    }
// Trong MainGUI
public void showBookStore() {
    setActiveMenu("Cửa hàng");
    setMainPanel(new JLabel("Đang tải...", SwingConstants.CENTER));
    SwingWorker<JComponent, Void> worker = new SwingWorker<>() {
        @Override
        protected JComponent doInBackground() {
            return new BookStorePanel(customerId, mainContentPanel);
        }
        @Override
        protected void done() {
            try {
                setMainPanel(get());
            } catch (Exception ex) {
                setMainPanel(new JLabel("Lỗi khi tải panel!"));
            }
        }
    };
    worker.execute();
}
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainGUI mainGUI = new MainGUI(1); // Truyền customerId mặc định
            mainGUI.setVisible(true);
        });
    }
}