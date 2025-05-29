package GUI;

import BLL.BookBLL;
import BLL.AuthorBLL;
import BLL.BookBLL.BookSortType;
import dao.CategoryDAO;
import model.Book;
import model.Category;
import model.Author;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;

public class BookStorePanel extends JPanel {
    private JPanel dynamicContent;
    private int customerId;
    private final BookBLL bookBLL = new BookBLL();
    private final AuthorBLL authorBLL = new AuthorBLL();
    private List<Category> categories;
    private List<Author> authors;
    private JPanel gridPanel;
    private int currentPage = 1;
    private int booksPerPage = 9;
    private int totalPages = 1;
    private JPanel paginationPanel;
    private JLabel infoLabel;

    // Filter/sort state
    private String searchKeyword = "";
    private Integer selectedCategoryId = null;
    private Integer selectedAuthorId = null;
    private Integer selectedRating = null;
    private BookSortType selectedSortType = BookSortType.DEFAULT;

    // Filter controls
    private JTextField searchField;
    private JComboBox<String> sortCombo;
    private ButtonGroup categoryGroup;
    private ButtonGroup authorGroup;

    // Constructor mặc định (hiển thị tất cả)
    public BookStorePanel(int customerId, JPanel dynamicContent) {
        this(customerId, dynamicContent, null);
    }

    // Constructor filter theo category
    public BookStorePanel(int customerId, JPanel dynamicContent, Integer filterCategoryId) {
        this.customerId = customerId;
        this.dynamicContent = dynamicContent;
        this.selectedCategoryId = filterCategoryId;
        setLayout(new BorderLayout());
        setBackground(new Color(246, 247, 251));

        // Lấy dữ liệu từ DB
        categories = new CategoryDAO().getAllCategories();
        authors = authorBLL.getAllAuthors();

        // Sidebar bộ lọc
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Main content
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // Banner
        mainContent.add(createBanner(), BorderLayout.NORTH);

        // Lưới sách + filter top
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Thanh filter top
        centerPanel.add(createTopFilterPanel());

        // Lưới sách (GridLayout 3x3)
        gridPanel = new JPanel(new GridLayout(0, 3, 24, 32));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        JScrollPane gridScroll = new JScrollPane(gridPanel);
        gridScroll.setBorder(null);
        gridScroll.setOpaque(false);
        gridScroll.getViewport().setOpaque(false);
        gridScroll.getVerticalScrollBar().setUnitIncrement(16);
        gridScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        centerPanel.add(gridScroll);

        mainContent.add(centerPanel, BorderLayout.CENTER);

        // Bọc mainContent bằng JScrollPane để cuộn nếu dài
        JScrollPane mainScroll = new JScrollPane(mainContent);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);

        // Tạo panel chứa cả mainScroll và paginationPanel
        paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        paginationPanel.setOpaque(false);

        JPanel contentWithPagination = new JPanel(new BorderLayout());
        contentWithPagination.add(mainScroll, BorderLayout.CENTER);
        contentWithPagination.add(paginationPanel, BorderLayout.SOUTH);

        add(contentWithPagination, BorderLayout.CENTER);

        // Load dữ liệu lần đầu
        updatePagingAndBooks();
    }

    // Banner gradient
    private JPanel createBanner() {
        JPanel banner = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), getWidth(), getHeight(), new Color(52, 152, 219));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.dispose();
            }
        };
        banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
        banner.setPreferredSize(new Dimension(0, 90));
        banner.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));
        JLabel title = new JLabel("Cửa hàng sách");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel subtitle = new JLabel("Trang chủ / Cửa hàng");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        subtitle.setForeground(new Color(230, 240, 255));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner.add(title);
        banner.add(Box.createVerticalStrut(6));
        banner.add(subtitle);
        return banner;
    }

    // Sidebar bộ lọc
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(246, 247, 251));
        sidebar.setBorder(BorderFactory.createEmptyBorder(18, 8, 18, 8));
        sidebar.setPreferredSize(new Dimension(290, 0));

        sidebar.add(filterCard(createSearchBox(), 80));
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(filterCard(createCategoryFilter(), Integer.MAX_VALUE));
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(filterCard(createAuthorFilter(), Integer.MAX_VALUE));
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(filterCard(createRatingFilter(), Integer.MAX_VALUE));
        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    // Tạo card bo góc, shadow cho từng filter
    private JPanel filterCard(JPanel inner, int maxHeight) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 240), 1, true),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        card.setMaximumSize(new Dimension(280, maxHeight));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    // Ô tìm kiếm hiện đại
    private JPanel createSearchBox() {
        JPanel panel = new JPanel(new BorderLayout(6, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(220, 40));
        panel.setPreferredSize(new Dimension(220, 40));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        searchField.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 0));
        searchField.setForeground(Color.GRAY);
        searchField.setText("");

        JButton searchBtn = new JButton("\uD83D\uDD0D");
        searchBtn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        searchBtn.setBackground(new Color(41, 128, 185));
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        searchBtn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Sự kiện tìm kiếm
        searchBtn.addActionListener(e -> {
            searchKeyword = searchField.getText().trim();
            currentPage = 1;
            updatePagingAndBooks();
        });
        searchField.addActionListener(e -> {
            searchKeyword = searchField.getText().trim();
            currentPage = 1;
            updatePagingAndBooks();
        });

        panel.add(searchField, BorderLayout.CENTER);
        panel.add(searchBtn, BorderLayout.EAST);
        return panel;
    }

    // Danh mục
    private JPanel createCategoryFilter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel title = new JLabel("Danh mục");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        panel.add(title);

        categoryGroup = new ButtonGroup();
        JRadioButton allBtn = new JRadioButton("Tất cả");
        allBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        allBtn.setBackground(Color.WHITE);
        allBtn.setSelected(true);
        allBtn.addActionListener(e -> {
            selectedCategoryId = null;
            currentPage = 1;
            updatePagingAndBooks();
        });
        categoryGroup.add(allBtn);
        panel.add(allBtn);

        for (Category cat : categories) {
            JRadioButton cb = new JRadioButton(cat.getName() + " (" + cat.getBookCount() + ")");
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            cb.setBackground(Color.WHITE);
            cb.addActionListener(e -> {
                selectedCategoryId = cat.getCategoryId();
                currentPage = 1;
                updatePagingAndBooks();
            });
            categoryGroup.add(cb);
            panel.add(cb);
        }
        return panel;
    }

    // Tác giả
    private JPanel createAuthorFilter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel title = new JLabel("Tác giả");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        panel.add(title);

        authorGroup = new ButtonGroup();
        JRadioButton allBtn = new JRadioButton("Tất cả");
        allBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        allBtn.setBackground(Color.WHITE);
        allBtn.setSelected(true);
        allBtn.addActionListener(e -> {
            selectedAuthorId = null;
            currentPage = 1;
            updatePagingAndBooks();
        });
        authorGroup.add(allBtn);
        panel.add(allBtn);

        for (Author author : authors) {
            JRadioButton cb = new JRadioButton(author.getName());
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            cb.setBackground(Color.WHITE);
            cb.addActionListener(e -> {
                selectedAuthorId = author.getAuthorId();
                currentPage = 1;
                updatePagingAndBooks();
            });
            authorGroup.add(cb);
            panel.add(cb);
        }
        return panel;
    }

    // Đánh giá (rating) filter như hình
    private JPanel createRatingFilter() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel title = new JLabel("Đánh giá");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        panel.add(title);

        ButtonGroup ratingGroup = new ButtonGroup();

        int[] ratings = {5, 4, 3};
        for (int r : ratings) {
            JRadioButton radio = new JRadioButton();
            radio.setOpaque(false);
            radio
.setFocusable(false);

            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setOpaque(false);

            row.add(radio);

            // Dãy sao vàng
            for (int i = 1; i <= 5; i++) {
                JLabel star = new JLabel(i <= r ? "\u2605" : "\u2606");
                star.setFont(new Font("Segoe UI", Font.BOLD, 18));
                star.setForeground(new Color(255, 180, 0));
                row.add(star);
            }

            if (r < 5) {
                JLabel label = new JLabel(" & trở lên");
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setForeground(Color.DARK_GRAY);
                row.add(label);
            }

            // Sự kiện chọn filter rating
            int ratingValue = r;
            radio.addActionListener(e -> {
                selectedRating = ratingValue;
                currentPage = 1;
                updatePagingAndBooks();
            });

            ratingGroup.add(radio);
            panel.add(row);
            panel.add(Box.createVerticalStrut(4));
        }

        // Nút "Tất cả"
        JRadioButton allBtn = new JRadioButton("Tất cả");
        allBtn.setOpaque(false);
        allBtn.setFocusable(false);
        allBtn.setSelected(true);
        allBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        allBtn.addActionListener(e -> {
            selectedRating = null;
            currentPage = 1;
            updatePagingAndBooks();
        });
        ratingGroup.add(allBtn);
        panel.add(allBtn);

        return panel;
    }

    // Thanh filter top (hiển thị số lượng, sắp xếp)
    private JPanel createTopFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));

        infoLabel = new JLabel();
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        infoLabel.setForeground(new Color(80, 80, 100));
        panel.add(infoLabel, BorderLayout.WEST);

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sortPanel.setOpaque(false);
        JLabel sortLabel = new JLabel("Sắp xếp theo:");
        sortLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sortCombo = new JComboBox<>(new String[]{"Mặc định", "Mới nhất", "Giá tăng dần", "Giá giảm dần", "Đánh giá cao"});
        sortCombo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sortCombo.addActionListener(e -> {
            int idx = sortCombo.getSelectedIndex();
            switch (idx) {
                case 1: selectedSortType = BookSortType.NEWEST; break;
                case 2: selectedSortType = BookSortType.PRICE_ASC; break;
                case 3: selectedSortType = BookSortType.PRICE_DESC; break;
                case 4: selectedSortType = BookSortType.RATING_DESC; break;
                default: selectedSortType = BookSortType.DEFAULT;
            }
            currentPage = 1;
            updatePagingAndBooks();
        });
        sortPanel.add(sortLabel);
        sortPanel.add(sortCombo);
        panel.add(sortPanel, BorderLayout.EAST);

        return panel;
    }

    // Cập nhật lại phân trang và load sách
    private void updatePagingAndBooks() {
        // Lấy tổng số sách sau filter
        int totalBooks = bookBLL.getBooksFilteredAndSorted(
            searchKeyword,
            selectedCategoryId,
            selectedAuthorId,
            selectedSortType,
            selectedRating,
            1,
            Integer.MAX_VALUE
        ).size();

        // Lấy tổng số trang sau filter (DÙNG HÀM ĐÚNG)
        totalPages = bookBLL.getTotalPagesFiltered(
            searchKeyword,
            selectedCategoryId,
            selectedAuthorId,
            selectedRating,
            booksPerPage
        );

        if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;
        if (totalPages == 0) currentPage = 1;
        updateInfoLabel(totalBooks);
        loadBooksToGrid(currentPage);
        updatePaginationPanel();
    }

    private void updateInfoLabel(int totalBooks) {
        int start = (currentPage - 1) * booksPerPage + 1;
        int end = Math.min(currentPage * booksPerPage, totalBooks);
        if (totalBooks == 0) {
            infoLabel.setText("Không có sách nào phù hợp");
        } else {
            infoLabel.setText("Hiển thị " + start + " - " + end + " / " + totalBooks + " cuốn sách");
        }
    }

    // Đổ dữ liệu sách vào grid cho từng trang
    private void loadBooksToGrid(int page) {
        gridPanel.removeAll();
        List<Book> books = bookBLL.getBooksFilteredAndSorted(
            searchKeyword,
            selectedCategoryId,
            selectedAuthorId,
            selectedSortType,
            selectedRating,
            page, // PHẢI truyền page (currentPage)
            booksPerPage
        );
        for (Book book : books) {
            gridPanel.add(createBookCard(book));
        }
        // Nếu trang cuối không đủ 9 quyển, thêm panel rỗng để căn giữa
        int empty = booksPerPage - books.size();
        for (int i = 0; i < empty; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setOpaque(false);
            gridPanel.add(emptyPanel);
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // Phân trang hiện đại
    private void updatePaginationPanel() {
        paginationPanel.removeAll();

        if (totalPages <= 1) {
            paginationPanel.revalidate();
            paginationPanel.repaint();
            return;
        }

        // Nút về đầu
        paginationPanel.add(createPageButton("«", 1, currentPage > 1));

        // Nút lùi
        paginationPanel.add(createPageButton("<", currentPage - 1, currentPage > 1));

        // Số trang (giới hạn hiển thị 5 số, có thể mở rộng)
        int maxShow = 5;
        int start = Math.max(1, currentPage - 2);
        int end = Math.min(totalPages, start + maxShow - 1);
        if (end - start < maxShow - 1) start = Math.max(1, end - maxShow + 1);

        for (int i = start; i <= end; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btn.setPreferredSize(new Dimension(40, 36));
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(true);
            btn.setOpaque(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (i == currentPage) {
                btn.setBackground(new Color(41, 128, 185));
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(Color.WHITE);
                btn.setForeground(new Color(41, 128, 185));
            }
            btn.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true));
            int page = i;
            btn.addActionListener(e -> {
                currentPage = page;
                updatePagingAndBooks();
            });
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (page != currentPage) btn.setBackground(new Color(230, 240, 255));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (page != currentPage) btn.setBackground(Color.WHITE);
                }
            });
            paginationPanel.add(btn);
        }

        // Nút tiến
        paginationPanel.add(createPageButton(">", currentPage + 1, currentPage < totalPages));

        // Nút về cuối
        paginationPanel.add(createPageButton("»", totalPages, currentPage < totalPages));

        paginationPanel.revalidate();
        paginationPanel.repaint();
    }

    private JButton createPageButton(String text, int page, boolean enabled) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setPreferredSize(new Dimension(40, 36));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(41, 128, 185));
        btn.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true));
        btn.setFocusPainted(false);
        btn.setEnabled(enabled);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            currentPage = page;
            updatePagingAndBooks();
        });
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(new Color(230, 240, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }

    // Phương thức hiển thị BookDetailPanel (đã sửa)
    private void openBookDetail(Book book) {
    System.out.println("openBookDetail called for book: " + book.getTitle());
    if (dynamicContent != null) {
        System.out.println("dynamicContent is not null, replacing content...");
        dynamicContent.removeAll();
        dynamicContent.add(new BookDetailPanel(book, customerId, dynamicContent), BorderLayout.CENTER);
        dynamicContent.revalidate();
        dynamicContent.repaint();
    } else {
        System.out.println("dynamicContent is null!");
    }
}

    // Card sách động từ DB, trạng thái + thể loại căn giữa, đánh giá là icon sao, bo góc, shadow, hover, giá gạch chân
    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 18));
                g2.fillRoundRect(4, 4, getWidth() - 8, getHeight() - 8, 18, 18);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.setPreferredSize(new Dimension(220, 350));
        card.setMaximumSize(new Dimension(220, 350));
        card.setAlignmentY(Component.TOP_ALIGNMENT);

        // Badge trạng thái
        String status;
        Color statusColor;
        if (book.getStockQuantity() == 0) {
            status = "Sắp hết";
            statusColor = new Color(231, 76, 60);
        } else if (book.getStockQuantity() <= 10) {
            status = "Sắp hết";
            statusColor = new Color(255, 165, 0);
        } else {
            status = "Còn hàng";
            statusColor = new Color(46, 204, 113);
        }
        JLabel statusLabel = new JLabel(status);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(statusColor);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

        // Badge danh mục (có thể nhiều badge, ví dụ: "Văn học Việt Nam", "Kỹ năng sống")
        JPanel catBadges = new JPanel();
        catBadges.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));
        catBadges.setOpaque(false);
        for (String cat : book.getCategoryName().split(",")) {
            JLabel catLabel = new JLabel(cat.trim());
            catLabel.setOpaque(true);
            catLabel.setBackground(new Color(41, 128, 185));
            catLabel.setForeground(Color.WHITE);
            catLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            catLabel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
            catBadges.add(catLabel);
        }

        // Panel chứa badge trạng thái + thể loại, căn giữa
        JPanel badgePanel = new JPanel();
        badgePanel.setLayout(new BoxLayout(badgePanel, BoxLayout.X_AXIS));
        badgePanel.setOpaque(false);
        badgePanel.add(statusLabel);
        badgePanel.add(Box.createHorizontalStrut(6));
        badgePanel.add(catBadges);
        badgePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ảnh sách
        JLabel imgLabel;
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            try {
                ImageIcon icon;
                if (book.getImageUrl().startsWith("http")) {
                    icon = new ImageIcon(new URL(book.getImageUrl()));
                } else {
                    icon = new ImageIcon(book.getImageUrl());
                }
                Image img = icon.getImage().getScaledInstance(120, 160, Image.SCALE_SMOOTH);
                imgLabel = new JLabel(new ImageIcon(img));
            } catch (Exception ex) {
                imgLabel = new JLabel("Tắt Đèn", JLabel.CENTER);
            }
        } else {
            imgLabel = new JLabel("Tắt Đèn", JLabel.CENTER);
        }
        imgLabel.setPreferredSize(new Dimension(120, 160));
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tên sách
        JLabel name = new JLabel(book.getTitle());
        name.setFont(new Font("Segoe UI", Font.BOLD, 15));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tác giả
        JLabel author = new JLabel(book.getAuthorName());
        author.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        author.setForeground(new Color(100, 100, 100));
        author.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Đánh giá: icon sao vàng, căn trái
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ratingPanel.setOpaque(false);
        double avg = book.getAvgRating();
        int fullStars = (int) avg;
        for (int i = 0; i < 5; i++) {
            JLabel star;
            if (i < fullStars) {
                star = new JLabel("\u2605");
                star.setForeground(new Color(255, 180, 0));
            } else {
                star = new JLabel("\u2606");
                star.setForeground(new Color(220, 220, 220));
            }
            star.setFont(new Font("Segoe UI", Font.PLAIN, 17));
            ratingPanel.add(star);
        }
        JLabel reviewCount = new JLabel(" (" + book.getReviewCount() + ")");
        reviewCount.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reviewCount.setForeground(new Color(120, 120, 120));
        ratingPanel.add(reviewCount);

        // Giá: in đậm, gạch chân, màu xanh
        JLabel price = new JLabel("<html><u>" + String.format("%,.0f", book.getPrice()) + " đ</u></html>");
        price.setFont(new Font("Segoe UI", Font.BOLD, 17));
        price.setForeground(new Color(41, 128, 185));
        price.setAlignmentX(Component.LEFT_ALIGNMENT);
        price.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        price.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Nút chi tiết bo góc, border xanh, hover
        JButton detailBtn = new JButton("Chi tiết");
        detailBtn.addActionListener(e -> openBookDetail(book));
        detailBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        detailBtn.setBackground(Color.WHITE);
        detailBtn.setForeground(new Color(41, 128, 185));
        detailBtn.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true));
        detailBtn.setFocusPainted(false);
        detailBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailBtn.setContentAreaFilled(false);
        detailBtn.setOpaque(true);
        detailBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                detailBtn.setBackground(new Color(230, 240, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                detailBtn.setBackground(Color.WHITE);
            }
        });

        // Hiệu ứng hover cho card
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true),
                    BorderFactory.createEmptyBorder(16, 16, 16, 16)
                ));
                card.setBackground(new Color(245, 250, 255));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
                card.setBackground(Color.WHITE);
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                openBookDetail(book);
            }
        });

        card.setOpaque(true);
        card.add(badgePanel);
        card.add(Box.createVerticalStrut(8));
        card.add(imgLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(name);
        card.add(author);
        card.add(Box.createVerticalStrut(4));
        card.add(ratingPanel);
        card.add(price);
        card.add(Box.createVerticalStrut(8));
        card.add(detailBtn);

        return card;
    }
}