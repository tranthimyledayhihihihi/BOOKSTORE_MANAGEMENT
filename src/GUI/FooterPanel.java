
package GUI;

import javax.swing.*;
import java.awt.*;

public class FooterPanel extends JPanel {
    public FooterPanel() {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 32, 8, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 24, 0, 24);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Cột 1: Giới thiệu + social
        JPanel col1 = new JPanel();
        col1.setOpaque(false);
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
        col1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title1 = new JLabel("NHÀ SÁCH ONLINE");
        title1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title1.setForeground(new Color(41, 128, 185));
        title1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc1 = new JLabel("<html>Đến với chúng tôi để trải nghiệm<br>thế giới sách tuyệt vời với đa dạng<br>thể loại và giá cả phải chăng.</html>");
        desc1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        desc1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel social = new JPanel();
        social.setOpaque(false);
        social.setLayout(new BoxLayout(social, BoxLayout.X_AXIS));
        social.setAlignmentX(Component.LEFT_ALIGNMENT);
        social.add(circleIcon("📘"));
        social.add(Box.createHorizontalStrut(10));
        social.add(circleIcon("🐦"));
        social.add(Box.createHorizontalStrut(10));
        social.add(circleIcon("📸"));
        social.add(Box.createHorizontalStrut(10));
        social.add(circleIcon("▶️"));

        col1.add(title1);
        col1.add(Box.createVerticalStrut(8));
        col1.add(desc1);
        col1.add(Box.createVerticalStrut(10));
        col1.add(social);

        // Cột 2: Liên kết nhanh
        JPanel col2 = new JPanel();
        col2.setOpaque(false);
        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));
        col2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title2 = new JLabel("Liên Kết Nhanh");
        title2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title2.setForeground(new Color(41, 128, 185));
        title2.setAlignmentX(Component.LEFT_ALIGNMENT);

        col2.add(title2);
        col2.add(linkLabel("Trang Chủ"));
        col2.add(linkLabel("Cửa Hàng"));
        col2.add(linkLabel("Giỏ Hàng"));
        col2.add(linkLabel("Đơn Hàng"));

        // Cột 3: Danh mục
        JPanel col3 = new JPanel();
        col3.setOpaque(false);
        col3.setLayout(new BoxLayout(col3, BoxLayout.Y_AXIS));
        col3.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title3 = new JLabel("Danh Mục");
        title3.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title3.setForeground(new Color(41, 128, 185));
        title3.setAlignmentX(Component.LEFT_ALIGNMENT);

        col3.add(title3);
        col3.add(linkLabel("Văn Học Việt Nam"));
        col3.add(linkLabel("Văn Học Nước Ngoài"));
        col3.add(linkLabel("Sách Thiếu Nhi"));
        col3.add(linkLabel("Kỹ Năng Sống"));

        // Cột 4: Liên hệ
        JPanel col4 = new JPanel();
        col4.setOpaque(false);
        col4.setLayout(new BoxLayout(col4, BoxLayout.Y_AXIS));
        col4.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title4 = new JLabel("Liên Hệ");
        title4.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title4.setForeground(new Color(41, 128, 185));
        title4.setAlignmentX(Component.LEFT_ALIGNMENT);

        col4.add(title4);
        col4.add(iconText("📍", "48 Cao Thắng, Hải Châu, Đà Nẵng"));
        col4.add(iconText("📞", "(012) 1234 5678"));
        col4.add(iconText("✉️", "contact@nhasachonline.com"));
        col4.add(iconText("⏰", "08:00 - 22:00, Thứ 2 - Chủ Nhật"));

        // Add các cột vào FooterPanel
        gbc.gridx = 0; gbc.weightx = 1.0;
        add(col1, gbc);
        gbc.gridx = 1;
        add(col2, gbc);
        gbc.gridx = 2;
        add(col3, gbc);
        gbc.gridx = 3;
        add(col4, gbc);

        // Dòng bản quyền
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        gbc.insets = new Insets(16, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel copyright = new JLabel("© 2023 Nhà Sách Online. Tất cả quyền được bảo lưu.");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        copyright.setForeground(new Color(100, 100, 100));
        add(copyright, gbc);
    }

    private JLabel circleIcon(String icon) {
        JLabel label = new JLabel(icon, JLabel.CENTER);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        label.setForeground(new Color(41, 128, 185));
        label.setOpaque(true);
        label.setBackground(new Color(245, 245, 255));
        label.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 255), 2, true));
        label.setPreferredSize(new Dimension(32, 32));
        label.setMaximumSize(new Dimension(32, 32));
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel linkLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(80, 80, 80));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel iconText(String icon, String text) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        iconLabel.setForeground(new Color(41, 128, 185));
        iconLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(new Color(80, 80, 80));
        textLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(iconLabel);
        panel.add(Box.createHorizontalStrut(6));
        panel.add(textLabel);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }
}
