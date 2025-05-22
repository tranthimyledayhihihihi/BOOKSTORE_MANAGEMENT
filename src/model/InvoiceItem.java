// Lớp InvoiceItem.java
package model;

public class InvoiceItem {
   private int id;
   private String invoiceId;
   private int bookId;
   private String bookTitle;
   private String bookAuthor;
   private int quantity;
   private double price;
   
   public InvoiceItem() {}
   
   public InvoiceItem(int id, String invoiceId, int bookId, String bookTitle, String bookAuthor, int quantity, double price) {
      this.id = id;
      this.invoiceId = invoiceId;
      this.bookId = bookId;
      this.bookTitle = bookTitle;
      this.bookAuthor = bookAuthor;
      this.quantity = quantity;
      this.price = price;
   }
   
   // Getters and setters
   public int getId() {
      return id;
   }
   
   public void setId(int id) {
      this.id = id;
   }
   
   public String getInvoiceId() {
      return invoiceId;
   }
   
   public void setInvoiceId(String invoiceId) {
      this.invoiceId = invoiceId;
   }
   
   public int getBookId() {
      return bookId;
   }
   
   public void setBookId(int bookId) {
      this.bookId = bookId;
   }
   
   public String getBookTitle() {
      return bookTitle;
   }
   
   public void setBookTitle(String bookTitle) {
      this.bookTitle = bookTitle;
   }
   
   public String getBookAuthor() {
      return bookAuthor;
   }
   
   public void setBookAuthor(String bookAuthor) {
      this.bookAuthor = bookAuthor;
   }
   
   public int getQuantity() {
      return quantity;
   }
   
   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }
   
   public double getPrice() {
      return price;
   }
   
   public void setPrice(double price) {
      this.price = price;
   }
}
