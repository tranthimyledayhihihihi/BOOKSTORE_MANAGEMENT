package service;

import dao.BookDAO;
import dao.CartDAO;
import model.Book;
import model.Cart;
import model.CartItem;
import model.CartSummary;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Service để quản lý giỏ hàng
 */
public class CartService {
    private CartDAO cartDAO;
    private BookDAO bookDAO;
    
    public CartService() {
        this.cartDAO = new CartDAO();
        this.bookDAO = new BookDAO();
    }
    
    /**
     * Lấy giỏ hàng theo customer ID
     * @param customerId ID khách hàng
     * @return Giỏ hàng
     */
    public Cart getCartByCustomerId(int customerId) {
        return cartDAO.getCartByCustomerId(customerId);
    }
    
    /**
     * Tạo giỏ hàng mới cho khách hàng
     * @param customerId ID khách hàng
     * @return Giỏ hàng mới
     */
    public Cart createNewCart(int customerId) {
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        // Lưu cart vào database và lấy cart ID
        int cartId = cartDAO.createCart(customerId);
        cart.setCartId(cartId);
        return cart;
    }
    
    /**
     * Thêm sách vào giỏ hàng
     * @param customerId ID khách hàng
     * @param bookId ID của sách
     * @param quantity Số lượng
     * @return true nếu thêm thành công
     */
    public boolean addToCart(int customerId, int bookId, int quantity) {
        // Kiểm tra số lượng
        if (quantity <= 0) {
            return false;
        }
        
        // Lấy thông tin sách
        Book book = bookDAO.getBookById(bookId);
        if (book == null || book.getStockQuantity() < quantity) {
            return false;
        }
        
        // Lấy giỏ hàng của khách hàng
        Cart cart = cartDAO.getCartByCustomerId(customerId);
        if (cart == null) {
            cart = createNewCart(customerId);
        }
        
        // Thêm item vào giỏ hàng
        CartItem item = new CartItem(cart.getCartId(), bookId, quantity);
        return cartDAO.addItemToCart(item);
    }
    
    /**
     * Cập nhật số lượng mục trong giỏ hàng
     * @param customerId ID khách hàng
     * @param bookId ID của sách
     * @param quantity Số lượng mới
     * @return true nếu cập nhật thành công
     */
    public boolean updateCartItem(int customerId, int bookId, int quantity) {
        // Kiểm tra số lượng
        if (quantity <= 0) {
            return removeFromCart(customerId, bookId);
        }
        
        // Lấy thông tin sách
        Book book = bookDAO.getBookById(bookId);
        if (book == null || book.getStockQuantity() < quantity) {
            return false;
        }
        
        // Lấy giỏ hàng và cập nhật
        Cart cart = cartDAO.getCartByCustomerId(customerId);
        if (cart != null) {
            CartItem item = cartDAO.getCartItemByBookId(cart.getCartId(), bookId);
            if (item != null) {
                return cartDAO.updateCartItemQuantity(item.getCartItemId(), quantity);
            }
        }
        return false;
    }
    
    /**
     * Xóa mục khỏi giỏ hàng
     * @param customerId ID khách hàng
     * @param bookId ID của sách cần xóa
     * @return true nếu xóa thành công
     */
    public boolean removeFromCart(int customerId, int bookId) {
        Cart cart = cartDAO.getCartByCustomerId(customerId);
        if (cart != null) {
            CartItem item = cartDAO.getCartItemByBookId(cart.getCartId(), bookId);
            if (item != null) {
                return cartDAO.removeCartItem(item.getCartItemId());
            }
        }
        return false;
    }
    
    /**
     * Xóa toàn bộ giỏ hàng
     * @param customerId ID khách hàng
     * @return true nếu xóa thành công
     */
    public boolean clearCart(int customerId) {
        Cart cart = cartDAO.getCartByCustomerId(customerId);
        if (cart != null) {
            return cartDAO.clearCart(cart.getCartId());
        }
        return false;
    }
    
    /**
     * Lấy tất cả items trong giỏ hàng
     * @param customerId ID khách hàng
     * @return Danh sách CartItem
     */
    public List<CartItem> getCartItems(int customerId) {
        Cart cart = cartDAO.getCartByCustomerId(customerId);
        if (cart != null) {
            return cartDAO.getCartItems(cart.getCartId());
        }
        return new ArrayList<>();
    }
    
    /**
     * Đếm tổng số items trong giỏ hàng
     * @param customerId ID khách hàng
     * @return Tổng số items
     */
    public int getTotalItemsCount(int customerId) {
        List<CartItem> items = getCartItems(customerId);
        int total = 0;
        for (CartItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }
    
    /**
     * Tính tổng giá trị giỏ hàng
     * @param customerId ID khách hàng
     * @return Tổng giá trị
     */
    public BigDecimal getCartTotal(int customerId) {
        List<CartItem> items = getCartItems(customerId);
        BigDecimal total = BigDecimal.ZERO;
        
        for (CartItem item : items) {
            Book book = bookDAO.getBookById(item.getBookId());
            if (book != null) {
                BigDecimal itemTotal = book.getPrice().multiply(new BigDecimal(item.getQuantity()));
                total = total.add(itemTotal);
            }
        }
        
        return total;
    }
    
    /**
     * Tạo đối tượng CartSummary để hiển thị trên trang giỏ hàng
     * @param customerId ID khách hàng
     * @return Đối tượng CartSummary
     */
    public CartSummary createCartSummary(int customerId) {
        CartSummary summary = new CartSummary();
        
        // Lấy thông tin giỏ hàng
        List<CartItem> items = getCartItems(customerId);
        summary.setTotalItems(getTotalItemsCount(customerId));
        
        BigDecimal subtotal = getCartTotal(customerId);
        summary.setSubtotal(subtotal);
        
        // Tính phí vận chuyển (ví dụ: 30.000đ nếu tổng đơn hàng < 200.000đ, miễn phí nếu >= 200.000đ)
        BigDecimal shippingFee = subtotal.compareTo(new BigDecimal(200000)) < 0 ? 
                                  new BigDecimal(30000) : BigDecimal.ZERO;
        summary.setShippingFee(shippingFee);
        
        // Áp dụng giảm giá nếu có
        BigDecimal discount = BigDecimal.ZERO;
        summary.setDiscount(discount);
        
        // Tính tổng cộng
        BigDecimal total = subtotal.add(shippingFee).subtract(discount);
        summary.setTotal(total);
        
        return summary;
    }
    
    /**
     * Kiểm tra xem sách có trong giỏ hàng không
     * @param customerId ID khách hàng
     * @param bookId ID sách
     * @return true nếu có trong giỏ hàng
     */
    public boolean isBookInCart(int customerId, int bookId) {
        Cart cart = cartDAO.getCartByCustomerId(customerId);
        if (cart != null) {
            CartItem item = cartDAO.getCartItemByBookId(cart.getCartId(), bookId);
            return item != null;
        }
        return false;
    }
    
    /**
     * Lấy số lượng của một sách trong giỏ hàng
     * @param customerId ID khách hàng
     * @param bookId ID sách
     * @return Số lượng sách trong giỏ hàng
     */
    public int getBookQuantityInCart(int customerId, int bookId) {
        Cart cart = cartDAO.getCartByCustomerId(customerId);
        if (cart != null) {
            CartItem item = cartDAO.getCartItemByBookId(cart.getCartId(), bookId);
            if (item != null) {
                return item.getQuantity();
            }
        }
        return 0;
    }
    
    /**
     * Kiểm tra tính hợp lệ của giỏ hàng (stock availability)
     * @param customerId ID khách hàng
     * @return Map chứa thông tin lỗi nếu có
     */
    public Map<String, String> validateCart(int customerId) {
        Map<String, String> errors = new HashMap<>();
        List<CartItem> items = getCartItems(customerId);
        
        for (CartItem item : items) {
            Book book = bookDAO.getBookById(item.getBookId());
            if (book == null) {
                errors.put("book_" + item.getBookId(), "Sách không tồn tại");
            } else if (book.getStockQuantity() < item.getQuantity()) {
                errors.put("book_" + item.getBookId(), 
                    "Chỉ còn " + book.getStockQuantity() + " cuốn trong kho");
            }
        }
        
        return errors;
    }
}