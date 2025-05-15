/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Publisher;
import util.DBConnection;

public class PublisherDAO {
    
    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        String sql = "SELECT\r\n" + //
                        "\tp.publisher_id,\r\n" + //
                        "\tp.name,\r\n" + //
                        "\tp.address,\r\n" + //
                        "\tp.phone,\r\n" + //
                        "\tCOUNT(b.publisher_id) AS bookcount\r\n" + //
                    "FROM\r\n" + //
                        "\tdbo.PUBLISHERS p\r\n" + //
                    "LEFT JOIN\r\n" + //
                        "\tdbo.BOOKS b ON b.publisher_id = p.publisher_id\r\n" + //
                    "GROUP BY\r\n" + //
                        "\tp.publisher_id,\r\n" + //
                        "\tp.name,\r\n" + //
                        "\tp.address,\r\n" + //
                        "\tp.phone\r\n" + //
                    "ORDER BY\r\n" + //
                        "\tp.publisher_id";
        
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Publisher publisher = mapResultSetToPublisher(rs);
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return publishers;
    }
    
    public Publisher getPublisherById(int publisherId) {
        String sql = "SELECT * FROM PUBLISHERS WHERE publisher_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, publisherId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPublisher(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean addPublisher(Publisher publisher) {
        String sql = "INSERT INTO PUBLISHERS (name, address, phone, email) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, publisher.getName());
            pstmt.setString(2, publisher.getAddress());
            pstmt.setString(3, publisher.getPhone());
            pstmt.setString(4, publisher.getEmail());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        publisher.setPublisherId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean updatePublisher(Publisher publisher) {
        String sql = "UPDATE PUBLISHERS SET name = ?, address = ?, phone = ?, email = ? WHERE publisher_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, publisher.getName());
            pstmt.setString(2, publisher.getAddress());
            pstmt.setString(3, publisher.getPhone());
            pstmt.setString(4, publisher.getEmail());
            pstmt.setInt(5, publisher.getPublisherId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean deletePublisher(int publisherId) {
        String sql = "DELETE FROM PUBLISHERS WHERE publisher_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, publisherId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private Publisher mapResultSetToPublisher(ResultSet rs) throws SQLException {
        Publisher publisher = new Publisher();
        publisher.setPublisherId(rs.getInt("publisher_id"));
        publisher.setName(rs.getString("name"));
        publisher.setAddress(rs.getString("address"));
        publisher.setPhone(rs.getString("phone"));
        publisher.setBookCount(rs.getInt("bookcount"));
        return publisher;
    }

    public static void main(String[] args) {
        PublisherDAO publisherDAO = new PublisherDAO();
        List<Publisher> publishers = publisherDAO.getAllPublishers();
        for (Publisher publisher : publishers) {
            System.out.println(publisher.toString());
        }
    }
} 
