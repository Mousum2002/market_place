package com.marketplace.product.servive;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.marketplace.product.dto.ProductRequest;
import com.marketplace.product.dto.ProductResponse;
import com.marketplace.product.exceptions.ProductNotFoundException;
import com.marketplace.product.mapper.DtoMapper;
import com.marketplace.product.model.Product;
import com.marketplace.product.repository.ProductRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

  private final ProductRepo repo;
  private final DtoMapper mapper;

  @CacheEvict(value = "products", allEntries = true)
  public ProductResponse createProduct(ProductRequest product) {
    Product prod = repo.insert(mapper.dtoToProduct(product));
    log.info("Product created with id: {}", prod.getId());
    return mapper.productToResponse(prod);
  }

  @Cacheable(value = "product-by-id", key = "#id")
  public ProductResponse findById(String id) {
    Product prod = repo.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    return mapper.productToResponse(prod);
  }

  @Cacheable("products")
  public List<ProductResponse> getAll() {
    return repo.findAll().stream().map(mapper::productToResponse).toList();
  }

  @CacheEvict(value = "products", allEntries = true)
  public void deleteById(String id) {
    repo.deleteById(id);
  }

  public Map<String, BigDecimal> getPricesByIds(List<String> ids) {
    Map<String, BigDecimal> prices = new HashMap<>();

    for (String id : ids) {
      prices.put(id, findById(id).price());
    }

    return prices;
  }
}
