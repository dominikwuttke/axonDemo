package com.example.axondemo.web;


import com.example.axondemo.command.TestCommand;
import com.example.axondemo.command.TestQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;

import org.springframework.hateoas.server.EntityLinks;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class axonRestController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private final EntityLinks entityLinks;

    public  axonRestController(CommandGateway commandGateway, QueryGateway queryGateway, EntityLinks entityLinks){
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
        this.entityLinks = entityLinks;
    }

    @GetMapping("/")
    Integer get() throws Exception{
        return queryGateway.query(new TestQuery(), Integer.class).get();

    }

    @GetMapping("/testCommand")
    void testAdd() throws Exception{
        commandGateway.send(new TestCommand());
    }

    @PutMapping("/")
    void add() throws Exception{
        commandGateway.send(new TestCommand());
    }

}
