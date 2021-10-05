package com.example.axondemo.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
public class testagregate {

    @AggregateIdentifier
    private UUID id;

    protected testagregate(){}

    @CommandHandler
    public testagregate(TestCommand testCommand){
        UUID id = UUID.randomUUID();
        AggregateLifecycle.apply(new TestEventCreated(id));
    }

    @EventSourcingHandler
    protected void on(TestEventCreated testEventCreated) {this.id = testEventCreated.getId();}
}
