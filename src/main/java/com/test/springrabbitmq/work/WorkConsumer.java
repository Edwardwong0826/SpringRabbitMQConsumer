package com.test.springrabbitmq.work;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkConsumer
{
    // here parameter type is the one producer message type, not always will be string
    // work queue model is round rabin schedule when have multiple message come in
    //    @RabbitListener(queuesToDeclare = @Queue("work"))
    //    public void received1(String message)
    //    {
    //        System.out.println("message1 = " + message);
    //    }
    //
    //    @RabbitListener(queuesToDeclare = @Queue("work"))
    //    public void received2(String message)
    //    {
    //        System.out.println("message2 = " + message);
    //    }

    // https://www.rabbitmq.com/tutorials/tutorial-two-java
    // in RabbitMQ, all queue are by default was fair dispatch
    // basicQos = 0 is fair dispatch in RabbitMQ library code
    // set the prefetchCount = 1 or prefetchCount > 0 to become non-fair dispatch
    // int prefetchCount = 1;
    // channel.basicQos(prefetchCount);
    // 不设置 basicQos的话是一次性平均分发给所有的队列，设置之后限制了一次分发消息的数量，在设置手动确认机制，这样在你没有提交已经处理好的消息的时候是不会给你分发消息的。实现的不公平分发。
    // so need to enable 手动确认机制 for non-fair dispatch

    // https://www.rabbitmq.com/docs/consumer-prefetch
    // 准确来说 prefetchCount 是通道中允许的未确认消息的最大数量
    // prefetch basically is the max amount of unacknowledged data allow to exist on the RabbitMQ channel
    // so that RabbitMQ will not send the data to that queue(as channel is unacknowledged data is full) anymore
    // if set auto ack mode and set prefetch value, data in the channel will lose if the queue got error or down
    // but if we use manual ack then it will still requeue back again

    // We can set in spring rabbit config prefetch = 1 or x
    // To force the consumer only fetch from the queue when consume finish (which is finish consume and send back acknowledge), thus those higher performance consumer can handle more queue messages and speed up process
    // So need to wait slow consumer with round-rabin scheduling, so it doesn't accumulate on queue, this is one of the way, add more consumer enable this prefetch config
    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String message) throws InterruptedException {
        System.out.println("Consumer 1 received Work queue message : " + message);
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String message) throws InterruptedException {
        System.out.println("Consumer 2 received Work queue message : " + message);
        Thread.sleep(200);
    }

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String message)
    {
        System.out.println("Simple queue message = " + message);
    }

}
