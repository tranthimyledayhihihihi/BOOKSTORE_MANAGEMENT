package model;

import java.util.Date;

public class Account {
    private int accountId;
    private String username;
    private String passwordHash;
    private String email;
    private int roleId;
    private Integer customerId; // Có thể null
    private boolean isActive;
    private Date createdDate;
    private Date lastLogin;
    
    // For display purpose (join queries)
    private String roleName;
    private String customerName;
    
    public Account() {
    }
    
    public Account(int accountId, String username, String passwordHash, String email, 
                  int roleId, Integer customerId, boolean isActive, Date createdDate, Date lastLogin) {
        this.accountId = accountId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.roleId = roleId;
        this.customerId = customerId;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.lastLogin = lastLogin;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", isActive=" + isActive +
                ", createdDate=" + createdDate +
                ", lastLogin=" + lastLogin +
                '}';
    }
} 