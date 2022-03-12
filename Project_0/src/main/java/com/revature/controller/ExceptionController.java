package com.revature.controller;

import com.revature.exception.ClientNotFoundException;
import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

// Instead of using try-catch (directly from the Java language), we can abstract that process away
// using Javalin's exception mapping functionality
// This ExceptionController provides us with the ability to "map" exceptions in one central location
// that get thrown from the endpoint handlers themselves

public class ExceptionController implements Controller {

    private ExceptionHandler clientNotFound = (e, ctx) -> {
        ctx.status(404);
        ctx.json(e.getMessage());
    };

    private ExceptionHandler badArgument = (e, ctx) -> {
        ctx.status(400);
        ctx.json(e.getMessage());
    };

    @Override
    public void mapEndpoints(Javalin app) {
        app.exception(ClientNotFoundException.class, clientNotFound);
        app.exception(IllegalArgumentException.class, badArgument);
    }

}