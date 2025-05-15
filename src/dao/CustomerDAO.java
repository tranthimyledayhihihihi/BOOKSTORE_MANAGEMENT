/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.Customer;
import util.DBConnection;

public class CustomerDAO {

   public List<Customer> getAllCustomers() {
      List<Customer> list = new ArrayList<>();

      try (Connection conn = DBConnection.getConnection()) {
         String sql = "SELECT * FROM customers";
         java.sql.PreparedStatement ps = conn.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery();

         while (rs.next()) {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));
            customer.setAddress(rs.getString("address"));
            
            list.add(customer);
         }
         
      } catch (Exception e) {
         e.printStackTrace();
      }
      return list;
   }

   public void getCustomerById(int id) {
      // Implementation to get a customer by ID from the database
   }

   public int getTotalCustomers() {
      int total = 0;
      try (Connection conn = DBConnection.getConnection()) {
         CallableStatement cstm = conn.prepareCall("{? = call fn_Total_Customers()}");
         cstm.registerOutParameter(1, Types.INTEGER);
         cstm.execute();

         total = cstm.getInt(1);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return total;
   }

   public static void main(String[] args) {
      CustomerDAO customerDAO = new CustomerDAO();
      List<Customer> customers = customerDAO.getAllCustomers();
      for (Customer customer : customers) {
         System.out.println(customer);
      }
      
      int totalCustomers = customerDAO.getTotalCustomers();
      System.out.println("Total Customers: " + totalCustomers);
   }
   
}