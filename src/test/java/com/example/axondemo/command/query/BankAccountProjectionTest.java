package com.example.axondemo.command.query;

import com.example.axondemo.command.BankAccount;
import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import com.example.axondemo.web.axonRestController;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BankAccountProjectionTest {


    private FixtureConfiguration<BankAccount> fixture;

    @Mock
    private BankAccount bankAccount;

    @Mock
    private CommandGateway commandGateway;
    @Mock
    private QueryGateway queryGateway;
    @Mock
    private EntityLinks entityLinks;

    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(BankAccount.class);
        fixture.registerInjectableResource(bankAccount);
    }





    @Test
    void on() {

        String id = "90";
        int deposit = 10;
        fixture.givenNoPriorActivity()
                .when(new AccountCreatedEvent(id, deposit)).expectNoEvents();


    }
}