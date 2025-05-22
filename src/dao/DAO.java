package dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import util.DBConnection;

public class DAO {
    
    // Doanh thu theo tháng 
    public Map<String, BigDecimal> getTotalRevenueByMonth(Date startDate, Date endDate) {
        
        Map<String, BigDecimal> revenueMap = new LinkedHashMap<>();
        String sql = "{call pr_GetBookRevenueByMonth(?,?)}"; // Call the stored procedure

        try (Connection conn = DBConnection.getConnection()) {
            CallableStatement cstmt = conn.prepareCall(sql);

            cstmt.setDate( 1, new java.sql.Date(startDate.getTime()));
            cstmt.setDate( 2, new java.sql.Date(endDate.getTime()));

            ResultSet rs = cstmt.executeQuery();

            while (rs.next()) {
                String month = rs.getString("MonthYear");
                BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
                revenueMap.put(month, totalRevenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenueMap;
    }

    // Tổng


    public static void main(String[] args) throws ParseException {
        DAO dao = new DAO();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2024-04-12");
        Date endDate = sdf.parse("2024-12-31");

        Map<String, BigDecimal> revenueMap = dao.getTotalRevenueByMonth(startDate, endDate);

        for (Map.Entry<String, BigDecimal> entry : revenueMap.entrySet()) {
            System.out.println("Month: " + entry.getKey() + ", Total Revenue: " + entry.getValue());
        }
    }
}
