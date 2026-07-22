package com.maketplace.analytics.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.maketplace.analytics.mapper.AnalyticsLogMapper;
import com.maketplace.analytics.model.AnalyticsLogEntity;
import com.maketplace.analytics.model.LogEvent;
import com.maketplace.analytics.repo.Repository;

import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsBufferFlushService {

  private static final int MAX_BATCH_SIZE = 1000;
  private static final int EARLY_FLUSH_THRESHOLD = 2000;

  private final AtomicBoolean flushing = new AtomicBoolean(false);
  private final AnalyticsEventBuffer buffer;
  private final Repository repo;
  private final AnalyticsLogMapper mapper;

  @Scheduled(fixedDelay = 5000)
  public void scheduledFlush() {
    flushBuffer();
  }

  public void requestFlushIfLarge() {
    if (buffer.queue().size() >= EARLY_FLUSH_THRESHOLD) {
      flushBuffer();
    }
  }

  @PreDestroy
  public void flushOnShutDown() {
    flushBuffer();
  }

  @Transactional
  public void flushBuffer() {
    if (!flushing.compareAndSet(false, true)) {
      return;
    }
    try {
      List<LogEvent> drained = new ArrayList<>(MAX_BATCH_SIZE);

      buffer.queue().drainTo(drained, MAX_BATCH_SIZE);

      if (drained.isEmpty()) {
        return;
      }

      List<AnalyticsLogEntity> entities = drained.stream().map(mapper::toeEntity).toList();
      repo.saveAll(entities);

    } finally {
      flushing.set(false);
    }
  }

}
