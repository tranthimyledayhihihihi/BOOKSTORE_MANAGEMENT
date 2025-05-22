package model;

import java.math.BigDecimal;

public class OrderItem {
    private int id;
    private Book book;
    private int quantity;
    private BigDecimal price;
    
    public OrderItem() {
    }
    
    public OrderItem(int id, Book book, int quantity, BigDecimal price) {
        this.id = id;
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getSubtotal() {
        return price.multiply(new BigDecimal(quantity));
    }
} 