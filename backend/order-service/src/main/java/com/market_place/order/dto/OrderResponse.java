package com.market_place.order.dto;

import java.util.List;
import java.util.UUID;

import com.market_place.order.model.OrderProduct;

public record OrderResponse(UUID id, List<OrderProduct> products) {
}
