package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import com.example.customerservice.entity.Customer;
import com.example.orderservice.DTO.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.KafkaProducerService;
import com.example.orderservice.service.OrderService;
import com.example.productservice.DTO.ProductDTO;

@SpringBootTest
public class OrderServiceTest {

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	private KafkaProducerService kafkaProducerService;

	@MockBean
	private RestTemplate restTemplate;

	@Autowired
	private OrderService orderService;

	private Order order;
	private OrderRequest orderRequest;

	@BeforeEach
	public void setup() {
		order = new Order();
		order.setOrderId(1L);
		order.setProductId(101L);
		order.setCustomerId(1001L);
		order.setQuantity(2);
		order.setPrice(50.0);
		order.setOrderDate(LocalDateTime.now());
		order.setStatus("PENDING");
		order.setTotalAmount(100.0);

		orderRequest = new OrderRequest();
		orderRequest.setProductId(101L);
		orderRequest.setCustomerId(1001L);
		orderRequest.setQuantity(2);
		orderRequest.setPrice(50.0);
	}

	@Test
	public void testPlaceOrder() {
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(new ProductDTO());

		Order result = orderService.placeOrder(order);

		assertNotNull(result);
		assertEquals("PENDING", result.getStatus());
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	public void testCreateOrder() {
		when(restTemplate.getForObject(anyString(), eq(ProductDTO.class))).thenReturn(new ProductDTO());
		when(restTemplate.getForObject(anyString(), eq(Customer.class))).thenReturn(new Customer());
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		Order result = orderService.createOrder(orderRequest);

		assertNotNull(result);
		assertEquals("Success", result.getStatus());
		verify(orderRepository, times(1)).save(order);
	}

	@Test
	public void testUpdateOrder() {
		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		order.setStatus("COMPLETED");

		Order updatedOrder = orderService.updateOrder(1L, order);

		assertNotNull(updatedOrder);
		assertEquals("COMPLETED", updatedOrder.getStatus());
	}

	@Test
	public void testCancelOrder() {
		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
		doNothing().when(orderRepository).deleteById(1L);

		orderService.cancelOrder(1L);

		verify(orderRepository, times(1)).deleteById(1L);
		verify(kafkaProducerService, times(1)).sendOrderStatusUpdate(anyString());
	}

	@Test
	public void testFindOrderById() {
		when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));

		Order result = orderService.findByOrderId(1L);

		assertNotNull(result);
		assertEquals(1L, result.getOrderId());
	}

	@Test
	public void testGetAllOrders() {
		when(orderRepository.findAll()).thenReturn(List.of(order));

		List<Order> result = orderService.getAllOrder();

		assertNotNull(result);
		assertFalse(result.isEmpty());
	}
}
