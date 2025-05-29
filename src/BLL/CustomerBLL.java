package BLL;

import dao.CustomerDAO;
import model.Customer;

import java.util.List;

public class CustomerBLL {
    private final CustomerDAO customerDAO;

    // Constructor mặc định
    public CustomerBLL() {
        this.customerDAO = new CustomerDAO();
    }

    // Constructor cho phép inject CustomerDAO (dùng để kiểm thử)
    public CustomerBLL(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    /**
     * Lấy danh sách tất cả khách hàng
     * @return Danh sách khách hàng
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    /**
     * Lấy tổng số khách hàng
     * @return Tổng số khách hàng
     */
    public int getTotalCustomers() {
        return customerDAO.getTotalCustomers();
    }

    /**
     * Lấy thông tin khách hàng theo ID
     * @param customerId ID khách hàng
     * @return Khách hàng nếu tìm thấy, null nếu không
     * @throws IllegalArgumentException nếu ID không hợp lệ
     */
    public Customer getCustomerById(int customerId) throws IllegalArgumentException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        // Hiện tại CustomerDAO chưa có phương thức getCustomerById, bạn cần thêm vào DAO
        // Đây là placeholder, bạn cần triển khai trong CustomerDAO
        return null; // Thay bằng logic thực tế khi DAO được cập nhật
    }
}