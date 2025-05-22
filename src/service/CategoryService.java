package service;

import dao.CategoryDAO;
import model.Category;

import java.util.List;

/**
 * Lớp service xử lý logic nghiệp vụ liên quan đến danh mục sách
 */
public class CategoryService {
    private CategoryDAO categoryDAO;
    
    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }
    
    /**
     * Lấy danh sách tất cả danh mục
     */
    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }
    
    /**
     * Lấy thông tin chi tiết một danh mục
     */
    public Category getCategoryById(int categoryId) {
        return categoryDAO.getCategoryById(categoryId);
    }
    
    /**
     * Thêm danh mục mới
     */
    public boolean addCategory(Category category) {
        // Kiểm tra dữ liệu đầu vào
        if (category == null || category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }
        
        return categoryDAO.addCategory(category);
    }
    
    /**
     * Cập nhật thông tin danh mục
     */
    public boolean updateCategory(Category category) {
        // Kiểm tra dữ liệu đầu vào
        if (category == null || category.getCategoryId() <= 0 || 
            category.getName() == null || category.getName().trim().isEmpty()) {
            return false;
        }
        
        return categoryDAO.updateCategory(category);
    }
    
    /**
     * Xóa danh mục
     */
    public boolean deleteCategory(int categoryId) {
        if (categoryId <= 0) {
            return false;
        }
        
        return categoryDAO.deleteCategory(categoryId);
    }
} 