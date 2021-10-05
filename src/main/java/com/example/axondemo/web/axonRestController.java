package com.example.axondemo.web;


import com.example.axondemo.command.BankAccount;
import com.example.axondemo.command.TestCommand;
import com.example.axondemo.command.TestQuery;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import com.example.axondemo.command.query.BankAccountProjection;
import com.example.axondemo.command.query.GetAccountByIdQuery;
import com.example.axondemo.command.query.GetAccountsQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class axonRestController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final EntityLinks entityLinks;

    public  axonRestController(CommandGateway commandGateway, QueryGateway queryGateway, EntityLinks entityLinks){
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.entityLinks = entityLinks;
    }

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
        commandGateway.send(new CreateAccountCommand("123456",150));
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(queryGateway.query(new GetAccountByIdQuery(id), BankAccount.class).get(), HttpStatus.OK);
    }
//
//    @GetMapping("/accounts/reactive/{id}")
//    Mono<BankAccount> getAccountByIdReactive(@PathVariable String id) throws Exception{
//        return Mono.just(queryGateway.query(new GetAccountByIdQuery(id), BankAccount.class).get());
//    }

    @PutMapping("/")
    void add() throws Exception{
        commandGateway.send(new TestCommand());
    }

}
