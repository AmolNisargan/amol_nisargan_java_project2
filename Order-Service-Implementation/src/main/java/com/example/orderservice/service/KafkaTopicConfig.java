package com.example.orderservice.service;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic orderStatusTopic() {
		return new NewTopic("order-status", 1, (short) 1);
	}
}
