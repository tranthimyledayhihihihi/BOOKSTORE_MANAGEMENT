package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Lớp tiện ích để mã hóa và xác minh mật khẩu
 */
public class PasswordHasher {
    
    private static final int SALT_LENGTH = 16; // Độ dài muối mặc định (16 byte)

    /**
     * Mã hóa mật khẩu sử dụng thuật toán SHA-256 mà không dùng muối
     * @param password Mật khẩu cần mã hóa
     * @return Chuỗi mã hóa dạng hex, hoặc null nếu lỗi
     * @throws IllegalArgumentException nếu mật khẩu null hoặc rỗng
     */
    public static String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được null hoặc rỗng");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Tạo chuỗi muối ngẫu nhiên
     * @return Chuỗi muối dạng hex
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }
    
    /**
     * Mã hóa mật khẩu sử dụng thuật toán SHA-256 với muối
     * @param password Mật khẩu cần mã hóa
     * @param salt Chuỗi muối
     * @return Chuỗi mã hóa dạng hex, hoặc null nếu lỗi
     * @throws IllegalArgumentException nếu mật khẩu hoặc muối null hoặc rỗng
     */
    public static String hashPassword(String password, String salt) {
        if (password == null || password.trim().isEmpty() || salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu và muối không được null hoặc rỗng");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;
            byte[] encodedHash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Chuyển đổi mảng byte thành chuỗi hex
     * @param hash Mảng byte cần chuyển đổi
     * @return Chuỗi hex
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    /**
     * Kiểm tra mật khẩu có khớp với mã hóa không (không dùng muối)
     * @param password Mật khẩu cần kiểm tra
     * @param hashedPassword Mật khẩu đã mã hóa
     * @return true nếu khớp, false nếu không khớp
     * @throws IllegalArgumentException nếu mật khẩu hoặc hashedPassword null hoặc rỗng
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || password.trim().isEmpty() || hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu và hashedPassword không được null hoặc rỗng");
        }
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hashedPassword);
    }
    
    /**
     * Kiểm tra mật khẩu có khớp với mã hóa không (dùng muối)
     * @param password Mật khẩu cần kiểm tra
     * @param hashedPassword Mật khẩu đã mã hóa
     * @param salt Chuỗi muối
     * @return true nếu khớp, false nếu không khớp
     * @throws IllegalArgumentException nếu mật khẩu, hashedPassword hoặc muối null hoặc rỗng
     */
    public static boolean verifyPassword(String password, String hashedPassword, String salt) {
        if (password == null || password.trim().isEmpty() || hashedPassword == null || hashedPassword.trim().isEmpty() || salt == null || salt.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu, hashedPassword và muối không được null hoặc rỗng");
        }
        String hashedInput = hashPassword(password, salt);
        return hashedInput.equals(hashedPassword);
    }
}