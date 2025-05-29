package bll;

import dao.AddressDAO;
import model.Address;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Lớp Business Logic Layer cho các thao tác với địa chỉ
 */
public class AddressBLL {
    private final AddressDAO addressDAO;

    // Biểu thức chính quy để kiểm tra số điện thoại (cho phép số, +, -, và khoảng trắng)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[\\d\\s\\-+]{8,15}$");

    // Constructor mặc định
    public AddressBLL() {
        this.addressDAO = new AddressDAO();
    }

    // Constructor để tiêm phụ thuộc (dùng cho kiểm thử)
    public AddressBLL(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    /**
     * Lấy danh sách địa chỉ của một khách hàng
     * 
     * @param customerId ID khách hàng
     * @return Danh sách địa chỉ
     * @throws IllegalArgumentException nếu customerId không hợp lệ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public List<Address> getAddressesByCustomer(int customerId) throws SQLException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        return addressDAO.getAddressesByCustomer(customerId);
    }

    /**
     * Lấy địa chỉ mặc định của một khách hàng
     * 
     * @param customerId ID khách hàng
     * @return Địa chỉ mặc định hoặc null nếu không có
     * @throws IllegalArgumentException nếu customerId không hợp lệ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public Address getDefaultAddress(int customerId) throws SQLException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        return addressDAO.getDefaultAddress(customerId);
    }

    /**
     * Lấy địa chỉ theo ID
     * 
     * @param addressId ID địa chỉ
     * @return Địa chỉ hoặc null nếu không tìm thấy
     * @throws IllegalArgumentException nếu addressId không hợp lệ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public Address getAddressById(int addressId) throws SQLException {
        if (addressId <= 0) {
            throw new IllegalArgumentException("ID địa chỉ không hợp lệ");
        }
        return addressDAO.getAddressById(addressId);
    }

    /**
     * Thêm một địa chỉ mới
     * 
     * @param address Địa chỉ cần thêm
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public boolean addAddress(Address address) throws SQLException {
        validateAddress(address);
        
        // Nếu đây là địa chỉ đầu tiên của khách hàng, đặt làm mặc định
        if (addressDAO.getAddressCountByCustomer(address.getCustomerId()) == 0) {
            address.setDefault(true);
        }
        
        try {
            int addressId = addressDAO.addAddress(address);
            return addressId > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Cập nhật một địa chỉ hiện có
     * 
     * @param address Địa chỉ cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public boolean updateAddress(Address address) throws SQLException {
        if (address == null || address.getAddressId() <= 0) {
            throw new IllegalArgumentException("ID địa chỉ không hợp lệ");
        }
        validateAddress(address);
        
        try {
            addressDAO.updateAddress(address);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Xóa một địa chỉ
     * 
     * @param addressId ID địa chỉ
     * @param customerId ID khách hàng
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu addressId hoặc customerId không hợp lệ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public boolean deleteAddress(int addressId, int customerId) throws SQLException {
        if (addressId <= 0) {
            throw new IllegalArgumentException("ID địa chỉ không hợp lệ");
        }
        if (customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        
        // Không cho phép xóa nếu đây là địa chỉ duy nhất
        if (addressDAO.getAddressCountByCustomer(customerId) <= 1) {
            throw new IllegalArgumentException("Không thể xóa địa chỉ duy nhất của khách hàng");
        }
        
        try {
            addressDAO.deleteAddress(addressId, customerId);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Lấy số lượng địa chỉ của một khách hàng
     * 
     * @param customerId ID khách hàng
     * @return Số lượng địa chỉ
     * @throws IllegalArgumentException nếu customerId không hợp lệ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public int getAddressCountByCustomer(int customerId) throws SQLException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        return addressDAO.getAddressCountByCustomer(customerId);
    }

    /**
     * Đặt một địa chỉ làm mặc định
     * 
     * @param addressId ID địa chỉ cần đặt làm mặc định
     * @param customerId ID khách hàng
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu addressId hoặc customerId không hợp lệ
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public boolean setDefaultAddress(int addressId, int customerId) throws SQLException {
        if (addressId <= 0) {
            throw new IllegalArgumentException("ID địa chỉ không hợp lệ");
        }
        if (customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        
        // Kiểm tra địa chỉ có tồn tại và thuộc về khách hàng hay không
        Address address = addressDAO.getAddressById(addressId);
        if (address == null || address.getCustomerId() != customerId) {
            throw new IllegalArgumentException("Địa chỉ không tồn tại hoặc không thuộc về khách hàng");
        }
        
        try {
            addressDAO.setDefaultAddress(addressId, customerId);
            return true;
        } catch (SQLException e) {
            return false;
        }
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
        return addressDAO.getAddressesByParameters(city, country);
    }

    /**
     * Lấy danh sách tất cả quốc gia từ địa chỉ
     * 
     * @return Danh sách quốc gia
     * @throws SQLException nếu xảy ra lỗi cơ sở dữ liệu
     */
    public List<String> getAllCountries() throws SQLException {
        // Ghi chú: AddressDAO chưa có phương thức này, cần triển khai
        throw new UnsupportedOperationException("Phương thức getAllCountries chưa được triển khai trong AddressDAO");
    }

    /**
     * Kiểm tra tính hợp lệ của địa chỉ
     * 
     * @param address Địa chỉ cần kiểm tra
     * @throws IllegalArgumentException nếu dữ liệu không hợp lệ
     */
    private void validateAddress(Address address) {
        if (address == null) {
            throw new IllegalArgumentException("Địa chỉ không được để trống");
        }
        if (address.getCustomerId() <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        String recipientName = address.getRecipientName();
        if (recipientName == null || recipientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người nhận không được để trống");
        }
        String phone = address.getPhone();
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ");
        }
        String addressLine1 = address.getAddressLine1();
        if (addressLine1 == null || addressLine1.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ dòng 1 không được để trống");
        }
        String city = address.getCity();
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("Thành phố không được để trống");
        }
        String state = address.getState();
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("Tiểu bang không được để trống");
        }
        String postalCode = address.getPostalCode();
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã bưu điện không được để trống");
        }
        String country = address.getCountry();
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Quốc gia không được để trống");
        }
    }
}