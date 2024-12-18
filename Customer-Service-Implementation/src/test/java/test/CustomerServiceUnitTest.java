package test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.customerservice.entity.Customer;
import com.example.customerservice.service.CustomerService;

// Unit Tests for CustomerService
class CustomerServiceUnitTest {

	@Mock
	private com.example.customerservice.repository.CustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAddCustomer() {
		Customer customer = new Customer();
		customer.setName("John Doe");
		customer.setEmail("john@example.com");
		customer.setPhone("1234567890");

		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		Customer result = customerService.addCustomer(customer);

		assertThat(result.getName()).isEqualTo("John Doe");
		verify(customerRepository, times(1)).save(customer);
	}

	@Test
	void testGetCustomerById() {
		Customer customer = new Customer();
		customer.setCustomerId(1L);
		customer.setName("John Doe");

		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

		Customer result = customerService.getCustomerById(1L);

		assertThat(result.getCustomerId()).isEqualTo(1L);
		verify(customerRepository, times(1)).findById(1L);
	}

	@Test
	void testGetCustomerByIdNotFound() {
		when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> customerService.getCustomerById(1L));
		verify(customerRepository, times(1)).findById(1L);
	}

	@Test
	void testUpdateCustomer() {
		Customer existingCustomer = new Customer();
		existingCustomer.setCustomerId(1L);
		existingCustomer.setName("John Doe");

		Customer updatedCustomer = new Customer();
		updatedCustomer.setName("Jane Doe");
		updatedCustomer.setEmail("jane@example.com");
		updatedCustomer.setPhone("9876543210");

		when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
		when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

		Customer result = customerService.updateCustomer(1L, updatedCustomer);

		assertThat(result.getName()).isEqualTo("Jane Doe");
		verify(customerRepository, times(1)).findById(1L);
		verify(customerRepository, times(1)).save(any(Customer.class));
	}

	@Test
	void testDeleteCustomer() {
		doNothing().when(customerRepository).deleteById(1L);

		customerService.deleteCustomer(1L);

		verify(customerRepository, times(1)).deleteById(1L);
	}
}
