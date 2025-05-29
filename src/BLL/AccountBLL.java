package bll;

import dao.AccountDAO;
import model.Account;
import model.Role;
import util.PasswordHasher;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Lớp Business Logic cho Account
 * Xử lý các nghiệp vụ và validation liên quan đến tài khoản
 */
public class AccountBLL {
    private AccountDAO accountDAO;
    
    // Regex patterns cho validation
    private static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private static final String PHONE_PATTERN = "^[0-9]{10,11}$";
    
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
    
    public AccountBLL() {
        this.accountDAO = new AccountDAO();
    }
    
    /**
     * Đăng nhập với validation
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Kết quả đăng nhập
     */
    public LoginResult login(String username, String password) {
        LoginResult result = new LoginResult();
        
        // Validation input
        if (username == null || username.trim().isEmpty()) {
            result.setSuccess(false);
            result.setMessage("Tên đăng nhập không được để trống");
            return result;
        }
        
        if (password == null || password.trim().isEmpty()) {
            result.setSuccess(false);
            result.setMessage("Mật khẩu không được để trống");
            return result;
        }
        
        try {
            Account account = accountDAO.login(username.trim(), password);
            
            if (account != null) {
                if (!account.isActive()) {
                    result.setSuccess(false);
                    result.setMessage("Tài khoản đã bị khóa");
                    return result;
                }
                
                result.setSuccess(true);
                result.setMessage("Đăng nhập thành công");
                result.setAccount(account);
            } else {
                result.setSuccess(false);
                result.setMessage("Tên đăng nhập hoặc mật khẩu không đúng");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Đăng ký tài khoản mới với validation đầy đủ
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @param email Email
     * @param customerName Tên khách hàng
     * @param phone Số điện thoại
     * @param address Địa chỉ
     * @return Kết quả đăng ký
     */
    public RegisterResult registerAccount(String username, String password, String email, 
                                        String customerName, String phone, String address) {
        RegisterResult result = new RegisterResult();
        
        // Validation input
        String validationError = validateRegistrationInput(username, password, email, customerName, phone, address);
        if (validationError != null) {
            result.setSuccess(false);
            result.setMessage(validationError);
            return result;
        }
        
        // Kiểm tra username đã tồn tại
        if (accountDAO.checkUsernameExists(username.trim())) {
            result.setSuccess(false);
            result.setMessage("Tên đăng nhập đã tồn tại");
            return result;
        }
        
        // Kiểm tra email đã tồn tại
        if (accountDAO.checkEmailExists(email.trim())) {
            result.setSuccess(false);
            result.setMessage("Email đã được sử dụng");
            return result;
        }
        
        try {
            // Tạo đối tượng Account
            Account account = new Account();
            account.setUsername(username.trim());
            account.setPasswordHash(password); // DAO sẽ hash password
            account.setEmail(email.trim());
            account.setRoleId(3); // Mặc định là Customer (role_id = 3)
            
            // Đăng ký tài khoản
            Account registeredAccount = accountDAO.registerAccount(account, customerName.trim(), 
                                                                phone.trim(), address.trim());
            
            if (registeredAccount != null) {
                result.setSuccess(true);
                result.setMessage("Đăng ký tài khoản thành công");
                result.setAccount(registeredAccount);
            } else {
                result.setSuccess(false);
                result.setMessage("Đăng ký thất bại, vui lòng thử lại");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Thay đổi mật khẩu với validation
     * @param accountId ID tài khoản
     * @param currentPassword Mật khẩu hiện tại
     * @param newPassword Mật khẩu mới
     * @param confirmPassword Xác nhận mật khẩu mới
     * @return Kết quả thay đổi mật khẩu
     */
    public ChangePasswordResult changePassword(int accountId, String currentPassword, 
                                             String newPassword, String confirmPassword) {
        ChangePasswordResult result = new ChangePasswordResult();
        
        // Validation
        if (newPassword == null || newPassword.length() < 6) {
            result.setSuccess(false);
            result.setMessage("Mật khẩu mới phải có ít nhất 6 ký tự");
            return result;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            result.setSuccess(false);
            result.setMessage("Xác nhận mật khẩu không khớp");
            return result;
        }
        
        // Lấy thông tin tài khoản để xác thực mật khẩu hiện tại
        Account account = accountDAO.getAccountByUsername(getCurrentUsername(accountId));
        if (account == null) {
            result.setSuccess(false);
            result.setMessage("Tài khoản không tồn tại");
            return result;
        }
        
        // Kiểm tra mật khẩu hiện tại
        if (!PasswordHasher.verifyPassword(currentPassword, account.getPasswordHash())) {
            result.setSuccess(false);
            result.setMessage("Mật khẩu hiện tại không đúng");
            return result;
        }
        
        try {
            boolean success = accountDAO.changePassword(accountId, newPassword);
            if (success) {
                result.setSuccess(true);
                result.setMessage("Thay đổi mật khẩu thành công");
            } else {
                result.setSuccess(false);
                result.setMessage("Thay đổi mật khẩu thất bại");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Cập nhật thông tin tài khoản
     * @param account Thông tin tài khoản cần cập nhật
     * @return Kết quả cập nhật
     */
    public UpdateAccountResult updateAccount(Account account) {
        UpdateAccountResult result = new UpdateAccountResult();
        
        // Validation email
        if (account.getEmail() != null && !isValidEmail(account.getEmail())) {
            result.setSuccess(false);
            result.setMessage("Email không hợp lệ");
            return result;
        }
        
        try {
            boolean success = accountDAO.updateAccount(account);
            if (success) {
                result.setSuccess(true);
                result.setMessage("Cập nhật thông tin thành công");
            } else {
                result.setSuccess(false);
                result.setMessage("Cập nhật thông tin thất bại");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Thay đổi vai trò người dùng (chỉ dành cho Admin)
     * @param accountId ID tài khoản cần thay đổi
     * @param newRoleId ID vai trò mới
     * @param currentUserRole Vai trò của người thực hiện thay đổi
     * @return Kết quả thay đổi vai trò
     */
    public ChangeRoleResult changeUserRole(int accountId, int newRoleId, String currentUserRole) {
        ChangeRoleResult result = new ChangeRoleResult();
        
        // Kiểm tra quyền thực hiện
        if (!"Admin".equals(currentUserRole)) {
            result.setSuccess(false);
            result.setMessage("Bạn không có quyền thực hiện chức năng này");
            return result;
        }
        
        try {
            boolean success = accountDAO.changeUserRole(accountId, newRoleId);
            if (success) {
                result.setSuccess(true);
                result.setMessage("Thay đổi vai trò thành công");
            } else {
                result.setSuccess(false);
                result.setMessage("Thay đổi vai trò thất bại");
            }
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Lỗi hệ thống: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Lấy danh sách tất cả tài khoản (chỉ dành cho Admin)
     * @param currentUserRole Vai trò của người yêu cầu
     * @return Danh sách tài khoản
     */
    public List<Account> getAllAccounts(String currentUserRole) {
        if (!"Admin".equals(currentUserRole)) {
            throw new SecurityException("Bạn không có quyền xem danh sách tài khoản");
        }
        
        return accountDAO.getAllAccounts();
    }
    
    /**
     * Lấy danh sách tất cả vai trò
     * @return Danh sách vai trò
     */
    public List<Role> getAllRoles() {
        return accountDAO.getAllRoles();
    }
    
    /**
     * Lấy thông tin tài khoản theo username
     * @param username Tên đăng nhập
     * @return Thông tin tài khoản
     */
    public Account getAccountByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        
        return accountDAO.getAccountByUsername(username.trim());
    }
    
    // Private helper methods
    
    /**
     * Validation cho dữ liệu đăng ký
     */
    private String validateRegistrationInput(String username, String password, String email,
                                           String customerName, String phone, String address) {
        if (username == null || username.trim().isEmpty()) {
            return "Tên đăng nhập không được để trống";
        }
        
        if (username.trim().length() < 3) {
            return "Tên đăng nhập phải có ít nhất 3 ký tự";
        }
        
        if (password == null || password.length() < 6) {
            return "Mật khẩu phải có ít nhất 6 ký tự";
        }
        
        if (email == null || !isValidEmail(email.trim())) {
            return "Email không hợp lệ";
        }
        
        if (customerName == null || customerName.trim().isEmpty()) {
            return "Tên khách hàng không được để trống";
        }
        
        if (phone == null || !isValidPhone(phone.trim())) {
            return "Số điện thoại không hợp lệ (10-11 chữ số)";
        }
        
        if (address == null || address.trim().isEmpty()) {
            return "Địa chỉ không được để trống";
        }
        
        return null; // Tất cả validation đều pass
    }
    
    /**
     * Kiểm tra email hợp lệ
     */
    private boolean isValidEmail(String email) {
        return emailPattern.matcher(email).matches();
    }
    
    /**
     * Kiểm tra số điện thoại hợp lệ
     */
    private boolean isValidPhone(String phone) {
        return phonePattern.matcher(phone).matches();
    }
    
    /**
     * Lấy username từ accountId (helper method)
     */
    private String getCurrentUsername(int accountId) {
        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account acc : accounts) {
            if (acc.getAccountId() == accountId) {
                return acc.getUsername();
            }
        }
        return null;
    }
    
    // Inner classes cho kết quả trả về
    
    public static class LoginResult {
        private boolean success;
        private String message;
        private Account account;
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Account getAccount() { return account; }
        public void setAccount(Account account) { this.account = account; }
    }
    
    public static class RegisterResult {
        private boolean success;
        private String message;
        private Account account;
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Account getAccount() { return account; }
        public void setAccount(Account account) { this.account = account; }
    }
    
    public static class ChangePasswordResult {
        private boolean success;
        private String message;
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class UpdateAccountResult {
        private boolean success;
        private String message;
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class ChangeRoleResult {
        private boolean success;
        private String message;
        
        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}