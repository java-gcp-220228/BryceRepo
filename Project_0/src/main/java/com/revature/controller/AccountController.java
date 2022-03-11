package com.revature.controller;
import com.revature.model.Account;
import com.revature.model.Client;
import com.revature.service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;


public class AccountController implements Controller {

    private AccountService accountService;

    public AccountController() {
        this.accountService = new AccountService();
    }

    // This lambda will implicitly have "throws Exception" based on the functional interface
    // This is something to be aware of, because you might actually want to handle some exceptions
    private Handler getAllClientAccounts = (ctx) -> {
        String c_id = ctx.pathParam("clientId");

        List<Account> accounts = accountService.getAllClientAccounts(c_id);


        ctx.json(accounts);
    };

    private Handler getAccountByClientAccountId = (ctx) -> {
        String a_id = ctx.pathParam("accountId");
        String c_id = ctx.pathParam("clientId");

        Account account = accountService.getAccountById(a_id, c_id);

        ctx.json(account);
    };

    private Handler addAccount = (ctx) -> {
        Account accountToAdd = ctx.bodyAsClass(Account.class);
        Client clientToAdd = ctx.bodyAsClass(Client.class);
        Account c = accountService.addNewAccount(accountToAdd, clientToAdd);

        ctx.status(201);
        ctx.json(c);
    };

    private Handler editAccount = (ctx) -> {
        Account accountToEdit = ctx.bodyAsClass(Account.class);

        Account editedAccount = accountService.editAccount(ctx.pathParam("accountId"),
                ctx.pathParam("clientId"), accountToEdit);

        ctx.status(200);
        ctx.json(editedAccount);
    };

    private Handler deleteAccount = (ctx) -> {
        Account accountToDelete = ctx.bodyAsClass(Account.class);

        Boolean deletedAccount = accountService.deleteAccount(ctx.pathParam("accountId"), ctx.pathParam("clientId"), accountToDelete);

        ctx.status(200);
        ctx.json("Account deleted: " + deletedAccount);
    };


    @Override
    public void mapEndpoints(Javalin app) {
       /*
        app.get("/accounts", getAllAccounts);
        app.get("/accounts/{accountId}", getAccountById);
       */
        app.get("/clients/{clientId}/accounts", getAllClientAccounts);
        app.get("/clients/{clientId}/accounts/{accountId}", getAccountByClientAccountId);
        app.post("/clients/{clientId}/accounts", addAccount);
        app.put("/clients/{clientId}/accounts/{accountId}", editAccount);
        app.delete("/accounts/{accountId}", deleteAccount);
    }
}