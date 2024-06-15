package com.test.springrabbitmq.work;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.test.springrabbitmq.model.Orders;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class IdempotentConsumer {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RedisTemplate redisTemplate;

    // Remember run redis container in docker
    @RabbitListener(queues = "queue.idempotent")
    public void listenIdempotentQueue(Message message, Channel channel) throws IOException {

        // deserialize from json
        Orders orders = objectMapper.readValue(message.getBody(), Orders.class);

        //System.out.println("queue.idempotent message = " + new String(message.getBody()));
        System.out.println("queue.idempotent message = " + orders.toString());

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try{

            // only insert to redis if the orders is not insert yet
            Boolean result = redisTemplate.opsForValue().setIfAbsent("idempotent:" + orders.getId(), orders.getId());
            if(result){
                // this part is to simulate if the rabbitMQ send multiple same messages or retry send the same messages
                // those same messages will have same UUID in this case
                // we use redis to set the key only for the first message UUID received and use result of the set key to do idempotent check
                // and do whatever logic that required idempotent
                System.out.println("insert order into database");

            }
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
