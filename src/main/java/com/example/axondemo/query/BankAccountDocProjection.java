package com.example.axondemo.query;

import com.example.axondemo.command.BankAccount;
import com.example.axondemo.command.coreapi.AccountCreatedEvent;
import com.example.axondemo.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BankAccountDocProjection {

    private final BankRepository bankRepository;

    @EventHandler
    public void on(AccountCreatedEvent event) {

        log.info("BankAccountDocProjection: handling AccountCreatedEvent");
        BankAccount account = new BankAccount(event.getId(), event.getDeposit());
        bankRepository.save(account).block();
    }
}