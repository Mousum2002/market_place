package com.market_place.order.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderPlacedNotificationEvent(
    UUID orderId,
    UUID customerId,
    String customerName,
    String customerEmail,
    List<NotificationOrderItem> products,
    Instant occurredAt) {
}
