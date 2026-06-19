package com.marketplace.product.exceptions;

public class ProductNotFoundException extends RuntimeException {
  public ProductNotFoundException(String msg) {
    super(msg);
  }
}
