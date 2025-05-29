package BLL;

import dao.BookDAO;
import dao.CategoryDAO;
import model.Category;

import java.util.List;

public class CategoryBLL {
    private final CategoryDAO categoryDAO;
    private final BookDAO bookDAO;

    // Constructor mặc định
    public CategoryBLL() {
        this.categoryDAO = new CategoryDAO();
        this.bookDAO = new BookDAO();
    }

    // Constructor cho phép inject CategoryDAO và BookDAO (dùng để kiểm thử)
    public CategoryBLL(CategoryDAO categoryDAO, BookDAO bookDAO) {
        this.categoryDAO = categoryDAO;
        this.bookDAO = bookDAO;
    }

    /**
     * Lấy danh sách tất cả danh mục
     * @return Danh sách danh mục
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    /**
     * Lấy danh mục theo ID
     * @param categoryId ID danh mục
     * @return Danh mục nếu tìm thấy, null nếu không
     * @throws IllegalArgumentException nếu ID không hợp lệ
     */
    public Category getCategoryById(int categoryId) throws IllegalArgumentException {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        return categoryDAO.getCategoryById(categoryId);
    }

    /**
     * Thêm danh mục mới
     * @param category Thông tin danh mục
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu dữ liệu đầu vào không hợp lệ
     */
    public boolean addCategory(Category category) throws IllegalArgumentException {
        if (category == null) {
            throw new IllegalArgumentException("Thông tin danh mục không được null");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
        if (category.getDescription() != null && category.getDescription().length() > 1000) {
            throw new IllegalArgumentException("Mô tả không được vượt quá 1000 ký tự");
        }
        return categoryDAO.addCategory(category);
    }

    /**
     * Cập nhật danh mục
     * @param category Thông tin danh mục cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu dữ liệu đầu vào không hợp lệ
     */
    public boolean updateCategory(Category category) throws IllegalArgumentException {
        if (category == null || category.getCategoryId() <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
        if (category.getDescription() != null && category.getDescription().length() > 1000) {
            throw new IllegalArgumentException("Mô tả không được vượt quá 1000 ký tự");
        }
        return categoryDAO.updateCategory(category);
    }

    /**
     * Xóa danh mục
     * @param categoryId ID danh mục
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu ID không hợp lệ hoặc danh mục có sách liên quan
     */
    public boolean deleteCategory(int categoryId) throws IllegalArgumentException {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        // Kiểm tra xem danh mục có sách liên quan không
        Category category = categoryDAO.getCategoryById(categoryId);
        if (category != null && category.getBookCount() > 0) {
            throw new IllegalArgumentException("Không thể xóa danh mục vì có sách liên quan");
        }
        return categoryDAO.deleteCategory(categoryId);
    }
}