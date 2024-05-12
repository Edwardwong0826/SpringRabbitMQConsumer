package com.test.springrabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringRabbitMQConsumerApplication {

	// This application is the consumer client, and use SpringAMQP to declare the queues / exchange / binding to auto create them in RabbitMQ
	public static void main(String[] args) {
		SpringApplication.run(SpringRabbitMQConsumerApplication.class, args);
	}

}
