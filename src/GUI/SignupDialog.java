package GUI;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import GUI.SignupDialog;

class SignupDialog extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SECONDARY_COLOR = new Color(100, 149, 237);
    private boolean success = false; // Flag to track signup success

    public SignupDialog(JFrame parent) {
        super(parent, "Đăng ký", true);
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);
        
        // Header (giống LoginDialog)
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
        
        // Nội dung chính
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Tiêu đề đăng ký
        JLabel signupTitle = new JLabel("Đăng ký tài khoản");
        signupTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        signupTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(signupTitle);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Form đăng ký
        String[] labels = {"Họ và tên:", "Email:", "Mật khẩu:", "Nhập lại mật khẩu:", "Số điện thoại:"};
        JTextField[] fields = new JTextField[labels.length];
        
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(label);
            contentPanel.add(Box.createVerticalStrut(5));
            
            if (i == 2 || i == 3) { // Ô mật khẩu
                JPasswordField pf = new JPasswordField();
                pf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                pf.setPreferredSize(new Dimension(0, 40));
                pf.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
                contentPanel.add(pf);
            } else {
                fields[i] = new JTextField();
                fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
                fields[i].setPreferredSize(new Dimension(0, 40));
                fields[i].setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
                contentPanel.add(fields[i]);
            }
            
            contentPanel.add(Box.createVerticalStrut(15));
        }
        
        // Nút đăng ký
        JButton signupButton = createStyledButton("Đăng ký", PRIMARY_COLOR, Color.WHITE);
        signupButton.setPreferredSize(new Dimension(0, 45));
        signupButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        signupButton.addActionListener(e -> {
            boolean valid = true;
            for (JTextField field : fields) {
                if (field != null && field.getText().isEmpty()) {
                    valid = false;
                    break;
                }
            }
            
            if (valid) {
                success = true; // Set success flag to true when registration is successful
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        contentPanel.add(signupButton);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Phần đã có tài khoản
        JPanel loginPanel = new JPanel();
        loginPanel.setOpaque(false);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
        
        JLabel haveAccountLabel = new JLabel("Đã có tài khoản?");
        haveAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel loginLabel = new JLabel("Đăng nhập");
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginLabel.setForeground(PRIMARY_COLOR);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginPanel.add(haveAccountLabel);
        loginPanel.add(Box.createHorizontalStrut(5));
        loginPanel.add(loginLabel);
        loginPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(loginPanel);
        add(contentPanel, BorderLayout.CENTER);
        
        // Footer (giống LoginDialog)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel searchLabel = new JLabel("Sách mới");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField searchField = new JTextField("Type here to search");
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchField.setPreferredSize(new Dimension(200, 30));
        
        footerPanel.add(searchLabel);
        footerPanel.add(Box.createHorizontalStrut(10));
        footerPanel.add(searchField);
        
        add(footerPanel, BorderLayout.SOUTH);
        
        // Sự kiện
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
    
    /**
     * Returns whether the signup was successful.
     * @return true if signup was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
    
    // Các phương thức helper giống với LoginDialog
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
            
            GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            super.paintComponent(g);
        }
    }
}