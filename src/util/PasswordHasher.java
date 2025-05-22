package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Lớp tiện ích để mã hóa mật khẩu
 */
public class PasswordHasher {
    
    /**
     * Mã hóa mật khẩu sử dụng thuật toán SHA-256
     * @param password Mật khẩu cần mã hóa
     * @return Chuỗi mã hóa dạng hex
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
    
    /**
     * Chuyển đổi mảng byte thành chuỗi hex
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
     * Kiểm tra mật khẩu có khớp với mã hóa không
     * @param password Mật khẩu cần kiểm tra
     * @param hashedPassword Mật khẩu đã mã hóa
     * @return true nếu khớp, false nếu không khớp
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hashedPassword);
    }
} 