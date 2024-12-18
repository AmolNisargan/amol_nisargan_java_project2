package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;

public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product product;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		product = new Product();
		product.setProductId(1L);
		product.setName("Product A");
		product.setDescription("Description A");
		product.setPrice(100.0);
		product.setStock(10);
	}

	@Test
	void testAddProduct() {
		when(productRepository.save(any(Product.class))).thenReturn(product);

		Product result = productService.addProduct(product);

		assertNotNull(result);
		assertEquals("Product A", result.getName());
		verify(productRepository, times(1)).save(product);
	}

	@Test
	void testGetProductById_ProductExists() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		Product result = productService.getProductById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getProductId());
	}

	@Test
	void testGetProductById_ProductNotFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(RuntimeException.class, () -> {
			productService.getProductById(1L);
		});

		assertEquals("Product not found", exception.getMessage());
	}

	@Test
	void testUpdateProduct() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(productRepository.save(any(Product.class))).thenReturn(product);

		product.setPrice(150.0);
		Product updatedProduct = productService.updateProduct(1L, product);

		assertNotNull(updatedProduct);
		assertEquals(150.0, updatedProduct.getPrice());
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	void testDeleteProduct() {
		doNothing().when(productRepository).deleteById(1L);

		productService.deleteProduct(1L);

		verify(productRepository, times(1)).deleteById(1L);
	}
}
