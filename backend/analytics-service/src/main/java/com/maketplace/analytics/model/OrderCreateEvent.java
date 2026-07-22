package com.maketplace.analytics.model;

import java.time.Instant;
import java.util.UUID;

public record OrderCreateEvent(UUID eventId, Instant occurredAt) implements LogEvent {

  @Override
  public String eventType() {
    return "PRUDUCT_CREATED";
  }
}
