/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import dao.AuthorDAO;
import model.Author;
import java.sql.SQLException;
import java.util.List;

public class AuthorService {
    private AuthorDAO authorDAO;
    private boolean isAdmin;

    public AuthorService() {
        this.authorDAO = new AuthorDAO();
        this.isAdmin = true; // Placeholder: Thay bằng logic kiểm tra quyền admin thực tế
    }

    public List<Author> getAllAuthors() throws SQLException {
        return authorDAO.getAllAuthors();
    }

    public Author getAuthorById(int authorId) throws SQLException {
        return authorDAO.getAuthorById(authorId);
    }

    public List<Author> getAuthorsByParameters(String keyword, String nationality) throws SQLException {
        return authorDAO.getAuthorsByParameters(keyword, nationality);
    }

    public List<String> getAllCountries() throws SQLException {
        return authorDAO.getAllCountries();
    }

    public boolean addAuthor(Author author) throws SQLException {
        if (isAdmin) {
            return authorDAO.addAuthor(author);
        } else {
            throw new SecurityException("Admin access required to add an author!");
        }
    }

    public boolean updateAuthor(Author author) throws SQLException {
        if (isAdmin) {
            return authorDAO.updateAuthor(author);
        } else {
            throw new SecurityException("Admin access required to update an author!");
        }
    }

    public boolean deleteAuthor(int authorId) throws SQLException {
        if (isAdmin) {
            return authorDAO.deleteAuthor(authorId);
        } else {
            throw new SecurityException("Admin access required to delete an author!");
        }
    }
}
