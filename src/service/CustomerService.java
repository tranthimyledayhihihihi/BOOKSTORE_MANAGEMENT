/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.CustomerDAO;
import model.Customer;
import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private CustomerDAO customerDAO;
    private boolean isAdmin;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
        this.isAdmin = true; // Placeholder: Thay bằng logic kiểm tra quyền admin thực tế
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }

    public void getCustomerById(int id) throws SQLException {
        customerDAO.getCustomerById(id); // Cần triển khai thêm trong DAO nếu cần
    }

    public int getTotalCustomers() throws SQLException {
        return customerDAO.getTotalCustomers();
    }
}
