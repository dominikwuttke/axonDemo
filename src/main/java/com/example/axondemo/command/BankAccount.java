package com.example.axondemo.command;

import java.util.UUID;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.interceptors.MessageHandlerInterceptor;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.CommandHandlerInterceptor;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import com.example.axondemo.command.coreapi.MoneyDepositCommand;
import com.example.axondemo.command.coreapi.MoneyDepositEvent;

import io.sapl.api.pdp.PolicyDecisionPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;



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


    /**
     * Interceptor funktioniert bei Konstruktur nicht.
     */
    @CommandHandler
    public BankAccount(CreateAccountCommand command, PolicyDecisionPoint pdp) {
        log.info("#### command = {} pdp = {}", command, pdp);

        // simple authorization subscription schreiben
        // subject: action: create account ressource non existing account
        // im pdp fragen und access denied schmeißen oder erlauben
        // erstmal blockend (blockFirst)
        AggregateLifecycle.apply(new AccountCreatedEvent(command.getId(), command.getDeposit()));
    }

    @CommandHandlerInterceptor
    public void interceptCommand(Object command, InterceptorChain interceptorChain, PolicyDecisionPoint pdp) throws Exception {
        log.info("command = {} pdp = {}", command, pdp);

        // wie getriggert? oder allgemeine Klasse?
        // welcher user triggert event
        // spring security, metadaten an command anfügen, metadaten in commandhandler injecten lassen,
        // eventuell schon suer daten enthalten?
        interceptorChain.proceed();
    }
    
    @MessageHandlerInterceptor
    public void interceptEvent(Object event, InterceptorChain interceptorChain, PolicyDecisionPoint pdp) throws Exception {
    	log.info("Event = {} pdp = {}", event, pdp);

        interceptorChain.proceed();
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.id = event.getId();
        this.deposit = event.getDeposit();
    }
    
    @CommandHandler
    public BankAccount(MoneyDepositCommand command) {
    	AggregateLifecycle.apply(new MoneyDepositEvent(command.getId(), command.getDeposit()));
    }
    
    @EventSourcingHandler
    public void on(MoneyDepositEvent event) {
        //this.id = event.getId();
    	this.id = UUID.randomUUID().toString();
        this.deposit = event.getDeposit();
    }
}
