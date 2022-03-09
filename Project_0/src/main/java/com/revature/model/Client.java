package com.revature.model;

import java.util.Date;
import java.util.Objects;

// TODO 7: Create a class that will serve as a model for records in a database table (clients table)
public class Client {

    private String clientId;
    private String accountId;
    private String firstName;
    private String lastName;
    private int clientAge;
    private String city;
    private String state;
    private String accountType;
    private Integer balance;
    private String accountStatus;
    private Date createDate;
    private Date updateDate;

    public Client(String clientId, String accountId, String firstName, String lastName, int clientAge, String city, String state,
                  String accountStatus, String accountType, int balance, Date createDate, Date updateDate) {
        this.clientId = clientId;
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientAge = clientAge;
        this.city = city;
        this.state = state;
        this.accountType = accountType;
        this.balance = balance;
        this.accountStatus = accountStatus;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }


    public String getClientId() {
        return clientId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getClientAge() {
        return clientAge;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getAccountType() {
        return accountType;
    }

    public Integer getBalance() {
        return balance;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setClientAge(int clientAge) {
        this.clientAge = clientAge;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return clientId == client.clientId && clientAge == client.clientAge && Objects.equals(firstName, client.firstName) && Objects.equals(lastName, client.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, firstName, lastName, clientAge);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + clientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + clientAge +
                '}';
    }
}