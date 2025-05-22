package dao;

import model.Account;
import model.Role;
import util.DBConnection;
import util.PasswordHasher;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    /**
     * Kiểm tra đăng nhập
     * @param username Tên đăng nhập
     * @param password Mật khẩu (chưa mã hóa)
     * @return Thông tin tài khoản nếu đăng nhập thành công, null nếu thất bại
     */
    public Account login(String username, String password) {
        Account account = null;
        String sql = "{call pr_login(?, ?, ?, ?, ?)}";
        
        try (Connection conn = DBConnection.getConnection();
            CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Mã hóa mật khẩu
            String hashedPassword = PasswordHasher.hashPassword(password);
            
            cstmt.setString(1, username);
            cstmt.setString(2, hashedPassword);
            cstmt.registerOutParameter(3, Types.BIT); // login_success
            cstmt.registerOutParameter(4, Types.NVARCHAR); // role_name
            cstmt.registerOutParameter(5, Types.INTEGER); // customer_id
            
            cstmt.execute();
            
            boolean loginSuccess = cstmt.getBoolean(3);
            
            if (loginSuccess) {
                account = new Account();
                account.setUsername(username);
                account.setRoleName(cstmt.getString(4));
                
                Integer customerId = cstmt.getInt(5);
                if (!cstmt.wasNull()) {
                    account.setCustomerId(customerId);
                }
                
                // Cập nhật thông tin tài khoản từ database
                account = getAccountByUsername(username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return account;
    }
    
    /**
     * Đăng ký tài khoản mới
     * @param account Thông tin tài khoản
     * @param customerName Tên khách hàng
     * @param phone Số điện thoại
     * @param address Địa chỉ
     * @return Thông tin tài khoản nếu đăng ký thành công, null nếu thất bại
     */
    public Account registerAccount(Account account, String customerName, String phone, String address) {
        String sql = "{call pr_register_account(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        
        try (Connection conn = DBConnection.getConnection();
            CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Mã hóa mật khẩu
            String hashedPassword = PasswordHasher.hashPassword(account.getPasswordHash());
            
            cstmt.setString(1, account.getUsername());
            cstmt.setString(2, hashedPassword);
            cstmt.setString(3, account.getEmail());
            cstmt.setString(4, customerName);
            cstmt.setString(5, phone);
            cstmt.setString(6, address);
            cstmt.registerOutParameter(7, Types.BIT); // success
            cstmt.registerOutParameter(8, Types.INTEGER); // customer_id
            cstmt.registerOutParameter(9, Types.INTEGER); // account_id
            cstmt.registerOutParameter(10, Types.NVARCHAR); // error_message
            
            cstmt.execute();
            
            boolean success = cstmt.getBoolean(7);
            
            if (success) {
                int accountId = cstmt.getInt(9);
                int customerId = cstmt.getInt(8);
                
                account.setAccountId(accountId);
                account.setCustomerId(customerId);
                account.setCustomerName(customerName);
                
                return account;
            } else {
                // Lỗi khi đăng ký
                String errorMessage = cstmt.getString(10);
                System.out.println("Error registering account: " + errorMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Lấy thông tin tài khoản theo tên đăng nhập
     * @param username Tên đăng nhập
     * @return Thông tin tài khoản, null nếu không tìm thấy
     */
    public Account getAccountByUsername(String username) {
        Account account = null;
        String sql = "SELECT * FROM vw_account_details WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    account = mapResultSetToAccount(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return account;
    }
    
    /**
     * Lấy danh sách tất cả tài khoản
     * @return Danh sách tài khoản
     */
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM vw_account_details";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Account account = mapResultSetToAccount(rs);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return accounts;
    }
    
    /**
     * Lấy danh sách tất cả vai trò
     * @return Danh sách vai trò
     */
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM ROLES";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Role role = new Role();
                role.setRoleId(rs.getInt("role_id"));
                role.setRoleName(rs.getString("role_name"));
                role.setDescription(rs.getString("description"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return roles;
    }
    
    /**
     * Thay đổi vai trò cho tài khoản
     * @param accountId ID tài khoản
     * @param roleId ID vai trò mới
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean changeUserRole(int accountId, int roleId) {
        String sql = "{call pr_change_user_role(?, ?, ?)}";
        
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            cstmt.setInt(1, accountId);
            cstmt.setInt(2, roleId);
            cstmt.registerOutParameter(3, Types.BIT); // success
            
            cstmt.execute();
            
            return cstmt.getBoolean(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Cập nhật thông tin tài khoản
     * @param account Thông tin tài khoản cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean updateAccount(Account account) {
        String sql = "UPDATE ACCOUNTS SET email = ?, is_active = ? WHERE account_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, account.getEmail());
            pstmt.setBoolean(2, account.isActive());
            pstmt.setInt(3, account.getAccountId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Thay đổi mật khẩu
     * @param accountId ID tài khoản
     * @param newPassword Mật khẩu mới (chưa mã hóa)
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean changePassword(int accountId, String newPassword) {
        String sql = "UPDATE ACCOUNTS SET password_hash = ? WHERE account_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Mã hóa mật khẩu mới
            String hashedPassword = PasswordHasher.hashPassword(newPassword);
            
            pstmt.setString(1, hashedPassword);
            pstmt.setInt(2, accountId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Helper method để map ResultSet thành đối tượng Account
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUsername(rs.getString("username"));
        account.setEmail(rs.getString("email"));
        account.setRoleId(rs.getInt("role_id"));
        account.setRoleName(rs.getString("role_name"));
        
        // Xử lý customerId có thể null
        Integer customerId = rs.getInt("customer_id");
        if (rs.wasNull()) {
            customerId = null;
        }
        account.setCustomerId(customerId);
        
        // Lấy tên khách hàng nếu có
        String customerName = rs.getString("customer_name");
        if (customerName != null) {
            account.setCustomerName(customerName);
        }
        
        account.setActive(rs.getBoolean("is_active"));
        account.setCreatedDate(rs.getTimestamp("created_date"));
        
        // Xử lý lastLogin có thể null
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            account.setLastLogin(lastLogin);
        }
        
        return account;
    }

    public static void main(String[] args) {
        AccountDAO accountDAO = new AccountDAO();
        // // Test login
        // Account account = accountDAO.login("testuser", "testpassword");
        // if (account != null) {
        //     System.out.println("Login successful: " + account.getUsername());
        // } else {
        //     System.out.println("Login failed.");
        // }

        // Account newAccount = new Account();
        // newAccount.setUsername("admin2");
        // String password = "123123";
        // newAccount.setPasswordHash(PasswordHasher.hashPassword(password));
        // newAccount.setEmail("admin2@gmail.com");
        // newAccount.setRoleId(1); // Giả sử vai trò admin có ID là 1
        Account account = accountDAO.login("admin2", "123123");
        // if (account != null) {
        //     System.out.println("Login successful: " + account.getUsername());
        // } else {
        //     System.out.println("Login failed.");
        // }
        String hashedPassword = PasswordHasher.hashPassword("123123");
        System.out.println("Hashed password: " + hashedPassword);
    }

    /**
     * Kiểm tra xem username đã tồn tại trong hệ thống chưa
     * @param username Tên đăng nhập cần kiểm tra
     * @return true nếu username đã tồn tại, false nếu chưa
     */
    public boolean checkUsernameExists(String username) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM ACCOUNTS WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra xem email đã tồn tại trong hệ thống chưa
     * @param email Email cần kiểm tra
     * @return true nếu email đã tồn tại, false nếu chưa
     */
    public boolean checkEmailExists(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM ACCOUNTS WHERE email = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
} 