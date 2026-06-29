package com.marketplace.inventory.service;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.marketplace.inventory.repo.Repository;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

  private final Repository repo;

  public Map<UUID, Boolean> isInInventory(Map<UUID, Integer> products) {
    return products.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            entry -> repo.existsByProductIdAndQuantityIsGreaterThanEqual(entry.getKey(), entry.getValue())));
  }

}
