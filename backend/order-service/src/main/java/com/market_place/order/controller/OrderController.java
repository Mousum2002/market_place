package com.market_place.order.controller;

import com.market_place.order.dto.OrderRequest;
import com.market_place.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    @PostMapping
    public ResponseEntity<UUID> placeOrder(@RequestBody OrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.placeOrder(request).id());
    }
}
