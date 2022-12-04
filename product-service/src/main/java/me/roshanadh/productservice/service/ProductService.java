package me.roshanadh.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.roshanadh.productservice.dto.ProductRequest;
import me.roshanadh.productservice.dto.ProductResponse;
import me.roshanadh.productservice.model.Product;
import me.roshanadh.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  private final ProductRepository productRepository;

  public void createProduct(ProductRequest productRequest) {
    Product product = Product.builder()
            .name(productRequest.getName())
            .description(productRequest.getDescription())
            .price(productRequest.getPrice())
            .build();
    productRepository.save(product);
    log.info("Product {} is saved", product.getId());
  }

  public List<ProductResponse> getAllProducts() {
    List<Product> products = productRepository.findAll();

    return products.stream()
            .map(this::mapToProductResponse)
            .collect(Collectors.toList());

  }

  private ProductResponse mapToProductResponse(Product product) {
    return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .build();
  }
}
