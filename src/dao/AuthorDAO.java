/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;




import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Author;
import util.DBConnection;

public class AuthorDAO {
    
    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT \r\n" + //
                        "\ta.author_id,\r\n" + //
                        "\ta.image_url,\r\n" + //
                        "\ta.name,\r\n" + //
                        "\ta.bio,\r\n" + //
                        "\ta.birth_year,\r\n" + //
                        "\ta.country,\r\n" + //
                        "\tCOUNT(b.author_id) AS bookcount\r\n" + //
                    "FROM \r\n" + //
                        "\tdbo.AUTHORS a\r\n" + //
                    "LEFT JOIN \r\n" + //
                        "\tdbo.BOOKS b ON b.author_id = a.author_id\r\n" + //
                    "GROUP BY \r\n" + //
                        "\ta.author_id,\r\n" + //
                        "\ta.image_url,\r\n" + //
                        "\ta.name,\r\n" + //
                        "\ta.bio,\r\n" + //
                        "\ta.birth_year,\r\n" + //
                        "\ta.country\r\n" + //
                    "ORDER BY \r\n" + //
                        "\ta.author_id";
        
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Author author = mapResultSetToAuthor(rs);
                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return authors;
    }
    
    public Author getAuthorById(int authorId) {
        String sql = "SELECT * FROM AUTHORS WHERE author_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, authorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAuthor(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addAuthor(Author author) {
        String sql = "INSERT INTO AUTHORS (name, bio, birth_year, country) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getBio());
            
            if (author.getBirthYear() != null) {
                pstmt.setInt(3, author.getBirthYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            pstmt.setString(4, author.getCountry());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        author.setAuthorId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public List<Author> getAuthorsByParameters(String keywork, String nationality) {
        List<Author> authors = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {

            CallableStatement cstm = conn.prepareCall("{call pr_Search_Authors(?, ?)}");
            cstm.setString(1, keywork);
            cstm.setString(2, nationality);
            ResultSet rs = cstm.executeQuery();

            while (rs.next()) {
                Author author = new Author();
                author.setAuthorId(rs.getInt("author_id"));
                author.setImage(rs.getString("image_url"));
                author.setName(rs.getString("name"));
                author.setCountry(rs.getString("country"));
                author.setBio(rs.getString("bio"));
                author.setBookCount(rs.getInt("bookcount"));

                authors.add(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return authors;
    }
    
    public boolean updateAuthor(Author author) {
        String sql = "UPDATE AUTHORS SET name = ?, bio = ?, birth_year = ?, country = ? WHERE author_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getBio());
            
            if (author.getBirthYear() != null) {
                pstmt.setInt(3, author.getBirthYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            pstmt.setString(4, author.getCountry());
            pstmt.setInt(5, author.getAuthorId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deleteAuthor(int authorId) {
        String sql = "DELETE FROM AUTHORS WHERE author_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, authorId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    public List<String> getAllCountries() {
        List<String> countries = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            CallableStatement cstm = conn.prepareCall("{call pr_GetCountriesFromAuthors()}");
            ResultSet rs = cstm.executeQuery();

            while (rs.next()) {
                String country = rs.getString("country");
                if (!countries.contains(country)) {
                    countries.add(country);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return countries;
    }
    
    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setAuthorId(rs.getInt("author_id"));
        author.setImage(rs.getString("image_url"));
        author.setName(rs.getString("name"));
        author.setBio(rs.getString("bio"));
        author.setBookCount(rs.getInt("bookcount"));
        author.setBirthYear(rs.getInt("birth_year"));
        
        author.setCountry(rs.getString("country"));
        return author;
    }

    public static void main(String[] args) {
        
        AuthorDAO authorDAO = new AuthorDAO();
        // List<Author> authors = authorDAO.getAuthorsByParameters("Ngô Tất Tố", null);
        // for (Author author : authors) {
        //     System.out.println(author.toString());
        // }

        List<String> countries = authorDAO.getAllCountries();
        for (String country : countries) {
            System.out.println(country);
        }
    }
} 
