package com.example.axondemo.command;

import java.util.UUID;

import io.sapl.api.pdp.AuthorizationSubscription;
import io.sapl.api.pdp.Decision;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.interceptors.MessageHandlerInterceptor;
import org.axonframework.modelling.command.AggregateIdentifier;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import org.axonframework.modelling.command.CommandHandlerInterceptor;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import com.example.axondemo.command.coreapi.CreateAccountCommand;
import com.example.axondemo.command.coreapi.MoneyDepositCommand;
import com.example.axondemo.command.coreapi.MoneyDepositEvent;
import com.example.axondemo.command.coreapi.MoneyWithdrawCommand;
import com.example.axondemo.command.coreapi.MoneyWithdrawEvent;

import io.sapl.api.pdp.PolicyDecisionPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Meta;


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
    public BankAccount(CreateAccountCommand command, PolicyDecisionPoint pdp, MetaData meta) throws Exception {

        // Schufa Prüfung etc.
        // Invarianten überprüfen

        log.info("#### command = {} pdp = {} meta = {} ", command, pdp, meta);

        String user = meta.get("userDummy").toString();
        log.info("### user{}", user);

        AuthorizationSubscription authzSubscription =
                AuthorizationSubscription.of("MaxUser", "create", this.getClass().getSimpleName());
        // pattern überlegen, acttion auch unique


        //block ! sequentielle Abarbeitung
        var authzDec = pdp.decide(authzSubscription).blockFirst();

        if (authzDec.getDecision() == Decision.DENY) {
            log.info("### CommandHandler : pdp - denied");
            apply(new AccountCreatedEventDenied(command.getId(), command.getDeposit()));
        }
        // simple authorization subscription schreiben
        // subject: action: create account ressource non existing account
        // im pdp fragen und access denied schmeißen oder erlauben
        // erstmal blockend (blockFirst)
        apply(new AccountCreatedEvent(command.getId(), command.getDeposit()));

    }

    @CommandHandlerInterceptor
    public void interceptCommand(Object command, InterceptorChain interceptorChain, PolicyDecisionPoint pdp) throws Exception {
        log.info("command = {} pdp = {}", command, pdp);

        AuthorizationSubscription authzSubscription =
       	AuthorizationSubscription.of("MaxUser", "create",
       	this.getClass().getSimpleName()); 
        var authzDec = pdp.decide(authzSubscription).blockFirst();
		 
		 if (authzDec.getDecision() != Decision.DENY) {
		 log.info("### CommandHandler : pdp ");
		 interceptorChain.proceed(); 
		 
		 }
		 log.info("Command intercepted");
        
    }
    
    @MessageHandlerInterceptor
    public void interceptEvent(Object message, InterceptorChain interceptorChain, PolicyDecisionPoint pdp) throws Exception {
    	log.info("Event/Command = {} pdp = {}", message, pdp);

        interceptorChain.proceed();
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.id = event.getId();
        this.deposit = event.getDeposit();
    }
    
    @CommandHandler
    public void depositBankAccount(MoneyDepositCommand command) {
    	log.info("MoneyDepositCommand = {}", command);
    	apply(new MoneyDepositEvent(command.getId(), command.getDeposit()));
    }
    
    @EventSourcingHandler
    public void on(MoneyDepositEvent event) {
    	this.id = UUID.randomUUID().toString();
        this.deposit = event.getDeposit();
    }
    
    @CommandHandler
    public void depositBankAccount(MoneyWithdrawCommand command) {
    	log.info("MoneyWithdrawCommand = {}", command);
    	apply(new MoneyWithdrawEvent(command.getId(), command.getDeposit()));
    }
    
    @EventSourcingHandler
    public void on(MoneyWithdrawEvent event) {
    	this.id = UUID.randomUUID().toString();
        this.deposit = event.getDeposit();
    }
}
