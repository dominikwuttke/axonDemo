package com.example.axondemo.command;

import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.data.mongodb.core.mapping.Document;



@Data
@Slf4j
@Document
@Aggregate
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @AggregateIdentifier
    private String id;
    private int deposit;

    @CommandHandler
    public BankAccount(CreateAccountCommand command) {
        AggregateLifecycle.apply(new AccountCreatedEvent(command.getId(), command.getDeposit()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.id = event.getId();
        this.deposit = event.getDeposit();
    }
}
