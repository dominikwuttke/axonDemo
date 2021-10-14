package com.example.axondemo.command.coreapi;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
public class TestAnnotCommand {

    @TargetAggregateIdentifier
    String id;
}


