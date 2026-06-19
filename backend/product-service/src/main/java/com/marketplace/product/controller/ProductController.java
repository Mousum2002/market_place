package com.marketplace.product.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.product.dto.ProductRequest;
import com.marketplace.product.dto.ProductResponse;
import com.marketplace.product.servive.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService service;

  @PostMapping("/create")
  public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest product) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(product));

  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProduct(@PathVariable String id) {
    return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
  }

  @GetMapping("/all")
  public ResponseEntity<List<ProductResponse>> getAll() {
    return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    service.deleteById(id);
  }
}
