package GUI;

import bll.AccountBLL;
import bll.AccountBLL.LoginResult;
import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    // UI Colors
    private static final Color PRIMARY_COLOR = new Color(59, 89, 182);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color TAB_INACTIVE_COLOR = new Color(149, 165, 166);

    // Business Logic
    private AccountBLL accountBLL;
    private boolean loginSuccess = false;
    private Account loggedInAccount = null;

    // UI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheck;
    private JLabel statusLabel;
    private JButton loginButton;
    private JPanel userTab;
    private JPanel adminTab;
    private JLabel userTabLabel;
    private JLabel adminTabLabel;
    private boolean isAdminTabSelected = false;

    public LoginDialog(JFrame parent) {
        super(parent, "Đăng nhập", true);
        this.accountBLL = new AccountBLL();
        initializeUI();
        setupEventHandlers();
    }

    private void initializeUI() {
        setSize(500, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Content
        mainPanel.add(createContent(), BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(createFooter(), BorderLayout.SOUTH);

        add(mainPanel);
        
        // Set default button
        getRootPane().setDefaultButton(loginButton);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Đăng nhập");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Chào mừng bạn trở lại!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createContent() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(0, 20));
        contentPanel.add(statusLabel, BorderLayout.NORTH);

        // Tabs Panel
        JPanel tabsPanel = createTabsPanel();
        contentPanel.add(tabsPanel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = createFormPanel();
        contentPanel.add(formPanel, BorderLayout.SOUTH);

        return contentPanel;
    }

    private JPanel createTabsPanel() {
        JPanel tabsContainer = new JPanel(new BorderLayout(0, 15));
        tabsContainer.setBackground(BACKGROUND_COLOR);

        JLabel tabsTitle = new JLabel("Chọn loại tài khoản");
        tabsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabsTitle.setForeground(TEXT_COLOR);
        tabsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        tabsContainer.add(tabsTitle, BorderLayout.NORTH);

        JPanel tabsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        tabsPanel.setBackground(BACKGROUND_COLOR);

        // User Tab
        userTab = new JPanel(new BorderLayout());
        userTab.setBackground(PRIMARY_COLOR);
        userTab.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        userTab.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userTabLabel = new JLabel("👤 Người dùng", SwingConstants.CENTER);
        userTabLabel.setForeground(Color.WHITE);
        userTabLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTab.add(userTabLabel, BorderLayout.CENTER);

        // Admin Tab
        adminTab = new JPanel(new BorderLayout());
        adminTab.setBackground(BACKGROUND_COLOR);
        adminTab.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TAB_INACTIVE_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        adminTab.setCursor(new Cursor(Cursor.HAND_CURSOR));

        adminTabLabel = new JLabel("🔐 Quản trị viên", SwingConstants.CENTER);
        adminTabLabel.setForeground(TAB_INACTIVE_COLOR);
        adminTabLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        adminTab.add(adminTabLabel, BorderLayout.CENTER);

        tabsPanel.add(userTab);
        tabsPanel.add(adminTab);
        tabsContainer.add(tabsPanel, BorderLayout.CENTER);

        return tabsContainer;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BACKGROUND_COLOR);

        // Username Field
        formPanel.add(createFieldGroup("Tên đăng nhập", true));
        formPanel.add(Box.createVerticalStrut(15));

        // Password Field
        formPanel.add(createFieldGroup("Mật khẩu", false));
        formPanel.add(Box.createVerticalStrut(15));

        // Remember and Forgot Panel
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(BACKGROUND_COLOR);
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        rememberCheck = new JCheckBox("Ghi nhớ đăng nhập");
        rememberCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rememberCheck.setForeground(TEXT_COLOR);
        rememberCheck.setBackground(BACKGROUND_COLOR);

        JLabel forgotLabel = new JLabel("Quên mật khẩu?");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgotLabel.setForeground(PRIMARY_COLOR);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginDialog.this,
                    "Tính năng quên mật khẩu đang phát triển",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotLabel.setText("<html><u>Quên mật khẩu?</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                forgotLabel.setText("Quên mật khẩu?");
            }
        });

        optionsPanel.add(rememberCheck, BorderLayout.WEST);
        optionsPanel.add(forgotLabel, BorderLayout.EAST);
        formPanel.add(optionsPanel);

        return formPanel;
    }

    private JPanel createFieldGroup(String labelText, boolean isUsername) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(BACKGROUND_COLOR);
        fieldPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Label
        JLabel label = new JLabel(labelText + " *");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(5));

        // Input Field
        JTextField inputField;
        if (isUsername) {
            usernameField = new JTextField();
            inputField = usernameField;
        } else {
            passwordField = new JPasswordField();
            inputField = passwordField;
        }

        styleTextField(inputField);
        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(inputField);

        return fieldPanel;
    }

    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout(0, 15));
        footerPanel.setBackground(BACKGROUND_COLOR);

        // Login Button
        loginButton = createStyledButton("ĐĂNG NHẬP");
        loginButton.addActionListener(e -> handleLogin());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(loginButton);
        footerPanel.add(buttonPanel, BorderLayout.CENTER);

        // Signup Link Panel
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signupPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel signupPrompt = new JLabel("Chưa có tài khoản? ");
        signupPrompt.setForeground(TEXT_COLOR);
        signupPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JLabel signupLink = new JLabel("Đăng ký ngay");
        signupLink.setForeground(PRIMARY_COLOR);
        signupLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    SignupDialog signupDialog = new SignupDialog((JFrame) getParent());
                    signupDialog.setVisible(true);
                    if (signupDialog.isSuccess()) {
                        dispose();
                    }
                } catch (Exception ex) {
                    setStatusMessage("Lỗi khi mở form đăng ký: " + ex.getMessage(), ERROR_COLOR);
                    ex.printStackTrace();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                signupLink.setText("<html><u>Đăng ký ngay</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                signupLink.setText("Đăng ký ngay");
            }
        });

        signupPanel.add(signupPrompt);
        signupPanel.add(signupLink);
        footerPanel.add(signupPanel, BorderLayout.SOUTH);

        return footerPanel;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        
        // Add focus listener for better UX
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(9, 11, 9, 11)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(PRIMARY_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PRIMARY_COLOR.brighter());
                } else {
                    g2.setColor(getBackground());
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(400, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void setupEventHandlers() {
        // User Tab Click
        userTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectUserTab();
            }
        });

        // Admin Tab Click
        adminTab.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectAdminTab();
            }
        });

        // Enter key handling
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }

    private void selectUserTab() {
        isAdminTabSelected = false;
        
        // Update User Tab (Active)
        userTab.setBackground(PRIMARY_COLOR);
        userTab.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        userTabLabel.setForeground(Color.WHITE);
        
        // Update Admin Tab (Inactive)
        adminTab.setBackground(BACKGROUND_COLOR);
        adminTab.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TAB_INACTIVE_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        adminTabLabel.setForeground(TAB_INACTIVE_COLOR);
        
        setStatusMessage("", Color.BLACK);
    }

    private void selectAdminTab() {
        isAdminTabSelected = true;
        
        // Update Admin Tab (Active)
        adminTab.setBackground(PRIMARY_COLOR);
        adminTab.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        adminTabLabel.setForeground(Color.WHITE);
        
        // Update User Tab (Inactive)
        userTab.setBackground(BACKGROUND_COLOR);
        userTab.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TAB_INACTIVE_COLOR, 2),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        userTabLabel.setForeground(TAB_INACTIVE_COLOR);
        
        setStatusMessage("", Color.BLACK);
    }

    private void handleLogin() {
        // Clear previous status
        setStatusMessage("", Color.BLACK);
        
        // Get input values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        // Client-side validation
        if (username.isEmpty()) {
            setStatusMessage("Vui lòng nhập tên đăng nhập", ERROR_COLOR);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            setStatusMessage("Vui lòng nhập mật khẩu", ERROR_COLOR);
            passwordField.requestFocus();
            return;
        }
        
        // Disable button during processing
        loginButton.setEnabled(false);
        loginButton.setText("ĐANG ĐĂNG NHẬP...");
        
        // Use SwingWorker for background task
        SwingWorker<LoginResult, Void> worker = new SwingWorker<LoginResult, Void>() {
            @Override
            protected LoginResult doInBackground() throws Exception {
                return accountBLL.login(username, password);
            }
            
            @Override
            protected void done() {
                try {
                    LoginResult result = get();
                    
                    if (!result.isSuccess()) {
                        setStatusMessage(result.getMessage(), ERROR_COLOR);
                        passwordField.selectAll();
                        passwordField.requestFocus();
                        return;
                    }

                    Account account = result.getAccount();
                    
                    // Check role authorization
                    if (isAdminTabSelected && account.getRoleId() != 1) { // 1 là Admin (theo code BLL)
                        setStatusMessage("Bạn không có quyền truy cập với vai trò Quản trị viên", ERROR_COLOR);
                        return;
                    } else if (!isAdminTabSelected && account.getRoleId() != 3) { // 3 là Customer (theo code BLL)
                        setStatusMessage("Bạn không có quyền truy cập với vai trò Người dùng", ERROR_COLOR);
                        return;
                    }

                    // Login successful
                    loggedInAccount = account;
                    String userType = isAdminTabSelected ? "Quản trị viên" : "Người dùng";
                    setStatusMessage("Đăng nhập thành công!", SUCCESS_COLOR);
                    
                    // Show success and close dialog
                    Timer timer = new Timer(1000, e -> {
                        JOptionPane.showMessageDialog(LoginDialog.this,
                            "Đăng nhập thành công với vai trò: " + userType,
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                        loginSuccess = true;
                        dispose();
                    });
                    timer.setRepeats(false);
                    timer.start();
                    
                } catch (Exception e) {
                    setStatusMessage("Lỗi hệ thống: " + e.getMessage(), ERROR_COLOR);
                    e.printStackTrace();
                } finally {
                    // Re-enable button
                    loginButton.setEnabled(true);
                    loginButton.setText("ĐĂNG NHẬP");
                }
            }
        };
        
        worker.execute();
    }

    private void setStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    // Getters
    public boolean isSuccess() {
        return loginSuccess;
    }

    public Account getLoggedInAccount() {
        return loggedInAccount;
    }

    // Utility method to clear all fields
    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        rememberCheck.setSelected(false);
        setStatusMessage("", Color.BLACK);
        selectUserTab(); // Reset to user tab
    }
}