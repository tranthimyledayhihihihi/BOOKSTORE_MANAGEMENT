package GUI;

import bll.AccountBLL;
import bll.AccountBLL.RegisterResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class SignupDialog extends JDialog {
    // UI Colors
    private static final Color PRIMARY_COLOR = new Color(59, 89, 182);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);

    // UI Constants
    private static final String[] ACCOUNT_FIELDS = {"Tên đăng nhập *", "Email *", "Mật khẩu *", "Xác nhận mật khẩu *"};
    private static final String[] PERSONAL_FIELDS = {"Họ và tên *", "Số điện thoại *", "Địa chỉ *"};

    // Input Fields
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField phoneField;
    private JTextArea addressArea;
    
    // Status Labels
    private JLabel statusLabel;
    private JButton signupButton;
    
    // Business Logic
    private AccountBLL accountBL;
    private boolean success = false;

    public SignupDialog(JFrame parent) {
        super(parent, "Đăng ký tài khoản", true);
        this.accountBL = new AccountBLL();
        initializeUI();
        setupValidation();
    }

    private void initializeUI() {
        setSize(650, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Content
        mainPanel.add(createContent(), BorderLayout.CENTER);
        
        // Footer
        mainPanel.add(createFooter(), BorderLayout.SOUTH);

        add(mainPanel);
        
        // Set default button
        getRootPane().setDefaultButton(signupButton);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Đăng ký tài khoản");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel subtitleLabel = new JLabel("Tạo tài khoản mới để truy cập hệ thống");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createContent() {
        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(0, 20));
        contentPanel.add(statusLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        formPanel.setBackground(BACKGROUND_COLOR);

        // Account Information Section
        JPanel accountPanel = createSection("Thông tin tài khoản", ACCOUNT_FIELDS);
        
        // Personal Information Section  
        JPanel personalPanel = createSection("Thông tin cá nhân", PERSONAL_FIELDS);

        formPanel.add(accountPanel);
        formPanel.add(personalPanel);
        contentPanel.add(formPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout(0, 15));
        footerPanel.setBackground(BACKGROUND_COLOR);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        signupButton = createStyledButton("ĐĂNG KÝ");
        signupButton.addActionListener(e -> handleSignup());
        buttonPanel.add(signupButton);

        // Login Link Panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel loginPrompt = new JLabel("Đã có tài khoản? ");
        loginPrompt.setForeground(TEXT_COLOR);
        loginPrompt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        JLabel loginLink = new JLabel("Đăng nhập ngay");
        loginLink.setForeground(PRIMARY_COLOR);
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                new LoginDialog((JFrame) getParent()).setVisible(true);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setText("<html><u>Đăng nhập ngay</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setText("Đăng nhập ngay");
            }
        });

        loginPanel.add(loginPrompt);
        loginPanel.add(loginLink);

        footerPanel.add(buttonPanel, BorderLayout.CENTER);
        footerPanel.add(loginPanel, BorderLayout.SOUTH);

        return footerPanel;
    }

    private JPanel createSection(String title, String[] fields) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(BACKGROUND_COLOR);

        // Section Title
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(TEXT_COLOR);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionPanel.add(sectionTitle);
        sectionPanel.add(Box.createVerticalStrut(15));

        // Create input fields
        for (String field : fields) {
            sectionPanel.add(createFieldGroup(field));
            sectionPanel.add(Box.createVerticalStrut(12));
        }

        return sectionPanel;
    }

    private JPanel createFieldGroup(String labelText) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setBackground(BACKGROUND_COLOR);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (labelText.endsWith("*")) {
            label.setToolTipText("Trường bắt buộc");
        }
        fieldPanel.add(label);
        fieldPanel.add(Box.createVerticalStrut(5));

        // Input Component
        JComponent inputComponent = createInputComponent(labelText);
        inputComponent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 
            labelText.equals("Địa chỉ *") ? 70 : 35));
        inputComponent.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldPanel.add(inputComponent);

        return fieldPanel;
    }

    private JComponent createInputComponent(String fieldType) {
        switch (fieldType) {
            case "Tên đăng nhập *":
                usernameField = new JTextField();
                styleTextField(usernameField);
                return usernameField;
                
            case "Email *":
                emailField = new JTextField();
                styleTextField(emailField);
                return emailField;
                
            case "Mật khẩu *":
                passwordField = new JPasswordField();
                styleTextField(passwordField);
                return passwordField;
                
            case "Xác nhận mật khẩu *":
                confirmPasswordField = new JPasswordField();
                styleTextField(confirmPasswordField);
                return confirmPasswordField;
                
            case "Họ và tên *":
                fullNameField = new JTextField();
                styleTextField(fullNameField);
                return fullNameField;
                
            case "Số điện thoại *":
                phoneField = new JTextField();
                styleTextField(phoneField);
                return phoneField;
                
            case "Địa chỉ *":
                addressArea = new JTextArea(3, 20);
                addressArea.setLineWrap(true);
                addressArea.setWrapStyleWord(true);
                addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                addressArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                JScrollPane scrollPane = new JScrollPane(addressArea);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                return scrollPane;
                
            default:
                JTextField textField = new JTextField();
                styleTextField(textField);
                return textField;
        }
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        // Add focus listener for better UX
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(7, 9, 7, 9)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
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
        button.setPreferredSize(new Dimension(450, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void setupValidation() {
        // Real-time validation setup could be added here
        // For now, we'll validate on submit
    }

    private void handleSignup() {
        // Clear previous status
        setStatusMessage("", Color.BLACK);
        
        // Disable button during processing
        signupButton.setEnabled(false);
        signupButton.setText("ĐANG XỬ LÝ...");
        
        SwingWorker<RegisterResult, Void> worker = new SwingWorker<RegisterResult, Void>() {
            @Override
            protected RegisterResult doInBackground() throws Exception {
                // Get input values
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                String fullName = fullNameField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressArea.getText().trim();
                
                // Client-side validation
                String validationError = validateInput(username, email, password, confirmPassword, fullName, phone, address);
                if (validationError != null) {
                    RegisterResult result = new RegisterResult();
                    result.setSuccess(false);
                    result.setMessage(validationError);
                    return result;
                }
                
                // Call business logic
                return accountBL.registerAccount(username, password, email, fullName, phone, address);
            }
            
            @Override
            protected void done() {
                try {
                    RegisterResult result = get();
                    
                    if (result.isSuccess()) {
                        setStatusMessage(result.getMessage(), SUCCESS_COLOR);
                        success = true;
                        
                        // Show success dialog and close
                        Timer timer = new Timer(1500, e -> {
                            JOptionPane.showMessageDialog(SignupDialog.this,
                                "Đăng ký thành công!\nVui lòng đăng nhập để tiếp tục.",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                        
                    } else {
                        setStatusMessage(result.getMessage(), ERROR_COLOR);
                        
                        // Focus on appropriate field based on error
                        focusErrorField(result.getMessage());
                    }
                } catch (Exception e) {
                    setStatusMessage("Lỗi hệ thống: " + e.getMessage(), ERROR_COLOR);
                    e.printStackTrace();
                } finally {
                    // Re-enable button
                    signupButton.setEnabled(true);
                    signupButton.setText("ĐĂNG KÝ");
                }
            }
        };
        
        worker.execute();
    }

    private String validateInput(String username, String email, String password, 
                               String confirmPassword, String fullName, String phone, String address) {
        // Check empty fields
        if (username.isEmpty()) return "Tên đăng nhập không được để trống";
        if (email.isEmpty()) return "Email không được để trống";
        if (password.isEmpty()) return "Mật khẩu không được để trống";
        if (confirmPassword.isEmpty()) return "Vui lòng xác nhận mật khẩu";
        if (fullName.isEmpty()) return "Họ và tên không được để trống";
        if (phone.isEmpty()) return "Số điện thoại không được để trống";
        if (address.isEmpty()) return "Địa chỉ không được để trống";
        
        // Password confirmation
        if (!password.equals(confirmPassword)) {
            return "Mật khẩu xác nhận không khớp";
        }
        
        // Additional client-side validations
        if (username.length() < 3) return "Tên đăng nhập phải có ít nhất 3 ký tự";
        if (password.length() < 6) return "Mật khẩu phải có ít nhất 6 ký tự";
        
        // Basic email format check
        if (!email.contains("@") || !email.contains(".")) {
            return "Email không hợp lệ";
        }
        
        // Basic phone format check
        if (!phone.matches("^[0-9]{10,11}$")) {
            return "Số điện thoại không hợp lệ (10-11 chữ số)";
        }
        
        return null; // All validations passed
    }

    private void focusErrorField(String errorMessage) {
        String lowerError = errorMessage.toLowerCase();
        
        if (lowerError.contains("tên đăng nhập")) {
            usernameField.requestFocus();
            usernameField.selectAll();
        } else if (lowerError.contains("email")) {
            emailField.requestFocus();
            emailField.selectAll();
        } else if (lowerError.contains("mật khẩu")) {
            passwordField.requestFocus();
            passwordField.selectAll();
        } else if (lowerError.contains("tên")) {
            fullNameField.requestFocus();
            fullNameField.selectAll();
        } else if (lowerError.contains("điện thoại")) {
            phoneField.requestFocus();
            phoneField.selectAll();
        } else if (lowerError.contains("địa chỉ")) {
            addressArea.requestFocus();
            addressArea.selectAll();
        }
    }

    private void setStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public boolean isSuccess() {
        return success;
    }

    // Utility method to clear all fields
    public void clearFields() {
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        fullNameField.setText("");
        phoneField.setText("");
        addressArea.setText("");
        setStatusMessage("", Color.BLACK);
    }
}