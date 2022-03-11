package com.revature.dao;

import com.revature.model.Client;
import com.revature.utility.ConnectionUtility;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    public Client addClient(Client client) throws SQLException, FileNotFoundException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "INSERT INTO " +
                            "client_info (first_name, last_name, client_age, city, state, create_date, update_date)" +
                            "VALUES (?,?,?,?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            String firstName = client.getFirstName();
            String lastName = client.getLastName();
            int clientAge = client.getClientAge();
            String city = client.getCity();
            String state = client.getState();

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
            pstmt.setLong(6, createDate);
            pstmt.setLong(7, updateDate);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            String generatedClientId = rs.getString(1);

            System.out.println("client_id: " + generatedClientId);

            return new Client(generatedClientId, firstName, lastName, clientAge, city, state, createDate, updateDate);

        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }

    // R
    public static Client getClientById(String clientId) throws SQLException, IOException {
        // TODO 9: Call the getConnection method from ConnectionUtility (which we made)
        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources
            // TODO 10: Create a (Prepared)Statement object using the Connection object

            String sql = "SELECT * FROM client_info WHERE client_id::text = ?";

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
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");


                return new Client(clientId, firstName, lastName, clientAge, city, state, createDate, updateDate);
            }

        }

        return null;
    }

    public List<Client> getAllClients() throws SQLException, IOException {
        List<Client> clients = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources

            String sql = "SELECT * FROM client_info";

            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            while (rs.next()) {
                String clientId = rs.getString("client_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int clientAge = rs.getInt("client_age");
                String city = rs.getString("city");
                String state = rs.getString("state");
                Long createDate = rs.getLong("create_date");
                Long updateDate = rs.getLong("update_date");

                clients.add(new Client(clientId, firstName, lastName, clientAge, city, state, createDate, updateDate));
            }

        }

        return clients;
    }

    // U
    public static Client updateClient(Client client) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE client_info " +
                    "SET first_name = ?, " +
                    "last_name = ?, " +
                    "client_age = ?, " +
                    "city = ?, " +
                    "state = ?, " +
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
            pstmt.setLong(6, client.getCreateDate());
            client.setUpdateDateToCurrent(); // updates date field
            pstmt.setLong(7, client.getUpdateDate());
            pstmt.setString(8, client.getClientId());

            pstmt.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return client;
    }

    // D
    // true if a record was deleted, false if a record was not deleted
    public static boolean deleteClientById(String clientId) throws SQLException, IOException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM client_info WHERE client_id::text = ?";

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