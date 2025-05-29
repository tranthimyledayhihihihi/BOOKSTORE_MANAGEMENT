package BLL;

import dao.AuthorDAO;
import model.Author;

import java.util.List;

public class AuthorBLL {
    private final AuthorDAO authorDAO;

    // Constructor mặc định
    public AuthorBLL() {
        this.authorDAO = new AuthorDAO();
    }

    // Constructor cho phép inject AuthorDAO (dùng để kiểm thử)
    public AuthorBLL(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    /**
     * Lấy danh sách tất cả tác giả
     * @return Danh sách tác giả
     */
    public List<Author> getAllAuthors() {
        return authorDAO.getAllAuthors();
    }

    /**
     * Lấy thông tin tác giả theo ID
     * @param authorId ID tác giả
     * @return Author nếu tìm thấy, null nếu không
     * @throws IllegalArgumentException nếu ID không hợp lệ
     */
    public Author getAuthorById(int authorId) throws IllegalArgumentException {
        if (authorId <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        return authorDAO.getAuthorById(authorId);
    }

    /**
     * Thêm tác giả mới
     * @param author Thông tin tác giả
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu dữ liệu đầu vào không hợp lệ
     */
    public boolean addAuthor(Author author) throws IllegalArgumentException {
        if (author == null || author.getName() == null || author.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tác giả không được để trống");
        }
        if (author.getCountry() != null && author.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Quốc gia không được để trống nếu có");
        }
        if (author.getBirthYear() != null && (author.getBirthYear() < 1800 || author.getBirthYear() > 2025)) {
            throw new IllegalArgumentException("Năm sinh phải nằm trong khoảng 1800-2025");
        }
        if (author.getBio() != null && author.getBio().length() > 1000) {
            throw new IllegalArgumentException("Tiểu sử không được vượt quá 1000 ký tự");
        }
        return authorDAO.addAuthor(author);
    }

    /**
     * Tìm kiếm tác giả theo từ khóa và quốc gia
     * @param keyword Từ khóa tìm kiếm (tên tác giả)
     * @param nationality Quốc gia
     * @return Danh sách tác giả phù hợp
     */
    public List<Author> getAuthorsByParameters(String keyword, String nationality) {
        // Cho phép keyword và nationality là null hoặc rỗng, như trong AuthorDAO
        return authorDAO.getAuthorsByParameters(keyword, nationality);
    }

    /**
     * Cập nhật thông tin tác giả
     * @param author Thông tin tác giả cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu dữ liệu đầu vào không hợp lệ
     */
    public boolean updateAuthor(Author author) throws IllegalArgumentException {
        if (author == null || author.getAuthorId() <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên tác giả không được để trống");
        }
        if (author.getCountry() != null && author.getCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("Quốc gia không được để trống nếu có");
        }
        if (author.getBirthYear() != null && (author.getBirthYear() < 1800 || author.getBirthYear() > 2025)) {
            throw new IllegalArgumentException("Năm sinh phải nằm trong khoảng 1800-2025");
        }
        if (author.getBio() != null && author.getBio().length() > 1000) {
            throw new IllegalArgumentException("Tiểu sử không được vượt quá 1000 ký tự");
        }
        return authorDAO.updateAuthor(author);
    }

    /**
     * Xóa tác giả
     * @param authorId ID tác giả
     * @return true nếu thành công, false nếu thất bại
     * @throws IllegalArgumentException nếu ID không hợp lệ
     */
    public boolean deleteAuthor(int authorId) throws IllegalArgumentException {
        if (authorId <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        // Kiểm tra xem tác giả có sách liên quan không
        Author author = authorDAO.getAuthorById(authorId);
        if (author != null && author.getBookCount() > 0) {
            throw new IllegalArgumentException("Không thể xóa tác giả vì có sách liên quan");
        }
        return authorDAO.deleteAuthor(authorId);
    }

    /**
     * Lấy danh sách tất cả quốc gia của tác giả
     * @return Danh sách quốc gia
     */
    public List<String> getAllCountries() {
        return authorDAO.getAllCountries();
    }
}