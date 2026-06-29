package com.marketplace.inventory.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.marketplace.inventory.service.Service;

import lombok.RequiredArgsConstructor;

@RestController("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
  private final Service service;

  @GetMapping
  public ResponseEntity<Map<UUID, Boolean>> isInInventory(@RequestBody Map<UUID, Integer> products) {
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.isInInventory(products));
  }
}
