package com.revature.controller;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


public class AccountController implements Controller {

    private AccountService accountService;

    public AccountController() {
        this.accountService = new AccountService();
    }

    // This lambda will implicitly have "throws Exception" based on the functional interface
    // This is something to be aware of, because you might actually want to handle some exceptions
    private Handler getAllAccounts = (ctx) -> {
        //String c_id = ctx.pathParam("clientId");

        List<Account> accounts = accountService.getAllAccounts();


        ctx.json(accounts);
    };

    private Handler getAccountByClientAccountId = (ctx) -> {
        String a_id = ctx.pathParam("accountId");
        String c_id = ctx.pathParam("clientId");

        Account account = accountService.getClientAccountById(a_id, c_id);

        ctx.json(account);
    };

    private Handler addAccount = (ctx) -> {
        Account accountToAdd = ctx.bodyAsClass(Account.class);
        Client clientToAdd = ctx.bodyAsClass(Client.class);
        Account a = accountService.addNewAccount(accountToAdd, clientToAdd);

        ctx.status(201);
        ctx.json(a);
    };

    private Handler editAccount = (ctx) -> {
        Account accountToEdit = ctx.bodyAsClass(Account.class);

        Account editedAccount = accountService.editAccount(
                accountToEdit,
                ctx.pathParam("accountId"),
                ctx.pathParam("clientId")
        );

        ctx.status(200);
        ctx.json(editedAccount);
    };

    private Handler deleteAccount = (ctx) -> {
        Account accountToDelete = ctx.bodyAsClass(Account.class);

        Boolean deletedAccount = accountService.deleteAccount(
                ctx.pathParam("accountId"),
                ctx.pathParam("clientId"),
                accountToDelete
        );

        ctx.status(200);
        ctx.json("Account deleted: " + deletedAccount);
    };

    private Handler getAllClientAccounts = (ctx) -> {
        String c_id = ctx.pathParam("clientId");
        System.out.println(c_id);


        List<Account> accounts = accountService.getAllClientAccounts(c_id);


        ctx.json(accounts);
    };

    private Handler getAccountsByQuery = (ctx) -> {
        String c_id = ctx.pathParam("clientId");
        String filter = ctx.pathParam("filter");

        List<Account> accounts = accountService.getAccountsByQuery(c_id, filter);


    };


    @Override
    public void mapEndpoints(Javalin app) {
        app.get("/accounts", getAllAccounts);  // for dev only, will remove later
        app.get("/clients/{clientId}/accounts", getAllClientAccounts);
        app.post("/clients/{clientId}/accounts", addAccount);
        app.get("/clients/{clientId}/accounts/{accountId}", getAccountByClientAccountId);
        app.put("/clients/{clientId}/accounts/{accountId}", editAccount);
        app.delete("/clients/{clientId}/accounts/{accountId}", deleteAccount);
        app.get("/clients/{clientId}/accounts?<filter>", getAccountsByQuery);
        // filters = amountLessThan=2000&amountGreaterThan=400
    }
}