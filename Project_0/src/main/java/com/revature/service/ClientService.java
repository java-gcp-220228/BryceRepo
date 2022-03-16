package com.revature.service;

import com.revature.dao.ClientDao;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Client;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    public List<Client> getAllClients() throws SQLException, IOException {
        return this.clientDao.getAllClients();
    }

    public Client getClientById(String clientId) throws SQLException, ClientNotFoundException, FileNotFoundException {
        try {

            validateClientId(clientId);

            Client c = clientDao.getClientById(clientId); // this could return null

            if (c == null) {
                throw new ClientNotFoundException("Client with ID " + clientId + " was not found");
            }




            return c;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID provided for client must be a valid UUID");

        } catch (IOException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }
        /*catch (SQLException e) {
            throw new SQLException("Unsupported SQL execution has occurred: ", e);
        }*/
    };

    public Client editClient(String id, Client client) throws SQLException, ClientNotFoundException, FileNotFoundException {
        try {
            String clientId = id;

            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }

            validateClientId(clientId);
            validateClientInformation(client);

            client.setClientId(clientId);
            Client editedClient = ClientDao.updateClient(client);

            return editedClient;

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for Client must be a valid int");
        } catch (IOException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }/*catch (SQLException e) {
            throw new SQLException("Unsupported SQL execution has occurred: ", e);
        }*/

    }

    public Boolean deleteClient(String id) throws SQLException, ClientNotFoundException, FileNotFoundException {
        try {
            String clientId = id;

            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }

            Boolean clientDeleted = ClientDao.deleteClientById(clientId);
            
            return clientDeleted;

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("ID provided for Client must be a valid UUID");
        } catch (IOException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }/*catch (SQLException e) {
            throw new SQLException("Unsupported SQL execution has occurred: ", e);
        }*/
    }

    public void validateClientId(String clientId) {
        clientId = clientId.trim();

        if(!clientId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new IllegalArgumentException("Client ID must be a valid UUID. Client ID input was " + clientId);
        }

    }


    public void validateClientInformation(Client client) {
        client.setFirstName(client.getFirstName().trim());
        client.setLastName(client.getLastName().trim());

        if (!client.getFirstName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("First name must only have alphabetical characters. First name input was " + client.getFirstName());
        }

        if (!client.getLastName().matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Last name must only have alphabetical characters. Last name input was " + client.getLastName());
        }

        if (client.getClientAge() < 0) {
            throw new IllegalArgumentException("Adding a student with age < 0 is not valid. Age provided was " + client.getClientAge());
        }
    }

    public Client addNewClient(Client c) throws SQLException, FileNotFoundException {

        validateClientInformation(c);

        Client addedClient = clientDao.addClient(c);

        return addedClient;

    }
}
