package com.marketplace.customer.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerCreatedLogEvent(UUID eventId, Instant occurredAt) {

}
