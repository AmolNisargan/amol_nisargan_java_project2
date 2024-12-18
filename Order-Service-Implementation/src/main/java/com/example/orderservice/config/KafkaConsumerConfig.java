package com.example.orderservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	@KafkaListener(topics = "order-status", groupId = "order-consumers")
	public void listen(String message) {
		System.out.println("Received message:::" + message);
	}
}
