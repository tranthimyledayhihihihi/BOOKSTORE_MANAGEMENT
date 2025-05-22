package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Đại diện cho giỏ hàng của người dùng
 */
public class Cart {
    private int cartId;
    private int customerId;
    private Date createdDate;
    private Date lastModified;
    
    private List<CartItem> items = new ArrayList<>();
    
    public Cart() {
    }
    
    public Cart(int customerId) {
        this.customerId = customerId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    
    /**
     * Thêm một mục vào giỏ hàng
     * @param item Mục cần thêm
     */
    public void addItem(CartItem item) {
        // Kiểm tra nếu sách đã có trong giỏ hàng
        for (CartItem existingItem : items) {
            if (existingItem.getBookId() == item.getBookId()) {
                // Cập nhật số lượng thay vì thêm mới
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        // Nếu chưa có, thêm mới vào giỏ hàng
        items.add(item);
    }
    
    /**
     * Cập nhật số lượng của một mục trong giỏ hàng
     * @param bookId ID của sách cần cập nhật
     * @param quantity Số lượng mới
     * @return true nếu cập nhật thành công, false nếu không tìm thấy
     */
    public boolean updateItemQuantity(int bookId, int quantity) {
        for (CartItem item : items) {
            if (item.getBookId() == bookId) {
                item.setQuantity(quantity);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Xóa một mục khỏi giỏ hàng
     * @param bookId ID của sách cần xóa
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    public boolean removeItem(int bookId) {
        return items.removeIf(item -> item.getBookId() == bookId);
    }
    
    /**
     * Xóa tất cả mục trong giỏ hàng
     */
    public void clear() {
        items.clear();
    }
    
    /**
     * Tính tổng số lượng sách trong giỏ hàng
     * @return Tổng số lượng sách
     */
    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
    
    /**
     * Tính tổng tiền của giỏ hàng
     * @return Tổng tiền
     */
    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 