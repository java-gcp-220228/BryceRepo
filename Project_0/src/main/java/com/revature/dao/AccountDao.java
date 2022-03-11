package com.revature.dao;

import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// TODO 6: Create a Dao (data access object) class for a particular "entity"
public class AccountDao {

    // TODO 8: Create the methods for the "CRUD" operations

    // C
    // Whenever you add a account, no id is associated yet
    // The id is automatically generated
    // So what we want to do is retrieve an id and return that with the Account object
    public Account addAccount(Account account, Client client) throws SQLException, FileNotFoundException {
        try (Connection con = ConnectionUtility.getConnection()) {

            String sql = "INSERT INTO " +
                    "account_info (account_type, balance, account_status, create_date, update_date)" +
                    "VALUES (?,?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            String accountType = account.getAccountType();
            Integer balance = account.getBalance();
            String accountStatus = account.getAccountStatus();

            account.setCreateDateToCurrent();  // new record needs to have current date
            Long createDate = account.getCreateDate();

            account.setUpdateDateToCurrent();
            Long updateDate = account.getUpdateDate();   // update will have the same date as createDate

            // String accountId = account.getAccountId();

            System.out.println("created: " + createDate + " {}}{}{} updated: " + updateDate);

            pstmt.setString(1, accountType);
            pstmt.setInt(2, balance);
            pstmt.setString(3, accountStatus);
            pstmt.setLong(4, createDate);
            pstmt.setLong(5, updateDate);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            String generatedAccountId = rs.getString(1); // 1st column of the ResultSet
            //String generatedAccountId = rs.getString(2);


            System.out.println("account_id: " + generatedAccountId);
            System.out.println("client_id: " + client.getClientId());

            String add_ids = "INSERT INTO all_accounts (account_id, client_id) VALUES (?,?)";

            PreparedStatement pstmt2 = con.prepareStatement(add_ids);

            pstmt2.setString(1, generatedAccountId);
            pstmt2.setString(2, client.getClientId());

            pstmt2.executeUpdate();

            return new Account(generatedAccountId, accountStatus, accountType, balance, createDate, updateDate);

        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    // R
    public static Account getAccountById(String accountId, String clientId) throws SQLException, IOException {
        // TODO 9: Call the getConnection method from ConnectionUtility (which we made)
        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources
            // TODO 10: Create a (Prepared)Statement object using the Connection object

            String sql = "SELECT * FROM account_info WHERE account_id::text = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            // TODO 11: If any parameters need to be set, set the parameters (?)
            pstmt.setString(1, accountId);

            // TODO 12: Execute the query and retrieve a ResultSet object
            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            // TODO 13: Iterate over record(s) using the ResultSet's next() method
            if (rs.next()) {
                // TODO 14: Grab the information from the record

                String accountStatus = rs.getString("account_status");
                String accountType = rs.getString("account_type");
                int balance = rs.getInt("balance");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");

                return new Account(accountId, accountStatus, accountType, balance, createDate, updateDate);
            }

        }

        return null;
    }

    public List<Account> getAllClientAccounts(String clientId) throws SQLException, IOException {
        List<Account> accounts = new ArrayList<>();

        System.out.println("Started");

        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources

            String sql = "SELECT * " +
                    "FROM account_info " +
                    "WHERE account_id IN " +
                    "(" +
                    "SELECT account_id FROM all_accounts " +
                    "WHERE client_id::text = ?" +
                    ");";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, clientId);


            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            while (rs.next()) {
                System.out.println(rs);
                String accountId = rs.getString("account_id");
                String accountStatus = rs.getString("account_status");
                String accountType = rs.getString("account_type");
                int balance = rs.getInt("balance");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");

                accounts.add(new Account(accountId, accountStatus, accountType, balance, createDate, updateDate));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return accounts;
    }

    // U
    public static Account updateAccount(Account account) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE account_info " +
                    "SET account_type = ?, " +
                    "balance = ?, " +
                    "account_status = ?, " +
                    "create_date = ?, " +
                    "update_date = ?  " +
                    "WHERE account_id::text = ?";

            System.out.println(sql);
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(6, account.getAccountType());
            pstmt.setInt(7, account.getBalance());
            pstmt.setString(8, account.getAccountStatus());
            pstmt.setLong(9, account.getCreateDate());
            account.setUpdateDateToCurrent(); // updates date field
            pstmt.setLong(10, account.getUpdateDate());

            pstmt.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return account;
    }

    // D
    // true if a record was deleted, false if a record was not deleted
    public static boolean deleteAccountById(String accountId) throws SQLException, IOException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM account_info WHERE account_id::text = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, accountId);

            int numberOfRecordsDeleted = pstmt.executeUpdate(); // executeUpdate() is used with INSERT, UPDATE, DELETE

            if (numberOfRecordsDeleted == 1) {
                return true;
            }
        }

        return false;
    }
}