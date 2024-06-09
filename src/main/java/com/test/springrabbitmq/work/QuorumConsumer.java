package com.test.springrabbitmq.work;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class QuorumConsumer {

    public static final String QUEUE_QUORUM = "queue.quorum.test";

    // For testing High Availability
    // we can go to Rabbit node in this case was docker container CLI run below command on the leader node
    // the cluster will elect the new leader, because quorum implemented Raft Consensus Algorithm for leader election and data replication in distributed system
    // - rabbitmqctl stop_app

    // if we straight stop the RabbitMQ leader node in docker, the other nodes management console cannot be access, although it was already elected the new leader node
    @RabbitListener(queues = {QUEUE_QUORUM})
    public void processQuorumMessage(String dataString, Message message, Channel channel) throws IOException {

        log.info("Quorum Message : " + dataString);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }

}
