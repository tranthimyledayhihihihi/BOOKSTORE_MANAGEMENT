package GUI;

import BLL.CartBLL;
import model.Cart;
import model.CartItem;
import model.Book;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CartPanel extends JPanel {
    private int customerId;
    private JPanel dynamicContent;
    private MainGUI mainGUI; // Tham chiếu MainGUI
    private CartBLL cartBLL;
    private JPanel itemsPanel;
    private JLabel totalLabel, itemCountLabel, shippingLabel, discountLabel, grandTotalLabel;
    private JTextField discountField;

    public CartPanel(int customerId, JPanel dynamicContent, MainGUI mainGUI) {
        this.customerId = customerId;
        this.dynamicContent = dynamicContent;
        this.mainGUI = mainGUI;
        this.cartBLL = new CartBLL();
        // ... (phần còn lại giữ nguyên)
    
        setLayout(new GridBagLayout());
        setBackground(new Color(246, 247, 251));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 20);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.7; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;

        // ==== BÊN TRÁI: CHI TIẾT GIỎ HÀNG ====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 30, 20, 30)
        ));

        JLabel title = new JLabel("Chi tiết giỏ hàng");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(41, 128, 185));
        leftPanel.add(title, BorderLayout.NORTH);

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        add(leftPanel, gbc);

        // ==== BÊN PHẢI: TÓM TẮT ĐƠN HÀNG ====
        gbc.gridx = 1; gbc.weightx = 0.3; gbc.insets = new Insets(30, 0, 30, 30);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 30, 20, 30)
        ));
        rightPanel.setPreferredSize(new Dimension(320, 400));

        JLabel summaryTitle = new JLabel("Tóm tắt đơn hàng");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        summaryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(summaryTitle);
        rightPanel.add(Box.createVerticalStrut(10));

        itemCountLabel = new JLabel("Tổng số sản phẩm: 0");
        totalLabel = new JLabel("Tạm tính: 0 đ");
        shippingLabel = new JLabel("Phí vận chuyển: 0 đ");
        discountLabel = new JLabel("Giảm giá: 0 đ");
        grandTotalLabel = new JLabel("Tổng cộng: 0 đ");
        for (JLabel lbl : new JLabel[]{itemCountLabel, totalLabel, shippingLabel, discountLabel, grandTotalLabel}) {
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            rightPanel.add(lbl);
            rightPanel.add(Box.createVerticalStrut(4));
        }

        rightPanel.add(Box.createVerticalStrut(10));
        JLabel discountText = new JLabel("Mã giảm giá:");
        discountText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rightPanel.add(discountText);

        JPanel discountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        discountPanel.setOpaque(false);
        discountField = new JTextField(10);
        JButton applyBtn = new JButton("Áp dụng");
        applyBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        applyBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng mã giảm giá đang phát triển!"));
        discountPanel.add(discountField);
        discountPanel.add(Box.createHorizontalStrut(8));
        discountPanel.add(applyBtn);
        rightPanel.add(discountPanel);

        rightPanel.add(Box.createVerticalStrut(16));
        JButton checkoutBtn = new JButton("Thanh toán");
        checkoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        checkoutBtn.setBackground(new Color(41, 128, 185));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkoutBtn.setBorder(new LineBorder(new Color(41, 128, 185), 1, true));
        checkoutBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng thanh toán đang phát triển!"));
        rightPanel.add(checkoutBtn);

        add(rightPanel, gbc);

        // Load dữ liệu giỏ hàng
        loadCart();
    }

    private void loadCart() {
        itemsPanel.removeAll();
        Cart cart = cartBLL.getCartByCustomerId(customerId);
        List<CartItem> items = (cart != null) ? cart.getItems() : null;
        if (items == null || items.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setOpaque(false);
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            JLabel icon = new JLabel("\uD83D\uDED2", SwingConstants.CENTER); // icon giỏ hàng
            icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel empty = new JLabel("Giỏ hàng trống");
            empty.setFont(new Font("Segoe UI", Font.BOLD, 20));
            empty.setForeground(Color.GRAY);
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel desc = new JLabel("Bạn chưa có sản phẩm nào trong giỏ hàng.");
            desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            desc.setForeground(Color.GRAY);
            desc.setAlignmentX(Component.CENTER_ALIGNMENT);
            JButton btnShop = new JButton("Tiếp tục mua sắm");
            btnShop.addActionListener(e -> {
    if (mainGUI != null) {
        mainGUI.showBookStore();
    }
});
            btnShop.setFont(new Font("Segoe UI", Font.BOLD, 15));
            btnShop.setBackground(new Color(41, 128, 185));
            btnShop.setForeground(Color.WHITE);
            btnShop.setFocusPainted(false);
            btnShop.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnShop.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnShop.addActionListener(e -> {
                if (dynamicContent != null) {
                    dynamicContent.removeAll();
                    dynamicContent.add(new BookStorePanel(customerId, dynamicContent), BorderLayout.CENTER);
                    dynamicContent.revalidate();
                    dynamicContent.repaint();
                }
            });
            emptyPanel.add(Box.createVerticalStrut(30));
            emptyPanel.add(icon);
            emptyPanel.add(Box.createVerticalStrut(10));
            emptyPanel.add(empty);
            emptyPanel.add(Box.createVerticalStrut(5));
            emptyPanel.add(desc);
            emptyPanel.add(Box.createVerticalStrut(15));
            emptyPanel.add(btnShop);
            itemsPanel.add(emptyPanel);

            // Reset summary
            itemCountLabel.setText("Tổng số sản phẩm: 0");
            totalLabel.setText("Tạm tính: 0 đ");
            shippingLabel.setText("Phí vận chuyển: 0 đ");
            discountLabel.setText("Giảm giá: 0 đ");
            grandTotalLabel.setText("Tổng cộng: 0 đ");
        } else {
            int total = 0, count = 0;
            for (CartItem item : items) {
                JPanel itemPanel = createCartItemPanel(item);
                itemsPanel.add(itemPanel);
                itemsPanel.add(Box.createVerticalStrut(10));
                total += item.getBook().getPrice().intValue() * item.getQuantity();
                count += item.getQuantity();
            }
            // Tóm tắt đơn hàng
            itemCountLabel.setText("Tổng số sản phẩm: " + count);
            totalLabel.setText("Tạm tính: " + String.format("%,d", total) + " đ");
            shippingLabel.setText("Phí vận chuyển: 0 đ");
            discountLabel.setText("Giảm giá: 0 đ");
            grandTotalLabel.setText("Tổng cộng: " + String.format("%,d", total) + " đ");
        }
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    private JPanel createCartItemPanel(CartItem item) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(500, 100));

        // Ảnh sách
        JLabel imgLabel = new JLabel();
        imgLabel.setPreferredSize(new Dimension(60, 80));
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(item.getBook().getImageUrl()));
            Image img = icon.getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);
            imgLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imgLabel.setText("No Image");
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        panel.add(imgLabel, BorderLayout.WEST);

        // Thông tin sách
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(item.getBook().getTitle());
        name.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel author = new JLabel("Tác giả: " + item.getBook().getAuthorName());
        author.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel price = new JLabel("Giá: " + String.format("%,d", item.getBook().getPrice().intValue()) + " đ");
        price.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoPanel.add(name);
        infoPanel.add(author);
        infoPanel.add(price);

        panel.add(infoPanel, BorderLayout.CENTER);

        // Số lượng + xóa
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        quantityPanel.setOpaque(false);
        JButton btnMinus = new JButton("-");
        JLabel lblQuantity = new JLabel(" " + item.getQuantity() + " ");
        JButton btnPlus = new JButton("+");
        btnMinus.setFocusPainted(false);
        btnPlus.setFocusPainted(false);

        btnMinus.addActionListener(e -> {
            if (item.getQuantity() > 1) {
                cartBLL.updateCartItemQuantity(item.getCartItemId(), item.getQuantity() - 1);
                loadCart();
            }
        });
        btnPlus.addActionListener(e -> {
            cartBLL.updateCartItemQuantity(item.getCartItemId(), item.getQuantity() + 1);
            loadCart();
        });

        quantityPanel.add(btnMinus);
        quantityPanel.add(lblQuantity);
        quantityPanel.add(btnPlus);

        JButton btnRemove = new JButton("Xóa");
        btnRemove.setForeground(Color.RED);
        btnRemove.setFocusPainted(false);
        btnRemove.addActionListener(e -> {
            cartBLL.removeCartItem(item.getCartItemId());
            loadCart();
        });

        actionPanel.add(quantityPanel);
        actionPanel.add(Box.createVerticalStrut(8));
        actionPanel.add(btnRemove);

        panel.add(actionPanel, BorderLayout.EAST);

        return panel;
    }
}
