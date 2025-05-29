package model;

import java.sql.Timestamp;

/**
 * Model class representing a Review
 */
public class Review {
    private int reviewId;
    private int customerId;
    private int bookId;
    private int orderId;
    private int rating;
    private String title;
    private String content;
    private Timestamp createdAt;
    
    // Additional attributes for display purposes (not stored in the database)
    private String customerName;
    private String customerAvatarUrl;
    private String bookTitle;
    private String authorName;
    private String bookImageUrl;
    
    /**
     * Default constructor
     */
    public Review() {
    }
    
    /**
     * Parameterized constructor
     * 
     * @param reviewId Review ID
     * @param customerId Customer ID
     * @param bookId Book ID
     * @param orderId Order ID
     * @param rating Rating (1-5)
     * @param title Review title
     * @param content Review content
     */
    public Review(int reviewId, int customerId, int bookId, int orderId, int rating, String title, String content) {
        this.reviewId = reviewId;
        this.customerId = customerId;
        this.bookId = bookId;
        this.orderId = orderId;
        this.rating = rating;
        this.title = title;
        this.content = content;
    }
    
    /**
     * Get review ID
     * 
     * @return Review ID
     */
    public int getReviewId() {
        return reviewId;
    }
    
    /**
     * Set review ID
     * 
     * @param reviewId Review ID
     */
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    
    /**
     * Get customer ID
     * 
     * @return Customer ID
     */
    public int getCustomerId() {
        return customerId;
    }
    
    /**
     * Set customer ID
     * 
     * @param customerId Customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    /**
     * Get book ID
     * 
     * @return Book ID
     */
    public int getBookId() {
        return bookId;
    }
    
    /**
     * Set book ID
     * 
     * @param bookId Book ID
     */
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    /**
     * Get order ID
     * 
     * @return Order ID
     */
    public int getOrderId() {
        return orderId;
    }
    
    /**
     * Set order ID
     * 
     * @param orderId Order ID
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    /**
     * Get rating
     * 
     * @return Rating (1-5)
     */
    public int getRating() {
        return rating;
    }
    
    /**
     * Set rating
     * 
     * @param rating Rating (1-5)
     */
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    /**
     * Get review title
     * 
     * @return Review title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Set review title
     * 
     * @param title Review title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Get review content
     * 
     * @return Review content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * Set review content
     * 
     * @param content Review content
     */
    public void setContent(String content) {
        this.content = content;
    }
    
    /**
     * Get creation timestamp
     * 
     * @return Creation timestamp
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Set creation timestamp
     * 
     * @param createdAt Creation timestamp
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Get customer name (for display)
     * 
     * @return Customer name
     */
    public String getCustomerName() {
        return customerName;
    }
    
    /**
     * Set customer name (for display)
     * 
     * @param customerName Customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    /**
     * Get customer avatar URL (for display)
     * 
     * @return Customer avatar URL
     */
    public String getCustomerAvatarUrl() {
        return customerAvatarUrl;
    }
    
    /**
     * Set customer avatar URL (for display)
     * 
     * @param customerAvatarUrl Customer avatar URL
     */
    public void setCustomerAvatarUrl(String customerAvatarUrl) {
        this.customerAvatarUrl = customerAvatarUrl;
    }
    
    /**
     * Get book title (for display)
     * 
     * @return Book title
     */
    public String getBookTitle() {
        return bookTitle;
    }
    
    /**
     * Set book title (for display)
     * 
     * @param bookTitle Book title
     */
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    /**
     * Get author name (for display)
     * 
     * @return Author name
     */
    public String getAuthorName() {
        return authorName;
    }
    
    /**
     * Set author name (for display)
     * 
     * @param authorName Author name
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    /**
     * Get book image URL (for display)
     * 
     * @return Book image URL
     */
    public String getBookImageUrl() {
        return bookImageUrl;
    }
    
    /**
     * Set book image URL (for display)
     * 
     * @param bookImageUrl Book image URL
     */
    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }
} 