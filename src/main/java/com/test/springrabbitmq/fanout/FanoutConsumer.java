package com.test.springrabbitmq.fanout;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


// Fanout exchange will broadcast the message to the queues that binds to its exchange when publisher publish messages
// Normally one queue represent one microservice, so that when each type microservice auto-scaling to multiple instances it can consume simultaneously from the same queue
// This is because message in the queue only can be consumed by consumer one time from the queue, so need to add lock

// Example go to RabbitMq create fanout exchange name hmall.fanout under /hmall virtual host in this example
// And then create fanout queues name fanout.queue1 and fanout.queue1 and binds to name hmall.fanout exchange

// The purpose of the RabbitMQ exchange received publisher publish message and route the message according to rules to the binding queues
// So Fanout exchange will route messages to every binding queues
@Component
public class FanoutConsumer
{

    @RabbitListener(bindings = @QueueBinding(
            //value = @Queue, //this will auto create random queue name from SpringAMQP and the queue will get remove when the application is stopped
            value = @Queue(name = "fanout.queue1", durable = "true"),
            exchange = @Exchange(name = "hmall.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void listenFanoutQueue1(String message) throws InterruptedException {
        System.out.println("Consumer 1 received fanout queue 1 message : " + message);
    }

    @RabbitListener(bindings = @QueueBinding(
            //value = @Queue, //this will auto create random queue name from SpringAMQP and the queue will get remove when the application is stopped
            value = @Queue(name = "fanout.queue2", durable = "true"),
            exchange = @Exchange(name = "hmall.fanout", type = ExchangeTypes.FANOUT)
    ))
    public void listenFanoutQueue2(String message) throws InterruptedException {
        System.out.println("Consumer 2 received fanout queue 2 message = " + message);
    }

}
