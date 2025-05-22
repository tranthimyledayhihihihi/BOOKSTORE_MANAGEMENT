package model;

import java.math.BigDecimal;

public class CartSummary {
    private int totalItems;
    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal discount;
    private BigDecimal total;
    
    public CartSummary() {
        this.totalItems = 0;
        this.subtotal = BigDecimal.ZERO;
        this.shippingFee = new BigDecimal(30000); // Default shipping fee
        this.discount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public BigDecimal getShippingFee() {
        return shippingFee;
    }
    
    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }
    
    public BigDecimal getDiscount() {
        return discount;
    }
    
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    
    public BigDecimal getTotal() {
        return total;
    }
    
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    
    /**
     * Tính toán tổng cộng từ subtotal, shipping fee và discount
     */
    public void calculateTotal() {
        this.total = subtotal.add(shippingFee).subtract(discount);
    }
    
    /**
     * Getter cho total với tính toán tự động (alternative method)
     * @return Tổng cộng được tính toán
     */
    public BigDecimal getCalculatedTotal() {
        return subtotal.add(shippingFee).subtract(discount);
    }
    
    /**
     * Kiểm tra xem có miễn phí vận chuyển không
     * @return true nếu phí vận chuyển = 0
     */
    public boolean isFreeShipping() {
        return shippingFee.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * Kiểm tra xem có giảm giá không
     * @return true nếu có giảm giá
     */
    public boolean hasDiscount() {
        return discount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * Lấy tỷ lệ giảm giá (%)
     * @return Tỷ lệ giảm giá từ 0-100
     */
    public double getDiscountPercentage() {
        if (subtotal.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return discount.divide(subtotal, 4, BigDecimal.ROUND_HALF_UP)
                      .multiply(new BigDecimal(100))
                      .doubleValue();
    }
    
    /**
     * Format giá tiền thành chuỗi có định dạng
     * @param amount Số tiền
     * @return Chuỗi định dạng tiền tệ
     */
    public String formatMoney(BigDecimal amount) {
        return String.format("%,.0f", amount.doubleValue()) + " đ";
    }
    
    /**
     * Lấy subtotal dưới dạng chuỗi định dạng
     * @return Subtotal được format
     */
    public String getFormattedSubtotal() {
        return formatMoney(subtotal);
    }
    
    /**
     * Lấy shipping fee dưới dạng chuỗi định dạng
     * @return Shipping fee được format
     */
    public String getFormattedShippingFee() {
        return formatMoney(shippingFee);
    }
    
    /**
     * Lấy discount dưới dạng chuỗi định dạng
     * @return Discount được format
     */
    public String getFormattedDiscount() {
        return formatMoney(discount);
    }
    
    /**
     * Lấy total dưới dạng chuỗi định dạng
     * @return Total được format
     */
    public String getFormattedTotal() {
        return formatMoney(total);
    }
    
    @Override
    public String toString() {
        return "CartSummary{" +
                "totalItems=" + totalItems +
                ", subtotal=" + subtotal +
                ", shippingFee=" + shippingFee +
                ", discount=" + discount +
                ", total=" + total +
                '}';
    }
}