package com.test.springrabbitmq.direct;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class DirectConsumer
{

//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue,
//                    exchange = @Exchange(value = "logs_direct", type = "direct"),
//                    key = {"info","error","warning"}
//            )
//    })
//    public void received1(String message)
//    {
//        System.out.println("message1 = " + message);
//    }
//
//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue,
//                    exchange = @Exchange(value = "logs_direct", type = "direct"),
//                    key = {"error"}
//            )
//    })
//    public void received2(String message)
//    {
//        System.out.println("message2 = " + message);
//    }

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
    public void listenDirectQueue1(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws InterruptedException, IOException {
        System.out.println("Consumer 1 received direct queue 1 message : " + message);
    }

    @RabbitListener(bindings = @QueueBinding(
            //value = @Queue, //this will auto create random queue name from SpringAMQP and the queue will get remove when the application is stopped
            value = @Queue(name = "direct.queue2", durable = "true"),
            exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws InterruptedException, IOException {
        System.out.println("Consumer 2 received direct queue 2 message = " + message);
    }

}
