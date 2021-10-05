package com.example.axondemo.command.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;


public class AccountCreatedEvent {

    @TargetAggregateIdentifier
    String id;
    int deposit;

    public AccountCreatedEvent(String id, int deposit) {
        this.id = id;
        this.deposit = deposit;
    }

    public String getId() {
        return id;
    }

    public int getDeposit(){
        return deposit;
    }
}
