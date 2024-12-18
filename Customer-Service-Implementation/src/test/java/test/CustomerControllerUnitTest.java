package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.example.customerservice.controller.CustomerController;
import com.example.customerservice.entity.Customer;
import com.example.customerservice.service.CustomerService;

//Unit Tests for CustomerController
class CustomerControllerUnitTest {

	@Mock
	private CustomerService customerService;

	@InjectMocks
	private CustomerController customerController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddCustomer() {
		Customer customer = new Customer();
		customer.setName("John Doe");

		when(customerService.addCustomer(any(Customer.class))).thenReturn(customer);

		ResponseEntity<Customer> response = customerController.addCustomer(customer);

		assertThat(response.getBody().getName()).isEqualTo("John Doe");
		verify(customerService, times(1)).addCustomer(customer);
	}

	@Test
	void testGetCustomer() {
		Customer customer = new Customer();
		customer.setCustomerId(1L);
		customer.setName("John Doe");

		when(customerService.getCustomerById(1L)).thenReturn(customer);

		ResponseEntity<Customer> response = customerController.getCustomer(1L);

		assertThat(response.getBody().getCustomerId()).isEqualTo(1L);
		verify(customerService, times(1)).getCustomerById(1L);
	}

	@Test
	void testGetAllCustomers() {
		Customer customer1 = new Customer();
		customer1.setName("John Doe");

		Customer customer2 = new Customer();
		customer2.setName("Jane Doe");

		List<Customer> customers = Arrays.asList(customer1, customer2);

		when(customerService.getAllCustomers()).thenReturn(customers);

		ResponseEntity<List<Customer>> response = customerController.getAllCustomers();

		assertThat(response.getBody().size()).isEqualTo(2);
		verify(customerService, times(1)).getAllCustomers();
	}

	@Test
	void testUpdateCustomer() {
		Customer customer = new Customer();
		customer.setName("Jane Doe");

		when(customerService.updateCustomer(eq(1L), any(Customer.class))).thenReturn(customer);

		ResponseEntity<Customer> response = customerController.updateCustomer(1L, customer);

		assertThat(response.getBody().getName()).isEqualTo("Jane Doe");
		verify(customerService, times(1)).updateCustomer(eq(1L), any(Customer.class));
	}

	@Test
	void testDeleteCustomer() {
		doNothing().when(customerService).deleteCustomer(1L);

		ResponseEntity<String> response = customerController.deleteCustomer(1L);

		assertThat(response.getBody()).isEqualTo("Customer deleted successfully");
		verify(customerService, times(1)).deleteCustomer(1L);
	}
}
