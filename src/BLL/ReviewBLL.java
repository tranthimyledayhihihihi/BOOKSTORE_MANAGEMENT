
package BLL;

import dao.ReviewDAO;
import model.Review;

import java.sql.SQLException;
import java.util.List;

/**
 * Business Logic Layer for Review operations
 */
public class ReviewBLL {
    private final ReviewDAO reviewDAO;

    public ReviewBLL() {
        reviewDAO = new ReviewDAO();
    }

    /**
     * Get count of reviews by customer
     */
    public int getReviewCountByCustomer(int customerId) {
        try {
            return reviewDAO.getReviewCountByCustomer(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get reviews by customer
     */
    public List<Review> getReviewsByCustomer(int customerId) {
        try {
            return reviewDAO.getReviewsByCustomer(customerId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get reviews by book with limit
     */
    public List<Review> getReviewsByBook(int bookId, int limit) {
        try {
            return reviewDAO.getReviewsByBook(bookId, limit);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Add a new review
     */
    public int addReview(Review review) {
        try {
            int reviewId = reviewDAO.addReview(review);
            reviewDAO.updateBookAverageRating(review.getBookId());
            return reviewId;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Check if customer already reviewed a book
     */
    public boolean hasCustomerReviewedBook(int customerId, int bookId) {
        try {
            return reviewDAO.hasCustomerReviewedBook(customerId, bookId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a review and update book rating
     */
    public boolean deleteReview(int reviewId, int customerId) {
        try {
            int bookId = reviewDAO.deleteReview(reviewId, customerId);
            return bookId != -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update book average rating manually
     */
    public void updateBookAverageRating(int bookId) {
        try {
            reviewDAO.updateBookAverageRating(bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}