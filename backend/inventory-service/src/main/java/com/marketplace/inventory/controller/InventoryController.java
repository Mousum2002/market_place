package com.marketplace.inventory.controller;

import com.marketplace.inventory.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final Service service;
    @GetMapping
    public ResponseEntity<Boolean> isInInventory(@RequestParam UUID productId, @RequestParam Integer quantity){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.isInInventory(productId,quantity));
    }
}
