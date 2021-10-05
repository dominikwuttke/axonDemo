package com.example.axondemo.command.query;

import com.example.axondemo.command.BankAccount;
import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;

import java.util.List;

public class BankAccountProjection {

    private BankAccount account;

    @EventHandler
    public void on(AccountCreatedEvent event) {

        String id = event.getId();
        int deposit = event.getDeposit();

        account = new BankAccount();
        account.setId(id);
        account.setDeposit(deposit);
    }

    @QueryHandler
    public BankAccount handle(GetAccountByIdQuery query) {

        //fetch by id ...
        return account;
    }
}
