package com.marketplace.inventory.repo;

import com.marketplace.inventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface Repository extends JpaRepository<Inventory, UUID> {
    boolean existsByProductIdAndQuantityIsGreaterThanEqual(UUID productId, Integer quantityIsGreaterThan);
}
