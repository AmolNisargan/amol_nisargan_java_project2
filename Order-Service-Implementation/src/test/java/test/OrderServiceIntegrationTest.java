package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.example.orderservice.DTO.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceIntegrationTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private MockRestServiceServer mockServer;
	private RestTemplate restTemplate;

	@BeforeEach
	public void setup() {
		// Reset the in-memory DB before each test
		orderRepository.deleteAll();
		restTemplate = new RestTemplate();
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void testPlaceOrderIntegration() {
		// Here, we would simulate a call to an external service via the mock server
		mockServer.expect(requestTo("http://localhost:8082/api/products/101"))
				.andRespond(withSuccess("{ \"price\": 50.0 }", MediaType.APPLICATION_JSON));

		Order order = new Order();
		order.setProductId(101L);
		order.setCustomerId(1001L);
		order.setQuantity(2);
		order.setStatus("PENDING");

		Order result = orderService.placeOrder(order);

		assertNotNull(result);
		assertEquals("PENDING", result.getStatus());
		assertEquals(100.0, result.getTotalAmount());
	}

	@Test
	public void testOrderCreationIntegration() {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setProductId(101L);
		orderRequest.setCustomerId(1001L);
		orderRequest.setQuantity(2);

		// Mock external service calls for product and customer
		mockServer.expect(requestTo("http://localhost:8082/api/products/101"))
				.andRespond(withSuccess("{ \"price\": 50.0 }", MediaType.APPLICATION_JSON));
		mockServer.expect(requestTo("http://localhost:8083/api/customers/1001"))
				.andRespond(withSuccess("{ \"id\": 1001, \"name\": \"John Doe\" }", MediaType.APPLICATION_JSON));

		Order result = orderService.createOrder(orderRequest);

		assertNotNull(result);
		assertEquals("Success", result.getStatus());
		assertEquals(100.0, result.getTotalAmount());
	}
}
