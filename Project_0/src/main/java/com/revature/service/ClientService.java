package com.revature.service;

import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {

    private ClientDao clientDao;

    public ClientService() {
        this.clientDao = new ClientDao();
    }

    public ClientService(ClientDao mockDao) {
        this.clientDao = mockDao;
    }

    public List<Client> getAllClients() throws SQLException {
        return this.clientDao.getAllClients();
    }

    public Client getClientById(String id) throws SQLException, ClientNotFoundException {
        try {
            int clientId = Integer.parseInt(id); // This could throw an unchecked exception
            // known as NumberFormatException
            // Important to take note of this, because any unhandled exceptions will result
            // in a 500 Internal Server Error (which we should try to avoid)

            Client s = clientDao.getClientById(clientId); // this could return null

            if (s == null) {
                throw new ClientNotFoundException("Client with id " + clientId + " was not found");
            }

            return s;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for client must be a valid int");
        }
    }
}