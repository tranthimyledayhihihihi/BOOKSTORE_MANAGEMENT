package model;

import java.sql.Timestamp;

/**
 * Model class representing an Address
 */
public class Address {
    private int addressId;
    private int customerId;
    private String recipientName;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private boolean isDefault;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    /**
     * Default constructor
     */
    public Address() {
    }
    
    /**
     * Parameterized constructor
     * 
     * @param addressId Address ID
     * @param customerId Customer ID
     * @param recipientName Recipient name
     * @param phone Phone number
     * @param addressLine1 Address line 1
     * @param addressLine2 Address line 2
     * @param city City
     * @param state State/Province
     * @param postalCode Postal code
     * @param country Country
     * @param isDefault Whether this is the default address
     */
    public Address(int addressId, int customerId, String recipientName, String phone, String addressLine1,
                  String addressLine2, String city, String state, String postalCode, String country, boolean isDefault) {
        this.addressId = addressId;
        this.customerId = customerId;
        this.recipientName = recipientName;
        this.phone = phone;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.isDefault = isDefault;
    }

    public boolean isIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    /**
     * Get address ID
     * 
     * @return Address ID
     */
    public int getAddressId() {
        return addressId;
    }
    
    /**
     * Set address ID
     * 
     * @param addressId Address ID
     */
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
    
    /**
     * Get customer ID
     * 
     * @return Customer ID
     */
    public int getCustomerId() {
        return customerId;
    }
    
    /**
     * Set customer ID
     * 
     * @param customerId Customer ID
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    /**
     * Get recipient name
     * 
     * @return Recipient name
     */
    public String getRecipientName() {
        return recipientName;
    }
    
    /**
     * Set recipient name
     * 
     * @param recipientName Recipient name
     */
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
    
    /**
     * Get phone number
     * 
     * @return Phone number
     */
    public String getPhone() {
        return phone;
    }
    
    /**
     * Set phone number
     * 
     * @param phone Phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    /**
     * Get address line 1
     * 
     * @return Address line 1
     */
    public String getAddressLine1() {
        return addressLine1;
    }
    
    /**
     * Set address line 1
     * 
     * @param addressLine1 Address line 1
     */
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }
    
    /**
     * Get address line 2
     * 
     * @return Address line 2
     */
    public String getAddressLine2() {
        return addressLine2;
    }
    
    /**
     * Set address line 2
     * 
     * @param addressLine2 Address line 2
     */
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }
    
    /**
     * Get city
     * 
     * @return City
     */
    public String getCity() {
        return city;
    }
    
    /**
     * Set city
     * 
     * @param city City
     */
    public void setCity(String city) {
        this.city = city;
    }
    
    /**
     * Get state/province
     * 
     * @return State/province
     */
    public String getState() {
        return state;
    }
    
    /**
     * Set state/province
     * 
     * @param state State/province
     */
    public void setState(String state) {
        this.state = state;
    }
    
    /**
     * Get postal code
     * 
     * @return Postal code
     */
    public String getPostalCode() {
        return postalCode;
    }
    
    /**
     * Set postal code
     * 
     * @param postalCode Postal code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    /**
     * Get country
     * 
     * @return Country
     */
    public String getCountry() {
        return country;
    }
    
    /**
     * Set country
     * 
     * @param country Country
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
    /**
     * Check if this is the default address
     * 
     * @return true if this is the default address, false otherwise
     */
    public boolean isDefault() {
        return isDefault;
    }
    
    /**
     * Set default status
     * 
     * @param isDefault true to set as default, false otherwise
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    /**
     * Get creation timestamp
     * 
     * @return Creation timestamp
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    /**
     * Set creation timestamp
     * 
     * @param createdAt Creation timestamp
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Get update timestamp
     * 
     * @return Update timestamp
     */
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    /**
     * Set update timestamp
     * 
     * @param updatedAt Update timestamp
     */
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    /**
     * Get formatted full address
     * 
     * @return Formatted full address
     */
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(addressLine1);
        
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            sb.append(", ").append(addressLine2);
        }
        
        sb.append(", ").append(city);
        
        if (state != null && !state.trim().isEmpty()) {
            sb.append(", ").append(state);
        }
        
        if (postalCode != null && !postalCode.trim().isEmpty()) {
            sb.append(" ").append(postalCode);
        }
        
        if (country != null && !country.trim().isEmpty()) {
            sb.append(", ").append(country);
        }
        
        return sb.toString();
    }
} 