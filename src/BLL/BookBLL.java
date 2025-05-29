
package BLL;

import dao.BookDAO;
import model.Book;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BookBLL {
    private final BookDAO bookDAO;

    // Enum cho kiểu sắp xếp
    public enum BookSortType {
        DEFAULT,
        PRICE_ASC,
        PRICE_DESC,
        NEWEST,
        RATING_DESC
    }

    // Constructor mặc định
    public BookBLL() {
        this.bookDAO = new BookDAO();
    }

    // Constructor cho phép inject BookDAO (dùng để kiểm thử)
    public BookBLL(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    /**
     * Lấy danh sách tất cả sách
     */
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    /**
     * Lấy danh sách sách cho 1 trang (phân trang)
     */
    public List<Book> getBooksForPage(int page, int booksPerPage) {
        List<Book> allBooks = getAllBooks();
        int start = (page - 1) * booksPerPage;
        int end = Math.min(start + booksPerPage, allBooks.size());
        if (start >= allBooks.size()) return new ArrayList<>();
        return allBooks.subList(start, end);
    }

    /**
     * Tính tổng số trang dựa trên số sách và số sách/trang
     */
    public int getTotalPages(int booksPerPage) {
        int totalBooks = getAllBooks().size();
        return (int) Math.ceil((double) totalBooks / booksPerPage);
    }

    /**
     * Lọc, sắp xếp, phân trang sách theo các tiêu chí (CÓ filter rating)
     */
    public List<Book> getBooksFilteredAndSorted(
            String keyword,
            Integer categoryId,
            Integer authorId,
            BookSortType sortType,
            Integer rating, // rating >= (ví dụ: 5, 4, 3 hoặc null)
            int page,
            int booksPerPage
    ) {
        // Lấy toàn bộ sách từ DAO
        List<Book> allBooks = bookDAO.getAllBooks();

        // Filter
        List<Book> filtered = allBooks.stream()
            .filter(book -> (keyword == null || keyword.isEmpty()
                    || book.getTitle().toLowerCase().contains(keyword.toLowerCase())
                    || book.getAuthorName().toLowerCase().contains(keyword.toLowerCase())))
              .filter(book -> (categoryId == null || categoryId <= 0 || book.getCategoryId() == categoryId))
            .filter(book -> (authorId == null || authorId <= 0 || book.getAuthorId() == authorId))
            .filter(book -> (rating == null || Math.round(book.getAvgRating()) >= rating))
            .collect(Collectors.toList());

        // Sort
        switch (sortType) {
            case PRICE_ASC:
                filtered.sort(Comparator.comparing(Book::getPrice));
                break;
            case PRICE_DESC:
                filtered.sort(Comparator.comparing(Book::getPrice).reversed());
                break;
            case NEWEST:
                filtered.sort(Comparator.comparing(Book::getPublicationDate, Comparator.nullsLast(Comparator.reverseOrder())));
                break;
            case RATING_DESC:
                filtered.sort(Comparator.comparing(Book::getAvgRating).reversed());
                break;
            default:
                // Không sort hoặc sort mặc định
                break;
        }

        // Paging
        int start = (page - 1) * booksPerPage;
        int end = Math.min(start + booksPerPage, filtered.size());
        if (start >= filtered.size()) return new ArrayList<>();
        return filtered.subList(start, end);
    }

    /**
     * Lấy tổng số trang sau khi filter (CÓ filter rating)
     */
    public int getTotalPagesFiltered(
        String keyword,
        Integer categoryId,
        Integer authorId,
        Integer rating,
        int booksPerPage
) {
    List<Book> allBooks = bookDAO.getAllBooks();
    List<Book> filtered = allBooks.stream()
        .filter(book -> (keyword == null || keyword.isEmpty()
                || book.getTitle().toLowerCase().contains(keyword.toLowerCase())
                || book.getAuthorName().toLowerCase().contains(keyword.toLowerCase())))
        .filter(book -> (categoryId == null || categoryId <= 0 || book.getCategoryId() == categoryId))
        .filter(book -> (authorId == null || authorId <= 0 || book.getAuthorId() == authorId))
        .filter(book -> (rating == null || Math.round(book.getAvgRating()) >= rating))
        .collect(Collectors.toList());
    return (int) Math.ceil((double) filtered.size() / booksPerPage);
}

    /**
     * Tìm kiếm sách theo từ khóa, danh mục, và tác giả (gọi trực tiếp DAO)
     */
    public List<Book> getBookByParameters(String keyword, Integer categoryId, Integer authorId) {
        if (categoryId != null && categoryId <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        if (authorId != null && authorId <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        return bookDAO.getBookByParemeters(keyword, categoryId, authorId);
    }

    /**
     * Lấy danh sách sách bán chạy
     */
    public List<Book> getBestSellBooks() {
        return bookDAO.getBestSellBooks();
    }

    /**
     * Lấy danh sách sách nổi bật
     */
    public List<Book> getFeaturedBooks() {
        return bookDAO.getFeaturedBooks();
    }

    /**
     * Lấy danh sách sách tồn kho thấp
     */
    public List<Book> getLowStockBooks() {
        return bookDAO.getLowStockBooks();
    }

    /**
     * Lấy tổng số sách
     */
    public int getTotalBooks() {
        return bookDAO.getTotalBooks();
    }

    /**
     * Lấy thông tin sách theo ID
     */
    public Book getBookById(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        return bookDAO.getBookById(bookId);
    }

    /**
     * Lấy danh sách sách theo danh mục
     */
    public List<Book> getBooksByCategory(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        return bookDAO.getBooksByCategory(categoryId);
    }

    /**
     * Tìm kiếm sách theo từ khóa
     */
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được để trống");
        }
        return bookDAO.searchBooks(keyword);
    }

    /**
     * Thêm sách mới
     */
    public boolean addBook(Book book) {
        validateBook(book);
        return bookDAO.addBook(book);
    }

    /**
     * Cập nhật thông tin sách
     */
    public boolean updateBook(Book book) {
        if (book == null || book.getBookId() <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        validateBook(book);
        return bookDAO.updateBook(book);
    }

    /**
     * Xóa sách
     */
    public boolean deleteBook(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("ID sách không hợp lệ");
        }
        return bookDAO.deleteBook(bookId);
    }

    /**
     * Kiểm tra dữ liệu sách hợp lệ
     */
    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Thông tin sách không được null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sách không được để trống");
        }
        if (book.getAuthorId() <= 0) {
            throw new IllegalArgumentException("ID tác giả không hợp lệ");
        }
        if (book.getCategoryId() <= 0) {
            throw new IllegalArgumentException("ID danh mục không hợp lệ");
        }
        if (book.getPublisherId() <= 0) {
            throw new IllegalArgumentException("ID nhà xuất bản không hợp lệ");
        }
        if (book.getIsbn() == null || !isValidIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("ISBN không hợp lệ");
        }
        if (book.getPrice() == null || book.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá sách phải lớn hơn 0");
        }
        if (book.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không được âm");
        }
        if (book.getDescription() != null && book.getDescription().length() > 2000) {
            throw new IllegalArgumentException("Mô tả không được vượt quá 2000 ký tự");
        }
    }

    /**
     * Kiểm tra định dạng ISBN
     */
    private boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        // Kiểm tra ISBN-10 hoặc ISBN-13
        String isbnRegex = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        Pattern pattern = Pattern.compile(isbnRegex);
        return pattern.matcher(isbn).matches();
  
}
}
