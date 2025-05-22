package service;

import dao.OrderDAO;
import dao.BookDAO;
import model.Book;
import model.Order;
import model.OrderDetail;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Lớp service xử lý logic nghiệp vụ liên quan đến đơn hàng
 */
public class OrderService {
    private OrderDAO orderDAO;
    private BookDAO bookDAO;
    
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.bookDAO = new BookDAO();
    }
    
    /**
     * Lấy danh sách đơn hàng của khách hàng
     */
    public List<Order> getOrdersByCustomer(int customerId, String status, String keyword, 
                                         Date startDate, Date endDate, int page, int recordsPerPage) throws SQLException {
        // Trong thực tế, bạn sẽ gọi hàm tương ứng từ OrderDAO
        // và có thể thực hiện thêm logic nghiệp vụ ở đây
        List<Order> orders = orderDAO.getAllOrders();
        
        // Lọc theo customerId và các điều kiện khác
        List<Order> filteredOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getCustomerId() == customerId) {
                // Thêm điều kiện lọc theo status nếu cần
                if (status != null && !status.equals("all") && !order.getStatus().equals(status)) {
                    continue;
                }
                
                // Thêm điều kiện lọc theo keyword nếu cần
                if (keyword != null && !keyword.isEmpty() && 
                    !String.valueOf(order.getOrderId()).contains(keyword)) {
                    continue;
                }
                
                // Thêm điều kiện lọc theo ngày nếu cần
                if (startDate != null && order.getOrderDate().before(startDate)) {
                    continue;
                }
                
                if (endDate != null && order.getOrderDate().after(endDate)) {
                    continue;
                }
                
                filteredOrders.add(order);
            }
        }
        
        // Tính phân trang
        int startIndex = (page - 1) * recordsPerPage;
        int endIndex = Math.min(startIndex + recordsPerPage, filteredOrders.size());
        
        if (startIndex >= filteredOrders.size()) {
            return new ArrayList<>();
        }
        
        return filteredOrders.subList(startIndex, endIndex);
    }
    
    /**
     * Đếm số lượng đơn hàng của khách hàng
     */
    public int countOrdersByCustomer(int customerId, String status, String keyword, 
                                   Date startDate, Date endDate) throws SQLException {
        // Trong thực tế, bạn sẽ gọi hàm tương ứng từ OrderDAO
        List<Order> orders = orderDAO.getAllOrders();
        
        // Lọc theo customerId và các điều kiện khác
        int count = 0;
        for (Order order : orders) {
            if (order.getCustomerId() == customerId) {
                // Thêm điều kiện lọc theo status nếu cần
                if (status != null && !status.equals("all") && !order.getStatus().equals(status)) {
                    continue;
                }
                
                // Thêm điều kiện lọc theo keyword nếu cần
                if (keyword != null && !keyword.isEmpty() && 
                    !String.valueOf(order.getOrderId()).contains(keyword)) {
                    continue;
                }
                
                // Thêm điều kiện lọc theo ngày nếu cần
                if (startDate != null && order.getOrderDate().before(startDate)) {
                    continue;
                }
                
                if (endDate != null && order.getOrderDate().after(endDate)) {
                    continue;
                }
                
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Lấy thông tin chi tiết đơn hàng
     */
    public Order getOrderById(int orderId) throws SQLException {
        // Trong thực tế, bạn sẽ gọi hàm tương ứng từ OrderDAO
        return orderDAO.getOrderById(orderId);
    }
    
    /**
     * Hủy đơn hàng
     */
    public boolean cancelOrder(Order order) throws SQLException {
        order.setStatus("cancelled");
        // Trong OrderDAO cần thêm các trường cancelledDate và cancelReason 
        // hoặc lưu thông tin cancel vào một bảng riêng
        
        // orderDAO.updateOrder(order);
        return true;
    }
    
    /**
     * Tạo mã đơn hàng
     */
    public String generateOrderCode() {
        String dateString = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String randomPart = String.format("%04d", (int) (Math.random() * 10000));
        return "ORD-" + dateString + "-" + randomPart;
    }
    
    /**
     * Tạo mã vận đơn
     */
    public String generateTrackingCode() {
        return "TRK" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
    
    /**
     * Lấy danh sách tất cả đơn hàng
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
    
    /**
     * Lấy danh sách đơn hàng gần đây
     */
    public List<Order> getRecentOrders() {
        return orderDAO.getRecenOrders();
    }
    
    /**
     * Lấy tổng số đơn hàng trong ngày hôm nay
     */
    public int getTotalTodayOrders() {
        return orderDAO.getTotalTodayOrders();
    }
    
    /**
     * Lấy doanh thu tháng hiện tại
     */
    public BigDecimal getMonthlyRevenue() {
        return orderDAO.getMonthlyRevenue();
    }
    
    /**
     * Cập nhật trạng thái đơn hàng
     */
    public boolean updateOrderStatus(int orderId, String status) {
        // Kiểm tra dữ liệu đầu vào
        if (orderId <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }
        
       
            Order order = orderDAO.getOrderById(orderId);
            if (order != null) {
                order.setStatus(status);
                // Giả sử có phương thức updateOrder trong OrderDAO
                // return orderDAO.updateOrder(order);
                return true; // Tạm thời trả về true vì chưa có phương thức updateOrder
            }
       
        
        return false;
    }
} 