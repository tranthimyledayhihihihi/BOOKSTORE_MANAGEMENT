package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import model.Book;
import service.BookService;

public class BookStoreGUI extends JFrame {
    // Color constants
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color ACCENT_COLOR = new Color(231, 76, 60);
    private static final Color BG_COLOR = new Color(248, 249, 250);
    private static final Color MENU_BG_COLOR = new Color(44, 62, 80);
    private static final Color MENU_HOVER_COLOR = new Color(52, 73, 94);
    private static final Color MENU_ACTIVE_COLOR = new Color(41, 128, 185);
    private static final Color MENU_TEXT_COLOR = new Color(236, 240, 241);
    private static final Color CARD_BG_COLOR = Color.WHITE;
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);

    private BookService bookService;
    private List<String> subscriptions;
    private JPanel mainContentPanel;
    private JPanel categoryGrid;
    private String currentMenu = "Trang chủ";
    private boolean isLoggedIn = false;
    private String loggedInUser = null;

    public BookStoreGUI() {
        // Initialize services
        bookService = new BookService();
        subscriptions = new ArrayList<>();

        // Frame setup
        setTitle("Nhà Sách Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create main panels
        createSidebar();
        createMainContent();
        createHeader();

        // Show default view
        showHomeView();
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(0, 60));

        // Logo
        JLabel logoLabel = new JLabel("📚 NHÀ SÁCH ONLINE");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
        logoLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Navigation links
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);
        String[] navItems = {"Trang chủ", "Cửa hàng", "Danh mục", "Đơn hàng", "Liên hệ"};
        for (String item : navItems) {
            JLabel navLabel = new JLabel(item);
            navLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            navLabel.setForeground(PRIMARY_COLOR);
            navLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            navPanel.add(navLabel);
        }
        headerPanel.add(navPanel, BorderLayout.CENTER);

        // User actions
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setOpaque(false);
        JButton cartButton = createIconButton("🛒", "Giỏ hàng");
        JButton loginButton = createStyledButton("Đăng nhập", SECONDARY_COLOR, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(100, 35));
        JButton registerButton = createStyledButton("Đăng ký", PRIMARY_COLOR, Color.WHITE);
        registerButton.setPreferredSize(new Dimension(100, 35));
        JButton logoutButton = createStyledButton("Đăng xuất", ACCENT_COLOR, Color.WHITE);
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setVisible(false);

        // Action listeners
        loginButton.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setVisible(true);
            if (loginDialog.isSuccess()) {
                isLoggedIn = true;
                loginButton.setVisible(false);
                registerButton.setVisible(false);
                logoutButton.setVisible(true);
                loggedInUser = "User";
                updateUserPanel(userPanel, logoutButton);
            }
        });

        registerButton.addActionListener(e -> {
            SignupDialog signupDialog = new SignupDialog(this);
            signupDialog.setVisible(true);
            if (signupDialog.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        logoutButton.addActionListener(e -> {
            isLoggedIn = false;
            loggedInUser = null;
            loginButton.setVisible(true);
            registerButton.setVisible(true);
            logoutButton.setVisible(false);
            updateUserPanel(userPanel, logoutButton);
            JOptionPane.showMessageDialog(this, "Đăng xuất thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        userPanel.add(cartButton);
        userPanel.add(loginButton);
        userPanel.add(registerButton);
        userPanel.add(logoutButton);
        headerPanel.add(userPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void updateUserPanel(JPanel userPanel, JButton logoutButton) {
        userPanel.removeAll();
        userPanel.add(createIconButton("🛒", "Giỏ hàng"));
        if (isLoggedIn) {
            JLabel userLabel = new JLabel("Xin chào, " + (loggedInUser != null ? loggedInUser : "User") + "!");
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            userLabel.setForeground(PRIMARY_COLOR);
            userPanel.add(userLabel);
            userPanel.add(Box.createHorizontalStrut(10));
            userPanel.add(logoutButton);
        } else {
            userPanel.add(createStyledButton("Đăng nhập", SECONDARY_COLOR, Color.WHITE));
            userPanel.add(createStyledButton("Đăng ký", PRIMARY_COLOR, Color.WHITE));
        }
        userPanel.revalidate();
        userPanel.repaint();
    }

    private void createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(MENU_BG_COLOR);
        sidebarPanel.setPreferredSize(new Dimension(250, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Menu items
        String[][] menuItems = {
            {"🏠", "Trang chủ", "home"},
            {"📚", "Cửa hàng", "store"},
            {"📑", "Danh mục", "categories"},
            {"🛒", "Giỏ hàng", "cart"},
            {"📦", "Đơn hàng", "orders"},
            {"⭐", "Yêu thích", "favorites"},
            {"👤", "Tài khoản", "account"}
        };

        for (String[] item : menuItems) {
            JPanel menuItem = createMenuItem(item[0], item[1], item[2]);
            sidebarPanel.add(menuItem);
            sidebarPanel.add(Box.createVerticalStrut(5));
        }

        sidebarPanel.add(Box.createVerticalGlue());

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
        footerPanel.setBackground(MENU_BG_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel footerText = new JLabel("© 2024 Nhà Sách Online");
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerText.setForeground(MENU_TEXT_COLOR);
        footerText.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerPanel.add(footerText);

        sidebarPanel.add(footerPanel);
        add(sidebarPanel, BorderLayout.WEST);
    }

    private void createMainContent() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.setBackground(BG_COLOR);

        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMenuItem(String icon, String text, String action) {
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
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(MENU_TEXT_COLOR);

        menuItem.add(iconLabel);
        menuItem.add(Box.createHorizontalStrut(15));
        menuItem.add(textLabel);
        menuItem.add(Box.createHorizontalGlue());

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!currentMenu.equals(text)) {
                    menuItem.setBackground(MENU_HOVER_COLOR);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!currentMenu.equals(text)) {
                    menuItem.setBackground(MENU_BG_COLOR);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                menuItem.setBackground(MENU_ACTIVE_COLOR);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!currentMenu.equals(text)) {
                    menuItem.setBackground(MENU_HOVER_COLOR);
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                currentMenu = text;
                switch (action) {
                    case "home": showHomeView(); break;
                    case "store": showStoreView(); break;
                    case "categories": showCategoriesView(); break;
                    case "cart": showCartView(); break;
                    case "orders": showOrdersView(); break;
                    case "favorites": showFavoritesView(); break;
                    case "account": showAccountView(); break;
                }
            }
        });

        return menuItem;
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private JButton createIconButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
        button.setForeground(PRIMARY_COLOR);
        button.setToolTipText(tooltip);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private class RoundedTextField extends JTextField {
        private Shape shape;
        private Color borderColor = new Color(200, 200, 200);

        public RoundedTextField(String text, int columns) {
            super(text, columns);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            g2.dispose();
        }

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
            return shape.contains(x, y);
        }
    }

    // View methods
    private void showHomeView() {
        mainContentPanel.removeAll();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));

        // Hero section
        JPanel heroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), getHeight(), SECONDARY_COLOR);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        JLabel heroTitle = new JLabel("Khám phá thế giới qua từng trang sách");
        heroTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heroTitle.setForeground(Color.WHITE);
        heroTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel heroDesc = new JLabel("Tìm kiếm tri thức, cảm xúc và niềm vui qua hàng ngàn đầu sách chất lượng");
        heroDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        heroDesc.setForeground(Color.WHITE);
        heroDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setOpaque(false);
        RoundedTextField searchField = new RoundedTextField("Tìm kiếm tên sách, tác giả...", 40);
        searchField.setPreferredSize(new Dimension(500, 40));
        JButton searchButton = createStyledButton("Tìm", PRIMARY_COLOR, Color.WHITE);
        searchButton.setPreferredSize(new Dimension(60, 40));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        heroPanel.add(heroTitle);
        heroPanel.add(Box.createVerticalStrut(10));
        heroPanel.add(heroDesc);
        heroPanel.add(Box.createVerticalStrut(20));
        heroPanel.add(searchPanel);
        heroPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        mainContentPanel.add(heroPanel);

        // Categories section
        JPanel categoriesPanel = createSectionPanel("Danh mục sách", "Khám phá sách theo thể loại");
        categoryGrid = new JPanel(new GridLayout(2, 4, 15, 15));
        categoryGrid.setOpaque(false);
        categoryGrid.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        String[][] categories = {
            {"📚", "Văn học Việt Nam", "125 sách"},
            {"🌍", "Văn học nước ngoài", "243 sách"},
            {"👶", "Sách thiếu nhi", "87 sách"},
            {"🧠", "Kỹ năng sống", "65 sách"},
            {"💻", "Tin học - Công nghệ", "42 sách"},
            {"💼", "Kinh tế - Quản trị", "92 sách"},
            {"🎓", "Giáo dục", "174 sách"},
            {"➕", "Xem tất cả", "1000+ sách"}
        };
        for (String[] category : categories) {
            categoryGrid.add(createCategoryCard(category[0], category[1], category[2]));
        }
        categoriesPanel.add(Box.createVerticalStrut(15));
        categoriesPanel.add(categoryGrid);
        mainContentPanel.add(categoriesPanel);

        // Newsletter section
        JPanel newsletterPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), getHeight(), SECONDARY_COLOR);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        newsletterPanel.setLayout(new BoxLayout(newsletterPanel, BoxLayout.Y_AXIS));
        newsletterPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        JLabel newsTitle = new JLabel("Đăng ký nhận thông tin");
        newsTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        newsTitle.setForeground(Color.WHITE);
        newsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel newsDesc = new JLabel("Đăng ký nhận email để cập nhật những sách mới nhất và khuyến mãi đặc biệt.");
        newsDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        newsDesc.setForeground(Color.WHITE);
        newsDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel newsInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        newsInputPanel.setOpaque(false);
        RoundedTextField newsField = new RoundedTextField("Địa chỉ email của bạn", 30);
        newsField.setPreferredSize(new Dimension(400, 40));
        JButton newsButton = createStyledButton("Đăng ký", PRIMARY_COLOR, Color.WHITE);
        newsButton.setPreferredSize(new Dimension(100, 40));
        newsInputPanel.add(newsField);
        newsInputPanel.add(newsButton);
        newsletterPanel.add(newsTitle);
        newsletterPanel.add(Box.createVerticalStrut(10));
        newsletterPanel.add(newsDesc);
        newsletterPanel.add(Box.createVerticalStrut(20));
        newsletterPanel.add(newsInputPanel);
        newsletterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        mainContentPanel.add(newsletterPanel);

        // Footer
        JPanel footerPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Column 1: About
        JPanel aboutCol = new JPanel();
        aboutCol.setLayout(new BoxLayout(aboutCol, BoxLayout.Y_AXIS));
        aboutCol.setOpaque(false);
        JLabel aboutTitle = new JLabel("NHÀ SÁCH ONLINE");
        aboutTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        aboutTitle.setForeground(PRIMARY_COLOR);
        JLabel aboutDesc = new JLabel("<html>Đến với chúng tôi để trải nghiệm<br>thế giới sách tuyệt vời và đa dạng<br>lớn với giá phải chăng.</html>");
        aboutDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        aboutDesc.setForeground(Color.BLACK);
        JPanel socialPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        socialPanel.setOpaque(false);
        String[] socialIcons = {"🐦", "📸", "📹"};
        for (String icon : socialIcons) {
            JLabel socialLabel = new JLabel(icon);
            socialLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            socialLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            socialPanel.add(socialLabel);
        }
        aboutCol.add(aboutTitle);
        aboutCol.add(Box.createVerticalStrut(10));
        aboutCol.add(aboutDesc);
        aboutCol.add(Box.createVerticalStrut(10));
        aboutCol.add(socialPanel);

        // Column 2: Links
        JPanel linksCol = new JPanel();
        linksCol.setLayout(new BoxLayout(linksCol, BoxLayout.Y_AXIS));
        linksCol.setOpaque(false);
        JLabel linksTitle = new JLabel("LIÊN KẾT NHANH");
        linksTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        linksTitle.setForeground(PRIMARY_COLOR);
        String[] links = {"Trang Chủ", "Cửa Hàng", "Giới Hàng", "Đơn Hàng"};
        for (String link : links) {
            JLabel linkLabel = new JLabel(link);
            linkLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            linkLabel.setForeground(Color.BLACK);
            linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            linksCol.add(linkLabel);
            linksCol.add(Box.createVerticalStrut(5));
        }

        // Column 3: Categories
        JPanel categoriesCol = new JPanel();
        categoriesCol.setLayout(new BoxLayout(categoriesCol, BoxLayout.Y_AXIS));
        categoriesCol.setOpaque(false);
        JLabel categoriesTitle = new JLabel("DANH MỤC");
        categoriesTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoriesTitle.setForeground(PRIMARY_COLOR);
        String[] footerCategories = {"Văn Học Việt Nam", "Văn Học Nước Ngoài", "Sách Thiếu Nhi", "Kỹ Năng Sống"};
        for (String category : footerCategories) {
            JLabel catLabel = new JLabel(category);
            catLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            catLabel.setForeground(Color.BLACK);
            catLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            categoriesCol.add(catLabel);
            categoriesCol.add(Box.createVerticalStrut(5));
        }

        // Column 4: Contact
        JPanel contactCol = new JPanel();
        contactCol.setLayout(new BoxLayout(contactCol, BoxLayout.Y_AXIS));
        contactCol.setOpaque(false);
        JLabel contactTitle = new JLabel("LIÊN HỆ");
        contactTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contactTitle.setForeground(PRIMARY_COLOR);
        JLabel addressLabel = new JLabel("📍 48 Cao Thang, Hai Chau, Đa Nang");
        addressLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        addressLabel.setForeground(Color.BLACK);
        JLabel phoneLabel = new JLabel("📞 (028) 1234 5678");
        phoneLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        phoneLabel.setForeground(Color.BLACK);
        JLabel emailLabel = new JLabel("📧 contact@nhasachonline.com");
        emailLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        emailLabel.setForeground(Color.BLACK);
        JLabel hoursLabel = new JLabel("⏰ 08:00 - 22:00, Thu 2 - Chu Nhat");
        hoursLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        hoursLabel.setForeground(Color.BLACK);
        contactCol.add(contactTitle);
        contactCol.add(Box.createVerticalStrut(10));
        contactCol.add(addressLabel);
        contactCol.add(Box.createVerticalStrut(5));
        contactCol.add(phoneLabel);
        contactCol.add(Box.createVerticalStrut(5));
        contactCol.add(emailLabel);
        contactCol.add(Box.createVerticalStrut(5));
        contactCol.add(hoursLabel);

        footerPanel.add(aboutCol);
        footerPanel.add(linksCol);
        footerPanel.add(categoriesCol);
        footerPanel.add(contactCol);

        JPanel footerWrapper = new JPanel(new BorderLayout());
        footerWrapper.setBackground(Color.WHITE);
        footerWrapper.add(footerPanel, BorderLayout.CENTER);
        JLabel footerBottom = new JLabel("© 2023 Nhà Sách Online. Tất cả quyền được bảo lưu.", SwingConstants.CENTER);
        footerBottom.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerBottom.setForeground(Color.BLACK);
        footerBottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        footerWrapper.add(footerBottom, BorderLayout.SOUTH);
        mainContentPanel.add(footerWrapper);

        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void showStoreView() {}
    private void showCategoriesView() {}
    private void showCartView() {}
    private void showOrdersView() {}
    private void showFavoritesView() {}
    private void showAccountView() {}

    private JPanel createSectionPanel(String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(0, 188, 212));
        separator.setMaximumSize(new Dimension(100, 2));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
        panel.add(descLabel);

        return panel;
    }

    private JPanel createBookCard(Book book) {
        return createBookCard(book.getTitle(), book.getAuthorName(), formatPrice(book.getPrice()));
    }

    private JPanel createBookCard(String title, String author, String price) {
        JPanel panel = new HoverPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(CARD_BG_COLOR);
        panel.setBorder(new CompoundBorder(new ShadowBorder(), BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Book image placeholder
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 240, 240));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
                g2.drawString("📚", getWidth()/2 - 16, getHeight()/2 + 12);
                g2.dispose();
            }
        };
        imagePanel.setPreferredSize(new Dimension(0, 200));
        panel.add(imagePanel, BorderLayout.NORTH);

        // Book info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel authorLabel = new JLabel(author);
        authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        authorLabel.setForeground(new Color(100, 100, 100));

        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(PRIMARY_COLOR);

        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(authorLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(priceLabel);

        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCategoryCard(String icon, String category, String count) {
        JPanel panel = new EnhancedHoverPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new CompoundBorder(new ShadowBorder(), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panel.setBackground(Color.WHITE);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel categoryLabel = new JLabel(category, SwingConstants.CENTER);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel countLabel = new JLabel(count, SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(new Color(100, 100, 100));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(iconLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(categoryLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(countLabel);

        panel.add(textPanel, BorderLayout.CENTER);
        return panel;
    }

    private String formatPrice(BigDecimal price) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(price);
    }

    private class HoverPanel extends JPanel {
        private Color originalColor = CARD_BG_COLOR;
        private Color hoverColor = new Color(245, 245, 245);

        public HoverPanel() {
            super();
            setBackground(originalColor);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(originalColor);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }
    }

    private class EnhancedHoverPanel extends JPanel {
        public EnhancedHoverPanel() {
            super();
            setOpaque(false);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    setBorder(new CompoundBorder(new ShadowBorder(), BorderFactory.createLineBorder(PRIMARY_COLOR, 2)));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    setBorder(new CompoundBorder(new ShadowBorder(), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
                }
            });
        }
    }

    private class ShadowBorder extends AbstractBorder {
        private static final int SHADOW_SIZE = 5;

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(SHADOW_COLOR);
            for (int i = 0; i < SHADOW_SIZE; i++) {
                g2.drawRoundRect(x + i, y + i, width - 1 - 2*i, height - 1 - 2*i, 10, 10);
            }
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new BookStoreGUI().setVisible(true);
        });
    }
}