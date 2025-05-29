
package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SubscribePanel extends JPanel {
    public SubscribePanel() {
        setBackground(new Color(41, 128, 185));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(32, 0, 32, 0));

        JLabel title = new JLabel("Đăng ký nhận thông tin");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Đăng ký email để cập nhật những sách mới nhất và khuyến mãi đặc biệt.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.X_AXIS));

        // Custom JTextField với placeholder
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        emailField.setPreferredSize(new Dimension(350, 40));
        emailField.setMaximumSize(new Dimension(350, 40));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            BorderFactory.createEmptyBorder(0, 12, 0, 0)
        ));

        // Placeholder cho JTextField
        String placeholder = "Địa chỉ email của bạn";
        emailField.setForeground(Color.GRAY);
        emailField.setText(placeholder);

        emailField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (emailField.getText().equals(placeholder)) {
                    emailField.setText("");
                    emailField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (emailField.getText().isEmpty()) {
                    emailField.setForeground(Color.GRAY);
                    emailField.setText(placeholder);
                }
            }
        });

        // Nút Đăng ký bo góc, hiệu ứng hover
        JButton subscribeBtn = new JButton("Đăng ký") {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(new Color(30, 110, 180).darker());
                } else if (getModel().isRollover()) {
                    g.setColor(new Color(30, 110, 180).brighter());
                } else {
                    g.setColor(new Color(30, 110, 180));
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                super.paintComponent(g);
            }
        };
        subscribeBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        subscribeBtn.setForeground(Color.WHITE);
        subscribeBtn.setFocusPainted(false);
        subscribeBtn.setContentAreaFilled(false);
        subscribeBtn.setBorder(BorderFactory.createEmptyBorder(8, 32, 8, 32));
        subscribeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        subscribeBtn.setOpaque(false);

        // Đảm bảo chữ không bị vẽ đè
        subscribeBtn.setHorizontalTextPosition(SwingConstants.CENTER);

        form.add(emailField);
        form.add(Box.createRigidArea(new Dimension(16, 0)));
        form.add(subscribeBtn);
        form.setAlignmentX(CENTER_ALIGNMENT);

        add(title);
        add(Box.createVerticalStrut(10));
        add(subtitle);
        add(Box.createVerticalStrut(24));
        add(form);
    }
}
