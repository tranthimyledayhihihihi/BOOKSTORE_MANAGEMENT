/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import util.DBConnection;

public class CategoryDAO {
    
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT \r\n" + //
                    "    CATEGORIES.category_id, \r\n" + //
                    "    CATEGORIES.name, \r\n" + //
                    "    COUNT(b.category_id) AS book_count\r\n" + //
                    "FROM \r\n" + //
                    "    CATEGORIES\r\n" + //
                    "JOIN \r\n" + //
                    "    BOOKS b ON b.category_id = CATEGORIES.category_id\r\n" + //
                    "GROUP BY \r\n" + //
                    "    CATEGORIES.category_id, \r\n" + //
                    "    CATEGORIES.name\r\n" + //
                    "ORDER BY \r\n" + //
                    "    CATEGORIES.category_id ASC;";
        
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Category category = mapResultSetToCategory(rs);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categories;
    }
    
    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM CATEGORIES WHERE category_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO CATEGORIES (name, description) VALUES (?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setCategoryId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateCategory(Category category) {
        String sql = "UPDATE CATEGORIES SET name = ?, description = ? WHERE category_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getCategoryId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM CATEGORIES WHERE category_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setName(rs.getString("name"));
        category.setBookCount(rs.getInt("book_count"));
        return category;
    }

    public static void main(String[] args) {
        
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAllCategories();
        for (Category category : categories) {
            System.out.println(category.toString());
        }
    }
} 
