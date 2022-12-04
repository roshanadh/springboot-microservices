package me.roshanadh.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.roshanadh.productservice.dto.ProductRequest;
import me.roshanadh.productservice.repository.ProductRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
						.andExpect(status().isCreated());

		Assertions.assertEquals(1, productRepository.findAll().size());
	}

	@Test
	void shouldGetAllProducts() throws Exception {
		ProductRequest productRequest = getProductRequest();
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$").isArray())
						.andExpect(jsonPath("$" , Matchers.hasSize(1)));

	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
						.name("iPhone 13")
						.description("iPhone 13")
						.price(BigDecimal.valueOf(1200))
						.build();
	}

}
