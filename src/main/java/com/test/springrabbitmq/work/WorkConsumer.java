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
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String message)
    {
        System.out.println("Simple queue message = " + message);
    }

    // we can set in spring rabbit config prefetch = x
    // to force the consumer only fetch from the queue when consume finish, thus those higher performance consumer can handle more queue messages and speed up process
    // so need to wait slow consumer with round-rabin scheduling, so it doesn't accumulate on queue, this is one of the way, add more consumer enable this prefetch config
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
}
