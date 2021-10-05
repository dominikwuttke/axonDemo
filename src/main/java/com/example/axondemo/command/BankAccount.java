package com.example.axondemo.command;

import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import com.example.axondemo.command.coreapi.DepositAccountCommand;
import com.example.axondemo.command.coreapi.DepositChangedEvent;

import io.sapl.api.pdp.PolicyDecisionPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.interceptors.MessageHandlerInterceptor;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.CommandHandlerInterceptor;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
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
    public BankAccount(CreateAccountCommand command, PolicyDecisionPoint pdp) {
        log.info("# command = {} pdp = {}", command, pdp);
        this.id = command.getId();
        
        // simple authorization subscription schreiben
        // subject: action: create account ressource non existing account
        // im pdp fragen und access denied schmeißen oder erlauben
        // erstmal blockend (blockFirst)
        AggregateLifecycle.apply(new AccountCreatedEvent(command.getId(), command.getDeposit()));
    }

    @CommandHandler
    public void handle(DepositAccountCommand command, PolicyDecisionPoint pdp) {
    	log.info("## command = {} pdp = {}", command, pdp);
    	this.deposit = command.getDeposit();
    	AggregateLifecycle.apply(new DepositChangedEvent(command.getId(), command.getDeposit()));
    }
    
    @MessageHandlerInterceptor
    public void intercept(Object command, InterceptorChain interceptorChain) throws Exception {
    	log.info("Event = {} pdp = {}", command, 21);

        // wie getriggert? oder allgemeine Klasse?
        // welcher user triggert event
        // spring security, metadaten an command anfügen, metadaten in commandhandler injecten lassen,
        // eventuell schon suer daten enthalten?
        interceptorChain.proceed();
    }
    
	@CommandHandlerInterceptor
	public void interceptCommand(CreateAccountCommand command, InterceptorChain interceptorChain) throws Exception {
		log.info("command = {} pdp = {}", command, 21);

		// wie getriggert? oder allgemeine Klasse?
		// welcher user triggert event
		// spring security, metadaten an command anfügen, metadaten in commandhandler
		// injecten lassen,
		// eventuell schon suer daten enthalten?
		interceptorChain.proceed();
	}

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.id = event.getId();
        this.deposit = event.getDeposit();
    }
}
