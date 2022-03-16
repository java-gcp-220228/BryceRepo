package com.revature.dao;

import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

// TODO 6: Create a Dao (data access object) class for a particular "entity"
public class AccountDao {

    // TODO 8: Create the methods for the "CRUD" operations

    // C
    // Whenever you add an account, no id is associated yet
    // The id is automatically generated
    // So what we want to do is retrieve an id and return that with the Account object
    public Account addAccount(Account account, String clientId) throws SQLException, FileNotFoundException {
        try (Connection con = ConnectionUtility.getConnection()) {

            String sql = "INSERT INTO " +
                    "account_info (account_type, balance, account_status, create_date, update_date)" +
                    "VALUES (?,?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            String accountType = account.getAccountType();
            Double balance = account.getBalance();
            String accountStatus = account.getAccountStatus();

            account.setCreateDateToCurrent();  // new record needs to have current date
            Long createDate = account.getCreateDate();

            account.setUpdateDateToCurrent();
            Long updateDate = account.getUpdateDate();   // update will have the same date as createDate

            pstmt.setString(1, accountType);
            pstmt.setDouble(2, balance);
            pstmt.setString(3, accountStatus);
            pstmt.setLong(4, createDate);
            pstmt.setLong(5, updateDate);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            String generatedAccountId = rs.getString(1); // 1st column of the ResultSet

            String add_ids = "INSERT INTO all_accounts (account_id, client_id) VALUES (?,?)";

            PreparedStatement pstmt2 = con.prepareStatement(add_ids);

            pstmt2.setObject(1, generatedAccountId, java.sql.Types.OTHER);
            pstmt2.setObject(2, clientId, java.sql.Types.OTHER);

            pstmt2.executeUpdate();

            return new Account(generatedAccountId, accountStatus, accountType, balance, createDate, updateDate);

        } catch (IOException e) {
            throw new FileNotFoundException("File not found: " + e);
        }
    }

    // R
    public static Account getClientAccountById(String accountId, String clientId) throws SQLException, IOException {

        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources


            String sql = "SELECT * " +
                    "FROM account_info " +
                    "WHERE account_id IN " +
                    "( " +
                    "SELECT account_id FROM all_accounts " +
                    "WHERE client_id::text = ? " +
                    "AND account_id::text = ?" +
                    ");";

            PreparedStatement pstmt = con.prepareStatement(sql);


            pstmt.setString(1, clientId);
            pstmt.setString(2, accountId);


            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT


            if (rs.next()) {
                String accountStatus = rs.getString("account_status");
                String accountType = rs.getString("account_type");
                Double balance = rs.getDouble("balance");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");

                return new Account(accountId, accountStatus, accountType, balance, createDate, updateDate);
            }

        }

        return null;
    }



    public List<Account> getClientAccounts(String clientId) throws SQLException, IOException {
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
                Double balance = rs.getDouble("balance");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");

                accounts.add(new Account(accountId, accountStatus, accountType, balance, createDate, updateDate));
            }

        }

        return accounts;
    }

    public List<Account> getClientAccounts(String clientId, String greaterThanValue, String lessThanValue ) throws SQLException, IOException {
        List<Account> accounts = new ArrayList<>();


        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources

            String sql = "SELECT * " +
                    "FROM account_info " +
                    "WHERE ((account_info.balance > ? AND account_info.balance < ?) " +
                    "AND " +
                    "account_id IN (SELECT account_id FROM all_accounts WHERE client_id::text = ?));";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(greaterThanValue));
            pstmt.setInt(2, Integer.parseInt(lessThanValue));
            pstmt.setString(3, clientId);


            System.out.println(pstmt);

            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            while (rs.next()) {
                System.out.println(rs);
                String accountId = rs.getString("account_id");
                String accountStatus = rs.getString("account_status");
                String accountType = rs.getString("account_type");
                Double balance = rs.getDouble("balance");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");

                accounts.add(new Account(accountId, accountStatus, accountType, balance, createDate, updateDate));
            }


        }

        return accounts;
    }


    // U
    public static Account updateAccount(Account account) throws SQLException, IOException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE account_info " +
                    "SET account_type = ?, " +
                    "balance = ?, " +
                    "account_status = ?, " +
                    "create_date = ?, " +
                    "update_date = ?  " +
                    "WHERE account_id::text = ?;";


            System.out.println(sql);
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, account.getAccountType());
            pstmt.setDouble(2, account.getBalance());
            pstmt.setString(3, account.getAccountStatus());
            pstmt.setLong(4, account.getCreateDate());
            account.setUpdateDateToCurrent(); // updates date field
            pstmt.setLong(5, account.getUpdateDate());
            pstmt.setString(6, account.getAccountId());



            pstmt.executeUpdate();
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

    public List<Account> getAllAccounts() throws SQLException, IOException {
        List<Account> accounts = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources

            String sql = "SELECT * FROM account_info";

            PreparedStatement pstmt = con.prepareStatement(sql);


            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            while (rs.next()) {
                System.out.println(rs);
                String accountId = rs.getString("account_id");
                String accountStatus = rs.getString("account_status");
                String accountType = rs.getString("account_type");
                Double balance = rs.getDouble("balance");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");

                accounts.add(new Account(accountId, accountStatus, accountType, balance, createDate, updateDate));
            }

        }

        return accounts;
    }



}
