package com.market_place.order.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.market_place.order.dto.OrderRequest;
import com.market_place.order.dto.OrderResponse;
import com.market_place.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService service;

  @PostMapping
  public ResponseEntity<UUID> placeOrder(@RequestHeader("X-Customer-Id") UUID customerID,
      @RequestBody OrderRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.placeOrder(customerID, request).id());
  }

  @GetMapping("/my-orders")
  public ResponseEntity<List<OrderResponse>> getCustomerOrder(@RequestHeader("X-Customer-Id") UUID customerID) {
    return ResponseEntity.ok(service.getOrder(customerID));
  }
}
