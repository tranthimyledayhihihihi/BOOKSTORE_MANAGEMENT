/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int bookId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    
    // For display purpose
    private String bookTitle;
    
    public OrderDetail() {
    }
    
    public OrderDetail(int orderDetailId, int orderId, int bookId, int quantity, BigDecimal unitPrice, BigDecimal discount) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    
    public BigDecimal getSubtotal() {
        BigDecimal discountAmount = unitPrice.multiply(discount).divide(new BigDecimal(100));
        BigDecimal discountedPrice = unitPrice.subtract(discountAmount);
        return discountedPrice.multiply(new BigDecimal(quantity));
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailId=" + orderDetailId +
                ", orderId=" + orderId +
                ", bookId=" + bookId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", discount=" + discount +
                ", bookTitle='" + bookTitle + '\'' +
                '}';
    }
} 