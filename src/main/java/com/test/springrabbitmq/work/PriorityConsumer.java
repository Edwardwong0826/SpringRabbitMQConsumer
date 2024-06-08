package com.test.springrabbitmq.work;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class PriorityConsumer {

    public static final String QUEUE_PRIORITY = "queue.test.priority";

    @RabbitListener(queues = {QUEUE_PRIORITY})
    public void processMessagePriority(String dataString, Message message, Channel channel) throws IOException {

        log.info("Priority Message : " + dataString);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }

}
