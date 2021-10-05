package com.example.axondemo.command;

import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BankAccountTest {

    private FixtureConfiguration<BankAccount> fixture;

    @Mock
    private BankAccount bankAccount;


    @BeforeEach
    public void setUp() {
        fixture = new AggregateTestFixture<>(BankAccount.class);
        fixture.registerInjectableResource(bankAccount);
    }


    @Test
    void handleAccountCreateCommand() {

        String id = "99";
        int deposit = 100;

        fixture.givenNoPriorActivity()
                .when(new CreateAccountCommand(id, deposit))
                .expectEvents(new AccountCreatedEvent(id, deposit));
    }

    @Test
    void on() {
    }
}