package com.marketplace.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.marketplace.product.model.Product;

public interface ProductRepo extends MongoRepository<Product, String> {

}
