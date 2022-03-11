package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.exception.AccountNotFoundException;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Account;
import com.revature.model.Client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private AccountDao accountDao;

    public AccountService() {
        this.accountDao = new AccountDao();
    }

    public AccountService(AccountDao mockDao) {
        this.accountDao = mockDao;
    }

    public List<Account> getAllAccounts() throws SQLException, IOException { // for development purposes
        try {
            System.out.println("Service");
            return this.accountDao.getAllAccounts();
        } catch (SQLException e) {
            throw new SQLException("SQL Exception: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("IOException: " + e.getMessage());
        }

    }

    public List<Account> getAllClientAccounts(String clientId) throws SQLException, IOException {
        try {
            System.out.println("Service");
            return this.accountDao.getAllClientAccounts(clientId);
        } catch (SQLException e) {
            throw new SQLException("SQL Exception: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("IOException: " + e.getMessage());
        }

    }

    public Account getClientAccountById(String accountId, String clientId) throws SQLException,
            AccountNotFoundException, FileNotFoundException, ClientNotFoundException {
        try {

            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }

            Account account = accountDao.getClientAccountById(accountId, clientId); // this could return null

            if (account == null) {
                throw new AccountNotFoundException("Account with id " + accountId + " was not found");
            }

            return account;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for account must be a valid int");

        } catch (IOException e) {
            throw new FileNotFoundException("File not found: " + e.getMessage());
        }
    };

    public Account editAccount(Account account, String accountId, String clientId) throws SQLException,
            AccountNotFoundException, FileNotFoundException, ClientNotFoundException {
        try {

            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }

            if (AccountDao.getClientAccountById(accountId, clientId) == null) {
                throw new AccountNotFoundException("User is trying to edit a Account that does not exist. Account with id " + accountId
                        + " was not found");
            }

            validateAccountInformation(account);

            account.setAccountId(accountId);
            Account editedAccount = AccountDao.updateAccount(account);

            return editedAccount;

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for Account must be a valid int");
        } catch (IOException e) {
            throw new FileNotFoundException("File not found: " + e.getMessage());
        }

    }

    public Boolean deleteAccount(String accountId, String clientId, Account account) throws SQLException,
            AccountNotFoundException, FileNotFoundException, ClientNotFoundException {
        try {
            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }

            if (AccountDao.getClientAccountById(accountId, clientId) == null) {
                throw new AccountNotFoundException("User is trying to edit a Account that does not exist. Account with id " + accountId
                        + " was not found");
            }

            account.setAccountId(accountId);
            Boolean accountDeleted = AccountDao.deleteAccountById(accountId);

            return accountDeleted;

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("ID provided for Account must be a valid UUID");
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
    }


    public void validateAccountInformation(Account account) {

        if (account.getBalance() < 0) {
            throw new IllegalArgumentException("A negative balance input is invalid. Balance input: " + account.getBalance());
        }
    }

    public Account addNewAccount(Account a, Client c) throws SQLException, FileNotFoundException {

        validateAccountInformation(a);

        Account addedAccount = accountDao.addAccount(a, c);

        return addedAccount;

    }
}
