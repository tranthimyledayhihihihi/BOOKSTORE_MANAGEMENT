package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice {
   private String invoiceId;
   private String customerName;
   private String customerPhone;
   private Date createdDate;
   private List<InvoiceItem> items;
   
   public Invoice() {
      this.items = new ArrayList<>();
   }
   
   public Invoice(String invoiceId, String customerName, String customerPhone, Date createdDate) {
      this.invoiceId = invoiceId;
      this.customerName = customerName;
      this.customerPhone = customerPhone;
      this.createdDate = createdDate;
      this.items = new ArrayList<>();
   }
   
   // Getters and setters
   public String getInvoiceId() {
      return invoiceId;
   }
   
   public void setInvoiceId(String invoiceId) {
      this.invoiceId = invoiceId;
   }
   
   public String getCustomerName() {
      return customerName;
   }
   
   public void setCustomerName(String customerName) {
      this.customerName = customerName;
   }
   
   public String getCustomerPhone() {
      return customerPhone;
   }
   
   public void setCustomerPhone(String customerPhone) {
      this.customerPhone = customerPhone;
   }
   
   public Date getCreatedDate() {
      return createdDate;
   }
   
   public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
   }
   
   public List<InvoiceItem> getItems() {
      return items;
   }
   
   public void setItems(List<InvoiceItem> items) {
      this.items = items;
   }
   
   public void addItem(InvoiceItem item) {
      this.items.add(item);
   }
   
   public double getTotalAmount() {
      double total = 0;
      for (InvoiceItem item : items) {
            total += item.getQuantity() * item.getPrice();
      }
      return total;
   }
}
