package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;

public class LoginDialog extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Modern blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Lighter blue
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light gray background
    private static final Color TEXT_COLOR = new Color(44, 62, 80); // Dark blue-gray text
    
    private boolean loginSuccess = false;
    private String loggedInRole = null;
    private JLabel homeLabel;
    private util.DBConnection dbConnection = new util.DBConnection();

    public LoginDialog(JFrame parent) {
        super(parent, "Đăng nhập", true);
        setLocationRelativeTo(parent); // Đặt vị trí trung tâm
        setLayout(new BorderLayout());
        setResizable(false); // Không cho phép thay đổi kích thước
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header with gradient
        JPanel headerPanel = new GradientPanel(PRIMARY_COLOR, SECONDARY_COLOR);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        headerPanel.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("Nhà Sách Online");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        homeLabel = createHoverLabel("Trang chủ");
        homeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        headerPanel.add(homeLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main Panel with shadow effect
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Login Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        JLabel loginIcon = new JLabel("\uD83D\uDD10"); // Unicode cho 🔐
        loginIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        JLabel loginTitle = new JLabel("Đăng nhập");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        loginTitle.setForeground(TEXT_COLOR);
        titlePanel.add(loginIcon);
        titlePanel.add(loginTitle);
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Tabs for User Types with modern design
        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new GridLayout(1, 2, 10, 0));
        tabPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        tabPanel.setBackground(Color.WHITE);

        // User Tab
        JPanel userTab = new JPanel();
        userTab.setLayout(new BorderLayout());
        userTab.setBackground(PRIMARY_COLOR);
        userTab.setBorder(new RoundedBorder(20));
        JLabel userLabel = new JLabel("Người dùng", SwingConstants.CENTER);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(Color.WHITE);
        userTab.add(userLabel, BorderLayout.CENTER);

        // Admin Tab
        JPanel adminTab = new JPanel();
        adminTab.setLayout(new BorderLayout());
        adminTab.setBackground(Color.WHITE);
        adminTab.setBorder(new RoundedBorder(20));
        JLabel adminLabel = new JLabel("Quản trị viên", SwingConstants.CENTER);
        adminLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        adminLabel.setForeground(TEXT_COLOR);
        adminTab.add(adminLabel, BorderLayout.CENTER);

        tabPanel.add(userTab);
        tabPanel.add(adminTab);

        JPanel tabContainer = new JPanel();
        tabContainer.setLayout(new BoxLayout(tabContainer, BoxLayout.Y_AXIS));
        tabContainer.setOpaque(false);
        tabContainer.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        tabContainer.add(tabPanel);
        tabContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(tabContainer);
        contentPanel.add(Box.createVerticalStrut(30));

        // Form Panel with modern styling
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username Field with icon
        JPanel usernamePanel = new JPanel(new BorderLayout(10, 0));
        usernamePanel.setOpaque(false);
        JLabel usernameIcon = new JLabel("\uD83D\uDC64"); // Unicode cho 👤
        usernameIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        JLabel usernameLabel = new JLabel("Tên đăng nhập");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        usernamePanel.add(usernameIcon, BorderLayout.WEST);
        usernamePanel.add(usernameLabel, BorderLayout.CENTER);
        formPanel.add(usernamePanel);
        formPanel.add(Box.createVerticalStrut(8));

        RoundedTextField usernameField = new RoundedTextField("", 20);
        usernameField.setPreferredSize(new Dimension(0, 45));
        usernameField.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(20));

        // Password Field with icon
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setOpaque(false);
        JLabel passwordIcon = new JLabel("\uD83D\uDD12"); // Unicode cho 🔒
        passwordIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        JLabel passwordLabel = new JLabel("Mật khẩu");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        passwordPanel.add(passwordIcon, BorderLayout.WEST);
        passwordPanel.add(passwordLabel, BorderLayout.CENTER);
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(8));

        RoundedPasswordField passwordField = new RoundedPasswordField();
        passwordField.setPreferredSize(new Dimension(0, 45));
        passwordField.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));

        // Remember Me and Forgot Password with modern styling
        JPanel rememberPanel = new JPanel(new BorderLayout());
        rememberPanel.setOpaque(false);
        rememberPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        JCheckBox rememberCheck = new JCheckBox("Ghi nhớ đăng nhập");
        rememberCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rememberCheck.setForeground(TEXT_COLOR);
        rememberCheck.setOpaque(false);
        rememberCheck.setFocusPainted(false);
        rememberPanel.add(rememberCheck, BorderLayout.WEST);

        JLabel forgotLabel = new JLabel("Quên mật khẩu?");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgotLabel.setForeground(PRIMARY_COLOR);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rememberPanel.add(forgotLabel, BorderLayout.EAST);

        formPanel.add(rememberPanel);
        formPanel.add(Box.createVerticalStrut(30));

        // Login Button with modern design
        JButton loginButton = createStyledButton("Đăng nhập", PRIMARY_COLOR, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(0, 50));
        loginButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 50));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(30));

        // Divider with modern styling
        JPanel dividerPanel = new JPanel();
        dividerPanel.setLayout(new BoxLayout(dividerPanel, BoxLayout.Y_AXIS));
        dividerPanel.setOpaque(false);

        JPanel linePanel = new JPanel();
        linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));
        linePanel.setOpaque(false);

        JSeparator leftSep = new JSeparator();
        leftSep.setForeground(new Color(220, 220, 220));

        JLabel orLabel = new JLabel("hoặc");
        orLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        orLabel.setForeground(Color.GRAY);
        orLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JSeparator rightSep = new JSeparator();
        rightSep.setForeground(new Color(220, 220, 220));

        linePanel.add(leftSep);
        linePanel.add(orLabel);
        linePanel.add(rightSep);

        dividerPanel.add(linePanel);
        dividerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dividerPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));

        formPanel.add(dividerPanel);
        formPanel.add(Box.createVerticalStrut(30));

        // Social Logins with modern design
        JPanel socialPanel = new JPanel();
        socialPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
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
        formPanel.add(Box.createVerticalStrut(30));

        // Signup Link with modern styling
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setOpaque(false);

        JLabel noAccountLabel = new JLabel("Chưa có tài khoản?");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccountLabel.setForeground(TEXT_COLOR);

        JLabel signupLabel = new JLabel("Đăng ký");
        signupLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
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

        // Sử dụng pack() để tự động điều chỉnh kích thước
        pack();

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
                JOptionPane.showMessageDialog(LoginDialog.this, 
                    "Tính năng quên mật khẩu đang phát triển", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        userTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userTab.setBackground(PRIMARY_COLOR);
                userLabel.setForeground(Color.WHITE);
                adminTab.setBackground(Color.WHITE);
                adminLabel.setForeground(TEXT_COLOR);
            }
        });

        adminTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                adminTab.setBackground(PRIMARY_COLOR);
                adminLabel.setForeground(Color.WHITE);
                userTab.setBackground(Color.WHITE);
                userLabel.setForeground(TEXT_COLOR);
            }
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginDialog.this, 
                    "Vui lòng điền đầy đủ thông tin", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            } else {
                if (authenticate(username, password)) {
                    boolean isAdmin = adminTab.getBackground().equals(PRIMARY_COLOR) && 
                                   "admin".equalsIgnoreCase(loggedInRole);
                    String userType = isAdmin ? "Quản trị viên" : "Người dùng";
                    JOptionPane.showMessageDialog(LoginDialog.this, 
                        "Đăng nhập thành công với vai trò: " + userType, 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    loginSuccess = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this, 
                        "Tên đăng nhập hoặc mật khẩu không đúng", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private boolean authenticate(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();

            String sql = "SELECT password_hash, role_id, is_active FROM ACCOUNTS WHERE username = ? AND is_active = 1";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("password_hash");
                int roleId = rs.getInt("role_id");
                boolean isActive = rs.getBoolean("is_active");

                if (!isActive) {
                    JOptionPane.showMessageDialog(this, 
                        "Tài khoản đã bị vô hiệu hóa", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                String hashedInputPassword = hashPassword(password);
                if (storedPasswordHash != null && storedPasswordHash.equals(hashedInputPassword)) {
                    loggedInRole = (roleId == 1) ? "user" : "admin";
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi kết nối hoặc truy vấn cơ sở dữ liệu: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isSuccess() {
        return loginSuccess;
    }

    public String getLoggedInRole() {
        return loggedInRole;
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
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
        button.setPreferredSize(new Dimension(45, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
            setBackground(new Color(245, 245, 245));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            super.paintComponent(g2);
            g2.dispose();
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            g2.dispose();
        }
        
        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 25, 25);
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
            setBackground(new Color(245, 245, 245));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            super.paintComponent(g2);
            g2.dispose();
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
            g2.dispose();
        }
        
        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                shape = new RoundRectangle2D.Float(0, 0, getWidth()-1, getHeight()-1, 25, 25);
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

    private class ShadowBorder extends AbstractBorder {
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw shadow
            for (int i = 0; i < 5; i++) {
                g2.setColor(new Color(0, 0, 0, 20 - i * 4));
                g2.drawRoundRect(x + i, y + i, width - 1 - i * 2, height - 1 - i * 2, 20, 20);
            }
            
            g2.dispose();
        }
    }
}