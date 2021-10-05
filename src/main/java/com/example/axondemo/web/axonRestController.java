package com.example.axondemo.web;


import com.example.axondemo.command.BankAccount;
import com.example.axondemo.command.TestCommand;
import com.example.axondemo.command.TestQuery;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import com.example.axondemo.command.query.BankAccountProjection;
import com.example.axondemo.command.query.GetAccountByIdQuery;
import com.example.axondemo.command.query.GetAccountsQuery;
import com.example.axondemo.command.query.TrackBankAccount;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
        commandGateway.send(new CreateAccountCommand(UUID.randomUUID().toString(),150));
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable String id) throws Exception{
        return new ResponseEntity<>(queryGateway.query(new GetAccountByIdQuery(id), BankAccount.class).get(), HttpStatus.OK);
    }

    @GetMapping("/accounts/reactive/{id}")
    Mono<BankAccount> getAccountByIdReactive(@PathVariable String id) throws Exception{
        return Mono.just(queryGateway.query(new GetAccountByIdQuery(id), BankAccount.class).get());
    }

    @PutMapping("/")
    void add() throws Exception{
        commandGateway.send(new TestCommand());
    }

    private SubscriptionQueryResult<Optional<BankAccount>,BankAccount> initialQuery;

    @GetMapping("/subscribeAccounts")
    Stream<BankAccount> getAccounts() throws Exception{
        initialQuery = queryGateway.subscriptionQuery(new GetAccountByIdQuery("5"), ResponseTypes.optionalInstanceOf(BankAccount.class),ResponseTypes.instanceOf(BankAccount.class));
        initialQuery.updates().subscribe(account -> System.out.printf("account "+account.getId()));

        return initialQuery.initialResult().block().stream();
    }


}
