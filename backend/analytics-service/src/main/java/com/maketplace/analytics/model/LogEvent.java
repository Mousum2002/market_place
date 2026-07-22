package com.maketplace.analytics.model;

import java.time.Instant;
import java.util.UUID;

public interface LogEvent {
  UUID eventId();

  Instant occurredAt();

  String eventType();
}
