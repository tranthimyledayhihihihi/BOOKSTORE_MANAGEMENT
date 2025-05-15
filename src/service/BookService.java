/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.BookDAO;
import model.Book;
import java.sql.SQLException;
import java.util.List;

public class BookService {
    private BookDAO bookDAO;
    private boolean isAdmin;

    public BookService() {
        this.bookDAO = new BookDAO();
        this.isAdmin = true; // Placeholder: Thay bằng logic kiểm tra quyền admin thực tế
    }

    public List<Book> getAllBooks() throws SQLException {
        return bookDAO.getAllBooks();
    }

    public List<Book> getBookByParameters(String keyword, Integer categoryId, Integer authorId) throws SQLException {
        return bookDAO.getBookByParemeters(keyword, categoryId, authorId);
    }

    public List<Book> getBestSellBooks() throws SQLException {
        return bookDAO.getBestSellBooks();
    }

    public List<Book> getLowStockBooks() throws SQLException {
        return bookDAO.getLowStockBooks();
    }

    public int getTotalBooks() throws SQLException {
        return bookDAO.getTotalBooks();
    }

    public Book getBookById(int bookId) throws SQLException {
        return bookDAO.getBookById(bookId);
    }

    public List<Book> getBooksByCategory(int categoryId) throws SQLException {
        return bookDAO.getBooksByCategory(categoryId);
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        return bookDAO.searchBooks(keyword);
    }

    public boolean addBook(Book book) throws SQLException {
        if (isAdmin) {
            return bookDAO.addBook(book);
        } else {
            throw new SecurityException("Admin access required to add a book!");
        }
    }

    public boolean updateBook(Book book) throws SQLException {
        if (isAdmin) {
            return bookDAO.updateBook(book);
        } else {
            throw new SecurityException("Admin access required to update a book!");
        }
    }

    public boolean deleteBook(int bookId) throws SQLException {
        if (isAdmin) {
            return bookDAO.deleteBook(bookId);
        } else {
            throw new SecurityException("Admin access required to delete a book!");
        }
    }
}
