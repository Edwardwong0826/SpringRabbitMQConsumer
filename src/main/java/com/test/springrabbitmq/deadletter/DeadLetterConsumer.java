package com.test.springrabbitmq.deadletter;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class DeadLetterConsumer {

    public static final String QUEUE_NORMAL = "queue.normal.video";
    public static final String QUEUE_DEAD_LETTER = "queue.dead.letter.video";

    // There will be 3 scenario message will become dead letter
    // 1. rejected - when message being reject , basicNack() / basicReject() and requeue = false
    // 2. overflow - when queue max length has reached, example 10 max, according to First In First Out, the earlier message will become Dead Letter Message
    // 3. timeout - when message timeout and haven't consume

    // we need to create Dead Letter Exchange (DLX) and queue for receive Dead Letter Message
    // we set the dead letter related config in the normal queue creation arguments
    // x-dead-letter-exchange - The Dead Letter Exchange, DLX
    // x-dead-letter-routing-key - routing key route to which Dead Letter Queue
    // max-length - to set queue maximum length to received message
    @RabbitListener(queues = {QUEUE_NORMAL})
    public void processMessageNormaL(Message message, Channel channel) throws IOException {

        // listen queue normally, but reject message
        log.info("message received but rejected");
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);

    }

    @RabbitListener(queues = {QUEUE_DEAD_LETTER})
    public void processDeadLetterMessage(String dataString, Message message, Channel channel) throws IOException {

        // listen dead letter queue
        log.info("dead letter message received : {}", dataString);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }

}
