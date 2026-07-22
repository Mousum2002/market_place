package com.maketplace.analytics.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "analytics_logs")
@AllArgsConstructor
@Getter
@Setter
public class AnalyticsLogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_id")
  private UUID eventId;

  @Column(name = "event_type")
  private String eventType;

  @Column(name = "created_at")
  private Instant createdAt;

  @PrePersist
  public void prePresist() {
    if (createdAt == null) {
      createdAt = Instant.now();
    }
  }

  public AnalyticsLogEntity(UUID eventId, String eventType, Instant createdAt) {
    this.eventId = eventId;
    this.eventType = eventType;
    this.createdAt = createdAt;
  }
}
