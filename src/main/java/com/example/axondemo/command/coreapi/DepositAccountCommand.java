package com.example.axondemo.command.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class DepositAccountCommand {

    @TargetAggregateIdentifier
    String id;
    int deposit;
}