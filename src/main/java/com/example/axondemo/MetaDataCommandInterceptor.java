package com.example.axondemo;

import com.example.axondemo.command.coreapi.CreateAccountCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.messaging.InterceptorChain;
import org.axonframework.messaging.MessageHandlerInterceptor;
import org.axonframework.messaging.MetaData;
import org.axonframework.messaging.unitofwork.UnitOfWork;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.UUID;

@Slf4j
public class MetaDataCommandInterceptor implements MessageHandlerInterceptor<CommandMessage<?>> {

    @Override
    public Object handle(UnitOfWork<? extends CommandMessage<?>> unitOfWork, InterceptorChain interceptorChain) throws Exception {
        CommandMessage<?> command = unitOfWork.getMessage();


        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        log.info("MetaDataCommandInterceptor: username from Spring: user = {}", username);

        MetaData meta = command.getMetaData();

        unitOfWork.transformMessage(com -> {
            Map<String, String> user = Map.of("userName", username);
            return GenericCommandMessage.asCommandMessage(com).andMetaData(user);
        });

        return interceptorChain.proceed();

    }

}
