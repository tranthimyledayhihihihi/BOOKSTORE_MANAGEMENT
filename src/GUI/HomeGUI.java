package GUI;

import BLL.BookBLL;
import dao.CategoryDAO;
import model.Book;
import model.Category;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HomeGUI extends JPanel {
    // Định nghĩa màu sắc
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(246, 247, 251);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_SECONDARY = new Color(100, 100, 100);
    private static final Color HOVER_COLOR = new Color(248, 249, 252);
    
    // Các thành phần giao diện
    private JPanel dynamicContent;
    private JLabel sectionTitle;
    private JPanel bookSection;
    private JPanel categoryPanel;
    
    // Dữ liệu và trạng thái
    private int customerId;
    private boolean isShowingBooks = false;
    private BookBLL bookBLL;
    private CategoryDAO categoryDAO;

    public HomeGUI(int customerId) {
        this.customerId = customerId;
        this.bookBLL = new BookBLL();
        this.categoryDAO = new CategoryDAO();
        
        initializeComponents();
        setupLayout();
    }

    /**
     * Khởi tạo các thành phần giao diện
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(CARD_BACKGROUND);
        
        // Khởi tạo section title (ẩn ban đầu)
        sectionTitle = new JLabel("Tất cả sách");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        sectionTitle.setForeground(PRIMARY_COLOR);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Khởi tạo dynamic content panel
        dynamicContent = new JPanel(new BorderLayout());
        dynamicContent.setBackground(BACKGROUND_COLOR);
        
        // Khởi tạo book section (ẩn ban đầu)
        bookSection = new JPanel(new BorderLayout());
        bookSection.setBackground(BACKGROUND_COLOR);
        bookSection.add(sectionTitle, BorderLayout.NORTH);
        bookSection.add(dynamicContent, BorderLayout.CENTER);
        bookSection.setVisible(false);
    }

    /**
     * Thiết lập layout chính
     */
    private void setupLayout() {
        // Tạo banner
        JPanel banner = createBanner();
        add(banner, BorderLayout.NORTH);

        // Tạo main content panel
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(CARD_BACKGROUND);

        // Tạo category panel
        categoryPanel = createCategoryPanel();

        // Thêm các component vào main content
        mainContent.add(categoryPanel, BorderLayout.NORTH);
        mainContent.add(bookSection, BorderLayout.CENTER);
        mainContent.add(new SubscribePanel(), BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
        add(new FooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Tạo banner với gradient background
     */
    private JPanel createBanner() {
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR, 
                    getWidth(), getHeight(), SECONDARY_COLOR
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Tiêu đề chính
        JLabel heroTitle = createStyledLabel(
            "Khám phá thế giới qua từng trang sách",
            new Font("Segoe UI", Font.BOLD, 28),
            Color.WHITE
        );
        heroTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Mô tả
        JLabel heroDesc = createStyledLabel(
            "Tìm kiếm tri thức, cảm xúc và niềm vui qua hàng ngàn đầu sách chất lượng",
            new Font("Segoe UI", Font.PLAIN, 16),
            Color.WHITE
        );
        heroDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        banner.add(heroTitle);
        banner.add(Box.createVerticalStrut(10));
        banner.add(heroDesc);
        banner.add(Box.createVerticalStrut(20));
        
        return banner;
    }

    /**
     * Tạo panel danh mục sách
     */
    private JPanel createCategoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Tiêu đề danh mục
        JLabel categoryTitle = createStyledLabel(
            "Danh mục sách",
            new Font("Segoe UI", Font.BOLD, 22),
            PRIMARY_COLOR
        );
        categoryTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(categoryTitle);
        panel.add(Box.createVerticalStrut(20));

        // Grid wrapper
        JPanel gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrapper.setOpaque(false);

        // Grid chứa các category card
        JPanel grid = new JPanel(new GridLayout(0, 4, 20, 20));
        grid.setBackground(CARD_BACKGROUND);

        // Thêm các category card
        addCategoryCards(grid);
        
        gridWrapper.add(grid);
        panel.add(gridWrapper);
        
        return panel;
    }

    /**
     * Thêm các thẻ danh mục vào grid
     */
    private void addCategoryCards(JPanel grid) {
        List<Category> categories = categoryDAO.getAllCategories();
        String[] icons = {
            "\uD83D\uDCDA", "\uD83C\uDF0D", "\uD83D\uDC36", 
            "\uD83E\uDDE0", "\uD83D\uDCBB", "\uD83D\uDCBC", "\uD83C\uDF93"
        };
        
        // Thêm từng category
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            String icon = icons[i % icons.length];
            
            JPanel card = createCategoryCard(
                icon, 
                category.getName(), 
                category.getBookCount() + " sách",
                category.getCategoryId(),
                category.getName()
            );
            grid.add(card);
        }
        
        // Thêm card "Xem tất cả"
        JPanel viewAllCard = createCategoryCard(
            "+", 
            "Xem tất cả", 
            "1000+ sách", 
            0, 
            "Tất cả sách"
        );
        grid.add(viewAllCard);
    }

    /**
     * Tạo thẻ danh mục
     */
    private JPanel createCategoryCard(String icon, String categoryName, 
                                     String count, int categoryId, String displayName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new CompoundBorder(
            new ShadowBorder(), 
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setBackground(CARD_BACKGROUND);
        card.setPreferredSize(new Dimension(200, 150));

        // Panel chứa nội dung
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Icon
        JLabel iconLabel = createStyledLabel(
            icon, 
            new Font("Segoe UI Emoji", Font.PLAIN, 32), 
            Color.BLACK
        );
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tên danh mục
        JLabel nameLabel = createStyledLabel(
            categoryName,
            new Font("Segoe UI", Font.BOLD, 16),
            Color.BLACK
        );
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Số lượng sách
        JLabel countLabel = createStyledLabel(
            count,
            new Font("Segoe UI", Font.PLAIN, 13),
            TEXT_SECONDARY
        );
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Thêm các component vào content panel
        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(countLabel);

        card.add(contentPanel, BorderLayout.CENTER);

        // Thêm mouse listener cho hover effect và click
        addCategoryCardListeners(card, categoryId, displayName);

        return card;
    }

    /**
     * Thêm các listener cho category card
     */
    private void addCategoryCardListeners(JPanel card, int categoryId, String displayName) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                    new ShadowBorder(), 
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 3)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                card.setBackground(HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(new CompoundBorder(
                    new ShadowBorder(), 
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                card.setBackground(CARD_BACKGROUND);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                showBooksByCategory(categoryId, displayName);
            }
        });
    }

    /**
     * Reset về trạng thái ban đầu (hiển thị danh mục)
     */
    public void resetToInitialState() {
        bookSection.setVisible(false);
        isShowingBooks = false;
        revalidate();
        repaint();
    }

    /**
     * Hiển thị sách theo danh mục
     */
    private void showBooksByCategory(int categoryId, String categoryName) {
        bookSection.setVisible(true);
        isShowingBooks = true;
        sectionTitle.setText(categoryName);
        
        showLoadingState();
        loadBooksAsync(categoryId);
    }

    /**
     * Hiển thị trạng thái loading
     */
    private void showLoadingState() {
        dynamicContent.removeAll();
        
        JLabel loadingLabel = createStyledLabel(
            "Đang tải sách...",
            new Font("Segoe UI", Font.ITALIC, 16),
            TEXT_SECONDARY
        );
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        dynamicContent.add(loadingLabel, BorderLayout.CENTER);
        dynamicContent.revalidate();
        dynamicContent.repaint();
    }

    /**
     * Load sách bất đồng bộ
     */
    private void loadBooksAsync(int categoryId) {
        SwingWorker<List<Book>, Void> worker = new SwingWorker<List<Book>, Void>() {
            @Override
            protected List<Book> doInBackground() throws Exception {
                return (categoryId == 0) ? 
                    bookBLL.getAllBooks() : 
                    bookBLL.getBooksByCategory(categoryId);
            }
            
            @Override
            protected void done() {
                try {
                    List<Book> books = get();
                    displayBooksResult(books);
                } catch (Exception e) {
                    e.printStackTrace();
                    displayErrorState();
                }
            }
        };
        worker.execute();
    }

    /**
     * Hiển thị kết quả danh sách sách
     */
    private void displayBooksResult(List<Book> books) {
        dynamicContent.removeAll();
        
        if (books == null || books.isEmpty()) {
            displayEmptyState();
        } else {
            displayBooksGrid(books);
        }
        
        dynamicContent.revalidate();
        dynamicContent.repaint();
    }

    /**
     * Hiển thị trạng thái không có sách
     */
    private void displayEmptyState() {
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel emptyLabel = createStyledLabel(
            "Không có sách nào trong danh mục này",
            new Font("Segoe UI", Font.ITALIC, 18),
            TEXT_SECONDARY
        );
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel buttonPanel = createBackButtonPanel();
        
        emptyPanel.add(emptyLabel, BorderLayout.CENTER);
        emptyPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dynamicContent.add(emptyPanel, BorderLayout.CENTER);
    }

    /**
     * Hiển thị trạng thái lỗi
     */
    private void displayErrorState() {
        dynamicContent.removeAll();
        
        JPanel errorPanel = new JPanel(new BorderLayout());
        errorPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel errorLabel = createStyledLabel(
            "Lỗi khi tải dữ liệu sách",
            new Font("Segoe UI", Font.BOLD, 16),
            Color.RED
        );
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel buttonPanel = createBackButtonPanel();
        
        errorPanel.add(errorLabel, BorderLayout.CENTER);
        errorPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dynamicContent.add(errorPanel, BorderLayout.CENTER);
        dynamicContent.revalidate();
        dynamicContent.repaint();
    }

    /**
     * Hiển thị lưới sách
     */
    private void displayBooksGrid(List<Book> books) {
        JPanel bookGridWrapper = new JPanel(new BorderLayout());
        bookGridWrapper.setBackground(BACKGROUND_COLOR);
        
        // Thêm nút quay lại
        JPanel backButtonPanel = createBackButtonPanel();
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        bookGridWrapper.add(backButtonPanel, BorderLayout.NORTH);
        bookGridWrapper.add(createBookGridScrollPane(books), BorderLayout.CENTER);
        
        dynamicContent.add(bookGridWrapper, BorderLayout.CENTER);
    }

    /**
     * Tạo panel chứa nút quay lại
     */
    private JPanel createBackButtonPanel() {
        JButton backButton = createStyledButton(
            "← Quay lại danh mục",
            PRIMARY_COLOR,
            Color.WHITE,
            new Font("Segoe UI", Font.BOLD, 14)
        );
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(e -> resetToInitialState());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(backButton);
        
        return buttonPanel;
    }

    /**
     * Tạo scroll pane chứa lưới sách
     */
    private JScrollPane createBookGridScrollPane(List<Book> books) {
        JPanel grid = new JPanel(new GridLayout(0, 4, 25, 25));
        grid.setBackground(BACKGROUND_COLOR);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        for (Book book : books) {
            grid.add(createBookCard(book));
        }
        
        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        return scrollPane;
    }

    /**
     * Tạo thẻ sách
     */
    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(220, 350));

        // Thêm các thành phần của card
        addBookCardComponents(card, book);
        
        // Thêm hover effect
        addBookCardHoverEffect(card);

        return card;
    }

    /**
     * Thêm các thành phần vào book card
     */
    private void addBookCardComponents(JPanel card, Book book) {
        // Ảnh sách
        JLabel imageLabel = createBookImageLabel(book);
        card.add(imageLabel);
        card.add(Box.createVerticalStrut(12));

        // Tên sách
        JLabel titleLabel = createBookTitleLabel(book);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));

        // Tác giả
        JLabel authorLabel = createBookAuthorLabel(book);
        card.add(authorLabel);
        card.add(Box.createVerticalStrut(8));

        // Giá
        JLabel priceLabel = createBookPriceLabel(book);
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(12));

        // Nút chi tiết
        JButton detailButton = createBookDetailButton(book);
        card.add(detailButton);
    }

    /**
     * Tạo label hiển thị ảnh sách
     */
    private JLabel createBookImageLabel(Book book) {
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(120, 160));
        imageLabel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));
        
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(book.getImageUrl()));
            Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setText("<html><center><div style='color: #666666; font-size: 12px;'>Không có<br>hình ảnh</div></center></html>");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setBackground(new Color(250, 250, 250));
            imageLabel.setOpaque(true);
        }
        
        return imageLabel;
    }

    /**
     * Tạo label hiển thị tên sách
     */
    private JLabel createBookTitleLabel(Book book) {
        String title = book.getTitle().length() > 50 ? 
            book.getTitle().substring(0, 47) + "..." : 
            book.getTitle();
            
        JLabel titleLabel = new JLabel(
            "<html><div style='text-align: center; width: 180px;'><b>" + title + "</b></div></html>"
        );
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return titleLabel;
    }

    /**
     * Tạo label hiển thị tác giả
     */
    private JLabel createBookAuthorLabel(Book book) {
        String author = book.getAuthorName().length() > 30 ? 
            book.getAuthorName().substring(0, 27) + "..." : 
            book.getAuthorName();
            
        JLabel authorLabel = createStyledLabel(
            author,
            new Font("Segoe UI", Font.ITALIC, 12),
            TEXT_SECONDARY
        );
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return authorLabel;
    }

    /**
     * Tạo label hiển thị giá
     */
    private JLabel createBookPriceLabel(Book book) {
        JLabel priceLabel = createStyledLabel(
            String.format("%,d", book.getPrice().intValue()) + " đ",
            new Font("Segoe UI", Font.BOLD, 16),
            PRIMARY_COLOR
        );
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        return priceLabel;
    }

    /**
     * Tạo nút chi tiết sách
     */
    private JButton createBookDetailButton(Book book) {
        JButton detailButton = createStyledButton(
            "Chi tiết",
            PRIMARY_COLOR,
            Color.WHITE,
            new Font("Segoe UI", Font.BOLD, 12)
        );
        detailButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailButton.setPreferredSize(new Dimension(100, 35));
        
        // Thêm hover effect
        detailButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                detailButton.setBackground(SECONDARY_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                detailButton.setBackground(PRIMARY_COLOR);
            }
        });
        
        detailButton.addActionListener(e -> showBookDetail(book));
        
        return detailButton;
    }

    /**
     * Thêm hover effect cho book card
     */
    private void addBookCardHoverEffect(JPanel card) {
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new ShadowBorder(),
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    new ShadowBorder(),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });
    }

    /**
     * Hiển thị dialog chi tiết sách
     */
    private void showBookDetail(Book book) {
        JDialog dialog = new JDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), 
            "Chi tiết sách", 
            true
        );
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel content = createBookDetailContent(book, dialog);
        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    /**
     * Tạo nội dung dialog chi tiết sách
     */
    private JPanel createBookDetailContent(Book book, JDialog dialog) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(CARD_BACKGROUND);

        // Tiêu đề
        JLabel titleLabel = createStyledLabel(
            book.getTitle(),
            new Font("Segoe UI", Font.BOLD, 20),
            Color.BLACK
        );
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(15));

        // Thông tin chi tiết
        addBookDetailInfo(content, book);

        // Nút đóng
        JButton closeButton = createStyledButton(
            "Đóng",
            PRIMARY_COLOR,
            Color.WHITE,
            new Font("Segoe UI", Font.BOLD, 14)
        );
        closeButton.addActionListener(e -> dialog.dispose());
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(Box.createVerticalStrut(20));
        content.add(closeButton);

        return content;
    }

    /**
     * Thêm thông tin chi tiết sách vào content panel
     */
    private void addBookDetailInfo(JPanel content, Book book) {
        String[] labels = {"Tác giả:", "Giá:", "Mô tả:"};
        String[] values = {
            book.getAuthorName(),
            String.format("%,d", book.getPrice().intValue()) + " đ",
            book.getDescription() != null ? book.getDescription() : "Chưa có mô tả"
        };

        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.setOpaque(false);
            
            JLabel label = createStyledLabel(
                labels[i],
                new Font("Segoe UI", Font.BOLD, 14),
                Color.BLACK
            );
            label.setPreferredSize(new Dimension(80, 25));
            
            JLabel value = new JLabel(
                "<html><div style='width: 350px;'>" + values[i] + "</div></html>"
            );
            value.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            row.add(label);
            row.add(value);
            content.add(row);
            content.add(Box.createVerticalStrut(10));
        }
    }

    // Utility methods

    /**
     * Tạo label với style
     */
    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    /**
     * Tạo button với style
     */
    private JButton createStyledButton(String text, Color bgColor, Color fgColor, Font font) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Lớp tạo hiệu ứng bóng đổ
     */
    private static class ShadowBorder extends AbstractBorder {
        private static final int SHADOW_SIZE = 8;
        private static final Color SHADOW_COLOR = new Color(0, 0, 0, 25);
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(SHADOW_COLOR);
            
            for (int i = 0; i < SHADOW_SIZE; i++) {
                g2.drawRoundRect(
                    x + i, y + i, 
                    width - 1 - 2 * i, height - 1 - 2 * i, 
                    12, 12
                );
            }
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE, SHADOW_SIZE);
        }
    }
}