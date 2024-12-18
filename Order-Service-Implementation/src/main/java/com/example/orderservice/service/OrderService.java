package com.example.orderservice.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.customerservice.entity.Customer;
import com.example.orderservice.DTO.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.productservice.DTO.ProductDTO;
import com.example.productservice.entity.Product;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private KafkaProducerService kafkaProducerService;
	@Autowired
	private RestTemplate restTemplate;

	public Order placeOrder(Order order) {

		order.setOrderDate(LocalDateTime.now());
		order.setStatus("PENDING");
		String productUrl = "http://localhost:8082/api/products/" + order.getProductId();
		ProductDTO product = restTemplate.getForObject(productUrl, ProductDTO.class);
		order.setPrice(product.getPrice());
		order.setTotalAmount(order.getQuantity() * product.getPrice());
		orderRepository.save(order);
		List<Order> orders = orderRepository.findAll();
		Order LastOrder = orders.get(orders.size() - 1);
		kafkaProducerService
				.sendOrderStatusUpdate("Order ID: " + LastOrder.getOrderId() + " Status: " + order.getStatus());

		return order;
	}

	public Order createOrder(OrderRequest orderRequest) {
		// Fetch Product Details
		String productUrl = "http://localhost:8082/api/products/" + orderRequest.getProductId();
		ProductDTO product = restTemplate.getForObject(productUrl, ProductDTO.class);
		// Fetch Customer Details
		String customerUrl = "http://localhost:8083/api/customers/" + orderRequest.getCustomerId();
		Customer customer = restTemplate.getForObject(customerUrl, Customer.class);

		if (product == null || customer == null) {
			throw new RuntimeException("Product or Customer not found");
		}

		// Calculate Total Amount
		double totalAmount = product.getPrice() * orderRequest.getQuantity();

		// Create Order
		Order order = new Order();
		order.setProductId(orderRequest.getProductId());
		order.setCustomerId(orderRequest.getCustomerId());
		order.setOrderDate(LocalDateTime.now());
		order.setStatus("Success");
		order.setQuantity(orderRequest.getQuantity());
		order.setPrice(product.getPrice());
		order.setTotalAmount(totalAmount);

		try {
			orderRepository.save(order);
			List<Order> orders = orderRepository.findAll();
			Order LastOrder = orders.get(orders.size() - 1);
			kafkaProducerService
					.sendOrderStatusUpdate("Order ID: " + LastOrder.getOrderId() + " Status: " + order.getStatus());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error saving order", e);
		}

		return order;
	}

	public Order updateOrder(Long orderId, Order updatedOrder) {

		Order existingOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		existingOrder.setStatus(updatedOrder.getStatus());
		kafkaProducerService.sendOrderStatusUpdate(
				"Order ID: " + updatedOrder.getOrderId() + " Status: " + updatedOrder.getStatus());

		return orderRepository.save(existingOrder);
	}

	public void cancelOrder(Long orderId) {
		kafkaProducerService.sendOrderStatusUpdate("Order ID: " + orderId + " Status: " + "Canceled");

		orderRepository.deleteById(orderId);
	}

	public Order findByOrderId(Long orderId) {
		Order existingOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));
		kafkaProducerService.sendOrderStatusUpdate("Order ID: " + orderId + " Status: " + existingOrder.getStatus());

		return orderRepository.save(existingOrder);
	}

//	@org.springframework.cache.annotation.Cacheable

	public List<Order> getAllOrder() {
		List<Order> existingOrder = orderRepository.findAll();
		kafkaProducerService.sendOrderStatusUpdate("Fetching All Orders : ");

		return existingOrder;
	}

	public Customer getCustomerById(Long customerId) {
		String url = "http://localhost:8083/api/customers/" + customerId;

		Product product = restTemplate.getForObject("http://localhost:8082/api/product", Product.class);
		Customer customer = restTemplate.getForObject("http://localhost:8083/api/customers", Customer.class);

//		System.out.println("Product: " + product);
//		System.out.println("Customer: " + customer);
		return restTemplate.getForObject(url, Customer.class);

	}

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void publishOrderEvent(String message) {
		kafkaTemplate.send("order-topic", message);
	}

}
