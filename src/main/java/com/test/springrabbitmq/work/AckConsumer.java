package com.test.springrabbitmq.work;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AckConsumer {

    // Message reply in RabbitMQ means does the consumer successfully consume the message or not
    // RabbitMQ use message manual ack the message will not lose, it will requeue to queue to retry consume by other
    // https://stackoverflow.com/questions/38728668/spring-rabbitmq-using-manual-channel-acknowledgement-on-a-service-with-rabbit
    // https://www.cnblogs.com/liang9479/p/17240141.html
    // every message has unique tag, rabbitmq identify the message by this, so that can manual ack or nack
    // need to set acknowledge-mode: manual
    // for those message haven't ack, when the consumer is down, rabbitMQ will requeue back to channel and send other consumer continue to consume

    // If want to use RabbitMQ library to do manual ack, can do below
    // for manual ack first parameter is tag, second parameter is it acknowledge all messages in the channel
    // channel.basicAck(tag, true);

    // For manual nack
    // first parameter is tag, second parameter is it acknowledge all messages in the channel
    // third parameter is it requeue
    // channel.basicNack(tag,true,true);

    // Spring AMQP already implements the message reply functionality for us, we just need to select acknowledge-mode
    // None - not handle, after message send to consumer ack immediately , message will delete queue directly
    // Manual - can send ack or reject in our business logic, provide flexibility for us
    // Auto - Spring AMQP use AOP do around advice with our consume logic part, when logic execute normal auto return ack
    //      - if logic got exception or error, auto return nack
    //      - if message handle or validation error, auto return reject, noted reject one will be refused and deleted from queue

    // Ack - RabbitMQ delete message from queue
    // Nack - Message will be requeue and resend to consumer again until it was success
    // Reject - Refused and RabbitMQ deleted from queue

    // when consumer got exception, message will keep requeue to RabbitMQ queue and resend back to consumer in endless loop WHICH causing stress to RabbitMQ
    // Spring AMQP got retry mechanism to let when consumer got exception try to do local retry first instead of unlimited requeue back to RabbitMQ queue
    // once Spring retry enabled, when the attempts exhausted and still failed, it will go MessageRecover interface for the next step
    // RejectAndDontRequeueRecover - Default one, after attempts exhausted direct reject and discarded the message
    // ImmediateRequeueMessageRecover - After attempts exhausted, return nack and requeue back to RabbitMQ queue
    // RepublishMessageRecover - After attempts exhausted, route the message to specific exchange and queue
    @RabbitListener(queues = "simple.queue2")
    public void listenSimpleQueue(String message)
    {
        System.out.println("Simple queue 2 message = " + message);

        // Thread.sleep(30000);
        // set acknowledge-mode: manual for manually do the acknowledgment using RabbitMQ library code
        // channel.basicAck(tag, true);
        // channel.basicNack(tag,true,true);

        // run in debug mode to simulate for spring acknowledge mode none and auto
        throw new RuntimeException("Intention");
    }


}
