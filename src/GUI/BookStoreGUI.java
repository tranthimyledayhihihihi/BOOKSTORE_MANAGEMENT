
package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import model.Book;
import service.BookService;

public class BookStoreGUI extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(100, 149, 237);
    private static final Color ACCENT_COLOR = new Color(65, 105, 225);
    private static final Color BG_COLOR = new Color(248, 249, 250);

    private BookService bookService;
    private List<String> subscriptions; // In-memory subscription storage

    public BookStoreGUI() {
        // Initialize services
        bookService = new BookService();
        subscriptions = new ArrayList<>();

        setTitle("Nhà Sách Online");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new GradientPanel(PRIMARY_COLOR, SECONDARY_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logoLabel = new JLabel("Nhà Sách Online");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(logoLabel, BorderLayout.WEST);

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        String[] menuItems = {"Trang chủ", "Cửa hàng", "Danh mục", "Đơn hàng"};
        for (String item : menuItems) {
            JLabel menuLabel = createHoverLabel(item);
            menuPanel.add(menuLabel);
        }
        headerPanel.add(menuPanel, BorderLayout.CENTER);

        JPanel authPanel = new JPanel();
        authPanel.setOpaque(false);
        authPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton loginButton = createStyledButton("Đăng nhập", Color.WHITE, PRIMARY_COLOR);
        JButton signupButton = createStyledButton("Đăng ký", PRIMARY_COLOR, Color.WHITE);
        authPanel.add(loginButton);
        authPanel.add(signupButton);
        headerPanel.add(authPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_COLOR);

        // Search Section
        JPanel searchPanel = new GradientPanel(SECONDARY_COLOR, PRIMARY_COLOR);
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel searchTitle = new JLabel("Khám phá thế giới qua từng trang sách");
        searchTitle.setForeground(Color.WHITE);
        searchTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        searchTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel searchDesc = new JLabel("Tìm kiếm tri thức, cảm xúc và niềm vui qua hàng ngàn đầu sách chất lượng");
        searchDesc.setForeground(new Color(255, 255, 255, 220));
        searchDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel searchInputPanel = new JPanel();
        searchInputPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchInputPanel.setOpaque(false);
        RoundedTextField searchField = new RoundedTextField("Tìm kiếm tên sách, tác giả...", 30);
        searchField.setPreferredSize(new Dimension(350, 40));
        JButton searchButton = createStyledButton("Tìm", ACCENT_COLOR, Color.WHITE);
        searchButton.setPreferredSize(new Dimension(80, 40));
        searchInputPanel.add(searchField);
        searchInputPanel.add(searchButton);

        searchPanel.add(searchTitle);
        searchPanel.add(Box.createVerticalStrut(10));
        searchPanel.add(searchDesc);
        searchPanel.add(Box.createVerticalStrut(20));
        searchPanel.add(searchInputPanel);
        contentPanel.add(searchPanel);

        // New Books Section (Using Best-Selling Books)
        JPanel newBooksPanel = createSectionPanel("Sách bán chạy", "Những đầu sách bán chạy nhất trong tuần qua");
        JPanel booksGrid = new JPanel(new GridLayout(1, 4, 15, 15));
        booksGrid.setOpaque(false);
        try {
            List<Book> bestSellBooks = bookService.getBestSellBooks();
            for (int i = 0; i < Math.min(4, bestSellBooks.size()); i++) {
                Book book = bestSellBooks.get(i);
                booksGrid.add(createBookCard(book.getTitle(), book.getAuthorName(), formatPrice(book.getPrice())));
            }
            if (bestSellBooks.isEmpty()) {
                booksGrid.add(createBookCard("Không có sách", "N/A", "₫0"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải sách bán chạy. Vui lòng kiểm tra kết nối cơ sở dữ liệu.", 
                                         "Lỗi", JOptionPane.ERROR_MESSAGE);
            booksGrid.add(createBookCard("Sách mẫu", "Tác giả mẫu", "₫99,000"));
        }
        newBooksPanel.add(Box.createVerticalStrut(15));
        newBooksPanel.add(booksGrid);
        contentPanel.add(newBooksPanel);

        // Categories Section (Mocked Data)
        JPanel categoryPanel = createSectionPanel("Danh mục sách", "Khám phá sách theo sở thích và nhu cầu của bạn");
        JPanel categoryGrid = new JPanel(new GridLayout(2, 4, 15, 15));
        categoryGrid.setOpaque(false);
        String[] categories = {"Văn học Việt Nam", "Văn học nước ngoài", "Sách thiếu nhi", "Kỹ năng sống", 
                              "Tin học - Công nghệ", "Kinh tế - Quản trị", "Giáo dục", "Xem tất cả"};
        String[] counts = {"125 sách", "250 sách", "87 sách", "61 sách", "42 sách", "32 sách", "74 sách", "1000+ sách"};
        for (int i = 0; i < categories.length; i++) {
            categoryGrid.add(createCategoryCard(categories[i], counts[i]));
        }
        categoryPanel.add(Box.createVerticalStrut(15));
        categoryPanel.add(categoryGrid);
        contentPanel.add(categoryPanel);

        // Subscribe Section
        JPanel subscribePanel = new GradientPanel(PRIMARY_COLOR, SECONDARY_COLOR);
        subscribePanel.setBorder(new RoundedBorder(10));
        subscribePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel subscribeTitle = new JLabel("Đăng ký nhận thông tin");
        subscribeTitle.setForeground(Color.WHITE);
        subscribeTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridwidth = 2;
        subscribePanel.add(subscribeTitle, gbc);

        gbc.gridy++;
        JLabel subscribeDesc = new JLabel("Đăng ký nhận email để cập nhật những sách mới nhất và khuyến mãi đặc biệt từ chúng tôi");
        subscribeDesc.setForeground(new Color(255, 255, 255, 220));
        subscribeDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subscribePanel.add(subscribeDesc, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        RoundedTextField emailField = new RoundedTextField("Địa chỉ email của bạn", 20);
        emailField.setPreferredSize(new Dimension(300, 40));
        subscribePanel.add(emailField, gbc);

        gbc.gridx = 1;
        JButton subscribeButton = createStyledButton("Đăng ký", Color.WHITE, PRIMARY_COLOR);
        subscribeButton.setPreferredSize(new Dimension(120, 40));
        subscribePanel.add(subscribeButton, gbc);

        JPanel subscribeContainer = new JPanel();
        subscribeContainer.setLayout(new BorderLayout());
        subscribeContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        subscribeContainer.add(subscribePanel, BorderLayout.CENTER);
        contentPanel.add(subscribeContainer);

        // Footer
        JPanel footerPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new CompoundBorder(new ShadowBorder(), BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JPanel infoPanel = createFooterSection("Nhà Sách Online", new String[]{"Đến với chúng tôi để trải nghiệm", 
                                                                               "thế giới sách tuyệt vời với đa dạng", 
                                                                               "thể loại và chính sách ưu đãi."});
        infoPanel.add(Box.createVerticalStrut(10));
        JLabel socialIcons = new JLabel("<html>📘 🐦 📸 🎥</html>");
        socialIcons.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        infoPanel.add(socialIcons);
        footerPanel.add(infoPanel);

        footerPanel.add(createFooterSection("Liên kết nhanh", new String[]{"Trang Chủ", "Cửa Hàng", "Giỏ Hàng", "Đơn Hàng"}));
        footerPanel.add(createFooterSection("Danh mục", new String[]{"Văn Học Việt Nam", "Văn Học Nước Ngoài", 
                                                                    "Sách Thiếu Nhi", "Kỹ Năng Sống"}));

        JPanel contactPanel = new JPanel();
        contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.Y_AXIS));
        contactPanel.setOpaque(false);
        JLabel contactLabel = new JLabel("Liên hệ");
        contactLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contactPanel.add(contactLabel);
        contactPanel.add(Box.createVerticalStrut(10));
        contactPanel.add(new JLabel("📍 123 Nguyễn Văn Linh, Q.7, TP.HCM"));
        contactPanel.add(Box.createVerticalStrut(5));
        contactPanel.add(new JLabel("📞 0703 1234 5678"));
        contactPanel.add(Box.createVerticalStrut(5));
        contactPanel.add(new JLabel("📧 contact@nhasachonline.com"));
        contactPanel.add(Box.createVerticalStrut(5));
        contactPanel.add(new JLabel("🕒 08:00 - 22:00, Thứ 2 - Chủ Nhật"));
        footerPanel.add(contactPanel);

        contentPanel.add(footerPanel);

        JPanel copyrightPanel = new JPanel();
        copyrightPanel.setOpaque(false);
        JLabel copyrightLabel = new JLabel("© 2023 Nhà Sách Online. Tất cả quyền được bảo lưu.");
        copyrightLabel.setForeground(new Color(100, 100, 100));
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyrightPanel.add(copyrightLabel);
        contentPanel.add(copyrightPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        contentPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            try {
                List<Book> searchResults = bookService.searchBooks(searchText);
                JOptionPane.showMessageDialog(this, "Tìm thấy " + searchResults.size() + " sách cho: " + searchText);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm sách", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        subscribeButton.addActionListener(e -> {
            String email = emailField.getText();
            if (email.isEmpty() || email.equals("Địa chỉ email của bạn") || !email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập email hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                subscriptions.add(email);
                JOptionPane.showMessageDialog(this, "Đăng ký thành công với email: " + email, 
                                             "Thành công", JOptionPane.INFORMATION_MESSAGE);
                emailField.setText("Địa chỉ email của bạn");
            }
        });

        loginButton.addActionListener(e -> {
            // Gọi LoginDialog
            LoginDialog loginDialog = new LoginDialog(BookStoreGUI.this);
            loginDialog.setVisible(true);
            // Xử lý kết quả
            if (loginDialog.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", 
                                             "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        signupButton.addActionListener(e -> {
            // Gọi SignupDialog
            SignupDialog signupDialog = new SignupDialog(BookStoreGUI.this);
            signupDialog.setVisible(true);
            // Xử lý kết quả
            if (signupDialog.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", 
                                             "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private String formatPrice(BigDecimal price) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(price);
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
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return button;
    }

    private JLabel createHoverLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(new Color(255, 255, 255, 180));
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.WHITE);
                label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        return label;
    }

    private JPanel createSectionPanel(String title, String description) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(new ShadowBorder(), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(descLabel);
        return panel;
    }

    private JPanel createBookCard(String title, String author, String price) {
        JPanel panel = new HoverPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(new ShadowBorder(), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 240, 240));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.GRAY);
                g2.drawString("📚", getWidth()/2 - 10, getHeight()/2 + 5);
                g2.dispose();
            }
        };
        imagePanel.setPreferredSize(new Dimension(0, 150));
        panel.add(imagePanel, BorderLayout.NORTH);
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

    private JPanel createCategoryCard(String category, String count) {
        JPanel panel = new HoverPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(new ShadowBorder(), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        JLabel categoryLabel = new JLabel(category, SwingConstants.CENTER);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel countLabel = new JLabel(count, SwingConstants.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(new Color(100, 100, 100));
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(categoryLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(countLabel);
        textPanel.add(Box.createVerticalGlue());
        panel.add(textPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooterSection(String title, String[] items) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        for (String item : items) {
            JLabel itemLabel = new JLabel(item);
            itemLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            itemLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    itemLabel.setForeground(PRIMARY_COLOR);
                    itemLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    itemLabel.setForeground(Color.BLACK);
                    itemLabel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
            panel.add(itemLabel);
            panel.add(Box.createVerticalStrut(5));
        }
        return panel;
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

    private class HoverPanel extends JPanel {
        private Color originalColor = Color.WHITE;
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

    private class GradientPanel extends JPanel {
        private Color color1;
        private Color color2;
        public GradientPanel(Color color1, Color color2) {
            super();
            this.color1 = color1;
            this.color2 = color2;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }

    private class RoundedBorder extends AbstractBorder {
        private int radius;
        public RoundedBorder(int radius) {
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground());
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }
    }

    private class ShadowBorder extends AbstractBorder {
        private static final int SHADOW_SIZE = 5;
        private static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);
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
            BookStoreGUI gui = new BookStoreGUI();
            gui.setVisible(true);
        });
    }
}