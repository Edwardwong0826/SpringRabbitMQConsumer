package com.test.springrabbitmq.work;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AckManualConsumer {

    // Message reply in RabbitMQ means does the consumer successfully consume the message or not
    // RabbitMQ use message manual ack the message will not lose, it will requeue to queue to retry consume by other
    // https://stackoverflow.com/questions/38728668/spring-rabbitmq-using-manual-channel-acknowledgement-on-a-service-with-rabbit
    // https://www.cnblogs.com/liang9479/p/17240141.html
    // every message has unique tag, rabbitmq identify the message by this, so that can manual ack or nack
    // need to set acknowledge-mode: manual
    // for those message haven't ack, when the consumer is down, rabbitMQ will requeue back to channel and send other consumer continue to consume
    // https://stackoverflow.com/questions/18418936/rabbitmq-and-relationship-between-channel-and-connection - refer this for channel in AMQP more details
    // Channel simple info: It is a virtual connection inside a connection. When publishing or consuming messages from a queue
    // it's all done over a channel Whereas Connection: It is a TCP connection between your application and the RabbitMQ broker.

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

    // By enable Spring retry, we can achieve at least once delivery for consumer but not idempotency

    // 1. Non-durable message sent to exchange and in queue, return ACK
    // 2. Durable message send to exchange and done persistent in disk, return ACK
    // other reason return NACK

    @RabbitListener(queues = "simple.queue2")
    public void listenSimpleQueue(Message message, Channel channel) throws IOException {
        System.out.println("Simple queue 2 message = " + new String(message.getBody()));

        // Thread.sleep(30000);
        // run in debug mode to simulate for spring acknowledge mode none and manual
        // in the RabbitMQ UI queues, during debug we can see that Message got Ready and Unacked tab
        // Ready - means the message waiting to be consumed by consumer
        // Unacked - means message send to consumer already but yet to received ack from consumer
        // set acknowledge-mode: manual for manually do the acknowledgment using RabbitMQ library code

        // if we want use acknowledge-mode manual, can write as below
        // Delivery tag the unique UUID for the message
        // what is the usage of delivery tag?
        // after consumer handle the message and send the ACK/NACK/REJECT back to MQ broker
        // MQ broker need to do action accordingly to the message like delete message, requeue message or mark as dead letter
        // so broker need to know which message to operate, and delivery tag as the message unique UUID is fulfilled this requirement

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try{

            // first parameter is unique id, second is multiple confirm or not
            channel.basicAck(deliveryTag, false);

        } catch (Exception e){

            // check if the message are redelivered
            Boolean redelivered = message.getMessageProperties().getRedelivered();

            // requeue parameter : if the rejected message(s) should be requeued rather than discarded/dead-lettered
            if(redelivered){

                channel.basicNack(deliveryTag,false,false);

            } else {

                channel.basicNack(deliveryTag,false,true);
                // difference with channelNack : ability to control multiple operation
                // channel.basicReject(deliveryTag, true);

            }

        }

        // Below code is to simulate when there is exception throws, eventually RabbitMQ will try to retry resend by requeue
        // once attempts exhausted, it will choose the action based on implementation of messageRecoverer, refer above info for MessageRecover interface
        //throw new RuntimeException("Intention");

    }

}
