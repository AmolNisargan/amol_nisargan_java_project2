package com.example.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String message) {
		kafkaTemplate.send("order-status", message);
	}

	@KafkaListener(topics = "order-status", groupId = "order-group")
	public void listenOrderUpdates(String message) {
		System.out.println("Received Order Update: " + message);

		// Log the message for debugging purposes
		logOrderUpdate(message);
	}

	// Method to log order update
	private void logOrderUpdate(String message) {
		System.out.println("Logging Order Update: " + message);
		// Add additional logging mechanisms if needed
	}

}
