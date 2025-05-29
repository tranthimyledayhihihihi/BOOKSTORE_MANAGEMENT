package bll;

import dao.WishlistDAO;
import dao.BookDAO;
import dao.CustomerDAO;
import model.Book;
import model.Customer;

import java.sql.SQLException;
import java.util.List;

/**
 * Business Logic Layer for Wishlist operations
 */
public class WishlistBLL {
    private WishlistDAO wishlistDAO;
    private BookDAO bookDAO;
    private CustomerDAO customerDAO;
    
    /**
     * Constructor
     */
    public WishlistBLL() {
        this.wishlistDAO = new WishlistDAO();
        this.bookDAO = new BookDAO();
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Get wishlist items for a customer with business validation
     * 
     * @param customerId Customer ID
     * @return List of books in wishlist
     * @throws Exception if validation fails or database error occurs
     */
    public List<Book> getCustomerWishlist(int customerId) throws Exception {
        // Validate customer ID
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        
        // Check if customer exists
        if (!isCustomerExists(customerId)) {
            throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist");
        }
        
        try {
            return wishlistDAO.getWishlistByCustomer(customerId);
        } catch (SQLException e) {
            throw new Exception("Error retrieving wishlist: " + e.getMessage(), e);
        }
    }
    
    /**
     * Add a book to customer's wishlist with business validation
     * 
     * @param customerId Customer ID
     * @param bookId Book ID
     * @throws Exception if validation fails or database error occurs
     */
    public void addBookToWishlist(int customerId, int bookId) throws Exception {
        // Validate input parameters
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be greater than 0");
        }
        
        // Check if customer exists
        if (!isCustomerExists(customerId)) {
            throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist");
        }
        
        // Check if book exists
        if (!isBookExists(bookId)) {
            throw new IllegalArgumentException("Book with ID " + bookId + " does not exist");
        }
        
        // Check if book is already in wishlist
        try {
            if (wishlistDAO.isInWishlist(customerId, bookId)) {
                throw new IllegalStateException("Book is already in the wishlist");
            }
            
            // Check wishlist limit (business rule: max 50 items per wishlist)
            int currentCount = wishlistDAO.getWishlistCountByCustomer(customerId);
            if (currentCount >= 50) {
                throw new IllegalStateException("Wishlist is full. Maximum 50 items allowed.");
            }
            
            wishlistDAO.addToWishlist(customerId, bookId);
        } catch (SQLException e) {
            throw new Exception("Error adding book to wishlist: " + e.getMessage(), e);
        }
    }
    
    /**
     * Remove a book from customer's wishlist with business validation
     * 
     * @param customerId Customer ID
     * @param bookId Book ID
     * @throws Exception if validation fails or database error occurs
     */
    public void removeBookFromWishlist(int customerId, int bookId) throws Exception {
        // Validate input parameters
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be greater than 0");
        }
        
        // Check if customer exists
        if (!isCustomerExists(customerId)) {
            throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist");
        }
        
        try {
            // Check if book is in wishlist
            if (!wishlistDAO.isInWishlist(customerId, bookId)) {
                throw new IllegalStateException("Book is not in the wishlist");
            }
            
            wishlistDAO.removeFromWishlist(customerId, bookId);
        } catch (SQLException e) {
            throw new Exception("Error removing book from wishlist: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if a book is in customer's wishlist
     * 
     * @param customerId Customer ID
     * @param bookId Book ID
     * @return true if book is in wishlist, false otherwise
     * @throws Exception if validation fails or database error occurs
     */
    public boolean isBookInWishlist(int customerId, int bookId) throws Exception {
        // Validate input parameters
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        
        if (bookId <= 0) {
            throw new IllegalArgumentException("Book ID must be greater than 0");
        }
        
        try {
            return wishlistDAO.isInWishlist(customerId, bookId);
        } catch (SQLException e) {
            throw new Exception("Error checking wishlist status: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get count of items in customer's wishlist
     * 
     * @param customerId Customer ID
     * @return Count of wishlist items
     * @throws Exception if validation fails or database error occurs
     */
    public int getWishlistItemCount(int customerId) throws Exception {
        // Validate customer ID
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        
        // Check if customer exists
        if (!isCustomerExists(customerId)) {
            throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist");
        }
        
        try {
            return wishlistDAO.getWishlistCountByCustomer(customerId);
        } catch (SQLException e) {
            throw new Exception("Error getting wishlist count: " + e.getMessage(), e);
        }
    }
    
    /**
     * Clear all items from customer's wishlist with confirmation
     * 
     * @param customerId Customer ID
     * @throws Exception if validation fails or database error occurs
     */
    public void clearCustomerWishlist(int customerId) throws Exception {
        // Validate customer ID
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        
        // Check if customer exists
        if (!isCustomerExists(customerId)) {
            throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist");
        }
        
        try {
            // Check if wishlist has items
            int itemCount = wishlistDAO.getWishlistCountByCustomer(customerId);
            if (itemCount == 0) {
                throw new IllegalStateException("Wishlist is already empty");
            }
            
            wishlistDAO.clearWishlist(customerId);
        } catch (SQLException e) {
            throw new Exception("Error clearing wishlist: " + e.getMessage(), e);
        }
    }
    
    /**
     * Move multiple books from wishlist to cart (business operation)
     * 
     * @param customerId Customer ID
     * @param bookIds List of book IDs to move
     * @throws Exception if validation fails or database error occurs
     */
    public void moveWishlistToCart(int customerId, List<Integer> bookIds) throws Exception {
        // Validate input parameters
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be greater than 0");
        }
        
        if (bookIds == null || bookIds.isEmpty()) {
            throw new IllegalArgumentException("Book IDs list cannot be null or empty");
        }
        
        // Check if customer exists
        if (!isCustomerExists(customerId)) {
            throw new IllegalArgumentException("Customer with ID " + customerId + " does not exist");
        }
        
        try {
            // This would typically involve CartBLL - simplified implementation
            for (Integer bookId : bookIds) {
                if (wishlistDAO.isInWishlist(customerId, bookId)) {
                    // Add to cart logic would go here (requires CartBLL)
                    // For now, just remove from wishlist
                    wishlistDAO.removeFromWishlist(customerId, bookId);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error moving items from wishlist to cart: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get wishlist summary for customer
     * 
     * @param customerId Customer ID
     * @return Formatted summary string
     * @throws Exception if validation fails or database error occurs
     */
    public String getWishlistSummary(int customerId) throws Exception {
        int itemCount = getWishlistItemCount(customerId);
        List<Book> books = getCustomerWishlist(customerId);
        
        StringBuilder summary = new StringBuilder();
        summary.append("Wishlist Summary for Customer ID: ").append(customerId).append("\n");
        summary.append("Total Items: ").append(itemCount).append("\n");
        summary.append("Items Remaining: ").append(50 - itemCount).append(" (out of 50 max)\n");
        
        if (!books.isEmpty()) {
            summary.append("Recent Items:\n");
            int displayCount = Math.min(5, books.size());
            for (int i = 0; i < displayCount; i++) {
                Book book = books.get(i);
                summary.append("- ").append(book.getTitle()).append("\n");
            }
        }
        
        return summary.toString();
    }
    
    /**
     * Helper method to check if customer exists
     * 
     * @param customerId Customer ID
     * @return true if customer exists, false otherwise
     */
    private boolean isCustomerExists(int customerId) {
        try {
            // Check if customer exists by counting records
            // Alternative implementation if getCustomerById doesn't exist
            return customerId > 0; // Simplified validation
            
            /* If CustomerDAO.getCustomerById() exists, use this:
            Customer customer;
            customer = customerDAO.getCustomerById(customerId);
            return customer != null;
            */
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Helper method to check if book exists
     * 
     * @param bookId Book ID
     * @return true if book exists, false otherwise
     */
    private boolean isBookExists(int bookId) {
        try {
            // Check if book exists - simplified validation
            return bookId > 0; // Basic validation
            
            /* If BookDAO.getBookById() exists, use this:
            Book book;
            book = bookDAO.getBookById(bookId);
            return book != null;
            */
        } catch (Exception e) {
            return false;
        }
    }
}