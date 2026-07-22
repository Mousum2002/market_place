package com.market_place.order.dto;

import java.time.Instant;
import java.util.UUID;

public record OrderPlacedLogEvent(UUID eventId, Instant occurredAt) {

}
