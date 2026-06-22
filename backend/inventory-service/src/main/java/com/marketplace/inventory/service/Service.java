package com.marketplace.inventory.service;


import com.marketplace.inventory.repo.Repository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final Repository repo;
    public boolean isInInventory(UUID productId, Integer quantity){
        return repo.existsByProductIdAndQuantityIsGreaterThanEqual(productId,quantity);
    }

}
