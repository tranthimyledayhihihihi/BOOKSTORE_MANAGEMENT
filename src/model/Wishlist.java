package model;

import java.sql.Timestamp;

/**
 * Model class representing a Wishlist item
 */
public class Wishlist {
    private int wishlistId;
    private int customerId;
    private int bookId;
    private Timestamp addedDate;
    
    // Additional fields for display
    private Book book;
    
    /**
     * Default constructor
     */
    public Wishlist() {
    }
    
    /**
     * Parameterized constructor
     * 
     * @param wishlistId Wishlist ID
     * @param customerId Customer ID
     * @param bookId Book ID
     * @param addedDate Date added to wishlist
     */
    public Wishlist(int wishlistId, int customerId, int bookId, Timestamp addedDate) {
        this.wishlistId = wishlistId;
        this.customerId = customerId;
        this.bookId = bookId;
        this.addedDate = addedDate;
    }
    
    /**
     * Get wishlist ID
     * 
     * @return Wishlist ID
     */
    public int getWishlistId() {
        return wishlistId;
    }
    
    /**
     * Set wishlist ID
     * 
     * @param wishlistId Wishlist ID
     */
    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
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
     * Get date added to wishlist
     * 
     * @return Date added to wishlist
     */
    public Timestamp getAddedDate() {
        return addedDate;
    }
    
    /**
     * Set date added to wishlist
     * 
     * @param addedDate Date added to wishlist
     */
    public void setAddedDate(Timestamp addedDate) {
        this.addedDate = addedDate;
    }
    
    /**
     * Get book object
     * 
     * @return Book object
     */
    public Book getBook() {
        return book;
    }
    
    /**
     * Set book object
     * 
     * @param book Book object
     */
    public void setBook(Book book) {
        this.book = book;
    }
} 