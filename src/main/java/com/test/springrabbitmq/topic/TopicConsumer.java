package com.test.springrabbitmq.topic;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicConsumer
{

//    // * means any 1 word matches
//    // # means 0 or more word matches
//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue,
//                    exchange = @Exchange(type = "topic", name = "topics"),
//                    key = {"user.save", "user.*"}
//            )
//    })
//    public void receive1(String message)
//    {
//        System.out.println("message 1 = " + message);
//    }
//
//    @RabbitListener(bindings = {
//            @QueueBinding(
//                    value = @Queue,
//                    exchange = @Exchange(type = "topic", name = "topics"),
//                    key = {"order.#", "product.#", "user.*"}
//            )
//    })
//    public void receive2(String message)
//    {
//        System.out.println("message 2 = " + message);
//    }

    // There is two ways to declare queue, exchange and binding in SpringAMQP
    // First way is use @Bean in configuration class
    // Second way is use @RabbitListener and recommended to use this as less tedious than first way
    // We can use @RabbitListener to declare queue and exchange as well
    // If the queue / exchange or routing key not create or exist in RabbitMQ, it will help us auto create and bind

    // We can use wild card in topic exchange to route message according the rules
    // * means any 1 word matches
    // # means 0 or more word matches

    // topic.queue1 routing key = china.#
    // Example routing key china.#, publisher send message to this routing key china.weather
    // It will receive, if send china.weather.xxx, it will not receive
    @RabbitListener(bindings = @QueueBinding(
            //value = @Queue, //this will auto create random queue name from SpringAMQP and the queue will get remove when the application is stopped
            value = @Queue(name = "topic.queue1", durable = "true"),
            exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
            key = {"china.#"}
    ))
    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String message) throws InterruptedException {
        System.out.println("Consumer 1 received topic queue 1 message : " + message);
    }

    // topic.queue2 routing key = #.news
    @RabbitListener(bindings = @QueueBinding(
            //value = @Queue, //this will auto create random queue name from SpringAMQP and the queue will get remove when the application is stopped
            value = @Queue(name = "topic.queue2", durable = "true"),
            exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
            key = {"#.news"}
    ))
    public void listenTopicQueue2(String message) throws InterruptedException {
        System.out.println("Consumer 2 received topic queue 2 message = " + message);
    }

}
