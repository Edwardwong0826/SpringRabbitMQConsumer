package com.test.springrabbitmq.direct;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class DirectConsumer
{

    // There is two ways to declare queue, exchange and binding in SpringAMQP
    // First way is use @Bean in configuration class
    // Second way is use @RabbitListener and recommended to use this as less tedious than first way
    // We can use @RabbitListener to declare queue and exchange as well
    // If the queue / exchange or routing key not create or exist in RabbitMQ, it will help us auto create and bind
    @RabbitListener(bindings = @QueueBinding(
            //value = @Queue, //this will auto create random queue name from SpringAMQP and the queue will get remove when the application is stopped
            value = @Queue(name = "direct.queue1", durable = "true"),
            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue1(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws InterruptedException, IOException {
        System.out.println("Consumer 1 received direct queue 1 message : " + new String(message.getBody()));
    }

    @RabbitListener(bindings = @QueueBinding(
            //value = @Queue, //this will auto create random queue name from SpringAMQP and the queue will get remove when the application is stopped
            value = @Queue(name = "direct.queue2", durable = "true"),
            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws InterruptedException, IOException {
        System.out.println("Consumer 2 received direct queue 2 message = " + new String(message.getBody()));
    }

}
