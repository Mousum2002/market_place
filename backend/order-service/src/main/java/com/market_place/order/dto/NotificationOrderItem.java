package com.market_place.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record NotificationOrderItem(UUID productId, Integer quantity, BigDecimal price) {
}
