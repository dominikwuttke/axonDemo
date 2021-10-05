package com.example.axondemo.command.query;


import com.example.axondemo.command.TestEventCreated;
import com.example.axondemo.command.TestQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class TestProjection {

    private Set<UUID> examples = new HashSet<>();

    @EventHandler
    protected void  on(TestEventCreated testEventCreated) { examples.add(testEventCreated.getId());
    }

    @QueryHandler
    int on(TestQuery testQuery) { return examples.size();}

}
