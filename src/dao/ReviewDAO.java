package dao;

import model.Review;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Review operations
 */
public class ReviewDAO {
    
    /**
     * Get count of reviews for a customer
     * 
     * @param customerId Customer ID
     * @return Count of reviews
     * @throws SQLException if database error occurs
     */
    public int getReviewCountByCustomer(int customerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE customer_id = ?";
        
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
     * Get reviews by customer
     * 
     * @param customerId Customer ID
     * @return List of reviews
     * @throws SQLException if database error occurs
     */
    public List<Review> getReviewsByCustomer(int customerId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, b.title, b.author_name, b.image_url " +
                    "FROM reviews r " +
                    "JOIN books b ON r.book_id = b.book_id " +
                    "WHERE r.customer_id = ? " +
                    "ORDER BY r.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Review review = mapResultSetToReview(rs);
                    review.setBookTitle(rs.getString("title"));
                    review.setAuthorName(rs.getString("author_name"));
                    review.setBookImageUrl(rs.getString("image_url"));
                    reviews.add(review);
                }
            }
        }
        
        return reviews;
    }
    
    /**
     * Get reviews by book
     * 
     * @param bookId Book ID
     * @param limit Maximum number of reviews to return
     * @return List of reviews
     * @throws SQLException if database error occurs
     */
    public List<Review> getReviewsByBook(int bookId, int limit) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT TOP(?) r.*, c.full_name, c.avatar_url " +
                    "FROM reviews r " +
                    "JOIN customers c ON r.customer_id = c.customer_id " +
                    "WHERE r.book_id = ? " +
                    "ORDER BY r.created_at DESC ";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            stmt.setInt(2, limit);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Review review = mapResultSetToReview(rs);
                    review.setCustomerName(rs.getString("full_name"));
                    review.setCustomerAvatarUrl(rs.getString("avatar_url"));
                    reviews.add(review);
                }
            }
        }
        
        return reviews;
    }
    
    /**
     * Add a new review
     * 
     * @param review Review to add
     * @return Generated review ID
     * @throws SQLException if database error occurs
     */
    public int addReview(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (customer_id, book_id, order_id, rating, title, content, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NOW())";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, review.getCustomerId());
            stmt.setInt(2, review.getBookId());
            stmt.setInt(3, review.getOrderId());
            stmt.setInt(4, review.getRating());
            stmt.setString(5, review.getTitle());
            stmt.setString(6, review.getContent());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating review failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating review failed, no ID obtained.");
                }
            }
        }
    }
    
    /**
     * Update book's average rating
     * 
     * @param bookId Book ID
     * @throws SQLException if database error occurs
     */
    public void updateBookAverageRating(int bookId) throws SQLException {
        String sql = "UPDATE books b SET " +
                    "b.average_rating = (SELECT AVG(rating) FROM reviews WHERE book_id = ?), " +
                    "b.review_count = (SELECT COUNT(*) FROM reviews WHERE book_id = ?) " +
                    "WHERE b.book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            stmt.setInt(2, bookId);
            stmt.setInt(3, bookId);
            
            stmt.executeUpdate();
        }
    }
    
    /**
     * Check if customer has already reviewed a book
     * 
     * @param customerId Customer ID
     * @param bookId Book ID
     * @return true if customer has already reviewed the book, false otherwise
     * @throws SQLException if database error occurs
     */
    public boolean hasCustomerReviewedBook(int customerId, int bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE customer_id = ? AND book_id = ?";
        
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
     * Delete a review
     * 
     * @param reviewId Review ID
     * @param customerId Customer ID (for security check)
     * @return Book ID of the deleted review
     * @throws SQLException if database error occurs
     */
    public int deleteReview(int reviewId, int customerId) throws SQLException {
        // First, get the book ID to update average rating later
        String selectSql = "SELECT book_id FROM reviews WHERE review_id = ? AND customer_id = ?";
        int bookId = -1;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            
            stmt.setInt(1, reviewId);
            stmt.setInt(2, customerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bookId = rs.getInt("book_id");
                } else {
                    return -1; // Review not found or doesn't belong to the customer
                }
            }
        }
        
        // Delete the review
        String deleteSql = "DELETE FROM reviews WHERE review_id = ? AND customer_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteSql)) {
            
            stmt.setInt(1, reviewId);
            stmt.setInt(2, customerId);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Update the book's average rating
                updateBookAverageRating(bookId);
                return bookId;
            }
        }
        
        return -1;
    }
    
    /**
     * Map ResultSet to Review object
     * 
     * @param rs ResultSet
     * @return Review object
     * @throws SQLException if database error occurs
     */
    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setCustomerId(rs.getInt("customer_id"));
        review.setBookId(rs.getInt("book_id"));
        review.setOrderId(rs.getInt("order_id"));
        review.setRating(rs.getInt("rating"));
        review.setTitle(rs.getString("title"));
        review.setContent(rs.getString("content"));
        review.setCreatedAt(rs.getTimestamp("created_at"));
        return review;
    }
} 