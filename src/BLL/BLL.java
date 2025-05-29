package BLL;

import dao.DAO;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Business Logic Layer cho các thao tác liên quan đến doanh thu
 */
public class BLL {
    private final DAO dao;

    // Constructor mặc định
    public BLL() {
        this.dao = new DAO();
    }

    // Constructor để tiêm phụ thuộc (dùng cho kiểm thử)
    public BLL(DAO dao) {
        this.dao = dao;
    }

    /**
     * Lấy tổng doanh thu theo tháng trong khoảng thời gian chỉ định
     * 
     * @param startDate Ngày bắt đầu (yyyy-MM-dd)
     * @param endDate Ngày kết thúc (yyyy-MM-dd)
     * @return Map chứa tháng-năm và tổng doanh thu, hoặc rỗng nếu không có dữ liệu
     * @throws IllegalArgumentException nếu startDate hoặc endDate không hợp lệ
     * @throws ParseException nếu định dạng ngày không đúng
     */
    public Map<String, BigDecimal> getTotalRevenueByMonth(String startDate, String endDate) throws ParseException {
        // Kiểm tra đầu vào
        if (startDate == null || startDate.trim().isEmpty() || endDate == null || endDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Ngày bắt đầu và ngày kết thúc không được để trống!");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Ngăn chặn phân tích ngày không hợp lệ (ví dụ: 2024-13-01)
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        // Kiểm tra startDate <= endDate
        if (start.after(end)) {
            throw new IllegalArgumentException("Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!");
        }

        // Gọi phương thức từ DAO
        return dao.getTotalRevenueByMonth(start, end);
    }

    // Phương thức để thử nghiệm
    public static void main(String[] args) {
        BLL bll = new BLL();
        try {
            Map<String, BigDecimal> revenueMap = bll.getTotalRevenueByMonth("2024-04-12", "2024-12-31");
            for (Map.Entry<String, BigDecimal> entry : revenueMap.entrySet()) {
                System.out.println("Month: " + entry.getKey() + ", Total Revenue: " + entry.getValue());
            }
        } catch (ParseException e) {
            System.out.println("Lỗi định dạng ngày: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi đầu vào: " + e.getMessage());
        }
    }
}