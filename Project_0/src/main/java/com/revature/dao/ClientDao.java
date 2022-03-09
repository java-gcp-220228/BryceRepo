package com.revature.dao;

import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// TODO 6: Create a Dao (data access object) class for a particular "entity"
public class ClientDao {

    // TODO 8: Create the methods for the "CRUD" operations

    // C
    // Whenever you add a client, no id is associated yet
    // The id is automatically generated
    // So what we want to do is retrieve an id and return that with the Client object
    public Client addClient(Client client) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "INSERT INTO " +
                            "client_data (first_name, last_name, client_age, city, state, account_type, " +
                            "balance, account_status, create_date, update_date)" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            String firstName = client.getFirstName();
            String lastName = client.getLastName();
            int clientAge = client.getClientAge();
            String city = client.getCity();
            String state = client.getState();
            String accountType = client.getAccountType();
            Integer balance = client.getBalance();
            String accountStatus = client.getAccountStatus();

            client.setCreateDateToCurrent();  // new record needs to have current date
            Long createDate = client.getCreateDate();

            client.setUpdateDateToCurrent();
            Long updateDate = client.getUpdateDate();   // update will have the same date as createDate

            // String accountId = client.getAccountId();

            System.out.println("created: " + createDate + " {}}{}{} updated: " + updateDate);

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setInt(3, clientAge);
            pstmt.setString(4, city);
            pstmt.setString(5, state);
            pstmt.setString(6, accountType);
            pstmt.setInt(7, balance);
            pstmt.setString(8, accountStatus);
            pstmt.setLong(9, createDate);
            pstmt.setLong(10, updateDate);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            String generatedAccountId = rs.getString(1); // 1st column of the ResultSet
            String generatedClientId = rs.getString(2);


            System.out.println("client_id: " + generatedClientId + "  ________ account_id: " + generatedAccountId);

            return new Client(generatedClientId, generatedAccountId, firstName, lastName, clientAge, city, state,
                    accountStatus, accountType, balance, createDate, updateDate);
        }
    }

    // R
    public static Client getClientById(String clientId) throws SQLException {
        // TODO 9: Call the getConnection method from ConnectionUtility (which we made)
        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources
            // TODO 10: Create a (Prepared)Statement object using the Connection object

            String sql = "SELECT * FROM client_data WHERE client_id::text = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            // TODO 11: If any parameters need to be set, set the parameters (?)
            pstmt.setString(1, clientId);

            // TODO 12: Execute the query and retrieve a ResultSet object
            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            // TODO 13: Iterate over record(s) using the ResultSet's next() method
            if (rs.next()) {
                // TODO 14: Grab the information from the record

                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int clientAge = rs.getInt("client_age");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String accountStatus = rs.getString("account_status");
                String accountType = rs.getString("account_type");
                int balance = rs.getInt("balance");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");
                String accountId = rs.getString("account_id");

                return new Client(clientId, accountId, firstName, lastName, clientAge, city, state,
                        accountStatus, accountType, balance, createDate, updateDate);
            }

        }

        return null;
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources

            String sql = "SELECT * FROM client_data";

            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            while (rs.next()) {
                String clientId = rs.getString("client_id");
                String accountId = rs.getString("account_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int clientAge = rs.getInt("client_age");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String accountStatus = rs.getString("account_status");
                String accountType = rs.getString("account_type");
                int balance = rs.getInt("balance");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");

                clients.add(new Client(clientId, accountId, firstName, lastName, clientAge, city, state,
                        accountStatus, accountType, balance, createDate, updateDate));
            }

        }

        return clients;
    }

    // U
    public static Client updateClient(Client client) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE client_data " +
                    "SET first_name = ?, " +
                    "last_name = ?, " +
                    "client_age = ?, " +
                    "city = ?, " +
                    "state = ?, " +
                    "account_type = ?, " +
                    "balance = ?, " +
                    "account_status = ?, " +
                    "create_date = ?, " +
                    "update_date = ?  " +
                    "WHERE client_id::text = ?";

            System.out.println(sql);
            PreparedStatement pstmt = con.prepareStatement(sql);



            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setInt(3, client.getClientAge());
            pstmt.setString(4, client.getCity());
            pstmt.setString(5, client.getState());
            pstmt.setString(6, client.getAccountType());
            pstmt.setInt(7, client.getBalance());
            pstmt.setString(8, client.getAccountStatus());
            pstmt.setLong(9, client.getCreateDate());

            client.setUpdateDateToCurrent(); // updates date field
            pstmt.setLong(10, client.getUpdateDate());
            pstmt.setString(11, client.getClientId());

            pstmt.executeUpdate();
        }

        return client;
    }

    // D
    // true if a record was deleted, false if a record was not deleted
    public static boolean deleteClientById(String clientId) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM client_data WHERE client_id::text = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, clientId);

            int numberOfRecordsDeleted = pstmt.executeUpdate(); // executeUpdate() is used with INSERT, UPDATE, DELETE

            if (numberOfRecordsDeleted == 1) {
                return true;
            }
        }

        return false;
    }
}