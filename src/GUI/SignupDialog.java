package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;

public class SignupDialog extends JDialog {

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Modern blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Lighter blue
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Light gray background
    private static final Color SIGNUP_TEXT_COLOR = new Color(44, 62, 80); // Dark blue-gray text

    private boolean success = false;
    private JLabel homeLabel;
    private util.DBConnection dbConnection = new util.DBConnection();

    public SignupDialog(JFrame parent) {
        super(parent, "Đăng ký", true);
        setSize(450, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
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

        // Main Panel
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Signup Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        JLabel signupIcon = new JLabel("\uD83D\uDCDD"); // Unicode cho 📝
        signupIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        JLabel signupTitle = new JLabel("Đăng ký tài khoản");
        signupTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        signupTitle.setForeground(SIGNUP_TEXT_COLOR);
        titlePanel.add(signupIcon);
        titlePanel.add(signupTitle);
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createVerticalStrut(30));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] labels = {"Họ và tên:", "Email:", "Tên đăng nhập:", "Mật khẩu:", "Nhập lại mật khẩu:", "Số điện thoại:"};
        JTextField[] fields = new JTextField[labels.length];
        JPasswordField passwordField = null;
        JPasswordField confirmPasswordField = null;

        for (int i = 0; i < labels.length; i++) {
            JPanel labelPanel = new JPanel(new BorderLayout(10, 0));
            labelPanel.setOpaque(false);
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setForeground(SIGNUP_TEXT_COLOR);
            labelPanel.add(label, BorderLayout.CENTER);
            contentPanel.add(labelPanel);
            contentPanel.add(Box.createVerticalStrut(5));

            if (i == 3) {
                passwordField = new RoundedPasswordField();
                passwordField.setPreferredSize(new Dimension(0, 40));
                passwordField.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
                contentPanel.add(passwordField);
            } else if (i == 4) {
                confirmPasswordField = new RoundedPasswordField();
                confirmPasswordField.setPreferredSize(new Dimension(0, 40));
                confirmPasswordField.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
                contentPanel.add(confirmPasswordField);
            } else {
                fields[i] = new RoundedTextField("", 20);
                fields[i].setPreferredSize(new Dimension(0, 40));
                fields[i].setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
                contentPanel.add(fields[i]);
            }
            contentPanel.add(Box.createVerticalStrut(15));
        }

        // Signup Button
        JButton signupButton = createStyledButton("Đăng ký", PRIMARY_COLOR, Color.WHITE);
        signupButton.setPreferredSize(new Dimension(0, 45));
        signupButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField finalPasswordField = passwordField;
        JPasswordField finalConfirmPasswordField = confirmPasswordField;
        signupButton.addActionListener(e -> {
            String fullName = fields[0].getText();
            String email = fields[1].getText();
            String username = fields[2].getText();
            String password = new String(finalPasswordField.getPassword());
            String confirmPassword = new String(finalConfirmPasswordField.getPassword());
            String phone = fields[5].getText();

            if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu không khớp", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (registerUser(fullName, email, username, password, phone)) {
                success = true;
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        });

        contentPanel.add(signupButton);
        contentPanel.add(Box.createVerticalStrut(20));

        // Login Link
        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel haveAccountLabel = new JLabel("Đã có tài khoản?");
        haveAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        haveAccountLabel.setForeground(SIGNUP_TEXT_COLOR);

        JLabel loginLabel = new JLabel("Đăng nhập");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLabel.setForeground(PRIMARY_COLOR);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginPanel.add(haveAccountLabel);
        loginPanel.add(Box.createHorizontalStrut(5));
        loginPanel.add(loginLabel);

        contentPanel.add(loginPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // Event Listeners
        homeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
            }
        });

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginDialog(parent).setVisible(true);
            }
        });
    }

    public boolean isSuccess() {
        return success;
    }

    private boolean registerUser(String fullName, String email, String username, String password, String phone) {
        Connection conn = null;
        PreparedStatement stmtCheck = null;
        PreparedStatement stmtCustomer = null;
        PreparedStatement stmtAccount = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);

            String checkSql = "SELECT username, email FROM ACCOUNTS WHERE username = ? OR email = ?";
            stmtCheck = conn.prepareStatement(checkSql);
            stmtCheck.setString(1, username);
            stmtCheck.setString(2, email);
            rs = stmtCheck.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc email đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String customerSql = "INSERT INTO CUSTOMERS (name, email, phone, created_date, address) VALUES (?, ?, ?, GETDATE(), ?)";
            stmtCustomer = conn.prepareStatement(customerSql, Statement.RETURN_GENERATED_KEYS);
            stmtCustomer.setString(1, fullName);
            stmtCustomer.setString(2, email);
            stmtCustomer.setString(3, phone);
            stmtCustomer.setString(4, "");
            stmtCustomer.executeUpdate();

            rs = stmtCustomer.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Failed to retrieve customer_id");
            }
            int customerId = rs.getInt(1);

            String hashedPassword = hashPassword(password);

            String accountSql = "INSERT INTO ACCOUNTS (username, password_hash, email, role_id, is_active, created_date, customer_id) " +
                              "VALUES (?, ?, ?, 1, 1, GETDATE(), ?)";
            stmtAccount = conn.prepareStatement(accountSql);
            stmtAccount.setString(1, username);
            stmtAccount.setString(2, hashedPassword);
            stmtAccount.setString(3, email);
            stmtAccount.setInt(4, customerId);
            stmtAccount.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Lỗi khi đăng ký: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmtCheck != null) stmtCheck.close();
                if (stmtCustomer != null) stmtCustomer.close();
                if (stmtAccount != null) stmtAccount.close();
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