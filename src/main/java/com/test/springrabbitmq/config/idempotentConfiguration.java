package com.test.springrabbitmq.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class idempotentConfiguration {


    // To achieve idempotent when consumer retry consume from queue, we can implement with different solution
    // unique message UUID solution
    // 1. Every message generate each unique UUID and send to consumer
    // 2. Consumer after received handle the business logic, after successfully handle save the message UUID to database
    // 3. if next time received the same message, go to database to check if the message exist on database, if exist then abort
    @Bean
    public MessageConverter jacksonMessageConvertor(){

        // usage of this Message Id
        // https://stackoverflow.com/questions/37494605/why-need-messageid-in-amqp

        // why we set CreateMessageIds to true is because rabbitTemplate.convertAndSend() it will call convertMessageIfNecessary()
        // convertMessageIfNecessary() call getRequiredMessageConverter().toMessage(object, new MessageProperties());
        // then go to AbstractMessageConverter class call ToMessage(), in this class CreateMessageIds boolean is false
        // only this boolean is true, AbstractMessageConverter in ToMessage() if condition will execute messageProperties.setMessageId(UUID.randomUUID().toString());
        // and return the message with unique UUID

        // we can also don't set this boolean to true, example use snowflake algorithm to ensure idempotency
        // means we generate unique UUID to messageProperties by ourselves

        Jackson2JsonMessageConverter jjmc = new Jackson2JsonMessageConverter();
        jjmc.setCreateMessageIds(true);
        return jjmc;

    }

}
