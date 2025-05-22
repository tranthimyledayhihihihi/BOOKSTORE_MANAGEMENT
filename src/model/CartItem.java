package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Đại diện cho một mục trong giỏ hàng
 */
public class CartItem {
    private int cartItemId;
    private int cartId;
    private int bookId;
    private int quantity;
    private Date addedDate;
    
    // Thông tin bổ sung từ join với Book
    private Book book;
    
    public CartItem() {
    }
    
    public CartItem(int bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }
    
    public CartItem(int cartId, int bookId, int quantity) {
        this.cartId = cartId;
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
    
    /**
     * Tính tổng giá tiền của item trong giỏ hàng
     * @return Tổng tiền = giá sách × số lượng
     */
    public BigDecimal getSubtotal() {
        if (book != null && book.getPrice() != null) {
            return book.getPrice().multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return book.getBookId() == cartItem.book.getBookId();
    }

    @Override
    public int hashCode() {
        return book.getBookId();
    }
} 