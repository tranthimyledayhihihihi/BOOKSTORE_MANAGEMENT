package BLL;

import dao.BookDAO;
import dao.CartDAO;
import model.Book;
import model.Cart;
import model.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CartBLL {
    private final CartDAO cartDAO;
    private final BookDAO bookDAO;

    // Constructor mặc định
    public CartBLL() {
        this.cartDAO = new CartDAO();
        this.bookDAO = new BookDAO();
    }

    // Constructor cho phép inject CartDAO và BookDAO (dùng để kiểm thử)
    public CartBLL(CartDAO cartDAO, BookDAO bookDAO) {
        this.cartDAO = cartDAO;
        this.bookDAO = bookDAO;
    }

    /**
     * Lấy giỏ hàng theo ID khách hàng
     * @param customerId ID khách hàng
     * @return Giỏ hàng nếu tìm thấy, null nếu không
     * @throws IllegalArgumentException nếu ID khách hàng không hợp lệ
     */
    public Cart getCartByCustomerId(int customerId) throws IllegalArgumentException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        Cart cart = cartDAO.getCartByCustomerId(customerId);
        if (cart == null) {
            // Tạo giỏ hàng mới nếu không tìm thấy
            cart = new Cart();
            cart.setCustomerId(customerId);
            if (cartDAO.createCart(cart)) {
                return cartDAO.getCartByCustomerId(customerId);
            }
        }
        return cart;
    }

    /**
     * Tạo giỏ hàng mới
     * @param customerId ID khách hàng
     * @return true nếu tạo thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu ID khách hàng không hợp lệ
     */
    public boolean createCart(int customerId) throws IllegalArgumentException {
        if (customerId <= 0) {
            throw new IllegalArgumentException("ID khách hàng không hợp lệ");
        }
        return cartDAO.createCart(customerId) != -1;
    }

    /**
     * Thêm một mục vào giỏ hàng
     * @param cartItem Mục cần thêm
     * @return true nếu thêm thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu dữ liệu đầu vào không hợp lệ
     */
    public boolean addItemToCart(CartItem cartItem) throws IllegalArgumentException {
        if (cartItem == null) {
            throw new IllegalArgumentException("Thông tin mục giỏ hàng không được null");
        }
        if (cartItem.getCartId() <= 0) {
            throw new IllegalArgumentException("ID giỏ hàng không hợp lệ");
        }
        if (cartItem.getBookId() <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        if (cartItem.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        // Kiểm tra xem sách có tồn tại và đủ số lượng tồn kho
        Book book = bookDAO.getBookById(cartItem.getBookId());
        if (book == null) {
            throw new IllegalArgumentException("Sách không tồn tại");
        }
        if (book.getStockQuantity() < cartItem.getQuantity()) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ");
        }

        return cartDAO.addItemToCart(cartItem);
    }

    /**
     * Cập nhật số lượng của một mục trong giỏ hàng
     * @param cartItemId ID của mục cần cập nhật
     * @param quantity Số lượng mới
     * @return true nếu cập nhật thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu dữ liệu đầu vào không hợp lệ
     */
    public boolean updateCartItemQuantity(int cartItemId, int quantity) throws IllegalArgumentException {
        if (cartItemId <= 0) {
            throw new IllegalArgumentException("ID mục giỏ hàng không hợp lệ");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        // Kiểm tra số lượng tồn kho
        CartItem existingItem = getCartItemById(cartItemId);
        if (existingItem == null) {
            throw new IllegalArgumentException("Mục giỏ hàng không tồn tại");
        }
        Book book = bookDAO.getBookById(existingItem.getBookId());
        if (book == null) {
            throw new IllegalArgumentException("Sách không tồn tại");
        }
        if (book.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ");
        }

        return cartDAO.updateCartItemQuantity(cartItemId, quantity);
    }

    /**
     * Xóa một mục khỏi giỏ hàng
     * @param cartItemId ID của mục cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu ID không hợp lệ
     */
    public boolean removeCartItem(int cartItemId) throws IllegalArgumentException {
        if (cartItemId <= 0) {
            throw new IllegalArgumentException("ID mục giỏ hàng không hợp lệ");
        }
        return cartDAO.removeCartItem(cartItemId);
    }

    /**
     * Xóa tất cả mục trong giỏ hàng
     * @param cartId ID của giỏ hàng
     * @return true nếu xóa thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu ID giỏ hàng không hợp lệ
     */
    public boolean clearCart(int cartId) throws IllegalArgumentException {
        if (cartId <= 0) {
            throw new IllegalArgumentException("ID giỏ hàng không hợp lệ");
        }
        return cartDAO.clearCart(cartId);
    }

    /**
     * Lấy danh sách các mục trong giỏ hàng
     * @param cartId ID của giỏ hàng
     * @return Danh sách các mục trong giỏ hàng
     * @throws IllegalArgumentException nếu ID giỏ hàng không hợp lệ
     */
    public List<CartItem> getCartItems(int cartId) throws IllegalArgumentException {
        if (cartId <= 0) {
            throw new IllegalArgumentException("ID giỏ hàng không hợp lệ");
        }
        return cartDAO.getCartItems(cartId);
    }

    /**
     * Lấy mục trong giỏ hàng theo ID sách
     * @param cartId ID của giỏ hàng
     * @param bookId ID của sách
     * @return Mục trong giỏ hàng, null nếu không tìm thấy
     * @throws IllegalArgumentException nếu ID không hợp lệ
     */
    public CartItem getCartItemByBookId(int cartId, int bookId) throws IllegalArgumentException {
        if (cartId <= 0) {
            throw new IllegalArgumentException("ID giỏ hàng không hợp lệ");
        }
        if (bookId <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        return cartDAO.getCartItemByBookId(cartId, bookId);
    }

    /**
     * Đếm tổng số mục trong giỏ hàng
     * @param cartId ID của giỏ hàng
     * @return Tổng số mục
     * @throws IllegalArgumentException nếu ID giỏ hàng không hợp lệ
     */
    public int getTotalItemsCount(int cartId) throws IllegalArgumentException {
        if (cartId <= 0) {
            throw new IllegalArgumentException("ID giỏ hàng không hợp lệ");
        }
        return cartDAO.getTotalItemsCount(cartId);
    }

    /**
     * Kiểm tra xem giỏ hàng có mục nào không
     * @param cartId ID của giỏ hàng
     * @return true nếu giỏ hàng có mục
     * @throws IllegalArgumentException nếu ID giỏ hàng không hợp lệ
     */
    public boolean hasItems(int cartId) throws IllegalArgumentException {
        if (cartId <= 0) {
            throw new IllegalArgumentException("ID giỏ hàng không hợp lệ");
        }
        return cartDAO.hasItems(cartId);
    }

    /**
     * Lấy mục trong giỏ hàng theo ID mục
     * @param cartItemId ID của mục giỏ hàng
     * @return Mục giỏ hàng, null nếu không tìm thấy
     * @throws IllegalArgumentException nếu ID không hợp lệ
     */
    private CartItem getCartItemById(int cartItemId) throws IllegalArgumentException {
        if (cartItemId <= 0) {
            throw new IllegalArgumentException("ID mục giỏ hàng không hợp lệ");
        }
        String sql = "SELECT * FROM CART_ITEMS WHERE cart_item_id = ?";
        try (Connection conn = util.DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartItemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem();
                    item.setCartItemId(rs.getInt("cart_item_id"));
                    item.setCartId(rs.getInt("cart_id"));
                    item.setBookId(rs.getInt("book_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setAddedDate(rs.getTimestamp("added_date"));
                    // Lấy thông tin sách từ bookDAO
                    Book book = bookDAO.getBookById(item.getBookId());
                    if (book != null) {
                        item.setBook(book);
                    } else {
                        item.setBook(null); // Hoặc xử lý theo logic của bạn
                    }
                    return item;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}