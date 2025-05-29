package GUI;

import model.Book;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

public class BookDetailPanel extends JPanel {
    private Book book;
    private int customerId;
    private int quantity = 1;
    private JLabel lblQuantity;
    private JPanel dynamicContent;

    public BookDetailPanel(Book book, int customerId, JPanel dynamicContent) {
        this.book = book;
        this.customerId = customerId;
        this.dynamicContent = dynamicContent;
        setOpaque(false);
        setLayout(new BorderLayout());

        // Panel căn giữa với padding
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel bo góc với shadow
JPanel cardPanel = new RoundedPanel(25, new Color(255,255,255,245));
cardPanel.setLayout(new BorderLayout(20, 0));
cardPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
cardPanel.setPreferredSize(new Dimension(700, 380)); // Tăng chiều cao lên

        // Ảnh bìa với border và shadow
        JLabel coverLabel = new JLabel();
        coverLabel.setPreferredSize(new Dimension(140, 200));
        coverLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(book.getImageUrl()));
            Image img = icon.getImage().getScaledInstance(140, 200, Image.SCALE_SMOOTH);
            coverLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            coverLabel.setText("No Image");
            coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            coverLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        }

        // Thông tin sách
       JPanel infoPanel = new JPanel();
infoPanel.setOpaque(false);
infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)); // Thêm dòng này
        // Nút quay lại với hiệu ứng hover
        if (dynamicContent != null) {
            JButton btnBack = createMainButton("← Quay lại", new Color(230, 240, 255), new Color(41, 128, 185));
            btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnBack.addActionListener(e -> {
                dynamicContent.removeAll();
                dynamicContent.add(new BookStorePanel(customerId, dynamicContent), BorderLayout.CENTER);
                dynamicContent.revalidate();
                dynamicContent.repaint();
            });
            infoPanel.add(btnBack);
            infoPanel.add(Box.createVerticalStrut(8));
        }

        // Tiêu đề sách với font đẹp
        JLabel title = new JLabel(book.getTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(41, 128, 185));
        infoPanel.add(title);

        // Tác giả
        JLabel author = new JLabel("Tác giả: " + book.getAuthorName());
        author.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        author.setForeground(new Color(52, 73, 94));
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(author);

        // Giá với font đậm và màu nổi bật
        JLabel price = new JLabel(String.format("%,d", book.getPrice().intValue()) + " đ");
        price.setFont(new Font("Segoe UI", Font.BOLD, 22));
        price.setForeground(new Color(0, 123, 255));
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(price);

        // Số lượng còn lại với màu cảnh báo
        JLabel stock = new JLabel("Còn " + book.getStockQuantity() + " sản phẩm");
        stock.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        stock.setForeground(new Color(183, 129, 0));
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(stock);

        // Thông tin chi tiết
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(labelInfo("Danh mục: ", book.getCategoryName()));
        infoPanel.add(labelInfo("Năm xuất bản: ", book.getPublicationDate() != null
                ? new SimpleDateFormat("yyyy").format(book.getPublicationDate())
                : "Không rõ"));
        infoPanel.add(labelInfo("Nhà xuất bản: ", book.getPublisherName()));
        infoPanel.add(labelInfo("ISBN: ", book.getIsbn()));

        // Số lượng mua với nút tăng giảm đẹp
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        quantityPanel.setOpaque(false);
        JButton btnMinus = createRoundButton("-");
        lblQuantity = new JLabel(" 1 ");
        lblQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JButton btnPlus = createRoundButton("+");
        quantityPanel.add(btnMinus);
        quantityPanel.add(lblQuantity);
        quantityPanel.add(btnPlus);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(quantityPanel);

        // Nút thao tác với màu sắc và hiệu ứng
        // Nút thao tác
JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
btnPanel.setOpaque(false);
JButton btnAddToCart = createMainButton("Thêm vào giỏ hàng", new Color(41, 128, 185), Color.WHITE);
JButton btnBuyNow = createMainButton("Mua ngay", new Color(39, 174, 96), Color.WHITE);
btnPanel.add(btnAddToCart);
btnPanel.add(btnBuyNow);
infoPanel.add(Box.createVerticalStrut(10));
infoPanel.add(btnPanel);

        // Sự kiện tăng giảm số lượng
        btnMinus.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                lblQuantity.setText(" " + quantity + " ");
            }
        });
        btnPlus.addActionListener(e -> {
            if (quantity < book.getStockQuantity()) {
                quantity++;
                lblQuantity.setText(" " + quantity + " ");
            }
        });

        // Sự kiện mua ngay
        btnBuyNow.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Bạn đã mua " + quantity + " cuốn '" + book.getTitle() + "' thành công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        });

        // Sự kiện thêm vào giỏ
        btnAddToCart.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Đã thêm " + quantity + " cuốn vào giỏ hàng!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        });

        // Layout trái phải
        cardPanel.add(coverLabel, BorderLayout.WEST);
        cardPanel.add(infoPanel, BorderLayout.CENTER);

        centerPanel.add(cardPanel);
        add(centerPanel, BorderLayout.NORTH);

        // Tab mô tả và thông tin chi tiết
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabbedPane.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Tab mô tả
        JTextArea descArea = new JTextArea(book.getDescription());
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        descArea.setBackground(new Color(246, 247, 251));
        descArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPane.addTab("Mô tả sách", new JScrollPane(descArea));

        // Tab thông tin chi tiết
       // Tab thông tin chi tiết
JPanel detailPanel = new JPanel();
detailPanel.setOpaque(false);
detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
detailPanel.add(labelInfo("Tác giả: ", book.getAuthorName()));
detailPanel.add(labelInfo("Danh mục: ", book.getCategoryName()));
detailPanel.add(labelInfo("Năm xuất bản: ", book.getPublicationDate() != null
        ? new SimpleDateFormat("yyyy").format(book.getPublicationDate())
        : "Không rõ"));
detailPanel.add(labelInfo("Nhà xuất bản: ", book.getPublisherName()));
detailPanel.add(labelInfo("ISBN: ", book.getIsbn()));

// Bọc detailPanel bằng JScrollPane để có thể cuộn
JScrollPane detailScroll = new JScrollPane(detailPanel);
detailScroll.setBorder(null); // Không viền
tabbedPane.addTab("Thông tin chi tiết", detailScroll);


        add(tabbedPane, BorderLayout.CENTER);
    }

    // Helper tạo label info
    private JPanel labelInfo(String label, String value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        JLabel l1 = new JLabel(label);
        l1.setFont(new Font("Segoe UI", Font.BOLD, 15));
        JLabel l2 = new JLabel(value);
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        p.add(l1);
        p.add(l2);
        return p;
    }

    // Helper tạo nút bo tròn nhỏ
    private JButton createRoundButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(41, 128, 185));
        btn.setBorder(new LineBorder(new Color(41, 128, 185), 1, true));
        btn.setPreferredSize(new Dimension(36, 36));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(230, 240, 255));
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }

    // Helper tạo nút chính bo tròn
    private JButton createMainButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(new RoundBorder(bg, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(bg.darker());
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    // Panel bo góc
    static class RoundedPanel extends JPanel {
        private int radius;
        private Color bg;
        public RoundedPanel(int radius, Color bg) {
            super();
            this.radius = radius;
            this.bg = bg;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }
    }

    // Border bo góc cho nút
    static class RoundBorder extends LineBorder {
        private int arc;
        public RoundBorder(Color color, int arc) {
            super(color, 1, true);
            this.arc = arc;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(lineColor);
            g2.drawRoundRect(x, y, width - 1, height - 1, arc, arc);
        }
    }
}