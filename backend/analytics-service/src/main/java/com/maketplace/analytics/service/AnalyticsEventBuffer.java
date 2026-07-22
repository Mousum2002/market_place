package com.maketplace.analytics.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Component;

import com.maketplace.analytics.model.LogEvent;

@Component
public class AnalyticsEventBuffer {

  private final BlockingQueue<LogEvent> queue = new LinkedBlockingQueue<>(50_000);

  public BlockingQueue<LogEvent> queue() {
    return this.queue;
  }
}
