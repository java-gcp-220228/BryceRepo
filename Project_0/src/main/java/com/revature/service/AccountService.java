package com.revature.service;

import com.revature.dao.AccountDao;
import com.revature.dao.ClientDao;
import com.revature.exception.AccountNotFoundException;
import com.revature.exception.ClientNotFoundException;
import com.revature.model.Account;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import java.util.*;

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
            throw new SQLException("SQL Exception: ",  e);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }

    }

    public List<Account> getClientAccounts(String clientId) throws SQLException, IOException {
        try {
            System.out.println("Service");
            return this.accountDao.getClientAccounts(clientId);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }

    }

    public List<Account> getClientAccounts(String clientId, String lowAmount, String highAmount) throws SQLException, IOException {
        try {
            System.out.println("Service");
            return this.accountDao.getClientAccounts(clientId, lowAmount, highAmount);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }

    }

    public Account getClientAccountById(String accountId, String clientId) throws SQLException,
            AccountNotFoundException, IOException, ClientNotFoundException {
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
            throw new IllegalArgumentException("Id provided for account must be a valid int", e);

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }
    };

    public Account editAccount(Account account, String accountId, String clientId) throws SQLException,
            AccountNotFoundException, IOException, ClientNotFoundException {
        try {

            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }

            if (AccountDao.getClientAccountById(accountId, clientId) == null) {
                throw new AccountNotFoundException("User is trying to edit an Account that does not exist. Account with id " + accountId
                        + " was not found");
            }

            validateAccountInformation(account);
            validateAccountId(account);

            account.setAccountId(accountId);
            Account editedAccount = AccountDao.updateAccount(account);

            return editedAccount;

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("Id provided for Account must be a valid uuid");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }

    }

    public Boolean deleteAccount(String accountId, String clientId) throws SQLException,
            AccountNotFoundException, IOException, ClientNotFoundException {
        try {
            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }

            if (AccountDao.getClientAccountById(accountId, clientId) == null) {
                throw new AccountNotFoundException("User is trying to edit an Account that does not exist. Account with id " + accountId
                        + " was not found");
            }

            //account.setAccountId(accountId);
            Boolean accountDeleted = AccountDao.deleteAccountById(accountId);

            return accountDeleted;

        } catch(NumberFormatException e) {
            throw new IllegalArgumentException("ID provided for Account must be a valid UUID");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }
    }


    public Account addNewAccount(Account a, String clientId) throws SQLException, IOException, ClientNotFoundException {

        validateAccountInformation(a);

        try {
            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }

        Account addedAccount = accountDao.addAccount(a, clientId);

        return addedAccount;

    }

 /*   public List<Account> getAccountsByQuery(String clientId, String filter) throws SQLException, IOException, ClientNotFoundException, EndpointQueryException {

        try {
            if (ClientDao.getClientById(clientId) == null) {
                throw new ClientNotFoundException("User is trying to edit a Client that does not exist. Client with id " + clientId
                        + " was not found");
            }
            //}
            *//*catch (FileNotFoundException e) {
            throw new FileNotFoundException("Property file not found: " + e);
        }*//*

        String[] filterSplit = filter.split("&");

        ArrayList<String> filterList = new ArrayList<>(
                Arrays.asList(filterSplit));

        // Hashtable<String, String> paramMap = new Hashtable<>();

        String s1 = null;
        String s2 = null;

        for (String s : filterList) {
            String[] param = s.split("=");

            if (param[0].equals("amountLessThan")) {
                s1 = "balance < " + param[1];
            }
            if (param[0].equals("amountGreaterThan")) {
                s2 = "balance > " + param[1];
            }
            // paramMap.put(param[0], param[1]);
        }

        if (s1 == null || s2 == null) {
            throw new EndpointQueryException("Endpoint query only take 'amountLessThan' and 'amountGreaterThan' as parameters. The input: " + filter);
        }
        String filterResult = "(WHERE (" + s1 + " AND " + s2 + "));";


        //System.out.println(paramMap);

        List<Account> returnedAccounts = accountDao.getC(clientId, );
    }
        return returnedAccounts;
    }*/


    public void validateAccountInformation(Account account) {
        if (account.getBalance() < 0) {
            throw new IllegalArgumentException("A negative balance input is invalid. Balance input: " + account.getBalance());
        }
        if (!Objects.equals(account.getAccountType(), "check") & !Objects.equals(account.getAccountType(), "save")) {
            throw new IllegalArgumentException("Account type must be either 'check' or 'save'. Account type input: " + account.getAccountType());
        }
    }
    public void validateAccountId(Account account) {
        account.setAccountId(account.getAccountId().trim());

        if(!account.getAccountId().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            throw new IllegalArgumentException("Account ID must be a valid UUID. Account ID input was " + account.getAccountId());
        }
    }
}

