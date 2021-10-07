package com.example.axondemo;

import io.sapl.api.pdp.AuthorizationSubscription;
import io.sapl.api.pdp.Decision;
import io.sapl.api.pdp.PolicyDecisionPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.unitofwork.UnitOfWork;

import com.example.axondemo.command.coreapi.CreateAccountRejectedCommand;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class PdpCommandInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {

    final private PolicyDecisionPoint pdp;

    @Override
    public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) throws Exception {
        CommandMessage<?> command = unitOfWork.getMessage();


        log.info("!!!!!!     PdpCommandInterceptor pdp = {}", pdp);
        // command = GenericCommandMessage{payload={CreateAccountCommand(id=9171081e-c800-4903-8f4f-c0379eebab1a,
        // deposit=150)}, metadata={'userName'->'axon'}, messageIdentifier='394d6d44-0e68-4f84-ad5a-003aeb566e8e',
        // commandName='com.example.axondemo.command.coreapi.CreateAccountCommand'
        log.info("!!!!!!     PdpCommandInterceptor command = {}", command);


        MetaData meta = command.getMetaData();
        String userName = meta.get("userName").toString();

        AuthorizationSubscription authzSubscription =
                AuthorizationSubscription.of(userName, "createAccount", "/createBankAccount");



        // nebenläufig sinnvoll????
        var authzDec = pdp.decide(authzSubscription).blockFirst();


        if (authzDec.getDecision() == Decision.DENY) {
            log.info("### PdpCommandInterceptor : pdp - denied");
            
            var co = new CreateAccountRejectedCommand(null);
            
            unitOfWork.transformMessage(com -> {
                return GenericCommandMessage.asCommandMessage(co);
            });
            log.info("transformed message command = {}", unitOfWork.getMessage());


            // authInterceptor (nicht in Reihenfolge):
            // weitergeleitet an ein Auth Aggregate (ruft pdp auf)
            // ersetzt in neues command: AuthorisiereCommand mit dem ursprünglichen als Payload
            // dann weiterleiten an die darunterliegende Struktur (geht jetzt durch den Autorisierungsinterceptor)
            
            
            //Transformiertes command wird hier weitergeleitet
            //return interceptorChain.proceed();

            return null;
        }


        log.info("### PdpCommandInterceptor : pdp - permit");

        return interceptorChain.proceed();
    }
}

