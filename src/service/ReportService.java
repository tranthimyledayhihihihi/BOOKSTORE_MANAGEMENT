package service;

import dao.BookDAO;
import dao.CustomerDAO;
import dao.DAO;
import dao.OrderDAO;
import model.Book;
import model.Customer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Lớp service xử lý logic nghiệp vụ liên quan đến báo cáo
 */
public class ReportService {
    private BookDAO bookDAO;
    private CustomerDAO customerDAO;
    private OrderDAO orderDAO;
    private DAO dao;
    
    public ReportService() {
        this.bookDAO = new BookDAO();
        this.customerDAO = new CustomerDAO();
        this.orderDAO = new OrderDAO();
        this.dao = new DAO();
    }
    
    /**
     * Lấy dữ liệu báo cáo doanh thu theo tháng
     */
    public Map<String, BigDecimal> getRevenueByMonth(Date startDate, Date endDate) {
        return dao.getTotalRevenueByMonth(startDate, endDate);
    }
    
    /**
     * Tính tổng doanh thu
     */
    public BigDecimal calculateTotalRevenue(Map<String, BigDecimal> revenueMap) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (BigDecimal revenue : revenueMap.values()) {
            totalRevenue = totalRevenue.add(revenue);
        }
        return totalRevenue;
    }
    
    /**
     * Lấy dữ liệu báo cáo khách hàng
     */
    public List<Customer> getCustomerReport() {
        return customerDAO.getAllCustomers();
    }
    
    /**
     * Lấy dữ liệu báo cáo tồn kho
     */
    public List<Book> getInventoryReport() {
        return bookDAO.getAllBooks();
    }
    
    /**
     * Lấy tổng số sách
     */
    public int getTotalBooks() {
        return bookDAO.getAllBooks().size();
    }
    
    /**
     * Lấy tổng số khách hàng
     */
    public int getTotalCustomers() {
        return customerDAO.getTotalCustomers();
    }
    
    /**
     * Lấy doanh thu tháng hiện tại
     */
    public BigDecimal getMonthlyRevenue() {
        return orderDAO.getMonthlyRevenue();
    }
} 