/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import util.DBConnection;


public class OrderDAO {

   public List<Order> getAllOrders() {
      List<Order> orders = new ArrayList<>();

      try (Connection conn = DBConnection.getConnection()) {
         String sql = "SELECT o.*, c.name FROM orders o JOIN customers c ON o.customer_id = c.customer_id";
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();

         while (rs.next()) {
            Order order = mapResultSetToOrder(rs);
            orders.add(order);
         }
      
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return orders;
   }
   
   public List<Order> getRecenOrders() {
      List<Order> orders = new ArrayList<>();

      try (Connection conn = DBConnection.getConnection()) {
         CallableStatement cstm = conn.prepareCall("{call pr_Recent_Orders()}");
         ResultSet rs = cstm.executeQuery();

         while (rs.next()) {
            Order order = new Order();
            order.setOrderId(rs.getInt("order_id"));
            order.setOrderDate(rs.getDate("order_date"));
            order.setTotalAmount(rs.getBigDecimal("total_amount"));
            order.setStatus(rs.getString("status"));
            order.setCustomerName(rs.getString("name"));
            orders.add(order);
         }
      
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return orders;
   }

   public int getTotalTodayOrders() {
      int totalOrders = 0;
      
      try (Connection conn = DBConnection.getConnection()) {
         CallableStatement cstm = conn.prepareCall("{? = call fn_Total_Today_Orders()}");
         cstm.registerOutParameter(1, Types.INTEGER);
         cstm.execute();

         totalOrders = cstm.getInt(1);

      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      return totalOrders;
   }

   public BigDecimal getMonthlyRevenue() {
      BigDecimal monthlyRevenue = BigDecimal.ZERO;
      
      try (Connection conn = DBConnection.getConnection()) {
         CallableStatement cstm = conn.prepareCall("{? = call fn_Monthly_Revenue()}");
         cstm.registerOutParameter(1, Types.DECIMAL);
         cstm.execute();

         monthlyRevenue = cstm.getBigDecimal(1);

      } catch (SQLException e) {
         e.printStackTrace();
      }
      
      return monthlyRevenue;
   }

   private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
      Order order = new Order();
      order.setOrderId(rs.getInt("order_id"));
      order.setCustomerId(rs.getInt("customer_id"));
      order.setOrderDate(rs.getDate("order_date"));
      order.setTotalAmount(rs.getBigDecimal("total_amount"));
      order.setStatus(rs.getString("status"));
      order.setShippingAddress(rs.getString("shipping_address"));
      order.setPaymentMethod(rs.getString("payment_method"));
      
      // Join fields
      order.setCustomerName(rs.getString("name"));
      
      return order;
   }

   public static void main(String[] args) {
      OrderDAO orderDAO = new OrderDAO();
      List <Order> orders = orderDAO.getAllOrders();
      for(Order order : orders) {
         System.out.println(order.toString());
      }

   }
}
