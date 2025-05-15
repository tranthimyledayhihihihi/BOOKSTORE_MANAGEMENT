
package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginDialog extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(70, 130, 180); // Match BookStoreGUI
    private static final Color SECONDARY_COLOR = new Color(100, 149, 237);
    private static final Color ACCENT_COLOR = new Color(65, 105, 225);
    
    // Biến để theo dõi trạng thái đăng nhập
    private boolean loginSuccess = false;

    public LoginDialog(JFrame parent) {
        super(parent, "Đăng nhập", true);
        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        // Header
        JPanel headerPanel = new GradientPanel(PRIMARY_COLOR, SECONDARY_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Nhà Sách Online");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel homeLabel = createHoverLabel("Trang chủ");
        headerPanel.add(homeLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Login Title
        JLabel loginTitle = new JLabel("Đăng nhập");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        loginTitle.setForeground(Color.BLACK);
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(loginTitle);
        contentPanel.add(Box.createVerticalStrut(30));

        // Tabs for User Types
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new GridLayout(1, 2));
        tabPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        tabPanel.setBackground(new Color(240, 240, 240));
        tabPanel.setBorder(new RoundedBorder(10));

        // User Tab
        JPanel userTab = new JPanel();
        userTab.setLayout(new BorderLayout());
        userTab.setBackground(PRIMARY_COLOR);
        JLabel userLabel = new JLabel("Người dùng", SwingConstants.CENTER);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        userTab.add(userLabel, BorderLayout.CENTER);

        // Admin Tab
        JPanel adminTab = new JPanel();
        adminTab.setLayout(new BorderLayout());
        adminTab.setBackground(Color.WHITE);
        JLabel adminLabel = new JLabel("Quản trị viên", SwingConstants.CENTER);
        adminLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        adminLabel.setForeground(Color.GRAY);
        adminTab.add(adminLabel, BorderLayout.CENTER);

        tabPanel.add(userTab);
        tabPanel.add(adminTab);

        JPanel tabContainer = new JPanel();
        tabContainer.setLayout(new BoxLayout(tabContainer, BoxLayout.Y_AXIS));
        tabContainer.setOpaque(false);
        tabContainer.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        tabContainer.add(tabPanel);
        tabContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(tabContainer);
        contentPanel.add(Box.createVerticalStrut(20));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username Field
        JLabel usernameLabel = new JLabel("Tên đăng nhập");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));

        RoundedTextField usernameField = new RoundedTextField("", 20);
        usernameField.setPreferredSize(new Dimension(0, 40));
        usernameField.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));

        // Password Field
        JLabel passwordLabel = new JLabel("Mật khẩu");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));

        RoundedPasswordField passwordField = new RoundedPasswordField();
        passwordField.setPreferredSize(new Dimension(0, 40));
        passwordField.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));

        // Remember Me and Forgot Password
        JPanel rememberPanel = new JPanel();
        rememberPanel.setLayout(new BorderLayout());
        rememberPanel.setOpaque(false);
        rememberPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 25));

        JCheckBox rememberCheck = new JCheckBox("Ghi nhớ đăng nhập");
        rememberCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberCheck.setOpaque(false);
        rememberCheck.setFocusPainted(false);
        rememberPanel.add(rememberCheck, BorderLayout.WEST);

        JLabel forgotLabel = new JLabel("Quên mật khẩu?");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        forgotLabel.setForeground(PRIMARY_COLOR);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rememberPanel.add(forgotLabel, BorderLayout.EAST);

        formPanel.add(rememberPanel);
        formPanel.add(Box.createVerticalStrut(25));

        // Login Button
        JButton loginButton = createStyledButton("Đăng nhập", PRIMARY_COLOR, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(0, 45));
        loginButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(20));

        // Divider
        JPanel dividerPanel = new JPanel();
        dividerPanel.setLayout(new BoxLayout(dividerPanel, BoxLayout.Y_AXIS));
        dividerPanel.setOpaque(false);

        JPanel linePanel = new JPanel();
        linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));
        linePanel.setOpaque(false);

        JSeparator leftSep = new JSeparator();
        leftSep.setForeground(new Color(220, 220, 220));

        JLabel orLabel = new JLabel("hoặc");
        orLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        orLabel.setForeground(Color.GRAY);
        orLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JSeparator rightSep = new JSeparator();
        rightSep.setForeground(new Color(220, 220, 220));

        linePanel.add(leftSep);
        linePanel.add(orLabel);
        linePanel.add(rightSep);

        dividerPanel.add(linePanel);
        dividerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dividerPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        formPanel.add(dividerPanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Social Logins
        JPanel socialPanel = new JPanel();
        socialPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        socialPanel.setOpaque(false);

        JButton fbButton = createSocialButton("FB", new Color(59, 89, 152));
        JButton googleButton = createSocialButton("G", new Color(211, 72, 54));
        JButton twitterButton = createSocialButton("X", new Color(0, 172, 237));
        JButton githubButton = createSocialButton("GH", new Color(51, 51, 51));

        socialPanel.add(fbButton);
        socialPanel.add(googleButton);
        socialPanel.add(twitterButton);
        socialPanel.add(githubButton);

        JPanel socialContainer = new JPanel();
        socialContainer.setLayout(new BoxLayout(socialContainer, BoxLayout.Y_AXIS));
        socialContainer.setOpaque(false);
        socialContainer.add(socialPanel);
        socialContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(socialContainer);
        formPanel.add(Box.createVerticalStrut(20));

        // Signup Link
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setOpaque(false);

        JLabel noAccountLabel = new JLabel("Chưa có tài khoản?");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel signupLabel = new JLabel("Đăng ký");
        signupLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        signupLabel.setForeground(PRIMARY_COLOR);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        signupPanel.add(noAccountLabel);
        signupPanel.add(signupLabel);

        JPanel signupContainer = new JPanel();
        signupContainer.setLayout(new BoxLayout(signupContainer, BoxLayout.Y_AXIS));
        signupContainer.setOpaque(false);
        signupContainer.add(signupPanel);
        signupContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(signupContainer);
        contentPanel.add(formPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Event Listeners
        homeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new SignupDialog(parent).setVisible(true);
            }
        });

        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginDialog.this, "Tính năng quên mật khẩu đang phát triển", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        userTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userTab.setBackground(PRIMARY_COLOR);
                userLabel.setForeground(Color.WHITE);
                adminTab.setBackground(Color.WHITE);
                adminLabel.setForeground(Color.GRAY);
            }
        });

        adminTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                adminTab.setBackground(PRIMARY_COLOR);
                adminLabel.setForeground(Color.WHITE);
                userTab.setBackground(Color.WHITE);
                userLabel.setForeground(Color.GRAY);
            }
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean isAdmin = adminTab.getBackground().equals(PRIMARY_COLOR);
                String userType = isAdmin ? "Quản trị viên" : "Người dùng";
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công với vai trò: " + userType, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loginSuccess = true; // Đánh dấu đăng nhập thành công
                dispose();
            }
        });
    }

    /**
     * Kiểm tra xem việc đăng nhập có thành công hay không
     * @return true nếu đăng nhập thành công, false nếu không thành công
     */
    public boolean isSuccess() {
        return loginSuccess;
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

    private JButton createSocialButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setPreferredSize(new Dimension(40, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
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

    private class RoundedPasswordField extends JPasswordField {
        private Shape shape;
        private Color borderColor = new Color(200, 200, 200);
        public RoundedPasswordField() {
            super();
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
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
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
            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }
    }
}