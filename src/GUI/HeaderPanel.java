package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HeaderPanel extends JPanel {
    private JButton loginBtn, registerBtn, cartButton;

    public HeaderPanel(JFrame parentFrame) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 32));
        setPreferredSize(new Dimension(0, 64)); // Header nhỏ gọn

        // Logo + Tên
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.X_AXIS));
        logoPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel logoIcon = new JLabel("📚");
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        logoIcon.setForeground(new Color(41, 128, 185));
        logoIcon.setAlignmentY(Component.CENTER_ALIGNMENT);

        JLabel logoText = new JLabel("NHÀ SÁCH ONLINE");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoText.setForeground(new Color(41, 128, 185));
        logoText.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        logoText.setAlignmentY(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoIcon);
        logoPanel.add(logoText);

        // Các nút bên phải
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        rightPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

       

        loginBtn = createRoundedButton("Đăng nhập", new Color(41, 128, 185), Color.WHITE, new Color(31, 108, 165));
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setPreferredSize(new Dimension(120, 44));
        loginBtn.setMaximumSize(new Dimension(120, 44));
        loginBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            LoginDialog loginDialog = new LoginDialog(parentFrame);
            loginDialog.setVisible(true);
        });

        registerBtn = createRoundedButton("Đăng ký", new Color(41, 128, 185), Color.WHITE, new Color(31, 108, 165));
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerBtn.setPreferredSize(new Dimension(120, 44));
        registerBtn.setMaximumSize(new Dimension(120, 44));
        registerBtn.setAlignmentY(Component.CENTER_ALIGNMENT);
        registerBtn.addActionListener(e -> {
            SignupDialog signupDialog = new SignupDialog(parentFrame);
            signupDialog.setVisible(true);
        });

      
        rightPanel.add(Box.createRigidArea(new Dimension(18, 0)));
        rightPanel.add(loginBtn);
        rightPanel.add(Box.createRigidArea(new Dimension(12, 0)));
        rightPanel.add(registerBtn);

        add(logoPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    

    private JButton createRoundedButton(String text, Color bgColor, Color fgColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? hoverColor : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        return button;
    }

    // Test
    public static void main(String[] args) {
        JFrame f = new JFrame("Header Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1200, 80);
        f.add(new HeaderPanel(f));
        f.setVisible(true);
    }
}