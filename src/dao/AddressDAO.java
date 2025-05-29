package dao;

import model.Address;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho các thao tác với địa chỉ
 */
public class AddressDAO {
    
    /**
     * Lấy danh sách địa chỉ của một khách hàng
     * 
     * @param customerId ID khách hàng
     * @return Danh sách địa chỉ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public List<Address> getAddressesByCustomer(int customerId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE customer_id = ? ORDER BY is_default DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Address address = mapResultSetToAddress(rs);
                    addresses.add(address);
                }
            }
        }
        
        return addresses;
    }
    
    /**
     * Lấy địa chỉ mặc định của một khách hàng
     * 
     * @param customerId ID khách hàng
     * @return Địa chỉ mặc định hoặc null nếu không có địa chỉ mặc định
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public Address getDefaultAddress(int customerId) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE customer_id = ? AND is_default = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAddress(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Lấy địa chỉ theo ID
     * 
     * @param addressId ID địa chỉ
     * @return Địa chỉ hoặc null nếu không tìm thấy
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public Address getAddressById(int addressId) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE address_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, addressId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAddress(rs);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Thêm một địa chỉ mới
     * 
     * @param address Địa chỉ cần thêm
     * @return ID địa chỉ được tạo
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public int addAddress(Address address) throws SQLException {
        String sql = "INSERT INTO addresses (customer_id, recipient_name, phone, address_line1, address_line2, city, " +
                     "state, postal_code, country, is_default, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, address.getCustomerId());
            stmt.setString(2, address.getRecipientName());
            stmt.setString(3, address.getPhone());
            stmt.setString(4, address.getAddressLine1());
            stmt.setString(5, address.getAddressLine2());
            stmt.setString(6, address.getCity());
            stmt.setString(7, address.getState());
            stmt.setString(8, address.getPostalCode());
            stmt.setString(9, address.getCountry());
            stmt.setBoolean(10, address.isDefault());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Tạo địa chỉ thất bại, không có dòng nào bị ảnh hưởng.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int addressId = generatedKeys.getInt(1);
                    
                    // Nếu đây là địa chỉ mặc định, cập nhật các địa chỉ khác
                    if (address.isDefault()) {
                        updateOtherAddressesDefaultStatus(address.getCustomerId(), addressId);
                    }
                    
                    return addressId;
                } else {
                    throw new SQLException("Tạo địa chỉ thất bại, không lấy được ID.");
                }
            }
        }
    }
    
    /**
     * Cập nhật một địa chỉ hiện có
     * 
     * @param address Địa chỉ cần cập nhật
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public void updateAddress(Address address) throws SQLException {
        String sql = "UPDATE addresses SET recipient_name = ?, phone = ?, address_line1 = ?, " +
                     "address_line2 = ?, city = ?, state = ?, postal_code = ?, country = ?, " +
                     "is_default = ?, updated_at = NOW() " +
                     "WHERE address_id = ? AND customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, address.getRecipientName());
            stmt.setString(2, address.getPhone());
            stmt.setString(3, address.getAddressLine1());
            stmt.setString(4, address.getAddressLine2());
            stmt.setString(5, address.getCity());
            stmt.setString(6, address.getState());
            stmt.setString(7, address.getPostalCode());
            stmt.setString(8, address.getCountry());
            stmt.setBoolean(9, address.isDefault());
            stmt.setInt(10, address.getAddressId());
            stmt.setInt(11, address.getCustomerId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0 && address.isDefault()) {
                // Nếu đây là địa chỉ mặc định, cập nhật các địa chỉ khác
                updateOtherAddressesDefaultStatus(address.getCustomerId(), address.getAddressId());
            }
        }
    }
    
    /**
     * Xóa một địa chỉ
     * 
     * @param addressId ID địa chỉ
     * @param customerId ID khách hàng (để kiểm tra bảo mật)
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public void deleteAddress(int addressId, int customerId) throws SQLException {
        String sql = "DELETE FROM addresses WHERE address_id = ? AND customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, addressId);
            stmt.setInt(2, customerId);
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Lấy số lượng địa chỉ của một khách hàng
     * 
     * @param customerId ID khách hàng
     * @return Số lượng địa chỉ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public int getAddressCountByCustomer(int customerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM addresses WHERE customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Đặt một địa chỉ làm mặc định và cập nhật các địa chỉ khác
     * 
     * @param addressId ID địa chỉ cần đặt làm mặc định
     * @param customerId ID khách hàng
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public void setDefaultAddress(int addressId, int customerId) throws SQLException {
        // Đầu tiên, đặt tất cả địa chỉ của khách hàng này thành không mặc định
        String sql1 = "UPDATE addresses SET is_default = 0, updated_at = NOW() WHERE customer_id = ?";
        
        // Sau đó, đặt địa chỉ được chỉ định làm mặc định
        String sql2 = "UPDATE addresses SET is_default = 1, updated_at = NOW() WHERE address_id = ? AND customer_id = ?";
        
        Connection conn = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmt1 = conn.prepareStatement(sql1)) {
                stmt1.setInt(1, customerId);
                stmt1.executeUpdate();
            }
            
            try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
                stmt2.setInt(1, addressId);
                stmt2.setInt(2, customerId);
                stmt2.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new SQLException("Lỗi trong quá trình rollback giao dịch", ex);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new SQLException("Lỗi khi đặt lại auto-commit", e);
                }
            }
        }
    }
    
    /**
     * Cập nhật trạng thái mặc định của các địa chỉ khác khi một địa chỉ mới được đặt làm mặc định
     * 
     * @param customerId ID khách hàng
     * @param excludeAddressId ID địa chỉ cần loại trừ (địa chỉ mặc định mới)
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    private void updateOtherAddressesDefaultStatus(int customerId, int excludeAddressId) throws SQLException {
        String sql = "UPDATE addresses SET is_default = 0, updated_at = NOW() " +
                     "WHERE customer_id = ? AND address_id != ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, excludeAddressId);
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Ánh xạ ResultSet thành đối tượng Address
     * 
     * @param rs ResultSet
     * @return Đối tượng Address
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    private Address mapResultSetToAddress(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setAddressId(rs.getInt("address_id"));
        address.setCustomerId(rs.getInt("customer_id"));
        address.setRecipientName(rs.getString("recipient_name"));
        address.setPhone(rs.getString("phone"));
        address.setAddressLine1(rs.getString("address_line1"));
        address.setAddressLine2(rs.getString("address_line2"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setPostalCode(rs.getString("postal_code"));
        address.setCountry(rs.getString("country"));
        address.setDefault(rs.getBoolean("is_default"));
        address.setCreatedAt(rs.getTimestamp("created_at"));
        address.setUpdatedAt(rs.getTimestamp("updated_at"));
        return address;
    }

    /**
     * Tìm kiếm địa chỉ theo thành phố hoặc quốc gia
     * 
     * @param city Từ khóa thành phố (có thể null hoặc rỗng)
     * @param country Từ khóa quốc gia (có thể null hoặc rỗng)
     * @return Danh sách địa chỉ phù hợp
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public List<Address> getAddressesByParameters(String city, String country) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE (city LIKE ? OR ? IS NULL) AND (country LIKE ? OR ? IS NULL)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, city != null ? "%" + city + "%" : null);
            stmt.setString(2, city != null ? "%" + city + "%" : null);
            stmt.setString(3, country != null ? "%" + country + "%" : null);
            stmt.setString(4, country != null ? "%" + country + "%" : null);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    addresses.add(mapResultSetToAddress(rs));
                }
            }
        }
        
        return addresses;
    }
}