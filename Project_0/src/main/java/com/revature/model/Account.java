package com.revature.model;

import java.time.Instant;
import java.util.Objects;

// TODO 7: Create a class that will serve as a model for records in a database table (accounts table)
public class Account {

    private String accountId;
    private String accountType;
    private Double balance;
    private String accountStatus;
    private Long createDate;
    private Long updateDate;

    public Account() {

    }

    public Account(String accountId, String accountStatus, String accountType, Double balance, Long createDate, Long updateDate) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.balance = balance;
        this.accountStatus = accountStatus;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public String getAccountId() { return accountId; }

    public String getAccountType() {
        return accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public Long getCreateDate() {
        return createDate;
    };

    public Long getUpdateDate() {
        return updateDate;
    };

    public void setAccountId(String accountId) { this.accountId = accountId; };

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDateToCurrent() {
        Instant instant = Instant.now();
        Long currentTime = instant.toEpochMilli();
        this.updateDate =  currentTime;
    }

    public void setCreateDateToCurrent() {
        Instant instant = Instant.now();
        Long currentTime = instant.toEpochMilli();
        this.createDate =  currentTime;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId = " + accountId + "\n" +
                "accountType = " + accountType + "\n" +
                "balance =  " + balance + "\n" +
                "accountStatus= " + accountStatus + "\n" +
                "createDate = " + createDate + "\n" +
                "updateDate = " + updateDate + "\n" +
                "}";
    }

}
