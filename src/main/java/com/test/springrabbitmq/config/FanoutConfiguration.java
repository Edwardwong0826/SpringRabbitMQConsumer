package com.test.springrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfiguration
{

    // This is one of the way to declare queue, exchange and binding in SpringAMQP
    // Not recommended use this because very tedious if future got many queues/exchange/binding to add, is just demo purpose
    // All method annotated with @Bean, it will be directly dynamic proxy by spring
    @Bean
    public FanoutExchange fanoutExchange(){

        // two ways to create the exchange
        // return ExchangeBuilder.fanoutExchange("hmall.fanout2").build();
        return new FanoutExchange("hmall.fanout2");
    }

    @Bean
    public Queue fanoutQueue3(){
        //return QueueBuilder.durable("fanout.queue3").build();
        return new Queue("fanout.queue3");
    }

    @Bean
    public Queue fanoutQueue4(){
        //return QueueBuilder.durable("fanout.queue4").build();
        return new Queue("fanout.queue4");
    }

    // two ways to binding, use constructor inject get the bean and pass in
    @Bean
    public Binding fanoutBinding3(Queue fanoutQueue3, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange);
    }

    // or direct call the method to return the bean
    @Bean
    public Binding fanoutBinding4(){
        return BindingBuilder.bind(fanoutQueue4()).to(fanoutExchange());
    }

}
