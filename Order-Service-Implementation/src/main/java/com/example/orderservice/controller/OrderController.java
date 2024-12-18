package com.example.orderservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderservice.DTO.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/place")
	public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
		return ResponseEntity.ok(orderService.placeOrder(order));
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody OrderRequest order) {
		return ResponseEntity.ok(orderService.createOrder(order));
	}

	@GetMapping
	public ResponseEntity<List<Order>> getAllOrders() {
		return ResponseEntity.ok(orderService.getAllOrder());
	}

	@PutMapping("/{orderId}")
	public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody Order order) {
		return ResponseEntity.ok(orderService.updateOrder(orderId, order));
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
		Order order = orderService.findByOrderId(orderId);
		if (order != null) {
			orderService.cancelOrder(orderId);
			return ResponseEntity.ok("Order cancelled successfully.");
		} else {
			return ResponseEntity.ok("Order not found.");
		}
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> findOrderById(@PathVariable Long orderId) {
		Order order = orderService.findByOrderId(orderId);
		if (order != null) {

			return ResponseEntity.ok(order);
		} else {
			return ResponseEntity.ok(null);
		}
	}
}
