package com.market_place.order.repository;

import java.util.UUID;

import com.market_place.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, UUID> {

}
