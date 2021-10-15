package com.example.axondemo.command;

import java.lang.annotation.Annotation;
import java.util.UUID;

import com.example.axondemo.annotations.PreEnforce;
import com.example.axondemo.command.coreapi.*;
import io.sapl.api.pdp.AuthorizationSubscription;
import io.sapl.api.pdp.Decision;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.common.annotation.AnnotationUtils;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.interceptors.MessageHandlerInterceptor;
import org.axonframework.modelling.command.AggregateIdentifier;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import org.axonframework.modelling.command.CommandHandlerInterceptor;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.data.mongodb.core.mapping.Document;

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
@PreEnforce(subject="Tom")
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


        // pattern überlegen, acttion auch unique


        //block ! sequentielle Abarbeitung

        // simple authorization subscription schreiben
        // subject: action: create account ressource non existing account
        // im pdp fragen und access denied schmeißen oder erlauben
        // erstmal blockend (blockFirst)
        apply(new AccountCreatedEvent(command.getId(), command.getDeposit()));

    }

//    @CommandHandlerInterceptor
//    public void interceptCommand(Object command, InterceptorChain interceptorChain, PolicyDecisionPoint pdp) throws Exception {
//        log.info("command = {} pdp = {}", command, pdp);
//
//        AuthorizationSubscription authzSubscription =
//       	AuthorizationSubscription.of("MaxUser", "create",
//       	this.getClass().getSimpleName());
//        var authzDec = pdp.decide(authzSubscription).blockFirst();
//
//		 if (authzDec.getDecision() != Decision.DENY) {
//		 log.info("### CommandHandler : pdp ");
//		 interceptorChain.proceed();
//
//		 }
//		 log.info("Command intercepted");
//
//    }
    
//    @MessageHandlerInterceptor
//    public void interceptEvent(Object message, InterceptorChain interceptorChain, PolicyDecisionPoint pdp) throws Exception {
//    	log.info("Event/Command = {} pdp = {}", message, pdp);
//
//        interceptorChain.proceed();
//    }

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

    @PreEnforce
    @CommandHandler
    public void testAnnot(TestAnnotCommand command, MetaData meta) {
        log.info("CommandHandler = {}", command);


        Class aggClass = this.getClass();
        Annotation[] annotations =aggClass.getAnnotations();
        log.info(" annotations n: {}", annotations.length);
        log.info(" this.getCLass {}", aggClass);

        for(Annotation annotation : annotations) {
            log.info(" annotations {}", annotation);
        }
    }

    @CommandHandlerInterceptor
    public void interceptTestAnnot(TestAnnotCommand command, InterceptorChain interceptorChain,
                                                          PolicyDecisionPoint pdp) throws Exception {
        log.info("CommandHandlerInterceptor = {}", command);

        Class aggClass = this.getClass();
        Annotation[] annotations =aggClass.getAnnotations();

        // with standard code (Reflection API)
        for(Annotation annotation : annotations) {
            log.info(" annotations {}", annotation);
            if (annotation instanceof PreEnforce) {
                log.info(" +++ aggregate has PreEnforce annotation");
                AuthorizationSubscription authzSubscription =
                        AuthorizationSubscription.of("MaxUser", "deposit",
                                this.getClass().getSimpleName() + this.getId());
                var authzDec = pdp.decide(authzSubscription).blockFirst();

                if (authzDec.getDecision() != Decision.DENY) {
                    log.info("### CommandHandler : pdp ");
                    interceptorChain.proceed();
                }
            }
        }

        //with Axon Utils
        if (AnnotationUtils.isAnnotationPresent(this.getClass(), PreEnforce.class)) {
            log.info(" +++ AnnotationUtils: aggregate has PreEnforce annotation");
        }



        interceptorChain.proceed();
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
