package dao;

import model.Book;
import model.Wishlist;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Wishlist operations
 */
public class WishlistDAO {
    
    /**
     * Get wishlist items for a customer
     * 
     * @param customerId Customer ID
     * @return List of books in wishlist
     * @throws SQLException if database error occurs
     */
    public List<Book> getWishlistByCustomer(int customerId) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b " +
                     "JOIN wishlists w ON b.book_id = w.book_id " +
                     "WHERE w.customer_id = ? " +
                     "ORDER BY w.added_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = mapBookFromResultSet(rs);
                    books.add(book);
                }
            }
        }
        
        return books;
    }
    
    /**
     * Map ResultSet to Book object
     * 
     * @param rs ResultSet containing book data
     * @return Book object
     * @throws SQLException if database error occurs
     */
    private Book mapBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthorId(rs.getInt("author_id"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setPublisherId(rs.getInt("publisher_id"));
        book.setIsbn(rs.getString("ISBN"));
        book.setPrice(rs.getBigDecimal("price"));
        book.setStockQuantity(rs.getInt("stock_quantity"));
        book.setPublicationDate(rs.getDate("publication_date"));
        book.setDescription(rs.getString("description"));
        book.setImageUrl(rs.getString("image_url"));
        
        return book;
    }
    
    /**
     * Add a book to wishlist
     * 
     * @param customerId Customer ID
     * @param bookId Book ID
     * @throws SQLException if database error occurs
     */
    public void addToWishlist(int customerId, int bookId) throws SQLException {
        String sql = "INSERT INTO wishlists (customer_id, book_id, added_date) " +
                     "VALUES (?, ?, NOW()) " +
                     "ON DUPLICATE KEY UPDATE added_date = NOW()";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, bookId);
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Remove a book from wishlist
     * 
     * @param customerId Customer ID
     * @param bookId Book ID
     * @throws SQLException if database error occurs
     */
    public void removeFromWishlist(int customerId, int bookId) throws SQLException {
        String sql = "DELETE FROM wishlists WHERE customer_id = ? AND book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, bookId);
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Check if a book is in customer's wishlist
     * 
     * @param customerId Customer ID
     * @param bookId Book ID
     * @return true if book is in wishlist, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean isInWishlist(int customerId, int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM wishlists WHERE customer_id = ? AND book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, bookId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get count of items in wishlist for a customer
     * 
     * @param customerId Customer ID
     * @return Count of wishlist items
     * @throws SQLException if database error occurs
     */
    public int getWishlistCountByCustomer(int customerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM wishlists WHERE customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        
        return 0;
    }
    
    /**
     * Clear all items from a customer's wishlist
     * 
     * @param customerId Customer ID
     * @throws SQLException if database error occurs
     */
    public void clearWishlist(int customerId) throws SQLException {
        String sql = "DELETE FROM wishlists WHERE customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            
            stmt.executeUpdate();
        }
    }
} 