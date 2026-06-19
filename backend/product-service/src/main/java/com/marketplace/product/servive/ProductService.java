package com.marketplace.product.servive;

import java.util.List;

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

  public ProductResponse createProduct(ProductRequest product) {
    Product prod = repo.insert(mapper.dtoToProduct(product));
    log.info("Product created with id: {}", prod.getId());
    return mapper.productToResponse(prod);
  }

  public ProductResponse findById(String id) {
    Product prod = repo.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    return mapper.productToResponse(prod);
  }

  public List<ProductResponse> getAll() {
    return repo.findAll().stream().map(mapper::productToResponse).toList();
  }

  public void deleteById(String id) {
    repo.deleteById(id);
  }
}
