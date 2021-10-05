package com.example.axondemo.repository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.axondemo.command.BankAccount;


public interface BankRepository extends ReactiveMongoRepository<BankAccount, String>  {

}
