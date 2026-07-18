package com.market_place.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.market_place.order.model.Order;

public interface OrderRepo extends JpaRepository<Order, UUID> {

  Optional<List<Order>> findByCustomerId(UUID customerId);

}
