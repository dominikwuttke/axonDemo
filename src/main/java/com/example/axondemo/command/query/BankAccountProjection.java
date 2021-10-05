package com.example.axondemo.command.query;

import com.example.axondemo.command.BankAccount;
import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import com.example.axondemo.repository.BankRepository;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BankAccountProjection {

    private BankAccount account;
    private final QueryUpdateEmitter queryUpdateEmitter;
    private final BankRepository bankRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {

        String id = event.getId();
        int deposit = event.getDeposit();

        account = new BankAccount();
        account.setId(id);
        account.setDeposit(deposit);

        queryUpdateEmitter.emit(GetAccountByIdQuery.class,query -> true,account);
    }

    @QueryHandler
    public BankAccount handle(GetAccountByIdQuery query) {
        return bankRepository.findById(query.getId()).block();
    }
    



}
