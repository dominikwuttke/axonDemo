package com.example.axondemo;


import com.example.axondemo.command.BankAccount;
import com.mongodb.client.MongoClient;
import io.sapl.api.pdp.PolicyDecisionPoint;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.*;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.messaging.ScopeAwareProvider;
import org.axonframework.messaging.annotation.MessageHandler;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.messaging.interceptors.CorrelationDataInterceptor;
import org.axonframework.modelling.saga.ResourceInjector;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.upcasting.event.EventUpcaster;
import org.axonframework.spring.config.AxonConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.axonframework.config.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class MyAxonSpringConfiguration {

    // The Event store `EmbeddedEventStore` delegates actual storage and retrieval of events to an `EventStorageEngine`.
    @Bean
    public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
        return EmbeddedEventStore.builder()
                .storageEngine(storageEngine)
                .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
                .build();
    }

    // The `MongoEventStorageEngine` stores each event in a separate MongoDB document
    @Bean
    public EventStorageEngine storageEngine(MongoClient client) {
        return MongoEventStorageEngine.builder()
                .mongoTemplate(DefaultMongoTemplate.builder().mongoDatabase(client).build())
                .build();
    }

    @Bean
    public CommandBus registerMessageHandlerInterceptors(PolicyDecisionPoint pdp) {
        log.info("### registering MessageHandler Interceptors");
        CommandBus commandBus = SimpleCommandBus.builder().build();
        commandBus.registerHandlerInterceptor(new MetaDataCommandInterceptor());
        commandBus.registerHandlerInterceptor(new PdpCommandInterceptor(pdp));
        return commandBus;
    }

    @Bean
    CommandBus configureCommandBUs(CommandBus commandBus) {

        //refelction rekursiv

        //wenn Aggregat gefunden
        //suche @COmmandHandler
        //wenn zusätzlich @Preenforce
        // registriere CommandHandlerInterceptor



//        MessageHandler handler =
        //registriere CommandHandlerInterceptor auf CommandBus
//        commandBus.subscribe("testCommand", commandHandler);

        return commandBus;
    }

//    @Bean
//    public void registerAggregates() {
//
//        log.info(" configureAggregates()");
//        Configurer configurer = DefaultConfigurer.defaultConfiguration().configureAggregate(BankAccount.class);
//    }

//    @Bean
//    public SimpleCommandBus commandBus(TransactionManager txManager, AxonConfiguration axonConfiguration) {
//
//        Class aggClass = axonConfiguration.aggregateConfiguration(BankAccount.class).getClass();
//        aggClass.getAnnotations();
//
//
//
////        SimpleCommandBus commandBus =
////                SimpleCommandBus.builder()
////                        .transactionManager(txManager)
////                        .messageMonitor(axonConfiguration.messageMonitor(CommandBus.class, "commandBus"))
////                        .build();
////        commandBus.registerHandlerInterceptor(
////                new CorrelationDataInterceptor<>(axonConfiguration.correlationDataProviders())
////        );
////        return commandBus;
//    }

    @Bean
    public void test() {
        log.info(" +++++++++ config mehtod was called");


       // Entry point of the Axon Configuration API. It implements the Configurer interface, providing access to the methods to
       // configure the default Axon components.
       // public class DefaultConfigurer implements Configurer
        DefaultConfigurer configurer;
        //
        //        /**
        //         * Initialize the Configurer.
        //         */
        //    protected DefaultConfigurer() {
        //            components.put(ParameterResolverFactory.class,
        //                    new Component<>(config, "parameterResolverFactory", this::defaultParameterResolverFactory));
        //            components.put(Serializer.class, new Component<>(config, "serializer", this::defaultSerializer));
        //            components.put(CommandBus.class, new Component<>(config, "commandBus", this::defaultCommandBus));
        //            components.put(EventBus.class, new Component<>(config, "eventBus", this::defaultEventBus));
        //            components.put(EventStore.class, new Component<>(config, "eventStore", org.axonframework.config.Configuration::eventStore));
        //            components.put(CommandGateway.class, new Component<>(config, "commandGateway", this::defaultCommandGateway));
        //            components.put(QueryBus.class, new Component<>(config, "queryBus", this::defaultQueryBus));
        //            components.put(
        //                    QueryUpdateEmitter.class, new Component<>(config, "queryUpdateEmitter", this::defaultQueryUpdateEmitter)
        //            );
        //            components.put(QueryGateway.class, new Component<>(config, "queryGateway", this::defaultQueryGateway));
        //            components.put(ResourceInjector.class,
        //                    new Component<>(config, "resourceInjector", this::defaultResourceInjector));
        //            components.put(ScopeAwareProvider.class,
        //                    new Component<>(config, "scopeAwareProvider", this::defaultScopeAwareProvider));
        //            components.put(DeadlineManager.class, new Component<>(config, "deadlineManager", this::defaultDeadlineManager));
        //            components.put(EventUpcaster.class, upcasterChain);
        //            components.put(EventGateway.class, new Component<>(config, "eventGateway", this::defaultEventGateway));
        //            components.put(TagsConfiguration.class, new Component<>(config, "tags", c -> new TagsConfiguration()));
        //            components.put(Snapshotter.class, new Component<>(config, "snapshotter", this::defaultSnapshotter));
        //        }


        // public static Configurer defaultConfiguration(boolean autoLocateConfigurerModules) {
        //     DefaultConfigurer configurer = new DefaultConfigurer();
        // Returns a Configurer instance with default components configured, such as a {@link SimpleCommandBus} and
        //  {@link SimpleEventBus}.
        //  @param autoLocateConfigurerModules flag indicating whether ConfigurerModules on the classpath should be
        // automatically retrieved. Should be set to {@code false} when using an
        // application container, such as Spring or CDI.
        DefaultConfigurer defaultConfigurer = (DefaultConfigurer) DefaultConfigurer.defaultConfiguration(false);

//        defaultConfigurer.configureAggregate(BankAccount.class)



    }

//    Consumer<Configuration> myConsumer = (config) ->
//    {
//        System.out.println("y = " );
//    };
//
//    //    Configurer configurer = DefaultConfigurer.defaultConfiguration()/
//    Configurer configurer = DefaultConfigurer.defaultConfiguration().onInitialize(config -> {
//        return DefaultConfigurer.defaultConfiguration();
//
//    });


}
