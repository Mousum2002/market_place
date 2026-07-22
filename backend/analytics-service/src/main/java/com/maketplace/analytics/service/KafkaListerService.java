package com.maketplace.analytics.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.maketplace.analytics.model.CustomerCreatedLogEvent;
import com.maketplace.analytics.model.LogEvent;
import com.maketplace.analytics.model.OrderCreateEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaListerService {

  private final AnalyticsEventBuffer buffer;
  private final AnalyticsBufferFlushService flushService;

  @KafkaListener(topics = "customer-created")
  public void onCustomerCreated(CustomerCreatedLogEvent event) {
    helper(event);
  }

  @KafkaListener(topics = "order-created")
  public void onOrderCreated(OrderCreateEvent event) {
    helper(event);
  }

  private void helper(LogEvent event) {
    buffer.queue().offer(event);

    flushService.requestFlushIfLarge();

  }
}
