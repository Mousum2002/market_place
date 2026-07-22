package com.maketplace.analytics.mapper;

import org.springframework.stereotype.Component;

import com.maketplace.analytics.model.AnalyticsLogEntity;
import com.maketplace.analytics.model.LogEvent;

@Component
public class AnalyticsLogMapper {

  public AnalyticsLogEntity toeEntity(LogEvent event) {
    return new AnalyticsLogEntity(event.eventId(), event.eventType(), event.occurredAt());
  }
}
