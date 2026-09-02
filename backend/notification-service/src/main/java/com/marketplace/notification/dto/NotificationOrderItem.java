package com.marketplace.notification.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record NotificationOrderItem(UUID productId, Integer quantity, BigDecimal price) {
}
