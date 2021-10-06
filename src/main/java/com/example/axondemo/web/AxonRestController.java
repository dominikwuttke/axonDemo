package com.example.axondemo.web;

import com.example.axondemo.command.BankAccount;
import com.example.axondemo.command.TestCommand;
import com.example.axondemo.command.TestQuery;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import com.example.axondemo.command.coreapi.MoneyDepositCommand;
import com.example.axondemo.command.query.GetAccountByIdQuery;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;

import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


@RestController
@RequiredArgsConstructor
public class AxonRestController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
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

        Map<String,String> user = Map.of("userDummy" , "max" );
        MetaData meta = MetaData.from(user);

        CreateAccountCommand command = new CreateAccountCommand(UUID.randomUUID().toString(),150);
        commandGateway.send(GenericCommandMessage.asCommandMessage(command).andMetaData(meta));


//        commandGateway.send(new CreateAccountCommand(UUID.randomUUID().toString(),150));
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable("id") final String id) throws Exception{
        return new ResponseEntity<>(queryGateway.query(new GetAccountByIdQuery(id), BankAccount.class).get(), HttpStatus.OK);
    }

    @GetMapping("/accounts/reactive/{id}")
    Mono<BankAccount> getAccountByIdReactive(@PathVariable("id") final String id) throws Exception{
        return reactiveQueryGateway.query(new GetAccountByIdQuery(id), BankAccount.class);
    }

    @PutMapping("/")
    void add() throws Exception{
        commandGateway.send(new TestCommand());
    }
    
    @PostMapping("/accounts/{id}/{amount}")
    void depositMoneyById(@PathVariable("id") final String id, @PathVariable("amount") final int deposit) throws Exception{
        commandGateway.send(new MoneyDepositCommand(id, deposit));


    }

    private SubscriptionQueryResult<BankAccount,BankAccount> initialQuery;

    @GetMapping(value = "/subscribeAccounts", produces = MediaType.APPLICATION_NDJSON_VALUE)
    Flux<ServerSentEvent<BankAccount>> getAccounts() throws Exception{
        return reactiveQueryGateway.subscriptionQuery(new GetAccountByIdQuery("5"),ResponseTypes.instanceOf(BankAccount.class)).
                map(bankAccount -> ServerSentEvent.<BankAccount>builder().data(bankAccount).build());

    }


}
