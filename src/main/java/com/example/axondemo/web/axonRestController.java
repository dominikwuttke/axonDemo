package com.example.axondemo.web;


import com.example.axondemo.command.BankAccount;
import com.example.axondemo.command.TestCommand;
import com.example.axondemo.command.TestQuery;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import com.example.axondemo.command.query.GetAccountByIdQuery;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.queryhandling.QueryGateway;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class axonRestController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final EntityLinks entityLinks;
    private final ReactorQueryGateway reactiveQueryGateway;


    @GetMapping("/")
    Integer get() throws Exception{
        return queryGateway.query(new TestQuery(), Integer.class).get();

    }

    @GetMapping("/testCommand")
    void testAdd() throws Exception{
        commandGateway.send(new TestCommand());
    }

    @GetMapping("/createBankAccount")
    void createBankAccount() throws Exception{
        commandGateway.send(new CreateAccountCommand(UUID.randomUUID().toString(),150));
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(queryGateway.query(new GetAccountByIdQuery(id), BankAccount.class).get(), HttpStatus.OK);
    }

    @GetMapping("/accounts/reactive/{id}")
    Mono<BankAccount> getAccountByIdReactive(@PathVariable String id) throws Exception{
        return reactiveQueryGateway.query(new GetAccountByIdQuery(id), BankAccount.class);
    }

    @PutMapping("/")
    void add() throws Exception{
        commandGateway.send(new TestCommand());
    }

}
