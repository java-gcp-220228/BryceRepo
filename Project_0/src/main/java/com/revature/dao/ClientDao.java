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
            String sql = "INSERT INTO client_data (first_name, last_name , account_type, balance, account_status, create_date, update_date)" +
                    "VALUES (?,?,?,?,?,?,?)";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setInt(3, client.getAge());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            rs.next();
            int generatedId = rs.getInt(1); // 1st column of the ResultSet

            return new Client(generatedId, client.getFirstName(), client.getLastName(), client.getAge());
        }
    }

    // R
    public Client getClientById(int id) throws SQLException {
        // TODO 9: Call the getConnection method from ConnectionUtility (which we made)
        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources
            // TODO 10: Create a (Prepared)Statement object using the Connection object
            String sql = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            // TODO 11: If any parameters need to be set, set the parameters (?)
            pstmt.setInt(1, id);

            // TODO 12: Execute the query and retrieve a ResultSet object
            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            // TODO 13: Iterate over record(s) using the ResultSet's next() method
            if (rs.next()) {
                // TODO 14: Grab the information from the record
//                int clientId = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");

                return new Client(id, firstName, lastName, age);
            }

        }

        return null;
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = new ArrayList<>();

        try (Connection con = ConnectionUtility.getConnection()) { // try-with-resources
            String sql = "SELECT * FROM clients";
            PreparedStatement pstmt = con.prepareStatement(sql);

            ResultSet rs = pstmt.executeQuery(); // executeQuery() is used with SELECT

            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                int age = rs.getInt("age");

                clients.add(new Client(id, firstName, lastName, age));
            }

        }

        return clients;
    }

    // U
    public Client updateClient(Client client) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "UPDATE clients " +
                    "SET first_name = ?, " +
                    "last_name = ?, " +
                    "age = ? " +
                    "WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, client.getFirstName());
            pstmt.setString(2, client.getLastName());
            pstmt.setInt(3, client.getAge());
            pstmt.setInt(4, client.getId());

            pstmt.executeUpdate();
        }

        return client;
    }

    // D
    // true if a record was deleted, false if a record was not deleted
    public boolean deleteClientById(int id) throws SQLException {
        try (Connection con = ConnectionUtility.getConnection()) {
            String sql = "DELETE FROM clients WHERE id = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, id);

            int numberOfRecordsDeleted = pstmt.executeUpdate(); // executeUpdate() is used with INSERT, UPDATE, DELETE

            if (numberOfRecordsDeleted == 1) {
                return true;
            }
        }

        return false;
    }
}