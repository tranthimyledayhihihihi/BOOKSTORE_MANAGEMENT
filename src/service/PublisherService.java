package service;

import dao.PublisherDAO;
import model.Publisher;

import java.util.List;

/**
 * Lớp service xử lý logic nghiệp vụ liên quan đến nhà xuất bản
 */
public class PublisherService {
    private PublisherDAO publisherDAO;
    
    public PublisherService() {
        this.publisherDAO = new PublisherDAO();
    }
    
    /**
     * Lấy danh sách tất cả nhà xuất bản
     */
    public List<Publisher> getAllPublishers() {
        return publisherDAO.getAllPublishers();
    }
    
    /**
     * Lấy thông tin chi tiết một nhà xuất bản
     */
    public Publisher getPublisherById(int publisherId) {
        return publisherDAO.getPublisherById(publisherId);
    }
    
    /**
     * Thêm nhà xuất bản mới
     */
    public boolean addPublisher(Publisher publisher) {
        // Kiểm tra dữ liệu đầu vào
        if (publisher == null || publisher.getName() == null || publisher.getName().trim().isEmpty()) {
            return false;
        }
        
        return publisherDAO.addPublisher(publisher);
    }
    
    /**
     * Cập nhật thông tin nhà xuất bản
     */
    public boolean updatePublisher(Publisher publisher) {
        // Kiểm tra dữ liệu đầu vào
        if (publisher == null || publisher.getPublisherId() <= 0 || 
            publisher.getName() == null || publisher.getName().trim().isEmpty()) {
            return false;
        }
        
        return publisherDAO.updatePublisher(publisher);
    }
    
    /**
     * Xóa nhà xuất bản
     */
    public boolean deletePublisher(int publisherId) {
        if (publisherId <= 0) {
            return false;
        }
        
        return publisherDAO.deletePublisher(publisherId);
    }
} 