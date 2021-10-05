package com.example.axondemo.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public class TestEventCreated {

    @TargetAggregateIdentifier
    private UUID id;

    private TestEventCreated(){}

    public UUID getId() { return id;}

    public TestEventCreated(UUID id) { this.id = id;
    }
}
