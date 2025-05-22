// Lớp InvoiceDAO.java
package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Invoice;
import model.InvoiceItem;
import util.DBConnection;

public class InvoiceDAO {
   
   public Invoice getInvoiceById(String invoiceId) {
      Invoice invoice = null;
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
            conn = DBConnection.getConnection();
            
            // Lấy thông tin hóa đơn
            String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, invoiceId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
               invoice = new Invoice();
               invoice.setInvoiceId(rs.getString("invoice_id"));
               invoice.setCustomerName(rs.getString("customer_name"));
               invoice.setCustomerPhone(rs.getString("customer_phone"));
               invoice.setCreatedDate(rs.getTimestamp("created_date"));
               
               // Lấy các mục trong hóa đơn
               List<InvoiceItem> items = getInvoiceItems(conn, invoiceId);
               invoice.setItems(items);
            }
            
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         // DBConnection.closeResultSet(rs);
         // DBConnection.closeStatement(pstmt);
         // DBConnection.closeConnection(conn);
      }
      
      return invoice;
   }
   
   private List<InvoiceItem> getInvoiceItems(Connection conn, String invoiceId) throws SQLException {
      List<InvoiceItem> items = new ArrayList<>();
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
            String sql = "SELECT i.*, b.title, b.author FROM invoice_items i "
                     + "JOIN books b ON i.book_id = b.id "
                     + "WHERE i.invoice_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, invoiceId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
               InvoiceItem item = new InvoiceItem();
               item.setId(rs.getInt("id"));
               item.setInvoiceId(rs.getString("invoice_id"));
               item.setBookId(rs.getInt("book_id"));
               item.setBookTitle(rs.getString("title"));
               item.setBookAuthor(rs.getString("author"));
               item.setQuantity(rs.getInt("quantity"));
               item.setPrice(rs.getDouble("price"));
               
               items.add(item);
            }
            
      } finally {
         // DBConnection.closeResultSet(rs);
         // DBConnection.closeStatement(pstmt);
      }
      
      return items;
   }

   public List<Invoice> getAllInvoices() {
      List<Invoice> invoices = new ArrayList<>();
      Connection conn = null;
      PreparedStatement pstmt = null;
      ResultSet rs = null;
      
      try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM invoices";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
               Invoice invoice = new Invoice();
               invoice.setInvoiceId(rs.getString("invoice_id"));
               invoice.setCustomerName(rs.getString("customer_name"));
               invoice.setCustomerPhone(rs.getString("customer_phone"));
               invoice.setCreatedDate(rs.getTimestamp("created_date"));
               
               invoices.add(invoice);
            }
            
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         // DBConnection.closeResultSet(rs);
         // DBConnection.closeStatement(pstmt);
         // DBConnection.closeConnection(conn);
      }
      
      return invoices;
   }
   
   // Các phương thức khác như createInvoice, updateInvoice, deleteInvoice, ...
}