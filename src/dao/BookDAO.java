/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import util.DBConnection;

public class BookDAO {
    
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.*, a.name as author_name, c.name as category_name, p.name as publisher_name, " +
                     "COALESCE(AVG(CAST(r.rating AS FLOAT)), 0) AS avg_rating, " +
                     "COUNT(r.review_id) AS review_count " +
                     "FROM BOOKS b " +
                     "LEFT JOIN AUTHORS a ON b.author_id = a.author_id " +
                     "LEFT JOIN CATEGORIES c ON b.category_id = c.category_id " +
                     "LEFT JOIN PUBLISHERS p ON b.publisher_id = p.publisher_id " +
                     "LEFT JOIN REVIEWS r ON b.book_id = r.book_id " +
                     "GROUP BY b.book_id, b.title, b.author_id, b.category_id, b.publisher_id, " +
                     "b.ISBN, b.price, b.stock_quantity, b.publication_date, b.description, " +
                     "b.image_url, a.name, c.name, p.name " +
                     "ORDER BY b.book_id";
        
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }

    public List<Book> getBookByParemeters(String keywork, Integer cateloryId, Integer authorId) {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {

            CallableStatement cstm = conn.prepareCall("{call pr_search_books(?, ?, ?)}");
            cstm.setString(1, keywork);
            cstm.setInt(2, cateloryId);
            cstm.setInt(3, authorId);
            ResultSet rs = cstm.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setImageUrl(rs.getString("image_url"));
                book.setTitle(rs.getString("title"));
                book.setAuthorName(rs.getString("author_name"));
                book.setCategoryName(rs.getString("category_name"));
                book.setPrice(rs.getBigDecimal("price"));
                book.setStockQuantity(rs.getInt("stock_quantity"));

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }

    public List<Book> getBestSellBooks () {
        List<Book> books = new ArrayList<>();   

        try (Connection conn = DBConnection.getConnection()) {
            CallableStatement cstm = conn.prepareCall("{call pr_Best_Selling_Books()}");
            ResultSet rs = cstm.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorName(rs.getString("author"));
                book.setCategoryName(rs.getString("category"));
                book.setPrice(rs.getBigDecimal("price"));
                book.setTotalSold(rs.getInt("total_sold"));

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }

    public List<Book> getLowStockBooks() {
        List<Book> books = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            
            CallableStatement cstm = conn.prepareCall("{call pr_Low_Stock_Books}");
            ResultSet rs = cstm.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthorName(rs.getString("author"));
                book.setCategoryName(rs.getString("category"));
                book.setStockQuantity(rs.getInt("stock_quantity"));

                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public int getTotalBooks() {
        int totalBooks = 0;
        
        try (Connection conn = DBConnection.getConnection()) {
            CallableStatement cstm = conn.prepareCall("{? = call fn_Total_Books()}");
            cstm.registerOutParameter(1, Types.INTEGER);
            cstm.execute();

            totalBooks = cstm.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return totalBooks;
    }

    public static void main(String[] args) {
        
        BookDAO bookDAO = new BookDAO();
        if(bookDAO.deleteBook(1)) {
            System.out.println("Delete book success!");
        } else {
            System.out.println("Delete book failed!");
        }
    }
    
    public Book getBookById(int bookId) {
        String sql = "SELECT b.*, a.name as author_name, c.name as category_name, p.name as publisher_name, " +
                     "COALESCE(AVG(CAST(r.rating AS FLOAT)), 0) AS avg_rating, " +
                     "COUNT(r.review_id) AS review_count " +
                     "FROM BOOKS b " +
                     "LEFT JOIN AUTHORS a ON b.author_id = a.author_id " +
                     "LEFT JOIN CATEGORIES c ON b.category_id = c.category_id " +
                     "LEFT JOIN PUBLISHERS p ON b.publisher_id = p.publisher_id " +
                     "LEFT JOIN REVIEWS r ON b.book_id = r.book_id " +
                     "WHERE b.book_id = ? " +
                     "GROUP BY b.book_id, b.title, b.author_id, b.category_id, b.publisher_id, " +
                     "b.ISBN, b.price, b.stock_quantity, b.publication_date, b.description, " +
                     "b.image_url, a.name, c.name, p.name";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<Book> getBooksByCategory(int categoryId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.*, a.name as author_name, c.name as category_name, p.name as publisher_name, " +
                     "COALESCE(AVG(CAST(r.rating AS FLOAT)), 0) AS avg_rating, " +
                     "COUNT(r.review_id) AS review_count " +
                     "FROM BOOKS b " +
                     "LEFT JOIN AUTHORS a ON b.author_id = a.author_id " +
                     "LEFT JOIN CATEGORIES c ON b.category_id = c.category_id " +
                     "LEFT JOIN PUBLISHERS p ON b.publisher_id = p.publisher_id " +
                     "LEFT JOIN REVIEWS r ON b.book_id = r.book_id " +
                     "WHERE b.category_id = ? " +
                     "GROUP BY b.book_id, b.title, b.author_id, b.category_id, b.publisher_id, " +
                     "b.ISBN, b.price, b.stock_quantity, b.publication_date, b.description, " +
                     "b.image_url, a.name, c.name, p.name " +
                     "ORDER BY b.title";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, categoryId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = mapResultSetToBook(rs);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }
    
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.*, a.name as author_name, c.name as category_name, p.name as publisher_name, " +
                     "COALESCE(AVG(CAST(r.rating AS FLOAT)), 0) AS avg_rating, " +
                     "COUNT(r.review_id) AS review_count " +
                     "FROM BOOKS b " +
                     "LEFT JOIN AUTHORS a ON b.author_id = a.author_id " +
                     "LEFT JOIN CATEGORIES c ON b.category_id = c.category_id " +
                     "LEFT JOIN PUBLISHERS p ON b.publisher_id = p.publisher_id " +
                     "LEFT JOIN REVIEWS r ON b.book_id = r.book_id " +
                     "WHERE b.title LIKE ? OR a.name LIKE ? OR b.ISBN LIKE ? OR b.description LIKE ? " +
                     "GROUP BY b.book_id, b.title, b.author_id, b.category_id, b.publisher_id, " +
                     "b.ISBN, b.price, b.stock_quantity, b.publication_date, b.description, " +
                     "b.image_url, a.name, c.name, p.name " +
                     "ORDER BY b.title";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Book book = mapResultSetToBook(rs);
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return books;
    }
    
    public boolean addBook(Book book) {
        String sql = "INSERT INTO BOOKS (title, author_id, category_id, publisher_id, ISBN, " +
                     "price, stock_quantity, publication_date, description, image_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setInt(2, book.getAuthorId());
            pstmt.setInt(3, book.getCategoryId());
            pstmt.setInt(4, book.getPublisherId());
            pstmt.setString(5, book.getIsbn());
            pstmt.setBigDecimal(6, book.getPrice());
            pstmt.setInt(7, book.getStockQuantity());
            
            if (book.getPublicationDate() != null) {
                pstmt.setDate(8, new java.sql.Date(book.getPublicationDate().getTime()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }
            
            pstmt.setString(9, book.getDescription());
            pstmt.setString(10, book.getImageUrl());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setBookId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updateBook(Book book) {
        String sql = "UPDATE BOOKS SET title = ?, author_id = ?, category_id = ?, publisher_id = ?, " +
                     "ISBN = ?, price = ?, stock_quantity = ?, publication_date = ?, description = ?, " +
                     "image_url = ? WHERE book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, book.getTitle());
            pstmt.setInt(2, book.getAuthorId());
            pstmt.setInt(3, book.getCategoryId());
            pstmt.setInt(4, book.getPublisherId());
            pstmt.setString(5, book.getIsbn());
            pstmt.setBigDecimal(6, book.getPrice());
            pstmt.setInt(7, book.getStockQuantity());
            
            if (book.getPublicationDate() != null) {
                pstmt.setDate(8, new java.sql.Date(book.getPublicationDate().getTime()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }
            
            pstmt.setString(9, book.getDescription());
            pstmt.setString(10, book.getImageUrl());
            pstmt.setInt(11, book.getBookId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM BOOKS WHERE book_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bookId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setTitle(rs.getString("title"));
        book.setAuthorId(rs.getInt("author_id"));
        book.setCategoryId(rs.getInt("category_id"));
        book.setPublisherId(rs.getInt("publisher_id"));
        book.setIsbn(rs.getString("ISBN"));
        book.setPrice(rs.getBigDecimal("price"));
        book.setStockQuantity(rs.getInt("stock_quantity"));
        book.setPublicationDate(rs.getDate("publication_date"));
        book.setDescription(rs.getString("description"));
        book.setImageUrl(rs.getString("image_url"));
        
        // Join fields
        book.setAuthorName(rs.getString("author_name"));
        book.setCategoryName(rs.getString("category_name"));
        book.setPublisherName(rs.getString("publisher_name"));
        book.setAvgRating(rs.getFloat("avg_rating"));
        book.setReviewCount(rs.getInt("review_count"));
        
        return book;
    }
} 
